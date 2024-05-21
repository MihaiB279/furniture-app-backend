import glob
import uuid

import pandas as pd
import psycopg2
from psycopg2 import sql

dbname = 'sitedb'
user = 'postgres'
password = 'password1234'
host = 'localhost'
port = '5432'

table_name = 'furniture_table'
conn = psycopg2.connect(dbname=dbname, user=user, password=password, host=host, port=port)
cur = conn.cursor()


def update_db():
    path = "C:/Users/MihaiBucur/Desktop/Licenta/bachelor_project/backend/src/main/resources/cvs/*.csv"
    for file in glob.glob(path):
        csv_input = pd.read_csv(file)
        for index, row in csv_input.iterrows():
            furniture_type = ""
            if row['furniture_type'] == 'Bed':
                furniture_type = "BED"
            elif row['furniture_type'] == 'Bar':
                furniture_type = "BAR"
            elif row['furniture_type'] == 'Dining Table':
                furniture_type = "DINING"
            elif row['furniture_type'] == 'Kitchen Cabinet':
                furniture_type = "KITCHEN"
            elif row['furniture_type'] == 'Recliner':
                furniture_type = "RECLINER"
            elif row['furniture_type'] == 'Showcase':
                furniture_type = "SHOWCASE"
            elif row['furniture_type'] == 'Sofa-bed':
                furniture_type = "SOFA_BED"
            elif row['furniture_type'] == 'TV unit':
                furniture_type = "TV_UNIT"
            id = uuid.uuid4()
            insert_query = sql.SQL("""
                    INSERT INTO {} (id, furniture_type, name, price, details, company)
                    VALUES (%s, %s, %s, %s, %s, %s)
                """).format(sql.Identifier(table_name))
            cur.execute(insert_query,
                        (str(id), furniture_type, row['name'], row['original_price'], row['product_details'],
                         row['company']))

    conn.commit()
    conn.close()


update_db()
