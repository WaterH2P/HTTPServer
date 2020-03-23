package process;

import http.HttpRequest;
import http.HttpResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * @author lixiaodong
 * @time 2019/11/15 10:48 上午
 * @description 静态资源处理器
 */
public class StaticResourceProcess {
    private static final Logger log = LogManager.getLogger(StaticResourceProcess.class);

    public void process(HttpRequest request, HttpResponse response) throws IOException {
        log.debug("Starting process static resource...");
        response.getStaticResource();
        response.insertHeader();
        response.sendResponse();
    }

}
