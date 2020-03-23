package process;

import Annotation.RequestController;
import Annotation.RequestMapping;
import http.HttpMethod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 扫描 root/src/main/java/Controller 下所有 @Controller 的类
 * 使用本框架，必须在 root/src/main/java/Controller 下声明 @Controller
 */
public class ControllerScanner {
	private static final Logger log = LogManager.getLogger(ControllerScanner.class);
	
	private static final ControllerScanner controllerScanner = new ControllerScanner();

	public static ControllerScanner getControllerScanner(){
		return controllerScanner;
	}
	
	private static Set<Class<?>> controllers;
	
	private static Map<String, Method> URIAndMethods_GET;
	private static Map<String, Method> URIAndMethods_POST;
	private static Map<String, Class<?>> URIAndControllers;
	
	/**
	 * singleton
	 */
	private ControllerScanner(){
		try{
			controllers = new HashSet<>();
			URIAndMethods_GET = new HashMap<>();
			URIAndMethods_POST = new HashMap<>();
			URIAndControllers = new HashMap<>();
			scanAnnotation_Controller("", "", true);
			scanAnnotation_ControllerAndMethod();
		} catch( Exception e ){
			e.printStackTrace();
		}
	}
	
	/**
	 * 在 packagePath 下扫描有 @Controller 注解的类
	 * @param packageName : 类的前缀
	 * @param packagePath : 当前查找路径
	 * @param recursive : 是否递归查找
	 */
	private static void scanAnnotation_Controller(String packageName, String packagePath, final boolean recursive){
		if (packageName.length() == 0) {
			packageName = "Controller";
		}
		if (packagePath.length() == 0) {
			// 如果没有指定路径，获取项目的根目录
			String projectRootDir = Thread.currentThread().getContextClassLoader().getResource("").getPath();
			packagePath = projectRootDir + "Controller";
		}
		// 获取此包的目录，建立一个 File
		File dir = new File(packagePath);
		if (!dir.exists() || !dir.isDirectory()) {
			log.info( "There is no class @Controller in " + packagePath + ".");
			return;
		}
		// 如果存在，就获取包下的所有文件包括目录
		File[] dirfiles = dir.listFiles(new FileFilter() {
			// 自定义过滤规则，如果可以循环(包含子目录) 或则是以 .class 结尾的文件
			public boolean accept(File file) {
				return (recursive && file.isDirectory())
						|| (file.getName().endsWith(".class"));
			}
		});
		// 循环所有文件
		for (File file : dirfiles) {
			// 如果是目录则继续扫描
			if (file.isDirectory()) {
				scanAnnotation_Controller(packageName + "." + file.getName(), file.getAbsolutePath(), recursive);
			} else {
				// 如果是 java 类文件，去掉后面的 .class 只留下类名
				String className = file.getName().substring(0, file.getName().length() - 6);
				try {
					// 添加到集合中去
					Class<?> cls = Thread.currentThread().getContextClassLoader().loadClass(packageName + "." + className);
					if (cls.isAnnotationPresent(RequestController.class)){
						controllers.add(cls);
					}
				} catch (ClassNotFoundException e) {
					log.error("Can't find file " + className + " .class");
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 扫描所有 Controller 下的 method，并根据 URI 保存到 Map 中
	 * @throws Exception
	 */
	private static void scanAnnotation_ControllerAndMethod() throws Exception{
		for (Class<?> controller : controllers ){
			Method[] methods = controller.getMethods();
			for (Method method : methods){
				// 有 @RequestMapping 注解的方法
				if (method.isAnnotationPresent(RequestMapping.class)){
					RequestMapping URI = method.getAnnotation(RequestMapping.class);
					if ( URIAndMethods_POST.containsKey(URI.value())){
						throw new Exception("There are multi methods mapping URI : " + URI.value());
					} else {
						if (URI.method() == HttpMethod.POST){
							URIAndMethods_POST.put(URI.value(), method);
							URIAndControllers.put(URI.value(), controller);
						} else if (URI.method() == HttpMethod.GET){
							URIAndMethods_GET.put(URI.value(), method);
							URIAndControllers.put(URI.value(), controller);
						}
					}
				}
			}
		}
	}
	
	public Method getMethod_POST_ByURI(String URI){
		if ( URIAndMethods_POST.containsKey(URI)){
			return URIAndMethods_POST.get(URI);
		}
		return null;
	}
	public Method getMethod_GET_ByURI(String URI){
		if ( URIAndMethods_GET.containsKey(URI)){
			return URIAndMethods_GET.get(URI);
		}
		return null;
	}
	public Class<?> getControllerByURI(String URI){
		if (URIAndControllers.containsKey(URI)){
			return URIAndControllers.get(URI);
		}
		return null;
	}
}
