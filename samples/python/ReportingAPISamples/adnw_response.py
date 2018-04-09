import json
import requests
import adnw_utils
import adnw_exception


class ADNWResponse(object):
    __instance = None

    @staticmethod
    def get_instance():
        if ADNWResponse.__instance is None:
            ADNWResponse()
        return ADNWResponse.__instance

    def __init__(self):
        if ADNWResponse.__instance:
            raise Exception("ADNWResponse class is a singleton!")
        else:
            ADNWResponse.__instance = self

    """ Check error message if status_code is not 200.
        Validate response (make sure the status in Json is "complete") 
        from sync request with query ids if status_code is 200.
    """
    def validate_response(self, _request: requests.request):
        _json = _request.json()
        if _request.status_code != 200:
            _error = _json["error"]
            _message = _error["message"]
            raise adnw_exception.HttpResponseError(_message)
        else:
            try:
                _data = _json["data"]
                if adnw_utils.is_list_empty(_data):
                    raise adnw_exception.InvalidQueryIdError("query id is invalid.")
                else:
                    for each in _data:
                        if each["status"] != "complete":
                            raise adnw_exception.ValidationError("Report is not ready in Backend, please retry later.")
            except KeyError:
                return

    def get_query_id(self, _json: json):
        try:
            query_id = _json["query_id"]
        except KeyError:
            raise adnw_exception.InvalidQueryIdError("Unable to get valid query id.")
        return query_id

