# Audience Network Reporting API Samples
The Audience Network Reporting API lets you automate downloading performance data for
your Business and Properties. This sample project walks through key API features and use cases
in Python to send requests (sync or async) to fetch performance data and save them as .csv file.

The API now supports sync (GET request) and async (POST request). Sync requests are
light and immediately return data. Async requests are designed to handle heavier data
loads and return a query ID for you to retrieve the data at a later time. More info below.

This repository includes contents as follows:

## Recommendation

We highly recommend you include our adnw_examples.py into your project because those examples are the best practices for
accessing our end point.

After you fetch query ids successfully, please wait for a few seconds (i.e. 10 seconds) before you send another "async" request with
query ids such that you are guaranteed to get results. Also, please do not try to request more than 50 times within 60 seconds.

## Run Reporting API Examples

In adnw_examples.py, it provides examples using adnw_requests to build sync and async requests
to get data. Uncomment corresponding test cases in adnw_examples.py and run it.

## Versions

The python version is 3.6.<br>
The [requests library](https://github.com/requests/requests) version is 2.18.4.