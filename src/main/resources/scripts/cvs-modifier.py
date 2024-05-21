import glob
import pandas as pd
import numpy as np
import re


def change_currency(x):
    x = x.replace(',', '')
    return round(int(x) * 0.58 * 1.73)


def modify_list_bar(item):
    items = item.split('\',')
    items[:len(items) - 1] = [s + '\'' for s in items[:len(items) - 1]]
    items[0] = '[\'Material: ' + items[0][2:]
    del items[1]
    items[-1] = '\'Delivery condition: ' + items[-1][2:]
    return ','.join(items)


def modify_list_recliner(item):
    items = item.split('\',')
    items[:len(items) - 1] = [s + '\'' for s in items[:len(items) - 1]]
    items_1 = items[1].split(' ')
    items[1] = ' \'' + items_1[2] + ' ' + items_1[3][:len(items_1[3]) - 1] + ': ' + items_1[1][1:] + '\''
    return ','.join(items)


def modify_list_kitchen(item):
    items = item.split('\',')
    items[:len(items) - 1] = [s + '\'' for s in items[:len(items) - 1]]
    items[len(items) - 1] = ' \'Delivery condition: ' + items[len(items) - 1][2:]
    items[1:] = [item[1:] for item in items[1:]]

    return ','.join(items)


def modify_list_showcase(item):
    items = item.split('\',')
    if len(items) > 1:
        items[0] = items[0] + '\''
        items[1:] = [s[2:] for s in items[1:]]
        details = ','.join(items[1:])
        items = items[:2]
        items[1] = '\'Details: ' + details
    items[0] = '[\'Delivery condition: ' + items[0][2:]
    return ','.join(items)


def modify_list_sofabed(item):
    items = item.split('\',')
    items[:len(items) - 1] = [s + '\'' for s in items[:len(items) - 1]]
    if 'Shape' not in items[0]:
        items.insert(0, '[\'Shape: not specified\'')
        items[1] = items[1][1:]
    return ','.join(items)


def delete_duplicates(csv):
    duplicate_rows = []
    for index1, row1 in csv.iterrows():
        for index2, row2 in csv.iterrows():
            if index1 not in duplicate_rows:
                if index1 != index2 and row1['furniture_type'] == row2['furniture_type'] and row1['name'] == row2[
                    'name'] and row1['company'] == row2['company']:
                    duplicate_rows.append(index2)

    return duplicate_rows


def repair_details(name, csv):
    rows_to_delete = []
    if name == 'bed':
        for index, row in csv.iterrows():
            if 'Recommended Mattress Size' in row['product_details'] or 'W x H x D' not in row['product_details']:
                rows_to_delete.append(index)
        csv.drop(rows_to_delete, inplace=True)
    elif name != 'showcase':
        for index, row in csv.iterrows():
            if 'W x H x D' not in row['product_details']:
                rows_to_delete.append(index)
        csv.drop(rows_to_delete, inplace=True)

    duplicated_rows = delete_duplicates(csv)
    csv.drop(duplicated_rows, inplace=True)

    #filtered_list = list(filter(lambda x: 'W x H x D' in x, csv['product_details']))
    #if name == 'bar':
    #    filtered_list = list(map(lambda x: modify_list_bar(x), csv['product_details']))
    #if name == 'kitchen':
   #     filtered_list = list(map(lambda x: modify_list_kitchen(x), csv['product_details']))
   # if name == 'recliner':
   #     filtered_list = list(map(lambda x: modify_list_recliner(x), csv['product_details']))
   # if name == 'showcase':
   #     filtered_list = list(map(lambda x: modify_list_showcase(x), csv['product_details']))
    #if name == 'sofabed':
   #     filtered_list = list(map(lambda x: modify_list_sofabed(x), csv['product_details']))

    #csv['product_details'] = filtered_list


def main():
    companies = ['Dedeman', 'Jysk', 'Ikea', 'Casa Rusu', 'Mobila Casa', 'Mob Expert', 'Casa Magica', 'FurnitureForYou']
    path = "C:/Users/MihaiBucur/Desktop/Licenta/bachelor_project/backend/src/main/resources/cvs/*.csv"
    for file in glob.glob(path):
        csv_input = pd.read_csv(file)
        # csv_input['company'] = np.random.choice(companies, size=len(csv_input), replace=True)
        # reg_str = "<li class=\"_21Ahn-\">(.*?)</li>"
        # csv_input['product_details'] = csv_input['product_details'].apply(lambda x: re.findall(reg_str, x))
        repair_details(file.split('\\')[1].split('.csv')[0], csv_input)
        # csv_input['discounted_price'] = csv_input['discounted_price'].apply(lambda x: change_currency(x[1:]))
        # csv_input['original_price'] = csv_input['original_price'].apply(lambda x: change_currency(x[1:]))
        csv_input.to_csv(file, index=False)


if __name__ == "__main__":
    main()
