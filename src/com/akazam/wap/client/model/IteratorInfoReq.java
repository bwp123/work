package com.akazam.wap.client.model;

import java.util.List;
import java.util.Map;

import com.akazam.wap.client.base.BaseReq;

public class IteratorInfoReq extends BaseReq {
	//key为故障级别，Link为测试记录信息
	private Map<Integer,List<LinkTestInfo>> linkInfos; // 测试信息集合
	private int testObject;// 遍历对象的类型
	private String testObjectName;// 遍历对象的类型名称
	private String reportPath;
	private String fileName;

	public Map<Integer, List<LinkTestInfo>> getLinkInfos() {
		return linkInfos;
	}
	public void setLinkInfos(Map<Integer, List<LinkTestInfo>> linkInfos) {
		this.linkInfos = linkInfos;
	}
	public int getTestObject() {
		return testObject;
	}
	public void setTestObject(int testObject) {
		this.testObject = testObject;
	}
	public String getTestObjectName() {
		return testObjectName;
	}
	public void setTestObjectName(String testObjectName) {
		this.testObjectName = testObjectName;
	}
	public String getReportPath()
	{
		return reportPath;
	}
	public void setReportPath(String reportPath)
	{
		this.reportPath = reportPath;
	}
	public String getFileName()
	{
		return fileName;
	}
	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

	
	
}
