package org.itat.message.vo;

public class TranObj {
	public final static int SUCCESS = 1;
	public final static int FAILURE = 0;
	
	private int result;
	private String msg;
	private Object obj;
	
	public TranObj(int result, String msg) {
		super();
		this.result = result;
		this.msg = msg;
	}
	
	
	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}



	public TranObj() {
		super();
	}
	
	public void setSuccess() {
		this.setResult(SUCCESS);
	}
	
	public void setFailure() {
		this.setResult(FAILURE);
	}

	/**
	 * 设置操作时候成功
	 * @return
	 */
	public int getResult() {
		return result;
	}
	public void setResult(int result) {
		this.result = result;
	}
	/**
	 * 操作完成之后提示信息
	 * @return
	 */
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	
}
