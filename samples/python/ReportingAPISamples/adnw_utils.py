from adnw_params import *
import datetime
import json
import csv
import shutil
import os.path


flat_item = {}
REPORTS_DIR = 'csv_reports'


def raw_value(enum_val: enum.Enum):
    return enum_val.value


def raw_statement(_filter: Filter):
    return _filter.raw_value()


def validate(date_text: string):
    try:
        datetime.datetime.strptime(date_text, '%Y-%m-%d')
    except ValueError:
        raise ValueError("Invalid data format, should be YYYY-MM-DD")


""" Flatten nested Json object."""
def flatten_item(value, key=None):
    global flat_item

    if type(value) is list:
        for item in value:
            flatten_item(item)
    elif type(value) is dict:
        (sub_key, sub_value) = add_separated_key_value_if_needed(value)
        if not (sub_key or sub_value):
            for (sub_key, sub_value) in value.items():
                flatten_item(sub_value, sub_key)
        else:
            flat_item[sub_key] = sub_value

    elif type(key) is str:
        flat_item[key] = value


""" Flatten nested Json and save to csv file"""
def write_to_csv(_json: json):
    if os.path.exists(REPORTS_DIR):
        shutil.rmtree(REPORTS_DIR)

    if not _json:
        print("Failed to writing results to csv, json results: \n" + str(_json))
        return

    flat_datas = list()
    global flat_item
    data_array = _json["data"]
    for data in data_array:
        flat_items = list()
        for item in data["results"]:
            flat_item = {}
            flatten_item(item)
            flat_items.append(flat_item)
        flat_datas.append(flat_items)

    if is_list_empty(flat_datas) or is_list_empty(flat_datas[0]):
        print("Failed to writing results to csv, json results: \n" + str(_json))
        return

    if not os.path.exists(REPORTS_DIR):
        os.makedirs(REPORTS_DIR)

    i = 0
    for flat_items in flat_datas:
        if is_list_empty(flat_items):
            continue
        csv_output = csv.DictWriter(open(REPORTS_DIR + "/report_" + str(i) + ".csv", "w+"), flat_items[0].keys(), quoting=csv.QUOTE_ALL)
        csv_output.writeheader()
        for flat_item in flat_items:
            csv_output.writerow(flat_item)
        i += 1

    print("json response: " + str(_json))
    print("Finish writing results to csv, check 'csv_reports/report.csv' in root directory.")


def add_separated_key_value_if_needed(node: dict):
    key = None
    value = None
    for (sub_key, sub_value) in node.items():
        if sub_key == 'key':
            key = sub_value
        elif sub_key == 'value':
            value = sub_value
        else:
            break
    return key, value


def is_list_empty(_list: list):
    return _list is None or len(_list) == 0


