import sys

import Levenshtein
import psycopg2
from sklearn.cluster import AgglomerativeClustering
import numpy as np
from utils import get_list


def create_empty_string(string):
    return 'Z' * len(string)


def get_details_for_type(item):
    dbname = 'sitedb'
    user = 'postgres'
    password = 'password1234'
    host = 'localhost'
    port = '5432'

    conn = psycopg2.connect(dbname=dbname, user=user, password=password, host=host, port=port)
    cur = conn.cursor()
    query = f"SELECT DISTINCT attribute FROM furniture_attributes_table where name=%s"
    cur.execute(query, (item,))
    rows = cur.fetchall()
    return [row[0] for row in rows]


def get_from_db_item(item):
    dbname = 'sitedb'
    user = 'postgres'
    password = 'password1234'
    host = 'localhost'
    port = '5432'

    conn = psycopg2.connect(dbname=dbname, user=user, password=password, host=host, port=port)
    cur = conn.cursor()
    query = f"SELECT * FROM furniture_table where furniture_type=%s"
    cur.execute(query, (item,))
    return cur.fetchall()


def cluster_item(item, checks):
    details_keys = get_details_for_type(item)
    for key in checks.keys():
        if checks[key] == "No preferences":
            if key in details_keys:
                details_keys.remove(key)
    all_items = get_from_db_item(item)
    companies = [row[1] for row in all_items]
    details = [row[2] for row in all_items]
    ftype = [row[3] for row in all_items]
    name = [row[4] for row in all_items]
    prices = [row[5] for row in all_items]
    details_prices = []
    for i in range(len(details)):
        details_prices.append(
            details[i][:len(details[i]) - 1] + ', \'Price: ' + str(prices[i]) + '\', \'Company: ' + companies[
                i] + '\', \'Name: ' + name[i] + '\', \'Furniture Type: ' + ftype[i] + '\']')
    details_as_list = [get_list(item) for item in details_prices]
    final_cluster = [details_as_list]
    for key in details_keys:
        aux_cluster = []
        for cluster in final_cluster:
            if len(cluster) == 1:
                aux_cluster.append(cluster)
            else:
                distance_matrix = np.zeros((len(cluster), len(cluster)))
                for i in range(len(cluster)):
                    for j in range(len(cluster)):
                        if key in cluster[i].keys() and key in cluster[j].keys():
                            distance_matrix[i, j] = Levenshtein.distance(cluster[i][key], cluster[j][key])
                        elif key in cluster[i].keys():
                            distance_matrix[i, j] = Levenshtein.distance(cluster[i][key],
                                                                         create_empty_string(cluster[i][key]))
                        elif key in cluster[j].keys():
                            distance_matrix[i, j] = Levenshtein.distance(create_empty_string(cluster[j][key]),
                                                                         cluster[j][key])
                        else:
                            distance_matrix[i, j] = Levenshtein.distance(create_empty_string("ZZZZ"),
                                                                         create_empty_string("ZZZZ"))

                clustering = AgglomerativeClustering(n_clusters=None, metric='precomputed', linkage='complete',
                                                     distance_threshold=1)
                cluster_labels = clustering.fit_predict(distance_matrix)
                clusters = {}
                for i, label in enumerate(cluster_labels):
                    if label not in clusters:
                        clusters[label] = []
                    clusters[label].append(cluster[i])

                for cluster_id, cluster_to_add in clusters.items():
                    aux_cluster.append(cluster_to_add)

        final_cluster = aux_cluster
    return final_cluster
