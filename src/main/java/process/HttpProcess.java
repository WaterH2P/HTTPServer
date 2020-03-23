package process;

import http.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.LoggerUtil;
import util.RequestUtil;

import java.io.*;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lixiaodong
 * @time 2019/11/15 2:06 下午
 * @description Http处理器
 */
public class HttpProcess implements Runnable{
    private Socket clientSocket;
    private static final Logger log = LogManager.getLogger(HttpProcess.class);

    private static final byte CARRIAGE_RETURN = '\r';

    private static final byte LINE_FEED = '\n';

    private HttpRequest request;

    private HttpResponse response;
    
    protected static final ControllerScanner controllers = ControllerScanner.getControllerScanner();
    public HttpProcess(){}
    public HttpProcess(Socket socket){
        this.clientSocket = socket;
    }


    /**
     * 解析请求，获得请求方法、请求 URI 和 HTTP Version，并校验 URI
     * @param input
     */
    private void parseRequest(InputStream input) throws Exception {
        StringBuilder sb = new StringBuilder();
        int cache;
        while ((cache = input.read()) != -1){
            if (cache == CARRIAGE_RETURN && (input.read() == LINE_FEED)){
                break;
            }
            sb.append((char)cache);
        }
        String[] requestLineArr = sb.toString().split(" ");
        if (requestLineArr.length < 3){
            log.error("Request Line elements is not standard.");
            throw new Exception("Request Line elements is not standard.");
        }
        if (HttpMethod.isAccept(requestLineArr[0])){
            request.setMethod(HttpMethod.getMethod(requestLineArr[0]));
        } else {
            log.error("Get the unsupported method.");
            throw new Exception("Get the unsupported method.");
        }

        // uri
        String uri = requestLineArr[1];
        int queryParameter = uri.indexOf('?');
        if (queryParameter >= 0){
            String queryString = uri.substring(queryParameter + 1, uri.length());
            request.setQueryString(queryString);
            request.setQueryParams(queryString);
            uri = uri.substring(0, queryParameter);
        }
        
        // URI 绝对路径转换为相对路径
        if (!uri.startsWith("/")){
            int idx = uri.indexOf("://");
            if(idx != -1){
                idx = uri.indexOf("/", idx + 3);
                if (idx == -1){
                    uri = "";
                } else {
                    uri = uri.substring(idx);
                }
            }
        }

        //TODO 如果uri中携带cookie信息，需要解析cookie
        if (RequestUtil.isStandardURI(uri)){
            request.setRequestURI(uri);
        }

        request.setHttpVersion(requestLineArr[2]);
    }

