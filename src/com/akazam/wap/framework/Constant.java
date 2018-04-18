package com.akazam.wap.framework;

import java.util.HashMap;
import java.util.Map;

import com.akazam.wap.client.base.BaseTestCase;
import com.akazam.wap.framework.core.utils.ConfigurableConstants;

public class Constant extends ConfigurableConstants
{
	public static final String URL_Internal = "http://localhost:8080/tysx/api";
	public static final String URL_External = "http://218.2.5.202:37289/tv189/api";

	public static final String RESPONSE_OK = "请求成功";// 请求成功
	public static final String RESPONSE_NOT_200_MSG = "请求错误";// 请求错误
	public static final String RESPONSE_TIME_OUT_MSG = "响应超时";// 响应超时
	public static final String RESPONSE_FORMAT_ERROR_MSG = "格式错误";// 格式错误
	public static final String RESPONSE_EMPTY_CONTENT_MSG = "返回内容为空";// 返回内容为空
	public static final String RESPONSE_LOGIC_ERROR_MSG = "逻辑错误";// 逻辑错误
	public static final String RESPONSE_404_ERROR = "请求返回404错误";// 请求返回404错误
	public static final String REQUEST_URL_ERROR = "请求地址不正确";// 请求地址不正确
	public static final String SYSTEM_EXCEPTION_MSG = "系统异常";// 系统异常
	public static final String REQUEST_CONNECT_ERROR_MSG = "请求连接错误";// 请求连接错误
	public static final String NET_ERROR_MSG = "网络不正常";// 网络不正常
	public static final String REQUEST_TIME_OUT_MSG = "连接超时";// 连接超时

	public final static String PENDING_MSG = "待处理";
	public final static String BUSY_MSG = "正在处理";
	public final static String EMPTY_URL_MSG = "空的链接";
	public final static String CONTENT_MISSING_MSG = "内容丢失";
	public final static String MAIL_HOST_OK_MSG = "邮件地址";
	public final static String IGNORE_TYPE_MSG = "忽略请求";
	public final static String NOT_FOUND_MSG = "找不到文件";

	public static final String WARN_LEVEL_1 = "重大";
	public static final String WARN_LEVEL_2 = "严重";
	public static final String WARN_LEVEL_3 = "主要";
	public static final String WARN_LEVEL_4 = "一般";
	public final static String MOBILEPHONE = "18962149371";
	public final static String ITERATOR_RECORD_METHOD = "submitIteratorResult";// 门户遍历发送测试记录调用的方法名

	public static Map<String, String> codeMaps = new HashMap<String, String>();

	static
	{
		initCodeMaps();
	}

	public static void initCodeMaps()
	{
		codeMaps.put(BaseTestCase.OK + "", Constant.RESPONSE_OK);
		codeMaps.put(BaseTestCase.RESPONSE_NOT_200 + "", Constant.RESPONSE_NOT_200_MSG);
		codeMaps.put(BaseTestCase.RESPONSE_TIME_OUT + "", Constant.RESPONSE_TIME_OUT_MSG);
		codeMaps.put(BaseTestCase.RESPONSE_EMPTY_CONTENT + "", Constant.RESPONSE_EMPTY_CONTENT_MSG);
		codeMaps.put(BaseTestCase.RESPONSE_FORMAT_ERROR + "", Constant.RESPONSE_FORMAT_ERROR_MSG);
		codeMaps.put(BaseTestCase.RESPONSE_LOGIC_ERROR + "", Constant.RESPONSE_LOGIC_ERROR_MSG);
		codeMaps.put(BaseTestCase.RESPONSE_404_ERROR + "", Constant.RESPONSE_404_ERROR);
		codeMaps.put(BaseTestCase.REQUEST_URL_ERROR + "", Constant.REQUEST_URL_ERROR);
		codeMaps.put(BaseTestCase.SYSTEM_EXCEPTION + "", Constant.SYSTEM_EXCEPTION_MSG);
		codeMaps.put(BaseTestCase.REQUEST_CONNECT_ERROR + "", Constant.REQUEST_CONNECT_ERROR_MSG);
		codeMaps.put(BaseTestCase.NET_ERROR + "", Constant.NET_ERROR_MSG);
		codeMaps.put(BaseTestCase.REQUEST_TIME_OUT + "", Constant.REQUEST_TIME_OUT_MSG);
		codeMaps.put(BaseTestCase.LEVEL_1 + "", Constant.WARN_LEVEL_1);
		codeMaps.put(BaseTestCase.LEVEL_2 + "", Constant.WARN_LEVEL_2);
		codeMaps.put(BaseTestCase.LEVEL_3 + "", Constant.WARN_LEVEL_3);
		codeMaps.put(BaseTestCase.LEVEL_4 + "", Constant.WARN_LEVEL_4);

		codeMaps.put(BaseTestCase.PENDING + "", Constant.PENDING_MSG);
		codeMaps.put(BaseTestCase.BUSY + "", Constant.BUSY_MSG);
		codeMaps.put(BaseTestCase.EMPTY_URL + "", Constant.EMPTY_URL_MSG);
		codeMaps.put(BaseTestCase.CONTENT_MISSING + "", Constant.CONTENT_MISSING_MSG);
		codeMaps.put(BaseTestCase.NOT_FOUND + "", Constant.NOT_FOUND_MSG);
		codeMaps.put(BaseTestCase.MAIL_HOST_OK + "", Constant.MAIL_HOST_OK_MSG);
		codeMaps.put(BaseTestCase.IGNORE_TYPE + "", Constant.IGNORE_TYPE_MSG);
	}

	public static String getCodeDescription(int statusCode)
	{
		return codeMaps.containsKey(statusCode + "") ? codeMaps.get(statusCode + "") : "";
	}

}
