package http;
/**
 * @author lixiaodong
 * @time 2019/11/13 10:48 上午
 * @description Http状态码
 */
public class HttpStatus {
    public final static int CONTINUE_100 = 100;
    public final static int SWITCHING_PROTOCOLS_101 = 101;
    public final static int PROCESSING_102 = 102;

    public final static int OK_200 = 200;
    public final static int CREATED_201 = 201;
    public final static int ACCEPTED_202 = 202;
    public final static int NON_AUTHORITATIVE_INFORMATION_203 = 203;
    public final static int NO_CONTENT_204 = 204;
    public final static int RESET_CONTENT_205 = 205;
    public final static int PARTIAL_CONTENT_206 = 206;
    public final static int MULTI_STATUS_207 = 207;

    public final static int MULTIPLE_CHOICES_300 = 300;
    public final static int MOVED_PERMANENTLY_301 = 301;
    public final static int MOVED_TEMPORARILY_302 = 302;
    public final static int FOUND_302 = 302;
    public final static int SEE_OTHER_303 = 303;
    public final static int NOT_MODIFIED_304 = 304;
    public final static int USE_PROXY_305 = 305;
    public final static int TEMPORARY_REDIRECT_307 = 307;

    public final static int BAD_REQUEST_400 = 400;
    public final static int UNAUTHORIZED_401 = 401;
    public final static int PAYMENT_REQUIRED_402 = 402;
    public final static int FORBIDDEN_403 = 403;
    public final static int NOT_FOUND_404 = 404;
    public final static int METHOD_NOT_ALLOWED_405 = 405;
    public final static int NOT_ACCEPTABLE_406 = 406;
    public final static int PROXY_AUTHENTICATION_REQUIRED_407 = 407;
    public final static int REQUEST_TIMEOUT_408 = 408;
    public final static int CONFLICT_409 = 409;
    public final static int GONE_410 = 410;
    public final static int LENGTH_REQUIRED_411 = 411;
    public final static int PRECONDITION_FAILED_412 = 412;
    public final static int REQUEST_ENTITY_TOO_LARGE_413 = 413;
    public final static int REQUEST_URI_TOO_LONG_414 = 414;
    public final static int UNSUPPORTED_MEDIA_TYPE_415 = 415;
    public final static int REQUESTED_RANGE_NOT_SATISFIABLE_416 = 416;
    public final static int EXPECTATION_FAILED_417 = 417;
    public final static int UNPROCESSABLE_ENTITY_422 = 422;
    public final static int LOCKED_423 = 423;
    public final static int FAILED_DEPENDENCY_424 = 424;

    public final static int INTERNAL_SERVER_ERROR_500 = 500;
    public final static int NOT_IMPLEMENTED_501 = 501;
    public final static int BAD_GATEWAY_502 = 502;
    public final static int SERVICE_UNAVAILABLE_503 = 503;
    public final static int GATEWAY_TIMEOUT_504 = 504;
    public final static int HTTP_VERSION_NOT_SUPPORTED_505 = 505;
    public final static int INSUFFICIENT_STORAGE_507 = 507;

    public static final int MAX_CODE = 507;

    private static final Code[] codeMap = new Code[MAX_CODE+1];

    static
    {
        for (Code code : Code.values())
        {
            codeMap[code.code] = code;
        }
    }


    public enum Code{
        /*
         * -------------------------------------
         * Informational messages in 1xx series.
         */

        CONTINUE(CONTINUE_100, "Continue"),
        SWITCHING_PROTOCOLS(SWITCHING_PROTOCOLS_101, "Switching Protocols"),
        PROCESSING(PROCESSING_102, "Processing"),

        /*
         * ------------------------------
         * Success messages in 2xx series.
         */

        OK(OK_200, "OK"),
        CREATED(CREATED_201, "Created"),
        ACCEPTED(ACCEPTED_202, "Accepted"),
        NON_AUTHORITATIVE_INFORMATION(NON_AUTHORITATIVE_INFORMATION_203, "Non Authoritative Information"),
        NO_CONTENT(NO_CONTENT_204, "No Content"),
        RESET_CONTENT(RESET_CONTENT_205, "Reset Content"),
        PARTIAL_CONTENT(PARTIAL_CONTENT_206, "Partial Content"),
        MULTI_STATUS(MULTI_STATUS_207, "Multi-Status"),

        /*
         * ----------------------------------
         * Redirection messages in 3xx series.
         */

        MULTIPLE_CHOICES(MULTIPLE_CHOICES_300, "Multiple Choices"),
        MOVED_PERMANENTLY(MOVED_PERMANENTLY_301, "Moved Permanently"),
        MOVED_TEMPORARILY(MOVED_TEMPORARILY_302, "Moved Temporarily"),
        FOUND(FOUND_302, "Found"),
        SEE_OTHER(SEE_OTHER_303, "See Other"),
        NOT_MODIFIED(NOT_MODIFIED_304, "Not Modified"),
        USE_PROXY(USE_PROXY_305, "Use Proxy"),
        TEMPORARY_REDIRECT(TEMPORARY_REDIRECT_307, "Temporary Redirect"),

