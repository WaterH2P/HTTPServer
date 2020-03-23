package Test;

public class Result {
	private int status;
	private String msg;
	private String desc;
	public Result(){
		this(0, "", "");
	}
	public Result(int status, String msg, String desc){
		this.status = status;
		this.msg = msg;
		this.desc = desc;
	}
	
	public void setStatus(int status){
		this.status = status;
	}
	
	public void setMsg(String msg){
		this.msg = msg;
	}
	
	public void setDesc(String desc){
		this.desc = desc;
	}
}
