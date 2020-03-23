package Controller;

import Annotation.ResponseBody;
import Test.Result;
import http.*;
import Annotation.RequestController;
import Annotation.RequestMapping;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.io.*;
import java.util.Map;
import java.util.Scanner;

@RequestController
public class TestController {
	@RequestMapping(value = "/getParams", method = HttpMethod.GET)
	public String getParams(HttpRequest request, HttpResponse response, Map<String, String> params){
		System.out.println("Controller : " + this.getClass().toString() + " ; URI : " + request.getRequestURI() + " ; Params : " + params.toString());
		return params.toString();
	}

	@RequestMapping(value = "/firstShowParams", method = HttpMethod.POST)
	public void firstShowParams(HttpRequest request, HttpResponse response, Map<String, String> params){
		System.out.println("Controller : " + this.getClass().toString() + " ; URI : " + request.getRequestURI() + " ; Params : " + params.toString());
	}

	@RequestMapping(value = "/secondShowParams", method = HttpMethod.POST)
	public void secondShowParams(HttpRequest request, HttpResponse response, Map<String, String> params){
		System.out.println("Controller : " + this.getClass().toString() + " ; URI : " + request.getRequestURI() + " ; Params : " + params.toString());
	}
	
	@ResponseBody
	@RequestMapping(value = "/getJSON", method = HttpMethod.POST)
	public Result getJSON(HttpRequest request, HttpResponse response, Map<String, String> params){
		return new Result(1, "OK", "");
	}
	
	@ResponseBody
	@RequestMapping(value = "/getJSON2", method = HttpMethod.POST)
	public HttpJSON getJSON2(HttpRequest request, HttpResponse response, Map<String, String> params){
		HttpJSON json = new HttpJSON();
		json.put("status", 1);
		json.put("msg", "OK");
		return json;
	}
	
	@RequestMapping(value = "/redirectTest", method = HttpMethod.GET)
	public String redirectTest(HttpRequest request, HttpResponse response, Map<String, String> params){
		return "redirect:/redirect.html";
	}
	
	@ResponseBody
	@RequestMapping(value = "/updateNameAndPassword", method = HttpMethod.POST)
	public Result updateNameAndPassword(HttpRequest request, HttpResponse response, Map<String, String> params){
		Result result = new Result();
		String userName = params.get("userName");
		String password = params.get("password");
		try{
			String path = this.getClass().getResource("").getPath() + ".." + File.separator + ".."
					+ File.separator + ".." + File.separator + "resource" + File.separator + "userInfo.txt";
			File file = new File(path);
			FileWriter fileWritter = new FileWriter(file.getName());
			BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
			bufferWritter.write(userName+"\n"+password);
			bufferWritter.close();
			result.setStatus(1);
			result.setMsg("success");
			result.setDesc("Welcome " + userName);
		} catch( IOException e ){
			e.printStackTrace();
			result.setStatus(0);
			result.setMsg(e.toString());
			result.setDesc("Welcome " + userName);
		}
		return result;
	}
	
	@ResponseBody
	@RequestMapping(value = "/uploadFile", method = HttpMethod.POST)
	public Result uploadFile(HttpRequest request, HttpResponse response, Map<String, String> params){
		Result result = new Result();
		return result;
	}
}
