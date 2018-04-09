class ValidationError(Exception):
    def __init__(self, message, errors=None):
        super().__init__(message)
        self.errors = errors
        self.message = message


class HttpResponseError(Exception):
    def __init__(self, message, errors=None):
        super().__init__(message)
        self.errors = errors
        self.message = message


class InvalidQueryIdError(Exception):
    def __init__(self, message, errors=None):
        super().__init__(message)
        self.errors = errors
        self.message = message