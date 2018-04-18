package com.akazam.wap.client.base;

public class BaseResp {
	protected boolean isError = false; // 是否有异常
	protected String errorCode; // 异常代码
	protected String errorMsg; // 异常信息
	
	public boolean isError() {
		return isError;
	}
	public void setError(boolean isError) {
		this.isError = isError;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

}
