package process;

import Annotation.ResponseBody;
import http.HttpJSON;
import http.HttpRequest;
import http.HttpResponse;
import http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.LoggerUtil;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

public class HttpProcessDispatch extends HttpProcess {
	
	private static final Logger log = LogManager.getLogger(HttpProcessDispatch.class);
	
    public void processGet(HttpRequest request, HttpResponse response){
		try{ String URI = request.getRequestURI();
			Class controller = controllers.getControllerByURI(URI);
			Method method = controllers.getMethod_GET_ByURI(URI);
			if (method == null){
				new StaticResourceProcess().process(request, response);
			} else {
				Object[] args = {request, response, request.getQueryParams()};
				// 调用处理 GET 请求的方法
				Object s = method.invoke(controller.newInstance(), args);
				if (!response.isSendResponse()){
					if (s==null){
						response.setStatus(HttpStatus.OK_200);
					} else if (method.isAnnotationPresent(ResponseBody.class)){
						String resJSON = new HttpJSON(s).toJSON();
						response.setHeaderJSON(resJSON);
						response.setBody(resJSON.getBytes());
					} else {
						if (s instanceof String){
							if (((String) s).startsWith("redirect:")){
								String redirectURL = ((String) s).substring(9);
								response.sendRedirect(redirectURL);
							} else {
								response.setStatus(HttpStatus.OK_200);
								response.setBody(((String) s).getBytes());
							}
						} else {
							response.setStatus(HttpStatus.OK_200);
						}
					}
					response.sendResponse();
				}
			}
		}  catch( InstantiationException | InvocationTargetException | IllegalAccessException e ){
			e.printStackTrace();
		} catch( IOException e ){
			System.out.println("Socket connection is closed.");
		}
	}
	
	public void processPost(HttpRequest request, HttpResponse response){
		try{
			String URI = request.getRequestURI();
			Class controller = controllers.getControllerByURI(URI);
			Method method = controllers.getMethod_POST_ByURI(URI);
			if (method == null){
				response.sendRedirect("/404.html");
				LoggerUtil.logResponse(log, request.getMethod(), request.getHttpVersion(), response.getStatus(), new Date());
				response.sendResponse();
			} else {
				Object[] args = {request, response, request.getPostParams()};
				// 调用处理 POST 请求的方法
				Object s = method.invoke(controller.newInstance(), args);
				if (!response.isSendResponse()){
					if (s==null){
						response.setStatus(HttpStatus.OK_200);
					} else if (method.isAnnotationPresent(ResponseBody.class)){
						String resJSON = new HttpJSON(s).toJSON();
						response.setHeaderJSON(resJSON);
						response.setBody(resJSON.getBytes());
					} else {
						if (s instanceof String){
							if (((String) s).startsWith("redirect:")){
								String redirectURL = ((String) s).substring(9);
								response.sendRedirect(redirectURL);
							} else {
								response.setStatus(HttpStatus.OK_200);
								response.setBody(((String) s).getBytes());
							}
						} else {
							response.setStatus(HttpStatus.OK_200);
						}
					}
					log.info("HTTP_Method: POST; Controller: " + controller.getName() + "; Method: " + method.getName() + "; Params: " + request.getPostParams());
					LoggerUtil.logResponse(log, request.getMethod(), request.getHttpVersion(), response.getStatus(), new Date());
					response.sendResponse();
				}
			}
		} catch( InstantiationException | InvocationTargetException | IllegalAccessException e ){
			e.printStackTrace();
		} catch( IOException e ){
			System.out.println("Socket connection is closed.");
		}
	}
}
