package com.akazam.wap.iterator.test;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.akazam.wap.client.base.BaseSendReq;
import com.akazam.wap.client.base.BaseTestCase;
import com.akazam.wap.client.model.IteratorInfoReq;
import com.akazam.wap.client.model.IteratorWarnRecord;
import com.akazam.wap.client.model.LinkTestInfo;
import com.akazam.wap.framework.Constant;
import com.akazam.wap.iterator.common.DBHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class MainWapHomePage
{
	public static String baseUri="";
	public static String mobile="18912638032";
	public static String testData;
	public static String token="";
	public static String testModel="";
	public static int maxVodPrograme=100;
	public static int categoryId = 1;
	public final static String URL = "http://v.vnet.mobi/";
	public final static String BASE_URL = getBaseUrl(URL);
	public final static String VOD_URL_PATTERN = "[a-z0-9]{19,}\\.jsp";
	public final static String UA = "Mozilla/5.0 (Linux; U; Android 2.3.7; zh-cn; MB525 Build/GWK74; CyanogenMod-72) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1";
	public final static String HOMEPAGE = "portal/480/home/maintain/syzx/320/index.jsp";
	public final static String FREE = "portal/480/czpd/mianfei/mfsywhq/syzxwh/320sy/index.jsp";
	public final static int ROOT = 200;
	public final static boolean isWindows = System.getProperty("os.name").toLowerCase().indexOf("windows") != -1;
	public static Gson gson = new GsonBuilder().create();
	public static Map<String, String> visits = new HashMap<String, String>();
	public static List<String> ignoreLinkNames = new ArrayList<String>();
	static
	{
		ignoreLinkNames.add("交友");
		ignoreLinkNames.add("客户端");
		ignoreLinkNames.add("个人中心");
		ignoreLinkNames.add("帮助");
		ignoreLinkNames.add("我的订购");
		ignoreLinkNames.add("搜索");
		ignoreLinkNames.add("更多>>");
		ignoreLinkNames.add("更多>");
		ignoreLinkNames.add("更多");
		ignoreLinkNames.add("天翼视讯首页");
	}

	public static boolean isEmpty(String str)
	{
		return str == null || str.trim().length() == 0;
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> parseGson(String str)
	{
		if (isEmpty(str))
		{
			return null;
		}
		return (Map<String, Object>) gson.fromJson(str, Object.class);
	}

	public static class ParamsNameValue
	{
		String name;
		String value;

		public ParamsNameValue(String name, String value)
		{
			this.name = name;
			this.value = value;
		}

		public String getName()
		{
			return name;
		}

		public void setName(String name)
		{
			this.name = name;
		}

		public String getValue()
		{
			return value;
		}

		public void setValue(String value)
		{
			this.value = value;
		}

		public String toString()
		{
			return name + "=" + value;
		}
	}

	public static String encodeUrl(List<ParamsNameValue> list, boolean encode)
	{
		if (list == null || 0 == list.size())
		{
			return "";
		}
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (int loc = 0, lenght = list.size(); loc < lenght; loc++)
		{
			if (first) first = false;
			else
				sb.append("&");
			if (encode)
			{
				try
				{
					if (isEmpty(list.get(loc).getValue()) || isEmpty(list.get(loc).getValue().trim())) sb.append(URLEncoder.encode(list.get(loc).getName(), "utf-8") + "=");
					else
						sb.append(URLEncoder.encode(list.get(loc).getName(), "utf-8") + "=" + URLEncoder.encode(list.get(loc).getValue().trim().replace("+", "plus"), "utf-8"));
				}
				catch (Exception e)
				{
				}
			}
			else
			{
				if (isEmpty(list.get(loc).getValue()) || isEmpty(list.get(loc).getValue().trim()))
				// sb.append(list.get(loc).getName() + "=null");
				sb.append(list.get(loc).getName() + "=");
				else
					sb.append(list.get(loc).getName() + "=" + list.get(loc).getValue().trim().replace("+", "plus"));
			}
		}
		return sb.toString();
	}

	public static String getSubStringByPattern(String str, String pattern)
	{
		String result = null;
		try
		{
			Pattern p = Pattern.compile(pattern);
			Matcher m = p.matcher(str);
			while (m.find())
			{
				result = m.group();
				break;
			}
		}
		catch (Exception e)
		{
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public static String getToken(String urlString, boolean hasOutput)
	{
		String paramString="";
		HttpURLConnection conn = null;
		InputStream is = null;
		ByteArrayOutputStream outStream = null;
		HttpURLConnection.setFollowRedirects(false);
		int statusCode =-1;
		for (int i = 0; i < 3; i++)
		{
			try
			{
				URL url = new URL(urlString);
				conn = (HttpURLConnection) url.openConnection();
				conn.setDoInput(true);
				if (hasOutput) conn.setDoOutput(true);
				conn.setRequestProperty("User-Agent", UA);
				conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
				conn.setRequestProperty("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
				conn.setRequestProperty("Accept-Encoding", "gzip, deflate");
				conn.setRequestProperty("Referer", "http://v.vnet.mobi/portal/wap/home/wdfw/user/index.jsp");
				conn.setRequestProperty("Connection", "keep-alive");
				conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
				if (hasOutput)
				{
					OutputStream os = (OutputStream) conn.getOutputStream();
					OutputStreamWriter osw = new OutputStreamWriter(os, "utf-8");
					List<ParamsNameValue> paramList = new ArrayList<ParamsNameValue>();
					paramList.add(new ParamsNameValue("accountNo", "18912638032"));
					paramList.add(new ParamsNameValue("password", "123123"));
					paramList.add(new ParamsNameValue("sueUrl", "/wap/home/index.jsp"));
					paramList.add(new ParamsNameValue("failUrl", "/480/tpl/10/8/663.jsp"));
					paramList.add(new ParamsNameValue("saveme", "1"));
					paramList.add(new ParamsNameValue("method:userLogin", " 登 录 "));
					paramString = encodeUrl(paramList, true);
					osw.write(paramString);
					osw.flush();
					osw.close();
				}
				statusCode = conn.getResponseCode();
				if(conn.getErrorStream()!=null)continue;
				if (statusCode == HttpURLConnection.HTTP_MOVED_TEMP || conn.getResponseCode() == HttpURLConnection.HTTP_MOVED_PERM)
				{
					Map map = conn.getHeaderFields();
					if (map.get("Set-Cookie") != null)
					{
						token = "";
						if (map.get("Set-Cookie") instanceof Collection)
						{
							for (Iterator it = ((Collection) map.get("Set-Cookie")).iterator(); it.hasNext();)
							{
								Object c = it.next();
								token += (token.length() == 0 ? "" : "; ") + c;
							}
						}
					}
					getToken(conn.getHeaderField("location"),false);
				}
				else
				{
					
					Map map = conn.getHeaderFields();
					if (map.get("Set-Cookie") != null)
					{
						if (map.get("Set-Cookie") instanceof Collection)
						{
							for (Iterator it = ((Collection) map.get("Set-Cookie")).iterator(); it.hasNext();)
							{
								Object c = it.next();
								token += (token.length() == 0 ? "" : "; ") + c;
							}
						}
					}
				}
			}
			catch (Exception e)
			{
				e.printStackTrace(System.out);
			}
			finally
			{
				conn = null;
				try
				{
					if (is != null)
					{
						is.close();
					}
				}
				catch (Exception e)
				{
				}
				try
				{
					if (outStream != null)
					{
						outStream.close();
					}
				}
				catch (Exception e)
				{
				}
				outStream = null;
			}
		}
		testData="登录";
		testModel="登录";
		System.out.println(testData);
		System.out.println(testModel);
		if(isEmpty(token))
		DBHelper.saveErrorLog(mobile, "登录失败,返回代码:"+statusCode, testData, urlString+"?"+paramString, testModel, categoryId, "页面遍历");
		return token;
	}

	public static String doHttpGet(String urlString)
	{
		baseUri=urlString;
		String result = "";
		int statusCode =-1;
		if(urlString.indexOf("/")==-1)
			return result;
		for (int i = 0; i < 3; i++)
		{
			HttpURLConnection conn = null;
			InputStream is = null;
			ByteArrayOutputStream outStream = null;
			try
			{
				//System.out.println("GETING:" + urlString);
				HttpURLConnection.setFollowRedirects(false);
				URL url = new URL(urlString);
				conn = (HttpURLConnection) url.openConnection();
				conn.setDoOutput(true);
				conn.setRequestProperty("User-Agent", UA);
				conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
				conn.setRequestProperty("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
				conn.setRequestProperty("Accept-Encoding", "gzip, deflate");
				if (!isEmpty(MainWapHomePage.token))
				{
					conn.setRequestProperty("Cookie", token);
				}
				conn.setRequestProperty("Connection", "keep-alive");
				conn.setRequestProperty("Cache-Control", "no-cache");
				if(conn.getErrorStream()!=null)continue;
				statusCode = conn.getResponseCode();
				if (statusCode == HttpURLConnection.HTTP_MOVED_TEMP || conn.getResponseCode() == HttpURLConnection.HTTP_MOVED_PERM)
				{
					//System.out.println("REDIRECT:" + statusCode);
					return doHttpGet(conn.getHeaderField("location"));
				}
				else
				{
					if(statusCode==500||statusCode==404)
					{
						break;
					}
					if (statusCode != 200)
					{
						is = conn.getErrorStream();
					}
					else
					{
						is = conn.getInputStream();
					}

					outStream = new ByteArrayOutputStream();
					byte[] buffer = new byte[8192];
					int len = 0;
					while ((len = is.read(buffer)) != -1)
					{
						outStream.write(buffer, 0, len);
					}
					result = new String(outStream.toByteArray(), "utf-8");
					//System.out.println("RESPONSE:" + result);
					break;
				}
			}
			catch (Exception e)
			{
				e.printStackTrace(System.out);
				result = e.getMessage();
			}
			finally
			{
				conn = null;
				try
				{
					if (is != null)
					{
						is.close();
					}
				}
				catch (Exception e)
				{
				}
				try
				{
					if (outStream != null)
					{
						outStream.close();
					}
				}
				catch (Exception e)
				{
				}
				outStream = null;
			}
		}
		System.out.println(testData);
		System.out.println(testModel);
//		System.out.println("====================================================================================================");
		System.out.println(urlString);
//		System.out.println(result);
//		System.out.println("====================================================================================================");
		if(urlString.indexOf("'")>=0&&testData.indexOf("->->")==-1&&urlString.toLowerCase().indexOf("javascript:")==-1&&urlString.indexOf("userLoginAction.html")<0&&urlString.indexOf("collectRevision.html")<0&&((statusCode==500||statusCode==404)))
		{
			String[] testDatas=testData.toString().split("->");
			LinkTestInfo info = new LinkTestInfo();
			info.setDescription(testData+"失败,返回代码:"+statusCode);
			info.setSublink_count(-1);
			info.setResult(testData+"失败,返回代码:"+statusCode);
			info.setDuration(0d);
			info.setSize(0l);
			
			info.setLevel(testDatas.length-1);
			info.setName(testData);
			info.setTestObject(categoryId);
			info.setTestObjectName(testData);
			info.setStatusCode(statusCode);
			info.setUrl(urlString);
			info.setTest_time(System.currentTimeMillis());
			info.setSpeed(-1d);
			
			IteratorWarnRecord iteratorWarnRecord = new IteratorWarnRecord();
			info.setIteratorWarnRecord(iteratorWarnRecord);
			
			int level=info.getLevel();
			if(level>=4)
			{
				level=BaseTestCase.LEVEL_4;
			}
			else if(level==0)
			{
				level=1;
			}
			iteratorWarnRecord.setSeverity_level(level);
			iteratorWarnRecord.setSeverity_name(Constant.getCodeDescription(level));
			List<LinkTestInfo> level0ErrorResultList = new ArrayList<LinkTestInfo>();
			level0ErrorResultList.add(info);
			Map<Integer, List<LinkTestInfo>> map = new HashMap<Integer, List<LinkTestInfo>>();
			map.put(level, level0ErrorResultList);
			IteratorInfoReq req = new IteratorInfoReq();
			req.setLinkInfos(map);
			req.setMethod(Constant.ITERATOR_RECORD_METHOD);
			req.setTestObject(categoryId);
			req.setTestObjectName("遍历");
			req.setFileName("");
			req.setReportPath("");
			try
			{
				BaseSendReq.sendJSon(isWindows?Constant.URL_Internal:Constant.URL_External, new Gson().toJson(req));
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			DBHelper.saveErrorLog(mobile, testData+",返回代码:"+statusCode, testData, urlString, testModel, categoryId, "页面遍历");
		}
		return result==null?"":result;
	}
 
	public static String clearPath(String path)
	{
		String r=path.substring(7);
		while(r.indexOf("//")>=0)
		{
			r=r.replaceAll("//", "/");
		}
		return "http://"+r;
	}
	
	@SuppressWarnings("unchecked")
	public static List<String[]> getChannels(String parentTitle, String jsonPath)
	{
		List<String[]> channels = new ArrayList<String[]>();
		testData="获取首页";
		testModel="首页";
		String response = doHttpGet((jsonPath.indexOf(URL) >= 0 ? "" : URL) + jsonPath);
		Map<String, Object> values = (Map<String, Object>) gson.fromJson(response, Object.class);
		try
		{
			List<Map<String, Object>> qks = (List<Map<String, Object>>) values.get("qkdata");
			for (int i = 0; i < qks.size(); i++)
			{
				String title = (String) qks.get(i).get("title");
				String curpath = (String) qks.get(i).get("curpath");
				if (title != null)
				{
					if (title.equals("导航"))
					{
						
					}
					else
					{
						channels.add(new String[]{title,curpath});
					}
				}
			}
		}
		catch (Exception e)
		{
		}
		return channels;
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
		}
		return "";
	}

	public static List<String[]> getChannelsByUrl(String parentTitle, String url)
	{
		List<String[]> channels = new ArrayList<String[]>();
		String realUrl = (url.startsWith("http") ? "" : URL) + url;
		if("首页->天翼诊聊室".equals(parentTitle))
		{
			channels.add(new String[]{"直播",url});
			return channels;
		}
		if(visits.containsKey(realUrl))
		{
			return channels;
		}
		visits.put(realUrl, "");
		String res = doHttpGet(realUrl);
		Document subdoc = Jsoup.parse(res, baseUri);
		Elements elements = subdoc.select("a[href]");
		for (int j = 0; j < elements.size(); j++)
		{
			Element e = elements.get(j);
			String href = e.attr("abs:href");
			int indexOfNo = href.indexOf("#");
			if (indexOfNo >= 0)
			{
				if (href.indexOf("?", indexOfNo) >= 0)
				{
					href = href.substring(0, indexOfNo) + href.substring(href.indexOf("?", indexOfNo), href.length());
				}
				else
				{
					href = href.substring(0, indexOfNo);
				}
			}
			String text = e.text();
			String baseUrl = getBaseUrl(href);
			if (isEmpty(href) || isEmpty(text) || !baseUrl.equals(BASE_URL) || isVodPrograme(href) || visits.containsKey(href) || ignoreLinkNames.contains(text) || href.indexOf("returnUrl") >= 0)
			{
				continue;
			}
			//System.out.println("name=" + parentTitle + "->" + text + ",path=" + href);
			channels.add(new String[]
			{ parentTitle + "->" + text, href });
		}
		return channels;
	}
	
	public static List<String[]> getUrls(String title,String url)
	{
		List<String[]> urls=new ArrayList<String[]>();
		String realUrl = (url.startsWith("http") ? "" : URL) + url;
		String res = doHttpGet(realUrl);
		Document subdoc = Jsoup.parse(res, baseUri);
		Elements elements = subdoc.select("a[href]");
		for (int j = 0; j < elements.size(); j++)
		{
			Element e = elements.get(j);
			String text = e.text();
			String href = e.attr("href");
			if(!isEmpty(text)&&!isEmpty(href)&&!href.toLowerCase().startsWith("rtsp://")&&(href.indexOf("'")<0)&&href.indexOf("\"")<0)
			{
				href=href.replaceAll("[\\r|\\n]", "");
				String rUrl = (href.startsWith("http") ? "" : URL) + href;
				urls.add(new String[]{text,rUrl});
			}
		}
		return urls;
	}

	public static List<String[]> getPrograms(String title,String url)
	{
		List<String[]> programes = new ArrayList<String[]>();
		if(url==null||url.indexOf(".apk")>=0||url.toLowerCase().indexOf("javascript:")>=0||url.toLowerCase().indexOf(".3gp?")>=0)
			return programes;
		String realUrl = (url.startsWith("http") ? "" : URL) + url;
		if(visits.containsKey(realUrl))
		{
			return programes;
		}
		visits.put(realUrl, "");
		String res = doHttpGet(realUrl);
		Document subdoc = Jsoup.parse(res, baseUri);
		Elements elements = subdoc.select("a[href]");
		for (int j = 0; j < elements.size(); j++)
		{
			Element e = elements.get(j);
			String href = e.attr("href");
			href=href.replaceAll("[\\r|\\n]", "");
			String text = e.text();
			if (isEmpty(href)||ignoreLinkNames.contains(text) )
			{
				continue;
			}
			if(title.startsWith("首页->股票老左")&&isEmpty(text))
			{
				try{
					text=e.parent().previousElementSibling().previousElementSibling().text()+"回看-"+e.parent().previousElementSibling().text();
				}catch(Exception e1){};
			}
			//if (isVodPrograme(href))
			{
				//System.out.println("================>name=" + text + ",path=" + href);
				programes.add(new String[]{ title+"->"+text, href });
			}
		}
		elements = subdoc.select("li[onclick]");
		for (int j = 0; j < elements.size(); j++)
		{
			Element e = elements.get(j);
			String href = e.attr("onclick");
			String text="";
			try
			{
				if(href.indexOf("location.href='")>=0)
				{
					href=href.substring(href.indexOf("location.href='")+"location.href='".length(),href.length()-1);
				}
				else
				{
					href=href.substring(href.indexOf("location='")+"location='".length(),href.length()-1);
				}
				text=((Element)((Element)e.childNode(0)).childNode(1)).text();
			}
			catch(Exception ex)
			{
			}
			if(isEmpty(text))
			{
				try
				{
					text=((Element)((Element)e.childNode(0))).text();
				}
				catch(Exception ex)
				{
				}
			}
			if(isEmpty(text))
			{
				try
				{
					text=((Element)((Element)e.childNode(1))).text();
				}
				catch(Exception ex)
				{
				}
			}
			if (isEmpty(href)||href.indexOf("/")==-1 )
			{
				continue;
			}
			programes.add(new String[]{ text, href });
		}
		elements = subdoc.select("div[onclick]");
		for (int j = 0; j < elements.size(); j++)
		{
			Element e = elements.get(j);
			String href = e.attr("onclick");
			String text="";
			try
			{
				if(href.indexOf("location.href='")>=0)
				{
					href=href.substring(href.indexOf("location.href='")+"location.href='".length(),href.length()-1);
				}
				else
				{
					href=href.substring(href.indexOf("location='")+"location='".length(),href.length()-1);
				}
				text=((Element)((Element)e.childNode(0)).childNode(1)).text();
			}
			catch(Exception ex)
			{
			}
			if(isEmpty(text))
			{
				try
				{
					text=((Element)((Element)e.childNode(0))).text();
				}
				catch(Exception ex)
				{
				}
			}
			if(isEmpty(text))
			{
				try
				{
					text=((Element)((Element)e.childNode(1))).text();
				}
				catch(Exception ex)
				{
				}
			}
			if (isEmpty(href)||href.indexOf("/")==-1 )
			{
				continue;
			}
			programes.add(new String[]{ text, href });
		}
		return programes;
	}
	
	/*直播回看*/
	public static String[] getLiveVod(String url,String title,String name)
	{
		String[] result = null;
		if(url==null)
			return result;
		String realUrl = (url.startsWith("http") ? "" : URL) + url;
		String res = doHttpGet(realUrl);
		Document subdoc = Jsoup.parse(res, baseUri);
		Elements elements = subdoc.select("div[onclick]");
		String rtsp=null;
		for (int j = 0; j < elements.size(); j++)
		{
			Element e = elements.get(j);
			String href = e.attr("onclick");
			String sub = getSubStringByPattern(href, "rtsp://[^\"']+");
			if(!isEmpty(sub))
			{
				rtsp=sub;
			}
		}
		if(!isEmpty(rtsp))
		{
			return (new String[]{ title, name+"->" + title, name+"->" + title, null, null, rtsp,realUrl });
		}
		return result;
	}

	public static String[] getProgramDetailUrls(String name, String url)
	{
		System.out.println("========>name=" + name + ",path=" + url);
		String[] result = null;
		if(url==null||url.indexOf(".apk")>=0||url.toLowerCase().indexOf("javascript:")>=0||url.toLowerCase().indexOf(".3gp?")>=0)
			return result;
		String realUrl = (url.startsWith("http") ? "" : URL) + url;
		if(visits.containsKey(realUrl))
		{
			System.out.println("******************ignore*******************");
			return result;
		}
		visits.put(realUrl, "");
		if(url.startsWith("rtsp://"))
		{
			String[] strs=name.split("->");
			System.out.println("========>"+strs[strs.length-1]);
			System.out.println("========>"+url);
			return (new String[]
			{ strs[strs.length-1], name, name, null, null, url,realUrl });
		}
		String livebutton = "/portal/480/homemain/img/v2013/details/d_buttom03.png";
		String res = doHttpGet(realUrl);
		Document subdoc = Jsoup.parse(res, baseUri);
		Elements titles = subdoc.select("title");
		Elements elements = subdoc.select("a[href]");
		if(titles==null||titles.size()==0)return null;
		boolean isLive = res.indexOf(livebutton) >= 0;
		if (isLive)
		{
			for (int j = 0; j < elements.size(); j++)
			{
				Element e = elements.get(j);
				String href = e.attr("href");
				if (isEmpty(href))
				{
					continue;
				}
				href=href.replaceAll("[\\r|\\n]", "");
				if (href.startsWith("rtsp://") && e.html().indexOf(livebutton) >= 0 && titles.size() > 0)
				{
					//System.out.println("================>"+href);
					//System.out.println("================>"+titles.get(0).text());
					String hk_realUrl=null;
					String hk_playUrl=null;
					try{
						Elements hk_elements = subdoc.select("a[data-qkid=today]");
						hk_realUrl=hk_elements.get(0).attr("data-url");
						String[] livevods=getLiveVod( hk_realUrl,titles.get(0).text(), name);
						if(livevods!=null)
						{
							hk_realUrl=livevods[6];
							hk_playUrl=livevods[5];
							//System.out.println("================>HuiKan:"+hk_playUrl);
						}
					}catch(Exception ex){};
					if(isEmpty(hk_playUrl))
					return (new String[]
					{ titles.get(0).text(), name+"->" + titles.get(0).text(), name+"->" + titles.get(0).text(), null, null, href,realUrl });
					else
						return (new String[]
					{ titles.get(0).text(), name+"->" + titles.get(0).text(), name+"->" + titles.get(0).text(), null, null, href,realUrl,hk_playUrl,hk_realUrl });
				}
			}
		}
		else
		{
			String playUrl=null;
			
			String downloadUrl=null;
			boolean isJu=false;
			for (int j = 0; j < elements.size(); j++)
			{
				Element e = elements.get(j);
				String href = e.attr("href");
				if("播 放".equals(e.text()))
				{
					if((href.startsWith("http://")||href.startsWith("rtsp://")))
					playUrl=href;
					isJu=href.equalsIgnoreCase("javascript:void(0)");
				}
				else if("下 载".equals(e.text()))
				{
					downloadUrl=href;
				}
				else if("1".equals(e.text()))
				{
					if(isJu)
					{
						playUrl=href;
						downloadUrl=href;
					}
				}
			}
			if(playUrl==null)
			{
				playUrl=getSubStringByPattern(res,"videoplayer\\.src=\".*\";");
			    if(!isEmpty(playUrl))
			    {
			    	playUrl=playUrl.substring(17,playUrl.length()-2).replaceAll("&amp;", "&");
			    }
			}
			if(!isEmpty(playUrl)&&!isEmpty(downloadUrl))
			{
				String title = titles.get(0).text();
				downloadUrl=downloadUrl.replaceAll("[\\r|\\n]", "\\s");
				playUrl=playUrl.replaceAll("[\\r|\\n]", "\\s");
				System.out.println("================>"+title);
				System.out.println("================>"+downloadUrl);
				System.out.println("================>"+playUrl);
				return (new String[]
				{ titles.get(0).text(),name+"->"+titles.get(0).text(), name, "", downloadUrl, playUrl,realUrl });
			}
		}
		return result;
	}

	public static boolean isVodPrograme(String url)
	{
		return !isEmpty(getSubStringByPattern(url, VOD_URL_PATTERN));
	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		visits.put(URL, "");
		visits.put(URL.substring(0, URL.length() - 1), "");
		visits.put("/portal/480/home/khdxzxtfl/index.jsp", "");
		visits.put("/portal/480/live/index.jsp", "");
		visits.put("/portal/collectListRevision.html?collectSuc=/480/home/wdfw/wdsc/index.jsp", "");
		visits.put("/portal/480/home/wdfw/bz/index.jsp", "");
		visits.put("/portal/480/home/search/index.jsp", "");
		visits.put("/portal/collectListRevision.html?collectSuc=/480/home/wdfw/wdsc/index.jsp", "");
		token = getToken("http://v.vnet.mobi/portal/480/tpl/10/8/userLoginAction.html", true);
		System.out.println(token);
		List<String> newVodChannels=new ArrayList<String>();
		newVodChannels.add("name=首页->首页,path=http://v.vnet.mobi/");
		newVodChannels.add("name=首页->股票老左,path=http://v.vnet.mobi/portal/480//kpcp/gpzal/index.jsp");
		Map<String, String> vodChannelMap = DBHelper.saveChannels(categoryId+10, ROOT, newVodChannels, false);
		//股票老左
		String channel="股票老左";
		List<String[]> laozuoprograms = new ArrayList<String[]>();
		laozuoprograms.add(new String[]{"首页->"+channel+"->直播咨询","http://v.vnet.mobi/portal/480//kpcp/gpzal/index.jsp?pg=0&mkId=GPLZ1031101"});
		laozuoprograms.add(new String[]{"首页->"+channel+"->直播咨询->今日关注","http://v.vnet.mobi/portal/480/kpcp/gpzal/whq/2013gb/sywhq/hdzq/jrgz/index.jsp"});
		laozuoprograms.add(new String[]{"首页->"+channel+"->直播咨询->直播","http://v.vnet.mobi/portal/480/kpcp/gpzal/whq/2013gb/sywhq/hdzq/jmd/index.jsp"});
		laozuoprograms.add(new String[]{"首页->"+channel+"->操盘宝典->个股","http://v.vnet.mobi/portal/480/kpcp/gpzal/whq/gsbd/index.jsp?pg=3&pg2=0&channelId=01112111"});
		laozuoprograms.add(new String[]{"首页->"+channel+"->操盘宝典->大盘","http://v.vnet.mobi/portal/480/kpcp/gpzal/whq/gsbd/index.jsp?pg=3&pg2=1&channelId=01112111"});
		laozuoprograms.add(new String[]{"首页->"+channel+"->操盘宝典->专家","http://v.vnet.mobi/portal/480/kpcp/gpzal/whq/gsbd/index.jsp?pg=3&pg2=2&channelId=01112111"});
		laozuoprograms.add(new String[]{"首页->"+channel+"->操盘宝典->百科","http://v.vnet.mobi/portal/480/kpcp/gpzal/whq/gsbd/index.jsp?pg=3&pg2=3&channelId=01112111"});
		laozuoprograms.add(new String[]{"首页->"+channel+"->操盘宝典->老左","http://v.vnet.mobi/portal/480/kpcp/gpzal/lzqha/index.jsp?qpg=3&pg2=4&channelId=01112111"});
		List<String[]> lprogrames=new ArrayList<String[]>();
		for(int i=0;i<laozuoprograms.size();i++)
		{
			testData="首页->"+channel;
			testModel="首页->"+channel;
			//if(isWindows&&!laozuoprograms.get(i)[0].endsWith("->直播"))continue;
			List<String[]> programs = getPrograms("首页->"+channel,laozuoprograms.get(i)[1]);
			List<String[]> subprogrames=new ArrayList<String[]>();
			for(int j=0;j<programs.size();j++)
			{
				String[] pros = programs.get(j);
				testData=pros[0];
				testModel=pros[0];
				String[] programe = getProgramDetailUrls(pros[0], pros[1]);
                if(programe!=null)
                {
                   subprogrames.add(programe);
                }
			}
			lprogrames.addAll(subprogrames);
		}
		DBHelper.saveVideos(categoryId+10, Integer.valueOf(vodChannelMap.get("股票老左")), vodChannelMap, lprogrames, false);
        //首页
		List<String[]> channels = getChannels("首页", URL + HOMEPAGE);
		channels.addAll(getChannels("首页", URL + "/portal/480/home/maintain/portal2013/dl/320shb/320shbjson/index.jsp"));
		channels.add(new String[]{"排行榜","http://v.vnet.mobi/portal/480/home/maintain/other/phb/index.jsp"});
		List<String[]> programes=new ArrayList<String[]>();
		for(int i=0;i<channels.size();i++)
		{
			//if(isWindows&&!channels.get(i)[0].equals("焦点推荐"))continue;
			List<String[]> subprogrames=new ArrayList<String[]>();
			System.out.println(channels.get(i)[0]+"************************************"+channels.get(i)[1]);
			testData="首页->"+channels.get(i)[0];
			testModel="首页->"+channels.get(i)[0];
			List<String[]> programs = getPrograms("首页->首页",channels.get(i)[1]);
			for(int j=0;j<programs.size();j++)
			{
				String[] pros = programs.get(j);
				testData="首页->"+channels.get(i)[0]+"->"+pros[0];
				testModel="首页->"+channels.get(i)[0]+"->"+pros[0];
				String[] programe = getProgramDetailUrls("首页->首页"+(programs.get(j)[1].startsWith("rtsp://")?("->"+pros[0]):""), pros[1]);
                if(programe!=null)
                {
                   subprogrames.add(programe);
                }
                else
                {   
                	if(!isVodPrograme( pros[1]))
                	{
                    	String realUrl = (pros[1].startsWith("http") ? "" : URL) + pros[1];
                    	visits.remove(realUrl);
                    	List<String[]> subprograms = getPrograms("首页->首页",pros[1]);
                    	int programsizes=Math.min(5, subprograms.size());
                    	for(int k=0;k<programsizes;k++)
                    	{
            				testData="首页->"+channels.get(i)[0]+"->"+pros[0]+"->"+subprograms.get(k)[0];
            				testModel="首页->"+channels.get(i)[0]+"->"+pros[0]+"->"+subprograms.get(k)[0];
            				String[] subprograme = getProgramDetailUrls("首页->首页"+(subprograms.get(k)[1].startsWith("rtsp://")?("->"+subprograms.get(k)[0]):""), subprograms.get(k)[1]);
            				if(subprograme!=null)
                            {
                               subprogrames.add(subprograme);
                            }
                    	}
                	}
                }
			}
			programes.addAll(subprogrames);
		}
		DBHelper.saveVideos(categoryId+10, Integer.valueOf(vodChannelMap.get("首页")), vodChannelMap, programes, false);

	}

}
