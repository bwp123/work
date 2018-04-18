package com.akazam.wap.client.model;

public class IteratorWarnRecord {
	public final static int LEVEL_1 = 1;// 重大
	public final static int LEVEL_2 = 2;// 严重
	public final static int LEVEL_3 = 3;// 主要
	public final static int LEVEL_4 = 4;// 一般
	private int id;
	private int severity_level;// 故障等级，
	private String severity_name;// 故障等级名称，
	private String receiver;// 故障接口人：告警邮件接收邮箱
	private int send_flag;// 是否已发送告警邮件，1:0=是：否

	public int getSeverity_level() {
		return severity_level;
	}

	public void setSeverity_level(int severity_level) {
		this.severity_level = severity_level;
	}

	public String getSeverity_name() {
		return severity_name;
	}

	public void setSeverity_name(String severity_name) {
		this.severity_name = severity_name;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public int getSend_flag() {
		return send_flag;
	}

	public void setSend_flag(int send_flag) {
		this.send_flag = send_flag;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