        /*
         * ------------------------------------
         * Client Error messages in 4xx series.
         */

        BAD_REQUEST(BAD_REQUEST_400, "Bad Request"),
        UNAUTHORIZED(UNAUTHORIZED_401, "Unauthorized"),
        PAYMENT_REQUIRED(PAYMENT_REQUIRED_402, "Payment Required"),
        FORBIDDEN(FORBIDDEN_403, "Forbidden"),
        NOT_FOUND(NOT_FOUND_404, "Not Found"),
        METHOD_NOT_ALLOWED(METHOD_NOT_ALLOWED_405, "Method Not Allowed"),
        NOT_ACCEPTABLE(NOT_ACCEPTABLE_406, "Not Acceptable"),
        PROXY_AUTHENTICATION_REQUIRED(PROXY_AUTHENTICATION_REQUIRED_407, "Proxy Authentication Required"),
        REQUEST_TIMEOUT(REQUEST_TIMEOUT_408, "Request Timeout"),
        CONFLICT(CONFLICT_409, "Conflict"),
        GONE(GONE_410, "Gone"),
        LENGTH_REQUIRED(LENGTH_REQUIRED_411, "Length Required"),
        PRECONDITION_FAILED(PRECONDITION_FAILED_412, "Precondition Failed"),
        REQUEST_ENTITY_TOO_LARGE(REQUEST_ENTITY_TOO_LARGE_413, "Request Entity Too Large"),
        REQUEST_URI_TOO_LONG(REQUEST_URI_TOO_LONG_414, "Request-URI Too Long"),
        UNSUPPORTED_MEDIA_TYPE(UNSUPPORTED_MEDIA_TYPE_415, "Unsupported Media Type"),
        REQUESTED_RANGE_NOT_SATISFIABLE(REQUESTED_RANGE_NOT_SATISFIABLE_416, "Requested Range Not Satisfiable"),
        EXPECTATION_FAILED(EXPECTATION_FAILED_417, "Expectation Failed"),
        UNPROCESSABLE_ENTITY(UNPROCESSABLE_ENTITY_422, "Unprocessable Entity"),
        LOCKED(LOCKED_423, "Locked"),
        FAILED_DEPENDENCY(FAILED_DEPENDENCY_424, "Failed Dependency"),

        /*
         * ------------------------------------
         * Server Error messages in 5xx series.
         */

        INTERNAL_SERVER_ERROR(INTERNAL_SERVER_ERROR_500, "Server Error"),
        NOT_IMPLEMENTED(NOT_IMPLEMENTED_501, "Not Implemented"),
        BAD_GATEWAY(BAD_GATEWAY_502, "Bad Gateway"),
        SERVICE_UNAVAILABLE(SERVICE_UNAVAILABLE_503, "Service Unavailable"),
        GATEWAY_TIMEOUT(GATEWAY_TIMEOUT_504, "Gateway Timeout"),
        HTTP_VERSION_NOT_SUPPORTED(HTTP_VERSION_NOT_SUPPORTED_505, "HTTP Version Not Supported"),
        INSUFFICIENT_STORAGE(INSUFFICIENT_STORAGE_507, "Insufficient Storage");


        private final int code;
        private final String message;

        private Code(int code, String message){
            this.code = code;
            this.message = message;
        }

        public int getCode(){return code;};
        public String getMessage(){return message;}

        public boolean equals(int code){return (this.code == code);}

        @Override
        public String toString()
        {
            return String.format("[%03d %s]",this.code ,this.getMessage());
        }


        public boolean isInformational()
        {
            return HttpStatus.isInformational(this.code);
        }

        public boolean isSuccess()
        {
            return HttpStatus.isSuccess(this.code);
        }

        public boolean isRedirection()
        {
            return HttpStatus.isRedirection(this.code);
        }

        public boolean isClientError()
        {
            return HttpStatus.isClientError(this.code);
        }

        public boolean isServerError()
        {
            return HttpStatus.isServerError(this.code);
        }

    }

    public static Code getCode(int code)
    {
        if (code <= MAX_CODE)
        {
            return codeMap[code];
        }
        return null;
    }

    public static String getMessage(int code)
    {
        Code codeEnum = getCode(code);
        if (codeEnum != null)
        {
            return codeEnum.getMessage();
        }
        else
        {
            return Integer.toString(code);
        }
    }


    public static boolean isInformational(int code)
    {
        return ((100 <= code) && (code <= 199));
    }

    public static boolean isSuccess(int code)
    {
        return ((200 <= code) && (code <= 299));
    }

    public static boolean isRedirection(int code)
    {
        return ((300 <= code) && (code <= 399));
    }

    public static boolean isClientError(int code)
    {
        return ((400 <= code) && (code <= 499));
    }

    public static boolean isServerError(int code)
    {
        return ((500 <= code) && (code <= 599));
    }

}
