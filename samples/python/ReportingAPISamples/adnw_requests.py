import datetime
import requests
import json
import adnw_utils
from adnw_params import *

# Base URL
BASE_URL = 'https://graph.facebook.com'
API_VERSION = 'v3.3'
ADNW_REQUEST_API = '/adnetworkanalytics'
ADNW_REQUEST_API_BY_QUERY_IDS = '/adnetworkanalytics_results'
ACCESS_TOKEN_KEY = 'access_token'
QUERY_IDS = 'query_ids'
MAX_ROW_LIMIT_ASYNC = 10000
MAX_ROW_LIMIT_SYNC = 1000
now = datetime.datetime.now()
date_format = "%Y-%m-%d"


class ADNWRequestBuilder(object):

    def __init__(self, app_id: string, access_token: string):
        self.url = BASE_URL + '/' + API_VERSION + '/' + app_id
        self.access_token = access_token
        self.metrics = set()
        self.breakdowns = set()
        self.date_since = now.date() + datetime.timedelta(days=-6)
        self.date_until = now.date()
        self.filters = set()
        self.ordering_column = OrderingColumn.TIME
        self.ordering_type = OrderingType.ASCENDING
        self.limit = MAX_ROW_LIMIT_SYNC
        self.aggregation_period = AggregationPeriod.DAY
        self.query_ids = set()

    def add_metrics(self, metrics: [Metric]):
        self.metrics.add(metrics)

    def add_metric(self, metric: Metric):
        self.metrics.add(metric)

    def add_breakdowns(self, breakdowns: [Breakdown]):
        self.breakdowns.add(breakdowns)

    def add_breakdown(self, breakdown: Breakdown):
        self.breakdowns.add(breakdown)

    def set_date_range(self, date_since: datetime.date, date_until: datetime.date):
        self.date_since = date_since
        self.date_until = date_until

    def add_filters(self, filters: [Filter]):
        self.filters.add(filters)

    def add_filter(self, _filter: Filter):
        self.filters.add(_filter)

    def set_ordering_type(self, ordering_type: OrderingType):
        self.ordering_type = ordering_type

    def set_ordering_column(self, ordering_column: OrderingColumn):
        self.ordering_column = ordering_column

    def set_aggregation_period(self, aggregation_period: AggregationPeriod):
        self.aggregation_period = aggregation_period

    def set_limit(self, limit: int):
        self.limit = limit

    def set_query_ids(self, query_ids: [string]):
        self.query_ids = query_ids

    """ Build sync request with provided params."""
    def build_sync_request(self):
        sync_url = self.url + ADNW_REQUEST_API + '/'
        self.validate_params()
        return requests.get(sync_url, self.get_params())

    """ Build sync request with query ids and access token.
        Any other param will be ignored.
    """
    def build_sync_request_with_query_ids(self):
        sync_url = self.url + ADNW_REQUEST_API_BY_QUERY_IDS + '/'
        print("You are using query ids to get results."
              " The fields except for query ids and access token will be ignored.")
        self.validate_params(is_using_query_ids=True)
        params = {
            ACCESS_TOKEN_KEY: self.access_token,
            QUERY_IDS: json.dumps(self.query_ids)
        }
        return requests.get(sync_url, params)

    """ Build async request to fetch query id."""
    def build_async_request(self):
        async_url = self.url + ADNW_REQUEST_API + '/'
        self.validate_params(is_sync_request=False)
        return requests.post(async_url, json=self.get_params())

    def get_params(self):
        metrics_list = list(map(adnw_utils.raw_value, self.metrics))
        breakdowns_list = list(map(adnw_utils.raw_value, self.breakdowns))
        filters_list = list(map(adnw_utils.raw_statement, self.filters))
        return {
            ACCESS_TOKEN_KEY: self.access_token,
            METRICS: json.dumps(metrics_list),
            BREAKDOWNS: json.dumps(breakdowns_list),
            Date_SINCE: self.date_since.strftime(date_format),
            Date_UNTIL: self.date_until.strftime(date_format),
            FILTERS: json.dumps(filters_list),
            ORDERING_COLUMN: self.ordering_column.value,
            ORDERING_TYPE: self.ordering_type.value,
            LIMIT: self.limit,
            AGGREGATION_PERIOD: self.aggregation_period.value
        }

    def validate_params(self, is_sync_request: bool = True, is_using_query_ids: bool = False):
        if not is_using_query_ids and len(self.metrics) == 0:
            raise Exception("The parameter metrics is required.")

        diff = (self.date_until - self.date_since).days
        if diff >= 7 or diff < 0:
            print("date since: " + str(self.date_since))
            print("date since: " + str(self.date_until))
        if diff >= 7:
            raise Exception("The time range needs to be at most 7 days.")
        if diff < 0:
            raise Exception("The time range is invalid.")

        if is_sync_request:
            if len(self.metrics) > 1:
                raise Exception("The parameter metrics cannot be more than 1 with Sync Request.")
            if len(self.breakdowns) > 2:
                raise Exception("The parameter breakdowns cannot be more than 2 with Sync Request.")
            if self.limit > 1000:
                raise Exception("The parameter limit cannot be more than 1000 with Sync Request.")
        else:
            if self.limit > 10000:
                raise Exception("The parameter limit cannot be more than 10000 with Async Request.")

        if is_using_query_ids:
            if len(self.query_ids) <= 0:
                raise Exception("The query ids are not valid in the request.")



