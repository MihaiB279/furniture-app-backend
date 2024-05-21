import uuid

import psycopg2
from psycopg2 import sql

from utils import get_list

dbname = 'sitedb'
user = 'postgres'
password = 'password1234'
host = 'localhost'
port = '5432'

table_name = 'furniture'
conn = psycopg2.connect(dbname=dbname, user=user, password=password, host=host, port=port)
cur = conn.cursor()

# Query the table for rows matching the given name
query = f"SELECT furniture_type, details FROM furniture_table"
cur.execute(query)

furniture = {}

for row in cur.fetchall():
    furniture_type = row[0]
    if furniture_type not in furniture.keys():
        furniture[furniture_type] = {}
    furniture_details = row[1]
    furniture_details = get_list(furniture_details)
    for key, item in furniture_details.items():
        if 'W x H x D' not in key:
            if key not in furniture[furniture_type].keys():
                furniture[furniture_type][key] = [item]
            elif item not in furniture[furniture_type][key]:
                furniture[furniture_type][key].append(item)

table_name = 'furniture_attributes_table'
for key, attributes in furniture.items():
    for attribute, values in attributes.items():
        for value in values:
            id = uuid.uuid4()
            insert_query = sql.SQL("""
                                INSERT INTO {} (id, name, attribute, value)
                                VALUES (%s, %s, %s, %s)
                            """).format(sql.Identifier(table_name))
            cur.execute(insert_query,
                        (str(id), key, attribute, value))

conn.commit()
cur.close()
conn.close()
