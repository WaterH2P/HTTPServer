package launcher;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.WebServer;

/**
 * @author lixiaodong
 * @time 2019/11/13 10:48 上午
 * @description Main
 */
public class Main {
    private static final Logger log = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        log.info("Logger initialized");

        WebServer server = new WebServer();
        server.start();
    }

}
