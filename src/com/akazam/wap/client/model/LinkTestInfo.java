package com.akazam.wap.client.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public  class LinkTestInfo {
	private int id;
    private int level=0;
    private int statusCode;
	private String description;
    private Double duration;
    private Double speed;
	private Long size;
    private String name;
    private String url;
    private String contentType;
    private String charset;
    private String result;
    private int testObject;// 遍历对象的类型
	private String testObjectName;// 遍历对象的类型名称
	private int sublink_count;//包含链接个数
	private IteratorWarnRecord iteratorWarnRecord = null;
	private long test_time;// 测试时间或者故障时间
	private int agent_id;// 测试帐号ID
    public String toString()
    {
    	return "\""+this.testObjectName+"\","
        +this.url+","
		+this.name+","
		+this.result+","
		+this.getDescription()+","
        +(this.getSize()==null?"":this.getSize())+","
        +(this.sublink_count==0?0:sublink_count)+","
        +this.getLevel()+","
		+(this.getDuration()==null?"":(this.getDuration()+""))+","
        +(this.getSpeed()==null?"":this.getSpeed())+","
          +this.getTest_time();
    }
    
    public String getReferByUrl()
    {
    	String result="\"";
    	if(this.getReferencedByLinks()!=null)
    	{
    	   List<String> links=new ArrayList<String>();
    	   for(int i=0;i<this.getReferencedByLinks().size();i++)
    	   {
    		   String url = this.getReferencedByLinks().get(i).getUrl();
			   if(!links.contains(url))
    		   {
    			   links.add(url);
        		   result+=(result.length()==1?"":",")+url;
    		   }
    	   }	
    	}
    	return result+"\"";
    }
	
	public LinkTestInfo newLink(String url, String name,int level) {
		LinkTestInfo l = new LinkTestInfo();
		l.setUrl(url);
		l.setName(name);
		l.setLevel(level);
		return l;
	}


	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}

	public Integer getStatusCode() 
	{
		return statusCode;
	}


	public void setStatusCode(Integer statusCode) 
	{
		this.statusCode = statusCode;
	}


	public String getDescription() 
	{
		return description;
	}


	public void setDescription(String description) 
	{
		this.description = description;
	}


	public Double getDuration() 
	{
		return duration;
	}


	public void setDuration(Double duration) 
	{
		this.duration = duration;
	}


	public Long getSize() 
	{
		return size;
	}


	public void setSize(Long size) 
	{
		this.size = size;
	}


	public String getName() 
	{
		return name;
	}


	public void setName(String name) 
	{
		this.name = name;
	}


	public String getUrl() 
	{
		return url;
	}


	public void setUrl(String url) 
	{
		this.url = url;
	}


	public String getContentType() 
	{
		return contentType;
	}


	public void setContentType(String contentType) 
	{
		this.contentType = contentType;
	}
    public String getCharset() {
		return charset;
	}
	public void setCharset(String charset) {
		this.charset = charset;
	}
    
    public void addReferencedByLinks(LinkTestInfo link)
    {
    	if(link!=null)
    	{
    		if(referencedByLinks==null)
    			referencedByLinks=new Vector<LinkTestInfo>();
    		referencedByLinks.add(link);
    	}
    }

	public List<LinkTestInfo> getReferencedByLinks() 
	{
		return referencedByLinks;
	}

	private List<LinkTestInfo> referencedByLinks;
	
    public Double getSpeed() 
    {
		return speed;
	}


	public void setSpeed(Double speed) 
	{
		this.speed = speed;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
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

	public int getSublink_count() {
		return sublink_count;
	}

	public void setSublink_count(int sublink_count) {
		this.sublink_count = sublink_count;
	}

	public IteratorWarnRecord getIteratorWarnRecord() {
		return iteratorWarnRecord;
	}

	public void setIteratorWarnRecord(IteratorWarnRecord iteratorWarnRecord) {
		this.iteratorWarnRecord = iteratorWarnRecord;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getTest_time() {
		return test_time;
	}

	public void setTest_time(long test_time) {
		this.test_time = test_time;
	}

	public int getAgent_id() {
		return agent_id;
	}

	public void setAgent_id(int agent_id) {
		this.agent_id = agent_id;
	}

	public void setReferencedByLinks(List<LinkTestInfo> referencedByLinks) {
		this.referencedByLinks = referencedByLinks;
	}


	
}
