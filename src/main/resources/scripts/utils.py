import sys


def get_list(string):
    string_list = eval(string)
    details_dict = {}

    for item in string_list:
        parts = item.split(':')
        if len(parts) == 2:
            details_dict[parts[0]] = parts[1]

    return details_dict


def get_index_off_generated(generated, available):
    list_indexes = []
    for single_list in generated:
        single_list_index = []
        for furniture_generated in single_list:
            for index, furniture_available in enumerate(available[furniture_generated['furniture_type']]):
                if furniture_available['Name'][1:] == furniture_generated['name'] and \
                        furniture_available['Company'][1:] == furniture_generated['company']:
                    single_list_index.append(index)
                    break
        list_indexes.append(single_list_index)
    return list_indexes
