package http;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lixiaodong
 * @time 2019/11/13 10:48 上午
 * @description 封装Request请求
 */
public class HttpRequest {
    private static final Logger log = LogManager.getLogger(HttpRequest.class);
    
    private InputStream inputStream;

    private HttpMethod method;

    private String httpVersion;

    private String requestURI;

    private String queryString;
    
    private Map<String, String> queryParams = new HashMap<>();
    
    private Map<String, String> postParams = new HashMap<>();

    private int contentLength;

    private String contentType;

    private HashMap<String, String> headers = new HashMap<>();


    public HttpRequest(InputStream inputStream){
        this.inputStream = inputStream;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public void setHttpVersion(String httpVersion) {
        this.httpVersion = httpVersion;
    }

    public String getRequestURI() {
        return requestURI;
    }

    public void setRequestURI(String requestURI) {
        this.requestURI = requestURI;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }
    
    public void setQueryParams(String queryString) throws UnsupportedEncodingException {
        String[] keyValues = queryString.split("&");
        for (String keyValue : keyValues){
            String[] keyAndValue = keyValue.split("=");
            if (keyAndValue.length == 2){
                String key = keyAndValue[0];
                String value = URLDecoder.decode(keyAndValue[1], "utf-8");
                log.info("GetParams  -->  key: " + key + "  " + "value: " + value);
                this.queryParams.put(key, value);
            }
        }
    }
    
    public String getQueryParam(String name) {
        if (name != null){
            name = name.toLowerCase();
            return this.queryParams.get(name);
        }
        return null;
    }
    public Map<String, String> getQueryParams() {
        return queryParams;
    }

    public void setHeader(String key, String value){
        this.headers.put(key, value);
    }
    
    public void setPostParams(String postParams) {
//        if (postParams.startsWith("{") && postParams.endsWith("}")){
//            int indexL = postParams.indexOf('{');
//            int indexR = postParams.indexOf('}');
//            postParams = postParams.substring(indexL+1, indexR).replace("\"", "");
//            String[] keyValues = postParams.split(",");
//            for (String keyValue : keyValues){
//                String[] keyAndValue = keyValue.split(":");
//                if (keyAndValue.length == 2){
//                    String key = keyAndValue[0];
//                    String value = keyAndValue[1];
//                    log.info("PostParams  -->  key: " + key + "  " + "value: " + value);
//                    this.postParams.put(key, value);
//                }
//            }
//        }
        String[] keyValues = postParams.split("&");
        for (String keyValue : keyValues){
            String[] keyAndValue = keyValue.split("=");
            if (keyAndValue.length == 2){
                String key = keyAndValue[0];
                String value = keyAndValue[1];
                log.info("PostParams  -->  key: " + key + "  " + "value: " + value);
                this.postParams.put(key, value);
            }
        }
    }
    
    public String getPostParam(String name) {
        if (name != null){
            name = name.toLowerCase();
            return this.postParams.get(name);
        }
        return null;
    }
    public Map<String, String> getPostParams() {
        return this.postParams;
    }

    public synchronized String getHeader(String name){
        if (name != null){
            name = name.toLowerCase();
            return this.headers.get(name);
        }
        return null;
    }

    public int getContentLength() {
        return contentLength;
    }

    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
