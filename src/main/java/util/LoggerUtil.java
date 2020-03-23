package util;

import http.HttpMethod;
import http.HttpVersion;
import org.apache.logging.log4j.Logger;

import java.util.Date;

/**
 * @author lixiaodong
 * @time 2019/11/16 9:02 下午
 * @description
 */
public class LoggerUtil {
    public static void logRequest(String hostAddress, Logger log, HttpMethod httpMethod, String httpVersion, String requestURI, Date beginTime, String userAgent){
        StringBuilder sb = new StringBuilder();
        sb.append(" HTTP_METHOD: ").append(httpMethod.toString()).append(" HTTP_VERSION: ").append(httpVersion)
                .append(" REQUEST_URI: ").append(requestURI).append(" BEGIN_TIME: ").append(beginTime)
                .append(" USER_AGENT: ").append(userAgent);
        log.info(sb.toString());
    }

    public static void logResponse(Logger log, HttpMethod httpMethod, String httpVersion, int status, Date time){
        StringBuilder sb = new StringBuilder();
        sb.append("HTTP_METHOD: ").append(httpMethod.toString()).append(" HTTP_VERSION: ")
                .append(httpVersion).append(" HTTP_STATUS: ").append(status).append(" TIME: ").append(time);
        log.info(sb.toString());
    }
}
