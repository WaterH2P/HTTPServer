package util;

/**
 * @author lixiaodong
 * @time 2019/11/16 10:54 上午
 * @description
 */
public class RequestUtil {

    //TODO 判断URI是否标准
    public static Boolean isStandardURI(String uri){
        return true;
    }

    public static Boolean isBlank(String string){
        return string == null || string.trim().equals("");
    }
}
