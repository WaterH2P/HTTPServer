package server;

import filter.IpFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import process.HttpProcess;
import util.ConfigUtil;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author lixiaodong
 * @time 2019/11/13 10:48 上午
 * @description Server类
 */
public class WebServer{
    private static final Logger log = LogManager.getLogger(WebServer.class);

    private static final String SERVER_HOST = "SERVER_HOST";
    private static final String SERVER_PORT = "SERVER_PORT";
    private static final String CRLF = "\r\n";

    private ServerSocket serverSocket = null;
    private IpFilter ipFilter = new IpFilter();


    public void start() {
        openServerSocket();
        ipFilter.initConfig();
        log.info("Web Server is opened.");
        Executor executor= Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        while (true) {
            Socket clientSocket = null;
            try {
                clientSocket = this.serverSocket.accept();
                clientSocket.setKeepAlive(true);
                clientSocket.setSoTimeout(20000);
                if (!ipFilter.filterIp(clientSocket.getInetAddress().getHostAddress())){
                    clientSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                executor.execute(new HttpProcess(clientSocket));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void processClientRequest(Socket clientSocket) throws IOException {
        InputStream inputStream = clientSocket.getInputStream();
        StringBuilder sb = new StringBuilder();
        BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream));
        String mString = null;
        while ((mString = bf.readLine()).length() > 0){
            sb.append(mString);
            sb.append(CRLF);
            if(mString == null){
                break;
            }
        }
        System.out.println(sb.toString());
        OutputStream outputStream = clientSocket.getOutputStream();
        outputStream.write(("HTTP/1.1 200 OK\n\n<html><body>" +
                "shelton的singleThreadWebServer: " +
                "</body></html>").getBytes());
        outputStream.flush();
        outputStream.close();
        inputStream.close();
    }

    private void openServerSocket(){
        int port = Integer.parseInt(ConfigUtil.getConfig(SERVER_PORT));
        try {
            this.serverSocket = new ServerSocket(port,1,InetAddress.getByName(ConfigUtil.getConfig(SERVER_HOST)));
        } catch (IOException e) {
            log.error("Cannot open port " + port);
            throw new RuntimeException("Cannot open port " + port, e);
        }
    }
}
