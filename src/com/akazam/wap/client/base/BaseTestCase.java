package com.akazam.wap.client.base;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import com.akazam.wap.framework.core.utils.LogUtils;

public abstract class BaseTestCase extends LogUtils
{

	public final static int OK = 0;// 接口测试成功
	public final static int SUCCESS = 1;
	public final static int FAIL = 0;
	public final static int EXCEPTION = -1;
	public final static int RESPONSE_NOT_200 = 10;// 请求错误
	public final static int REQUEST_CONNECT_ERROR = 15;// 请求连接错误（可能连接超时、连接拒绝等）
	public final static int REQUEST_TIME_OUT = 16;// 连接超时
	public final static int RESPONSE_TIME_OUT = 20;// 响应超时
	public final static int RESPONSE_FORMAT_ERROR = 30;// 格式错误
	public final static int RESPONSE_EMPTY_CONTENT = 40;// 返回内容为空
	public final static int RESPONSE_LOGIC_ERROR = 50;// 逻辑错误
	public final static int SYSTEM_EXCEPTION = 55;// 系统错误（程序异常）
	public final static int RESPONSE_404_ERROR = 60;// 请求返回404错误
	public final static int REQUEST_URL_ERROR = 70;// 请求地址不正确
	public final static int NET_ERROR = 75;// 网络不正常

	public final static int PENDING = 80;
	public final static int BUSY = 90;
	public final static int EMPTY_URL = 100;
	public final static int CONTENT_MISSING = 110;
	public final static int MAIL_HOST_OK = 120;
	public final static int IGNORE_TYPE = 130;
	public final static int NOT_FOUND = 404;
	public final static int HTTP_CODE_404 = 404;
	public final static int HTTP_CODE_200 = 200;
	public final static int HTTP_CODE_206 = 206;
	public final static int HTTP_CODE_302 = 302;

	// 定义故障等级
	public final static int LEVEL_1 = 1;// 重大
	public final static int LEVEL_2 = 2;// 严重
	public final static int LEVEL_3 = 3;// 主要
	public final static int LEVEL_4 = 4;// 一般
	public final static int DEFAULT_LEVEL = 0;// 默认告警级别等于0表示测试OK的记录


	public final static int TEST_WAP_480 = 3;
	
	public final static String TEST_WAP_480_VERSION_NAME = "WAP";
	

	public final static String HTTP_REQUEST_GET = "GET";
	public final static String HTTP_REQUEST_POST = "POST";

	public final static String SUCCESS_STR = "ok";
	public final static String FAIL_STR = "fail";

	private int severity = LEVEL_1;

	public final static int MAX_CONN_RETRIES = 3;// 请求尝试次数
	public String[] names = null;// 测试接口内容
	public String[] urls = null;// 测试接口地址
	public String modelName = null;// 模块名称

	public final static String SEARCH_KEYWORD = "三国";// 搜索游戏关键字

	public final static int PAGE_LEVEL0 = 0;// 页面层级

	public final static int PAGE_LEVEL1 = 1;// 页面层级

	public final static int PAGE_LEVEL2 = 2;// 页面层级

	public static String encodeUrlString(String str)
	{
		try
		{
			return URLEncoder.encode(str, "utf-8");
		}
		catch (Exception e)
		{
		}
		return null;
	}

	public int getSeverity()
	{
		return severity;
	}

	public void setSeverity(int severity)
	{
		this.severity = severity;
	}

	public static String getBaseUrl(String url)
	{
		URL u;
		try
		{
			u = new URL(url);
			return u.getProtocol() + "://" + u.getHost() + (u.getPort() != 80 && u.getPort() > 0 ? (":" + u.getPort()) : "");
		}
		catch (MalformedURLException e)
		{
			e.printStackTrace();
		}
		return "";
	}

	public static String[][] selectors = new String[][]
	{ new String[]
	{ "a[href]", "href" }, new String[]
	{ "[background]", "background" }, new String[]
	{ "img[src]", "src" }, new String[]
	{ "[type=image]", "src" }, new String[]
	{ "script[src]", "src" }, new String[]
	{ "iframe[src]", "src" }, new String[]
	{ "embed[src]", "src" }, new String[]
	{ "bgsound[src]", "src" }, new String[]
	{ "link[rel=stylesheet]", "href" }, new String[]
	{ "link[rel=shortcut icon]", "href" } };

	public static String getTestTypeName(int testType)
	{
		String result = null;
		if (testType == TEST_WAP_480)
		{
			result = TEST_WAP_480_VERSION_NAME;
		}
		return result;
	}
}
