package util;

import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.ResourceBundle;

/**
 * @author lixiaodong
 * @time 2019/11/16 5:04 下午
 * @description
 */
public class ConfigUtil {
    private static HashMap<String, String> configs = new HashMap<>();

    static {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("config");
        Enumeration enumeration = resourceBundle.getKeys();
        while (enumeration.hasMoreElements()){
            String key = (String) enumeration.nextElement();
            try {
                String value = new String(resourceBundle.getString(key).getBytes("iso-8859-1"),"gbk");
                configs.put(key, value);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getConfig(String key){
        return configs.get(key);
    }
}
