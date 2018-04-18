package com.akazam.wap.iterator.base;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import com.akazam.wap.client.base.BaseSendReq;
import com.akazam.wap.client.base.BaseTestCase;
import com.akazam.wap.client.model.IteratorInfoReq;
import com.akazam.wap.client.model.IteratorWarnRecord;
import com.akazam.wap.client.model.Link;
import com.akazam.wap.client.model.LinkTestInfo;
import com.akazam.wap.framework.Constant;
import com.akazam.wap.framework.core.utils.StringUtils;
import com.akazam.wap.iterator.test.IteratorTestCase;
import com.google.gson.Gson;

public class IteratorTestSuite
{
	private Map<String, Link> links = new HashMap<String, Link>();
	private int maxLevel = -1;
	private int completed = 0;
	private int total = 0;
	private int error = 0;
	private Link head;
	private Link pointer;
	private List<String> whiteBaseLinks = new ArrayList<String>();
	private String userAgent = "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; Trident/4.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; InfoPath.2)";
	private int threadCount = -1;
	private int testObject;// 遍历对象的类型
	private String testObjectName;// 遍历对象的类型名称
	private String whiteLinks;
	private String blackLinks;
	private List<String> blackLinkList=new ArrayList<String>();
	private String url;
	private String send_url;// 请求地址
	private String configName = null;
	private String reportFilePath;// 遍历报告文件路径
	private String fileName;// 遍历报告文件名称
	private String savePath;// 遍历报告保存路径

	private boolean flag = false;

	public void addCompleted()
	{
		completed++;
	}

	public void threadCompleted()
	{
		threadCount--;
	}

	public void foundError()
	{
		error++;
	}

	public String getReportFilePath()
	{
		return reportFilePath;
	}

	public void setReportFilePath(String reportFilePath)
	{
		this.reportFilePath = reportFilePath;
	}

	public String getFileName()
	{
		return fileName;
	}

	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

	public String getSavePath()
	{
		return savePath;
	}

	public void setSavePath(String savePath)
	{
		this.savePath = savePath;
	}

	public Map<String, Link> getLinks()
	{
		return links;
	}

	public void setLinks(Map<String, Link> links)
	{
		this.links = links;
	}

	public int getMaxLevel()
	{
		return maxLevel;
	}

	public void setMaxLevel(int maxLevel)
	{
		this.maxLevel = maxLevel;
	}

	public int getCompleted()
	{
		return completed;
	}

	public void setCompleted(int completed)
	{
		this.completed = completed;
	}

	public int getTotal()
	{
		return total;
	}

	public void setTotal(int total)
	{
		this.total = total;
	}

	public int getError()
	{
		return error;
	}

	public void setError(int error)
	{
		this.error = error;
	}

	public Link getHead()
	{
		return head;
	}

	public void setHead(Link head)
	{
		this.head = head;
	}

	public Link getPointer()
	{
		return pointer;
	}

	public void setPointer(Link pointer)
	{
		this.pointer = pointer;
	}

	public List<String> getWhiteBaseLinks()
	{
		return whiteBaseLinks;
	}

	public void setWhiteBaseLinks(List<String> whiteBaseLinks)
	{
		this.whiteBaseLinks = whiteBaseLinks;
	}

	public String getUserAgent()
	{
		return userAgent;
	}

	public void setUserAgent(String userAgent)
	{
		this.userAgent = userAgent;
	}

	public int getThreadCount()
	{
		return threadCount;
	}

	public void setThreadCount(int threadCount)
	{
		this.threadCount = threadCount;
	}

	public int getTestObject()
	{
		return testObject;
	}

	public void setTestObject(int testObject)
	{
		this.testObject = testObject;
	}

	public String getTestObjectName()
	{
		return testObjectName;
	}

	public void setTestObjectName(String testObjectName)
	{
		this.testObjectName = testObjectName;
	}

	public String getWhiteLinks()
	{
		return whiteLinks;
	}

