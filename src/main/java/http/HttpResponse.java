package http;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.ConfigUtil;
import util.LoggerUtil;

import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lixiaodong
 * @time 2019/11/13 10:48 上午
 * @description 封装Response
 */
public class HttpResponse {

    private static final String WEB_PROJECT_ROOT = "WEB_PROJECT_ROOT";

    public static final int BUFFER_SIZE = 1024;

    private static final Logger log = LogManager.getLogger(HttpResponse.class);

    private OutputStream outputStream;

    private HttpRequest request;

    private String cookieStr;
    private HashMap<String, String> headers = new HashMap<>();
    private StringBuilder resStr = new StringBuilder();
    private int status;
    private byte[] body;
    private boolean isResponse = false;

    public HttpResponse(OutputStream outputStream, HttpRequest request){
        this.outputStream = outputStream;
        this.request = request;
    }

    public void setMimeType(String fileName){
        String postfix = fileName.substring(fileName.lastIndexOf("."));
        setHeader(HttpHeader.CONTENT_TYPE.getDesc(), MimeType.getType(postfix));
    }

    public void getStaticResource() throws IOException {
        String webRootURI = ConfigUtil.getConfig(WEB_PROJECT_ROOT).equals("") || ConfigUtil.getConfig(WEB_PROJECT_ROOT) == null ?
                this.getClass().getResource("").getPath() + "../../../resource/" : ConfigUtil.getConfig(WEB_PROJECT_ROOT);
        System.out.println(webRootURI);
        File staticResource = new File(webRootURI + request.getRequestURI());
        if (staticResource.exists()){
            if (staticResource.isDirectory()){
                File file = new File(staticResource+"/"+ConfigUtil.getConfig("INDEX_FILE"));
                if (staticResource.listFiles().length == 0 || !file.exists()){
                    this.sendRedirect("/404.html");
                } else {
                    this.setStatus(HttpStatus.OK_200);
                    this.writeFile(file);
                    this.setMimeType(file.getName());
                }
            } else {
                this.setStatus(HttpStatus.OK_200);
                this.writeFile(staticResource);
                this.setMimeType(request.getRequestURI());
            }
        } else {
            this.sendRedirect("/404.html");
        }
    }

    private void writeFile(File file) {
        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] cache = new byte[BUFFER_SIZE];
            int read;
            while ((read = fis.read(cache, 0, BUFFER_SIZE)) != -1){
                //outputStream.write(cache, 0, read);
                bos.write(cache,0,read);
            }
            bos.close();
            fis.close();
            setBody(bos.toByteArray());
            //resStr.append(bos.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private byte[] responseToByte( int status) {
        return new StringBuilder().append(HttpVersion.HTTP_1_1).append(" ")
                .append(status).append(" ")
                .append(HttpStatus.getCode(status).getMessage()).append("\r\n\r\n")
                .toString().getBytes();
    }

    public void insertHeader(){
        if(headers.size() != 0){
            StringBuilder headerStr = new StringBuilder();
            for (Map.Entry<String,String> entry : headers.entrySet()) {
                headerStr.append(entry.getKey() + ": "+ entry.getValue() + "\n");
            }
            String header = headerStr.toString();
            //去除hearder最后一个\n，否则格式不准确
            header = header.length() > 0 ? header.substring(0, header.length() - 1) : header;
            //找到状态码的位置 在其后插入header
            int index = resStr.indexOf("\r\n\r\n");
            resStr.insert(index+1, header);
        }
    }


    private byte[] str2Byte(StringBuilder str){
        return str.toString().getBytes();
    }

    public void sendResponse() throws IOException {
        LoggerUtil.logResponse(log, this.request.getMethod(), this.request.getHttpVersion(), this.status, new Date());
        byte[] res = str2Byte(resStr);
        outputStream.write(res);
        if(null != getBody()){
            outputStream.write(getBody());
        }
        outputStream.close();
        isResponse = true;
    }
    
    public boolean isSendResponse(){ return isResponse; }


    public String getCookieStr() {
        return cookieStr;
    }

    public void setCookieStr(String cookieStr) {
        this.cookieStr = cookieStr;
    }

    public void setHeader(String key,String value){
        this.headers.put(key.toLowerCase(),value);
    }

    public String getHeader(String key){
        return this.headers.get(key.toLowerCase());
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
        resStr.append(HttpVersion.HTTP_1_1).append(" ")
                .append(status).append(" ").append(HttpStatus.getCode(status).getMessage()).append("\r\n")
                .append("readyState: 4;").append("\r\n\r\n");
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }
    
    public void sendRedirect(String URI){
        this.status = HttpStatus.FOUND_302;
        resStr.append(HttpVersion.HTTP_1_1).append(" ")
                .append(status).append(" ")
                .append(HttpStatus.getCode(status).getMessage()).append("\r\n")
                .append(HttpHeader.LOCATION.getDesc()).append(": ").append(URI).append("\r\n\r\n");
    }
    
    public void setHeaderJSON(String json){
        this.status = HttpStatus.OK_200;
        resStr.append(HttpVersion.HTTP_1_1).append(" ")
                .append(status).append(" ")
                .append(HttpStatus.getCode(status).getMessage()).append("\r\n")
                .append(HttpHeader.CONTENT_TYPE.getDesc()).append(": ").append(MimeType.APPLICATION_JSON.getType()+";charset=UTF-8").append("\r\n")
                .append(HttpHeader.CONTENT_LENGTH.getDesc()).append(": ").append(json.length()).append("\r\n")
                .append(HttpHeader.CONNECTION.getDesc()).append(": ").append("keep-alive").append("\r\n")
                .append("status: 200;\r\nreadyState: 4;\r\n")
                .append("\r\n");
    }

}
