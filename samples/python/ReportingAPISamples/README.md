# audience-network-reporting-api-samples
The Audience Network Reporting API lets you automate downloading performance data for
your Business and Properties. This sample project walks through key API features and use cases
in Python to send requests (sync or async) to fetch performance data and save them as .csv file.

The API now supports sync (GET request) and async (POST request). Sync requests are
light and immediately return data. Async requests are designed to handle heavier data
loads and return a query ID for you to retrieve the data at a later time. More info below.

This repository includes contents as follows:

## Run Reporting API Examples

In adnw_examples.py, it provides examples using adnw_requests to build sync and async requests
to get data. Uncomment corresponding test cases in adnw_examples.py and run it.

## Versions

The python version is 3.6.<br>
The requests library version is 2.18.4.