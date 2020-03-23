package http;

/**
 * @author lixiaodong
 * @time 2019/11/13 10:48 上午
 * @description 请求方法
 */
public enum HttpMethod {
    GET,
    POST,
    CONNECT,
    DELETE,
    HEAD,
    OPTIONS,
    PATCH,
    PUT,
    TRACE;

    public static boolean isAccept(String method){
        for(HttpMethod m : HttpMethod.values()){
            if(m.name().equals(method)){
                return true;
            }
        }
        return false;
    }

    public static HttpMethod getMethod(String method){
        for(HttpMethod m : HttpMethod.values()){
            if(m.name().equals(method)){
                return m;
            }
        }
        return null;
    }
}
