package http;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class HttpJSON {
	private Map<String, Object> json;
	
	public HttpJSON(){
		this.json = new HashMap<>();
	}
	
	public HttpJSON(HttpJSON json){
		this.json = new HashMap<>();
		Map<String, Object> jsonT = json.json;
		for (String key : jsonT.keySet()){
			json.put(key, jsonT.get(key));
		}
	}
	
	public HttpJSON(Object obj){
		this.json = new HashMap<>();
		Class objCLass = (Class) obj.getClass();
		// 得到类中的所有属性集合
		Field[] fs = objCLass.getDeclaredFields();
		for (int i = 0; i < fs.length; i++) {
			Field f = fs[i];
			// 设置些属性是可以访问的
			f.setAccessible(true);
			Object val = new Object();
			try {
				// 得到此属性的值
				val = f.get(obj);
				if (val instanceof Number || val instanceof String || val instanceof HttpJSON){
					this.json.put(f.getName(), val);
				} else {
					this.json.put(f.getName(), val.toString());
				}
			} catch ( IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void put(String key, Object value){
		this.json.put(key, value);
	}
	
	public String toJSON(){
		String jsonStr = "{";
		for (String key : this.json.keySet()){
			jsonStr += "\"" + key + "\":";
			Object value = this.json.get(key);
			if (value instanceof Number || value instanceof Boolean){
				jsonStr += value +",";
			} else if (value instanceof HttpJSON){
				String jsonT = ((HttpJSON) value).toJSON();
				jsonStr += jsonT.substring(1, jsonT.length()-1);
			} else {
				jsonStr += "\"" + value.toString() +"\",";
			}
		}
		jsonStr = jsonStr.substring(0, jsonStr.length() - 1);
		jsonStr += "}";
		return  jsonStr;
	}
}
