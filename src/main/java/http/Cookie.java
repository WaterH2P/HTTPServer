package http;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import process.HttpProcess;

import java.util.HashMap;
import java.util.Map;

public class Cookie {

    private HashMap<String,String> data;
    private static final Logger log = LogManager.getLogger(HttpProcess.class);

    public HttpResponse response;


    /**
     * 服务器打给客户端（浏览器）
     * 可能有多个Set-cookie,但一个Cookie对象代表一个Cookie
     * @param data
     * @return
     */
    public String formResponse(HashMap<String,String> data){
        StringBuilder sb = new StringBuilder();

        for(Map.Entry<String,String> entry: data.entrySet()){
            sb.append(entry.getKey() + "=" + entry.getValue() + ";");
        }

        return sb.toString();
    }


    /**
     * 解析cookieData为 Map<>data
     * cookieData 格式为"Cookie: ..."
     * @param cookieData
     */
    public HashMap<String,String> parseCookieData(String cookieData){

        //items 格式 "name=value"
        String[] items = cookieData.split(";");
        HashMap<String, String> data = new HashMap<>();
        for(String item: items){
            String key = item.split("=")[0];
            String value = item.split("=")[1];
            System.out.println("key: " + key + "   " + "value: " + value);
            data.put(key,value);
        }

        return data;
    }

    /**
     * 测试用
     * @return
     */
    public HashMap<String,String> getGeneralData(){
        HashMap<String, String> data = new HashMap<>();
        data.put("uuid",String.valueOf(System.currentTimeMillis()));
        return data;
    }

    public Map<String, String> addCookieData(Map<String, String> data,String key,String value) {
        data.put(key, value);
        return data;
    }


    public HashMap<String, String> getData() {
        return data;
    }

    public void setData(HashMap<String, String> data) {
        this.data = data;
    }
}
