import enum
import string

# Params Key
METRICS = 'metrics'
BREAKDOWNS = 'breakdowns'
FILTERS = 'filters'
VALUES = 'values'
ORDERING_COLUMN = 'ordering_column'
ORDERING_TYPE = 'ordering_type'
LIMIT = 'limit'
AGGREGATION_PERIOD = 'aggregation_period'
Date_SINCE = 'since'
Date_UNTIL = 'until'


# Required Parameters for Sync Request without query ids
class Metric(enum.Enum):
    ADNW_REVENUE = 'fb_ad_network_revenue'
    ADNW_REQUEST = 'fb_ad_network_request'
    ADNW_CMP = 'fb_ad_network_cpm'
    ADNW_CLICK = 'fb_ad_network_click'
    ADNW_IMPRESSION = 'fb_ad_network_imp'
    ADNW_FILLED_REQUEST = 'fb_ad_network_filled_request'
    ADNW_FILL_RATE = 'fb_ad_network_fill_rate'
    ADNW_CTR = 'fb_ad_network_ctr'
    ADNW_SHOW_RATE = 'fb_ad_network_show_rate'
    ADNW_VIDEO_GUARANTEE_REVENUE = 'fb_ad_network_video_guarantee_revenue'
    ADNW_VIDEO_VIEW = 'fb_ad_network_video_view'
    ADNW_VIDEO_VIEW_RATE = 'fb_ad_network_video_view_rate'
    ADNW_VIDEO_MRC = 'fb_ad_network_video_mrc'
    ADNW_VIDEO_MRC_RATE = 'fb_ad_network_video_mrc_rate'
    ADNW_BIDDING_REQUEST = 'fb_ad_network_bidding_request'
    ADNW_BIDDING_RESPONSE = 'fb_ad_network_bidding_response'


# Optional Parameters
class Breakdown(enum.Enum):
    COUNTRY = 'country'
    DELIVERY_METHOD = 'delivery_method'
    PLATFORM = 'platform'
    APP = 'app'
    PROPERTY = 'property'
    PLACEMENT = 'placement'
    DEAL = 'deal'


class FilterOperator(enum.Enum):
    IN = 'in'


class Filter:
    def __init__(self, condition: Breakdown, operator: FilterOperator, filters: [string]):
        self.condition = condition
        self.operator = operator
        self.filters = filters

    def raw_value(self):
        return {'field': self.condition.value,
                'operator': self.operator.value,
                'values': self.filters}

    def __hash__(self):
        return self.condition.__hash__()

    def __eq__(self, other):
        return self.condition == other.condition


class OrderingColumn(enum.Enum):
    TIME = 'time'
    VALUE = 'value'


class OrderingType(enum.Enum):
    ASCENDING = 'ascending'
    DESCENDING = 'descending'


class AggregationPeriod(enum.Enum):
    DAY = 'day'
    TOTAL = 'total'