    /**
     * 解析HTTP请求头
     * @param input
     * @throws IOException
     */
    private void parseHeaders(InputStream input) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cache;
        while (input.available() > 0 && (cache = input.read()) > -1){
            if ((char)cache == '\r'){
                sb.append((char)cache);
                if (input.available() > 0 && (cache = input.read()) > -1){
                    sb.append((char)cache);
                    if ((char)cache == '\n'){
                        if (input.available() > 0 && (cache = input.read()) > -1){
                            sb.append((char)cache);
                            if ((char)cache == '\r'){
                                if (input.available() > 0 && (cache = input.read()) > -1){
                                    sb.append((char)cache);
                                    if ((char)cache == '\n'){
                                        sb.append((char)cache);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                sb.append((char)cache);
            }
        }
    
        Queue<String> headers = Arrays.stream(sb.toString().split("\r\n")).collect(Collectors.toCollection(LinkedList::new));
        while (!headers.isEmpty()){
            String headerString = headers.poll();
            if(RequestUtil.isBlank(headerString)){
                break;
            }
            String[] keyValue = headerString.split(": ");
            request.setHeader(keyValue[0].toLowerCase(), keyValue[1]);
        }

        String contentLength = request.getHeader(HttpHeader.CONTENT_LENGTH.getDesc());
        if (contentLength != null){
            request.setContentLength(Integer.parseInt(contentLength));
        }
        request.setContentType(request.getHeader(HttpHeader.CONTENT_LENGTH.getDesc()));
        
        String contentType = request.getHeader(HttpHeader.CONTENT_TYPE.getDesc());
        // 上传文件
        if (request.getMethod() == HttpMethod.POST && contentType != null && contentType.startsWith("multipart/form-data")){
            String boundary = "\r\n--" + contentType.substring(contentType.indexOf("boundary") + "boundary=".length()) + "--";
            String filename = "";
            while (input.available() > 0 && (cache = input.read()) > -1){
                // 读取文件名称
                if ((char)cache == 'f'){
                    if (input.available() > 0 && (cache = input.read()) == 'i'){
                        if (input.available() > 0 && (cache = input.read()) == 'l'){
                            if (input.available() > 0 && (cache = input.read()) == 'e'){
                                if (input.available() > 0 && (cache = input.read()) == 'n'){
                                    if (input.available() > 0 && (cache = input.read()) == 'a'){
                                        if (input.available() > 0 && (cache = input.read()) == 'm'){
                                            if (input.available() > 0 && (cache = input.read()) == 'e'){
                                                if (input.available() > 0 && (cache = input.read()) == '='){
                                                    if (input.available() > 0 && (cache = input.read()) == '\"'){
                                                        StringBuilder sbfilename = new StringBuilder();
                                                        while (input.available() > 0 && (cache = input.read()) != '\"'){
                                                            sbfilename.append((char)cache);
                                                        }
                                                        filename = URLDecoder.decode(sbfilename.toString(), "UTF-8");
                                                        input.read();
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            // 读取无用行
            while (input.available() > 0 && (cache = input.read()) != '\n'){}
            while (input.available() > 0 && (cache = input.read()) != '\n'){}
            while (input.available() > 0 && (cache = input.read()) != '\n'){}
            
            if (filename.length() > 0){
                File file = new File(this.getClass().getResource("").getPath() + ".." + File.separator + ".."
                        + File.separator + ".." + File.separator + "resource" + File.separator + filename);
                if (!file.exists()){
                    boolean b = file.createNewFile();
                }
                DataOutputStream output = new DataOutputStream(new FileOutputStream(file));
                boolean isOver = false;
                while (input.available() > 0 && (cache = input.read()) > -1 && !isOver){
                    // 判断是否到达文件末尾
                    if ((char)cache == '\r'){
                        byte[] bytes = new byte[boundary.length()];
                        bytes[0] = (byte)cache;
                        int i = 1;
                        while (input.available() > 0 && (cache = input.read()) > -1 && i < boundary.length()){
                            if ((char)cache == boundary.charAt(i)){
                                bytes[i] = (byte)cache;
                                if (i == boundary.length() - 1){
                                    isOver = true;
                                    break;
                                }
                                i++;
                            } else {
                                output.write(bytes, 0, i);
                                output.write(cache);
                                break;
                            }
                        }
                    } else {
                        output.write(cache);
                    }
                }
                output.close();
            }
        } else {
            sb = new StringBuilder();
            while (input.available() > 0 && (cache = input.read()) > -1){
                sb.append((char)cache);
            }
            headers = Arrays.stream(sb.toString().split("\r\n")).collect(Collectors.toCollection(LinkedList::new));
            // 如果空行后面还有数据，就是 POST 请求的表单参数
            if (!headers.isEmpty()){
                request.setPostParams(headers.poll());
            }
        }
    }

    private void parseCookies(HttpRequest request){
        String cookieValue = request.getHeader(HttpHeader.COOKIE.toString());
        Cookie cookie = new Cookie();
        // 浏览器没有 Cookie，为其设置 Cookie
        if (cookieValue == null){
            HashMap<String,String> data = cookie.getGeneralData();
            String cookieStr = cookie.formResponse(data);
            // 给 response;
            response.setHeader(HttpHeader.SET_COOKIE.getDesc(),cookieStr);
        } else {
            Map<String, String> cookieMap = cookie.parseCookieData(cookieValue);
            Set<String> keys = cookieMap.keySet();
            boolean hasUuid = false;
            for(String key:keys){
                if (key.trim().equals("uuid")) {
                    hasUuid = true;
                    break;
                }
            }
            if (!hasUuid) {
                HashMap<String, String> data = cookie.getGeneralData();
                response.setHeader(HttpHeader.SET_COOKIE.getDesc(),cookie.formResponse(data));
            }
        }

    }

    private void setCookie(){
        Cookie cookie = new Cookie();
        HashMap<String,String> data = cookie.getGeneralData();
        String cookieStr = cookie.formResponse(data);
        response.setHeader(HttpHeader.SET_COOKIE.getDesc(), cookieStr);
    }

    @Override
    public void run() {
        try {
            InputStream input = clientSocket.getInputStream();
            OutputStream output = clientSocket.getOutputStream();
            request = new HttpRequest(input);
            response = new HttpResponse(output, request);
            this.parseRequest(input);
            log.info(request.getMethod() + " " + request.getRequestURI());
            this.parseHeaders(input);
            this.parseCookies(request);
            //this.setCookie();
            LoggerUtil.logRequest(clientSocket.getInetAddress().getHostAddress(), log, request.getMethod(), request.getHttpVersion(), request.getRequestURI(),
                    new Date(), request.getHeader("user-agent"));
            if (request.getMethod() == HttpMethod.GET){
                new HttpProcessDispatch().processGet(request, response);
            } else if (request.getMethod() == HttpMethod.POST){
                new HttpProcessDispatch().processPost(request, response);
            } else {
                log.error("Request is not supported.");
                throw new Exception("Request is not supported.");
            }
        } catch (Exception e) {
            log.error("Catch exception from Socket process: ", e);
            e.printStackTrace();
        }
    }
}
