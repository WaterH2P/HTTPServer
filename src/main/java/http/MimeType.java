package http;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author lixiaodong
 * @time 2019/11/13 10:48 上午
 * @description 主体的文件类型
 */
public enum MimeType {
    FORM_ENCODED("","application/x-www-form-urlencoded"),

    //text
    TEXT_HTM(".htm","text/html"),
    TEXT_HTML(".html","text/html"),
    TEXT_PLAIN(".txt","text/plain"),
    TEXT_XML(".xml","text/xml"),
    TEXT_XSL(".xsl","text/xml"),
    TEXT_JSON(".json","text/json"),
    TEXT_CSS(".css","text/css"),

    //image
    IMAGE_GIF(".gif","image/gif"),
    IMAGE_JPG(".jpg","image/jpeg"),
    IMAGE_PNG(".png","image/png"),
    IMAGE_BMP(".bmp","image/bmp"),

    //javascript
    APPLICATION_JS(".js","application/javascript"),
    APPLICATION_JSON(".json", "application/json"),

    //oct
    APPLICATION_OCTET("","application/octet-stream");

    private static final Logger log = LogManager.getLogger(MimeType.class);

    private final String postfix;

    private final String type;

    private MimeType(String postfix, String type){
        this.postfix = postfix;
        this.type = type;
    }

    public String  getType(){ return type;}

    public static String getType(String postfix){
        for(MimeType entry : MimeType.values()){
            if(entry.postfix.equals(postfix)){
                return entry.type;
            }
        }
        log.error("Unkown resource type: " + postfix);
        return APPLICATION_OCTET.type;
    }

}