	public void setWhiteLinks(String whiteLinks)
	{
		this.whiteLinks = whiteLinks;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public String getSend_url()
	{
		return send_url;
	}

	public void setSend_url(String send_url)
	{
		this.send_url = send_url;
	}

	public String getConfigName()
	{
		return configName;
	}

	public void setConfigName(String configName)
	{
		this.configName = configName;
	}

	public void showProgress()
	{
		System.out.println("Error:" + error + "\tTotal:" + total + "\t" + "Completed:" + (completed > total ? total : completed) + "\t" + "Percent:" + (100f * (completed > total ? total : completed) / total) + "%\t");
	}

	public Link addLink(Link link)
	{
		if (link != null)
		{
			Link l = links.get(link.getUrl());
			if (l != null)
			{
				return l;
			}
			else
			{
				pointer.setNext(link);
				total++;
				links.put(link.getUrl(), link);
				pointer = link;
				return link;
			}
		}
		return null;
	}

	public IteratorTestSuite(int testType)
	{
		initSuite(testType);
		initSendUrl();
	}

	private void initSuite(int testType)
	{
		testObject = testType;
		if (this.testObject == BaseTestCase.TEST_WAP_480)
		{
			configName = "wap.properties";
			testObjectName = BaseTestCase.TEST_WAP_480_VERSION_NAME;
		}
		Properties props = new Properties();
		InputStream in = null;
		try
		{
			System.out.println(configName);
			in = new FileInputStream(new File(configName));
			props.load(in);
			url = props.getProperty("config.url");
			System.out.println(url);
			userAgent = props.getProperty("config.userAgent");
			whiteLinks = props.getProperty("config.whiteLinks");
			blackLinks = props.getProperty("config.blackLinks");
			if(blackLinks!=null&&blackLinks.length()>0)
			{
				blackLinkList=StringUtils.array2List(blackLinks.split(","));
			}
			maxLevel = Integer.valueOf(props.getProperty("config.maxLevel"));
			flag = true;
		}
		catch (Exception e1)
		{
			System.out.println(new File(configName).getAbsolutePath());
			e1.printStackTrace();
		}
	}

	public void run()
	{
		if (!this.flag)
		{
			return;
		}
		IteratorTestCase testCase = new IteratorTestCase(this);
		Link link = new Link();
		link.setUrl(url);
		links.put(link.getUrl(), link);
		total++;
		head = link;
		pointer = link;
		whiteBaseLinks.add(BaseTestCase.getBaseUrl(link.getUrl()));
		if (whiteLinks != null && whiteLinks.length() > 0)
		{
			String[] strs = whiteLinks.split(",");
			for (int i = 0; i < strs.length; i++)
			{
				whiteBaseLinks.add(strs[i].trim());
			}
		}
		Link l = head;
		testCase.setLink(l);
		int level1ErrorCount = 0;
		int level2ErrorCount = 0;
		List<LinkTestInfo> level0ErrorResultList = new ArrayList<LinkTestInfo>();
		List<LinkTestInfo> level1ErrorResultList = new ArrayList<LinkTestInfo>();
		List<LinkTestInfo> level2ErrorResultList = new ArrayList<LinkTestInfo>();
		List<LinkTestInfo> otherLevelErrorResultList = new ArrayList<LinkTestInfo>();
		List<LinkTestInfo> okResultList = new ArrayList<LinkTestInfo>();
		Map<Integer, List<LinkTestInfo>> map = new HashMap<Integer, List<LinkTestInfo>>();
		LinkTestInfo info = null;
		int i=0;
		String cookie=null;
		for(int j=0;j<10;j++)
		{
			cookie=BaseIteratorTestCase.doLogin();
			if(cookie!=null&&cookie.length()>0)
			{
				break;
			}
		}
		while (l != null)
		{
			i++;
			l.setTestObject(testObject);
			l.setTestObjectName(testObjectName);
			l.setTest_time(System.currentTimeMillis());
			testCase.doTest(cookie);
			l.setSublink_count(l.getSubLinks() == null ? 0 : l.getSubLinks().size());
			info = new LinkTestInfo();
			info.setDescription(l.getDescription());
			info.setSublink_count(l.getSublink_count());
			info.setResult(l.getResult());
			info.setDuration(l.getDuration());
			info.setSize(l.getSize());
			info.setLevel(l.getLevel());
			info.setName(l.getName());
			info.setTestObject(l.getTestObject());
			info.setTestObjectName(l.getTestObjectName());
			info.setStatusCode(l.getStatusCode());
			info.setUrl(l.getUrl());
			info.setTest_time(l.getTest_time());
			info.setSpeed(l.getSpeed());
			if (l.getResult().equals(BaseTestCase.FAIL_STR))
			{
				IteratorWarnRecord iteratorWarnRecord = new IteratorWarnRecord();
				info.setIteratorWarnRecord(iteratorWarnRecord);
				if (l.getLevel() == BaseTestCase.PAGE_LEVEL0)
				{
					iteratorWarnRecord.setSeverity_level(BaseTestCase.LEVEL_1);
					iteratorWarnRecord.setSeverity_name(Constant.getCodeDescription(BaseTestCase.LEVEL_1));
					level0ErrorResultList.add(info);
					map.put(BaseTestCase.LEVEL_1, level0ErrorResultList);
					sendTestRecord(map);
					level0ErrorResultList.clear();
					map.clear();
				}
				else if (l.getLevel() == BaseTestCase.PAGE_LEVEL1)
				{
					level1ErrorResultList.add(info);
					level1ErrorCount++;
				}
				else if (l.getLevel() == BaseTestCase.PAGE_LEVEL2)
				{
					level2ErrorResultList.add(info);
					level2ErrorCount++;
				}
				else
				{// 其它层级
					otherLevelErrorResultList.add(info);
				}
			}
			else
			{
				okResultList.add(info);
			}
			l = l.getNext();
			testCase.setLink(l);
		}
		if (level1ErrorResultList.size() > 0)
		{
			int level = BaseTestCase.LEVEL_3;
			if (level1ErrorCount >= 3)
			{
				level = BaseTestCase.LEVEL_2;
			}
			String levelName = Constant.getCodeDescription(level);
			for (LinkTestInfo lk : level1ErrorResultList)
			{
				lk.getIteratorWarnRecord().setSeverity_level(level);
				lk.getIteratorWarnRecord().setSeverity_name(levelName);
			}
			if (map.containsKey(level))
			{
				map.get(level).addAll(level1ErrorResultList);
			}
			else
			{
				map.put(level, level1ErrorResultList);
			}
		}
		if (level2ErrorResultList.size() > 0)
		{
			int level = BaseTestCase.LEVEL_4;
			if (level2ErrorCount >= 3)
			{
				level = BaseTestCase.LEVEL_3;
			}
			String levelName = Constant.getCodeDescription(level);
			for (LinkTestInfo lk : level2ErrorResultList)
			{
				lk.getIteratorWarnRecord().setSeverity_level(level);
				lk.getIteratorWarnRecord().setSeverity_name(levelName);
			}
			if (map.containsKey(level))
			{
				map.get(level).addAll(level2ErrorResultList);
			}
			else
			{
				map.put(level, level2ErrorResultList);
			}
		}
		if (otherLevelErrorResultList.size() > 0)
		{
			int level = BaseTestCase.LEVEL_4;
			String levelName = Constant.getCodeDescription(level);
			for (LinkTestInfo lk : otherLevelErrorResultList)
			{
				lk.getIteratorWarnRecord().setSeverity_level(level);
				lk.getIteratorWarnRecord().setSeverity_name(levelName);
			}
			if (map.containsKey(level))
			{
				map.get(level).addAll(otherLevelErrorResultList);
			}
			else
			{
				map.put(level, otherLevelErrorResultList);
			}
		}
		map.put(BaseTestCase.DEFAULT_LEVEL, okResultList);
		createReportFile();
		sendTestRecord(map);
		level1ErrorResultList.clear();
		level2ErrorResultList.clear();
		otherLevelErrorResultList.clear();
		map.clear();
	}

	private void sendTestRecord(Map<Integer, List<LinkTestInfo>> map)
	{
		IteratorInfoReq req = new IteratorInfoReq();
		req.setLinkInfos(map);
		req.setMethod(Constant.ITERATOR_RECORD_METHOD);
		req.setTestObject(testObject);
		req.setTestObjectName(testObjectName);
		req.setFileName(this.reportFilePath);
		req.setReportPath(this.reportFilePath);
		try
		{
			BaseSendReq.sendJSon(send_url, new Gson().toJson(req));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 初始化imeter调用地址
	 */
	private void initSendUrl()
	{
		this.send_url = Constant.URL_External;
		HttpURLConnection hc = null;
		try
		{
			hc = (HttpURLConnection) new URL(this.send_url).openConnection();
			hc.setConnectTimeout(3000);
			hc.getResponseCode();
		}
		catch (Exception e)
		{
			//e.printStackTrace();
			this.send_url = Constant.URL_Internal;
		}
		finally
		{
			hc = null;
		}
		flag = true;
	}

	private void createReportFile()
	{
		String[] strs = this.configName.split("\\.");
		String path = strs[0] + "_testresult";
		SimpleDateFormat DF_SHORT_CN = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.CHINESE);
		FileWriter fw = null;
		OutputStreamWriter bw = null;
		Link l = head;
		this.fileName = strs[0] + "_" + DF_SHORT_CN.format(new Date(System.currentTimeMillis())) + ".csv";
		if (StringUtils.isEmpty(savePath))
		{
			this.savePath = path;
		}
		else
		{
			this.savePath = savePath + path;
		}
		try
		{
			File file = new File(savePath);
			File file2 = new File(savePath + "/" + fileName);
			if (!file.exists())
			{
				file.mkdirs();
			}
			bw = new OutputStreamWriter(new FileOutputStream(file2), "GBK");
			bw.write("门户分类,链接地址,测试模块,测试结果,测试描述,文件大小,包含链接数,层次,响应时间(秒),下载速度(KB/s),测试时间\n");
			while (l != null)
			{
				bw.write(l + "\n");
				l = l.getNext();
			}
			this.setReportFilePath(file2.getAbsolutePath());
		}
		catch (Exception e)
		{
			this.reportFilePath = null;
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (bw != null) bw.close();
				if (fw != null) fw.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args)
	{
		IteratorTestSuite suite = new IteratorTestSuite(BaseTestCase.TEST_WAP_480);
		suite.run();
	}

	public List<String> getBlackLinkList()
	{
		return blackLinkList;
	}
}
