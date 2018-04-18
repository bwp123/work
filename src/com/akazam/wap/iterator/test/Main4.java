package com.akazam.wap.iterator.test;
import java.io.*;
import java.net.*;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.*;

import com.akazam.wap.iterator.common.DBHelper;
import com.akazam.wap.iterator.test.Main5.ParamsNameValue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class Main4
{
	public final static String homepage="/clt4/home/clt4/sy/index.json";
	public final static String channel="/clt4/home/clt4/pd/index.json";
	public final static String live="/clt4/home/clt4/zb/index.json";
	public final static String prefix = "http://api.tv189.com/Internet?";
	public static final String APP_SECRET = "C225EFE2D5734FB89AA3D8118B3ED354";
	public static final SimpleDateFormat	TIMESTAMP		= new SimpleDateFormat("yyyyMMddHHmmss");
    public static Gson gson = new GsonBuilder().create();
    public static int categoryId=2;
	public final static String APPID="10100002000";
	public static final SimpleDateFormat	DATE_TIMESTAMP		= new SimpleDateFormat("yyyyMMdd");

	public static int HOMEPAGE_ID=6;
	public static int CHANNEL_ID=7;
	public static int LIVE_ID=8;
	
	public static Integer toInteger(String str)
	{
		try{return Double.valueOf(str.trim()).intValue();}catch(Exception e){};
		return null;
	}
	
	public static int countOccurrence(String str,String findStr )
	{
		int lastIndex = 0;
		int count =0;
		while(lastIndex != -1){
		       lastIndex = str.indexOf(findStr,lastIndex);
		       if( lastIndex != -1){
		             count ++;
		             lastIndex+=findStr.length();
		      }
		}
        return count;
	}  
	
	public static List<ParamsNameValue> signParams(List<ParamsNameValue> params)
	{
		params.add(new ParamsNameValue("time",TIMESTAMP.format(Calendar.getInstance().getTime())));
		params.add(new ParamsNameValue("sign",EncoderByMd5(encodeUrl(params,  false)+"&"+APP_SECRET)));
		return params;
	}
	
	public static String genTokenUrl()
	{
		List<ParamsNameValue> params=new ArrayList<ParamsNameValue>();
		params.add(new ParamsNameValue("NetType","WIFI"));
		params.add(new ParamsNameValue("ac","login"));
		params.add(new ParamsNameValue("accessType","20"));
		params.add(new ParamsNameValue("appid",APPID));
		params.add(new ParamsNameValue("clienttype","3"));
		params.add(new ParamsNameValue("devid","000001"));
		params.add(new ParamsNameValue("f","mobilelogin"));
		params.add(new ParamsNameValue("flogin","1"));
		params.add(new ParamsNameValue("imsiid","null"));
		params.add(new ParamsNameValue("os","Android 4.1.2"));
		params.add(new ParamsNameValue("pcode","4"));
		params.add(new ParamsNameValue("resolution","480*800"));
		params.add(new ParamsNameValue("terminal","SCH-I829"));
		params.add(new ParamsNameValue("version","5.0.8.8"));
		return prefix+encodeUrl(signParams(params),true);
	}
	
	public static String genLoginUrl(String token)
	{
		List<ParamsNameValue> params=new ArrayList<ParamsNameValue>();
		params.add(new ParamsNameValue("ac","login"));
		params.add(new ParamsNameValue("appid",APPID));
		params.add(new ParamsNameValue("clienttype","3"));
		params.add(new ParamsNameValue("devid","000001"));
		params.add(new ParamsNameValue("f","userlogin"));
		params.add(new ParamsNameValue("pcode","4"));
		params.add(new ParamsNameValue("token",token));
		params.add(new ParamsNameValue("uname","18013102268"));
		params.add(new ParamsNameValue("upass","114119"));
		return prefix+encodeUrl(signParams(params),true);
	}
	
	@SuppressWarnings("unchecked")
	public static String getLiveVodPlayUrl(String contentid,String liveId,String token)
	{
		List<ParamsNameValue> params=new ArrayList<ParamsNameValue>();
		params.add(new ParamsNameValue("ac", "play"));
		params.add(new ParamsNameValue("appid",APPID));
		params.add(new ParamsNameValue("clienttype", "3"));
		params.add(new ParamsNameValue("contentid", contentid));
		params.add(new ParamsNameValue("devid","000001"));
		params.add(new ParamsNameValue("f", "mobileScheduleInfo"));
		params.add(new ParamsNameValue("liveid", liveId));
		params.add(new ParamsNameValue("pcode","4"));
		params.add(new ParamsNameValue("token",token));
		String url=prefix+encodeUrl(signParams(params),true);
		String result =doHttpGet(url);
		String playUrl=null;
		try{
			Map<String,Object> values=(Map<String,Object>)gson.fromJson(result, Object.class);
			Map<String,Object> list=(Map<String,Object>)values.get("info");
			playUrl=((List<Map<String,Object>>)list.get("videos")).get(0).get("playUrl").toString();
			
		}catch(Exception e){};
		return playUrl;
	}
	
	@SuppressWarnings("unchecked")
	public static String[] getLiveVod(String liveId,String token)
	{
		String[] urls=null;
		List<ParamsNameValue> params=new ArrayList<ParamsNameValue>();
		params.add(new ParamsNameValue("ac", "program"));
		params.add(new ParamsNameValue("appid",APPID));
		params.add(new ParamsNameValue("clienttype", "3"));
		params.add(new ParamsNameValue("date", DATE_TIMESTAMP.format(Calendar.getInstance().getTime())));
		params.add(new ParamsNameValue("devid","000001"));
		params.add(new ParamsNameValue("f", "schedule"));
		params.add(new ParamsNameValue("liveid", liveId));
		params.add(new ParamsNameValue("pcode","4"));
		params.add(new ParamsNameValue("token",token));
		String url=prefix+encodeUrl(signParams(params),true);
		String result =doHttpGet(url);
		String nowpid=null;
		String prepid=null;
		String preliveid=null;
		try{
			Map<String,Object> values=(Map<String,Object>)gson.fromJson(result, Object.class);
			List<Map<String,Object>> list=(List<Map<String,Object>>)values.get("info");
			nowpid=getLiveNowProgram( liveId, token);
			for(int i=0;i<list.size();i++)
			{
//				System.out.println("========================");
//				System.out.println(list.get(i).get("pid"));
//				System.out.println(list.get(i).get("liveid"));
//				System.out.println(list.get(i).get("title"));
//				System.out.println(list.get(i).get("duration"));
//				System.out.println(list.get(i).get("starttime"));
//				System.out.println(list.get(i).get("endtime"));
//				System.out.println(list.get(i).get("state"));
//				System.out.println(list.get(i).get("isedit"));
//				System.out.println(list.get(i).get("isrecord"));
				if(!isEmpty(nowpid)&&nowpid.equals(list.get(i).get("pid")+""))
				{
					break;
				}
				prepid=Double.valueOf(list.get(i).get("pid")+"").intValue()+"";
				preliveid=list.get(i).get("liveid")+"";
			}
			String playUrl=getLiveVodPlayUrl( prepid,preliveid, token);
			if(!isEmpty(playUrl))
			{
				urls=new String[]{"","","",null,null,playUrl,prepid+","+preliveid};
			}
		}catch(Exception e){};
		return urls;
	}
	
	
	
		@SuppressWarnings("unchecked")
	public static String getLiveNowProgram(String liveId,String token)
	{
		String livePid=null;
		List<ParamsNameValue> params=new ArrayList<ParamsNameValue>();
		params.add(new ParamsNameValue("ac", "program"));
		params.add(new ParamsNameValue("appid",APPID));
		params.add(new ParamsNameValue("clienttype", "3"));
		params.add(new ParamsNameValue("devid","000001"));
		params.add(new ParamsNameValue("f", "nowProgram"));
		params.add(new ParamsNameValue("liveid", liveId));
		params.add(new ParamsNameValue("pcode","4"));
		params.add(new ParamsNameValue("token",token));
		String url=prefix+encodeUrl(signParams(params),true);
		String result =doHttpGet(url);
		try{
			Map<String,Object> values=(Map<String,Object>)gson.fromJson(result, Object.class);
			List<Map<String,Object>> list=(List<Map<String,Object>>)values.get("info");
			for(int i=0;i<list.size();i++)
			{
				//System.out.println("===========now=============");
				livePid=list.get(i).get("pid")+"";
				//System.out.println(livePid);
			}
		}catch(Exception e){};
		return livePid;
	}
		
	public static String genJsonUrl(String token,String path,int clickType)
	{
		List<ParamsNameValue> params=new ArrayList<ParamsNameValue>();
		if(clickType==1)
		{
			String[] ps=path.split("&");
			params.add(new ParamsNameValue("ac","program"));
			params.add(new ParamsNameValue("appid",APPID));
			for(int i=0;i<ps.length;i++)
			{
				String[] str=ps[i].split("=");
				if(str[0].equals("categoryname"))
				params.add(new ParamsNameValue(str[0],str[1]));
			}
			params.add(new ParamsNameValue("clienttype","3"));
			params.add(new ParamsNameValue("devid","000001"));
			params.add(new ParamsNameValue("eggpain","true"));
			params.add(new ParamsNameValue("f","programSearch"));
			params.add(new ParamsNameValue("lan","1"));
			params.add(new ParamsNameValue("orderby","createtime"));
			params.add(new ParamsNameValue("otherAttrs","title,description,contentId,productId,himgM7,length,contentType,categoryId"));
			params.add(new ParamsNameValue("pcode","4"));
			params.add(new ParamsNameValue("pno","1"));
			for(int i=0;i<ps.length;i++)
			{
				String[] str=ps[i].split("=");
				if(str[0].equals("productid"))
				params.add(new ParamsNameValue(str[0],str[1]));
			}
			params.add(new ParamsNameValue("psize","10"));
			params.add(new ParamsNameValue("ptype","1"));
			params.add(new ParamsNameValue("token",token));
		}
		else
		{
			params.add(new ParamsNameValue("ac","index"));
			params.add(new ParamsNameValue("appid",APPID));
			params.add(new ParamsNameValue("clienttype","3"));
			params.add(new ParamsNameValue("devid","000001"));
			params.add(new ParamsNameValue("f","getIndex"));
			params.add(new ParamsNameValue("lan","1"));
			params.add(new ParamsNameValue("path",path));
			params.add(new ParamsNameValue("pcode","4"));
			params.add(new ParamsNameValue("token",token));
			params.add(new ParamsNameValue("type","omspath"));
		}
		return prefix+encodeUrl(signParams(params),true);
	}
	
	@SuppressWarnings("unchecked")
	public static String genCollectionUrl(String contentId,String token)
	{
		List<ParamsNameValue> params=new ArrayList<ParamsNameValue>();
		params.add(new ParamsNameValue("ac", "action"));
		params.add(new ParamsNameValue("appid",APPID));
		params.add(new ParamsNameValue("clienttype", "3"));
		params.add(new ParamsNameValue("contentType", "1"));
		params.add(new ParamsNameValue("contentid", contentId));
		params.add(new ParamsNameValue("devid","000001"));
		params.add(new ParamsNameValue("f", "addfavourite_v2"));
		params.add(new ParamsNameValue("token",token));
		String result = prefix+encodeUrl(signParams(params),true);
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public static String getDownloadUrl(String contentId,String token)
	{
		List<ParamsNameValue> params=new ArrayList<ParamsNameValue>();
		params.add(new ParamsNameValue("ac", "play"));
		params.add(new ParamsNameValue("appid", APPID));
		params.add(new ParamsNameValue("clienttype", "3"));
		params.add(new ParamsNameValue("contentType", "1"));
		params.add(new ParamsNameValue("contentid", contentId));
		params.add(new ParamsNameValue("devid", "000001"));
		params.add(new ParamsNameValue("f", "DownLoadInfo"));
		params.add(new ParamsNameValue("pcode", "4"));
		params.add(new ParamsNameValue("token",token));
		String result = prefix+encodeUrl(signParams(params),true);
		result=doHttpGet(result);
		String url=null;
		if(!isEmpty(result))
		{
			Map<String,Object> values=(Map<String,Object>)gson.fromJson(result, Object.class);
			if(values!=null)
			{
				try
				{
					url=((List<Map<String,Object>>)((Map<String,Object>)values.get("info")).get("videos")).get(0).get("playUrl").toString();
				}
				catch(Exception e)
				{
				}
			}
		}
		return url;
	}
	
	@SuppressWarnings("unchecked")
	public static String getPlayUrl(String contentId,String liveId,String token)
	{
		List<ParamsNameValue> params=new ArrayList<ParamsNameValue>();
		if(!isEmpty(liveId))
		{
			params.add(new ParamsNameValue("ac", "play"));
			params.add(new ParamsNameValue("appid", APPID));
			params.add(new ParamsNameValue("clienttype", "3"));
			params.add(new ParamsNameValue("devid", "000001"));
			params.add(new ParamsNameValue("f", "mobileLivePlayInfo"));
			params.add(new ParamsNameValue("liveid", liveId));
			params.add(new ParamsNameValue("pcode", "4"));
			params.add(new ParamsNameValue("token",token));
		}
		else
		{
			params.add(new ParamsNameValue("ac", "play"));
			params.add(new ParamsNameValue("appid", APPID));
			params.add(new ParamsNameValue("clienttype", "3"));
			params.add(new ParamsNameValue("contentid", contentId));
			params.add(new ParamsNameValue("devid", "000001"));
			params.add(new ParamsNameValue("f", "mobilePlayInfo"));
			params.add(new ParamsNameValue("freeflag", "0"));
			params.add(new ParamsNameValue("pcode", "4"));
			params.add(new ParamsNameValue("token",token));
		}
		String result = prefix+encodeUrl(signParams(params),true);
		result=doHttpGet(result);
		//System.out.println(result);
		Map<String,Object> values=(Map<String,Object>)gson.fromJson(result, Object.class);
		String url=null;
		try
		{
			List<Map<String,Object>> videos=((List<Map<String,Object>>)((Map<String,Object>)values.get("info")).get("videos"));
			Map<String,Object> video=null;
			Integer quality=null;
			for(int i=0;i<videos.size();i++)
			{
				String tmp=videos.get(i).get("quality").toString();
				if(tmp.toUpperCase().startsWith("P"))
				{
					tmp=tmp.substring(1);
				}
				Integer currentQuality=Integer.valueOf(tmp);
				if(quality==null||currentQuality<quality)
				{
					quality=currentQuality;
					video=videos.get(i);
				}
			}
			url=video.get("playUrl").toString();
		}
		catch(Exception e)
		{
		}
		return url;
	}
	
	
	public static String EncoderByMd5(String str)
	{
		// Log.d(str);
		char hexDigits[] =
		{ '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		try
		{
			byte[] btInput = str.getBytes("utf-8");
			// 获得MD5摘要算法的 MessageDigest 对象
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// 使用指定的字节更新摘要
			mdInst.update(btInput);
			// 获得密文
			byte[] md = mdInst.digest();
			// 把密文转换成十六进制的字符串形式
			int j = md.length;
			char str_char[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++)
			{
				byte byte0 = md[i];
				str_char[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str_char[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str_char);
		}
		catch (Exception e)
		{
		}
		return null;
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
			return name+"="+value;
		}
	}

	public static boolean isEmpty(String str)
	{
		return str == null || str.trim().length() == 0;
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

	public static String doHttpGet(String urlString)
	{
		String result = "";
		for(int i=0;i<3;i++)
		{
			HttpURLConnection conn = null;
			InputStream is = null;
			ByteArrayOutputStream outStream = null;
			try
			{
				URL url = new URL(urlString);
				conn = (HttpURLConnection) url.openConnection();
				conn.setDoInput(true);
				conn.setDoOutput(true);
				conn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
				conn.setRequestProperty("User-Agent", "Java/1.6.0_33");
				conn.setRequestProperty("Accept", "text/html, image/gif, image/jpeg, *; q=.2, */*; q=.2");
				//conn.setRequestProperty("Connection", "close");
				int statusCode = conn.getResponseCode();
				if (statusCode == HttpURLConnection.HTTP_MOVED_TEMP || conn.getResponseCode() == HttpURLConnection.HTTP_MOVED_PERM)
				{
					//System.out.println("REDIRECT:" + statusCode);
					return doHttpGet(conn.getHeaderField("location"));
				}
				else
				{
					if (statusCode != 200)
					{
						is = conn.getErrorStream();
					}
					else
					{
						is = conn.getInputStream();
					}
	                if(conn.getErrorStream()!=null)
	                {
	                	continue;
	                }
					outStream = new ByteArrayOutputStream();
					byte[] buffer = new byte[8192];
					int len = 0;
					while ((len = is.read(buffer)) != -1)
					{
						outStream.write(buffer, 0, len);
					}
					result = new String(outStream.toByteArray(), "utf-8");
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
		return result==null?"":result;
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String,Object> parseGson(String str)
	{
	    if(isEmpty(str))
	    {
	    	return null;
	    }
		return (Map<String,Object>)gson.fromJson(str, Object.class);
	}
	
	
	public static String getToken()
	{
		String result=doHttpGet(genTokenUrl());
		Map<String,Object> values=parseGson(result);
		if(values!=null)
		{
			result=(String)values.get("token");
		}
		return result;
	}
	
    public static class Node
    {
    	String title;
    	String token;
    	String path;
    	int ct;
    	Node next;
    	public static Map<String,String> visiteds=new HashMap<String,String>();
    	public static List<String> channels=new ArrayList<String>();
    	public static List<String[]> realtimes=new ArrayList<String[]>();
    	public static List<String[]> records=new ArrayList<String[]>();
    	public Node(String title,String token,String path,int ct)
    	{
        	this.title= title;
        	this.token=token;
        	this.path=path;
        	this.ct=ct;
    	}
		
		public void append(List<Node> nodes)
		{
			Node tail=this;
			while(tail.next!=null)
			{
				tail=tail.next;
			}
			if(nodes!=null)
			{
				for(int i=0;i<nodes.size();i++)
				{
					if(i==0)
					{
						tail.next=nodes.get(i);
					}
					else
					{
						nodes.get(i-1).next=nodes.get(i);
					}
				}
			}
		}
		
		public void visit(boolean isGetChannel)
		{
			Node node=this;
			while(node!=null)
			{
				node.append(getChildNodes(node,isGetChannel));
				node=node.next;
				if(Node.records.size()>=50)break;
			}
		}
		
		public String toString()
		{
			return title+"_"+path;
		}
		
		@SuppressWarnings("unchecked")
		public static List<Node> getChildNodes(Node node,boolean isGetChannel)
		{
			String title=node.title;
			String token=node.token;
			String path=node.path;
			int ct=node.ct;
			visiteds.put(path, "");
			String url=genJsonUrl( token, path,ct);
			String result=doHttpGet(url);
			Map<String,Object> values=null;
			try{values=(Map<String,Object>)gson.fromJson(result,  Object.class);}catch(Exception e){}
			if(title.endsWith("->null"))
			{
				String currentName=getCurrentName(values);
				if(!isEmpty(currentName))
				{
					title=title.substring(0,title.lastIndexOf("->null"))+"->"+currentName;
				}
				else
				{
					title=title.substring(0,title.lastIndexOf("->null"));
				}
			}
			System.out.println("visiting "+title+", current path="+path);
			//System.out.println(result);
			List<Node> subVisites=new ArrayList<Node>();
			if(countOccurrence(title,"->")==1)
			{
				channels.add("name="+title+",path="+path);
				if(isGetChannel)
				return subVisites;
			}
			if(values==null)
			{
				return subVisites;
			}
			for(Iterator<String> it=values.keySet().iterator();it.hasNext();)
			{
				String key=it.next();
				Object value=values.get(key);
				//||key.equals("tags")
				//||key.equals("more")
				if(key.equals("tabs")||key.equals("data")||key.equals("children")||key.equals("info")||key.equals("channels"))
				{
				   if(key.equals("more"))
				   {
					   List newValue=new ArrayList();
					   newValue.add(value);
					   value=newValue;
				   }
				   else if(key.equals("info")||key.equals("children"))
				   {
					   if(value instanceof Map&&((Map)(value)).containsKey("data"))
					   {
						   value=((Map)(value)).get("data");
					   }
				   }
				   if(value instanceof List)
				   {
					   List newValue=new ArrayList();
					   for(int i=0;i<((List)value).size();i++)
					   {
						   Object subvalue=((List)value).get(i);
						   if(subvalue instanceof Map&&((Map)(subvalue)).containsKey("data"))
						   {
							   Object subdata=((Map)(subvalue)).get("data");
							   if(subdata instanceof List)
							   {
								   newValue.addAll((List)subdata);
							   }
						   }
					   }
					   if(newValue.size()>0)
					   value=newValue;
				   }
				   if(value instanceof List)
				   {
					   List<Object> ls=(List<Object>)value;
					   for(int i=0;i<ls.size();i++)
					   {
						   if(ls.get(i) instanceof Map)
						   {
							   Map<String,Object> tab=(Map<String,Object>)ls.get(i);
							   String subname=getName(tab);
							   Integer clickType=tab.containsKey("clickType")?toInteger(tab.get("clickType").toString()):null;
							   if(key.equals("info")||tab.containsKey("liveId"))
							   {
								   clickType=0;
							   }
							   String subpath=null;
							   if(clickType!=null)
							   {
								   if(clickType.intValue()==0)
								   {
									   String str="";
									   if(tab.containsKey("liveId"))
									   {
										   //点播节目
										   str=("name="+title+"->"+subname+",contentId="+tab.get("contentId")+",liveId="+tab.get("liveId")+",freeLiveId="+tab.get("freeLiveId")+","+"productId="+tab.get("productId"));
										   if(countOccurrence(title+"->"+subname,"->")==1)
										   {
												String liveId=(String)tab.get("liveId");
												if(isEmpty(liveId))
												{
													liveId=(String)tab.get("freeLiveId");
												}
												if(!isEmpty(liveId))
												{
													 channels.add("name="+title+"->"+subname+",liveId="+liveId);
													 String playUrl=getPlayUrl("",liveId,token);
													 if(!isEmpty(playUrl))
													 {
														 System.out.println(playUrl);
														 String[] schedules=getLiveVod((String)tab.get("liveId"), token);
														 if(schedules!=null)
														 {
															 realtimes.add(new String[]{subname,title+"->"+subname,liveId,null,null,playUrl,liveId,schedules[5],schedules[6]});
														 }
														 else
														 {
															 realtimes.add(new String[]{subname,title+"->"+subname,liveId,null,null,playUrl,liveId});
														 }
													 }
												}
										   }
									   }
									   else
									   {
										   str=("name="+title+"->"+subname+",contentId="+tab.get("contentId")+","+"productId="+tab.get("productId"));
										   String contentId = (String)tab.get("contentId");
										   if(!isEmpty(contentId))
										   {
											   String collectUrl=genCollectionUrl(contentId,token);
											   String downloadUrl=getDownloadUrl(contentId,token);
											   String playUrl=getPlayUrl(contentId,"",token);
											   if(!isEmpty(downloadUrl)&&!isEmpty(playUrl)&&downloadUrl.startsWith("http://"))
											   {
												   System.out.println("collectUrl:"+collectUrl);
												   System.out.println("downloadUrl:"+downloadUrl);
												   System.out.println("playUrl:"+playUrl);
												   records.add(new String[]{subname,title+"->"+subname,contentId,collectUrl,downloadUrl,playUrl,contentId});
											   }
										   }
									   }
									   System.out.println(str);
								   }
								   else if(clickType.intValue()==2)
								   {
									   //wap网页链接
								   }
								   else if(clickType.intValue()==1)
								   {
									   //参数链接
									   subpath=tab.get("clickParam")==null?"":tab.get("clickParam").toString();
								   }
								   else
								   {
									   subpath=getPath(tab);
								   }
							   }
							   else
							   {
								   subpath=getPath(tab);
							   }
							   if(!isEmpty(subpath)&&!visiteds.containsKey(subpath))
							   {
								   Integer c = Integer.valueOf(clickType==null?"-1":clickType.toString());
								   Node n=new Node(title+"->"+subname,token,subpath,c);
								   subVisites.add(n);
							   }
						   }
					   }
				   }
				}
			}
			return subVisites;
		}
		
		
    }
	
	
	public static String getName(Map<String,Object> map)
	{
		if(map.containsKey("name"))
		{
			return map.get("name").toString();
		}
		else if(map.containsKey("title"))
		{
			return map.get("title").toString();
		}
		else if(map.containsKey("label"))
		{
			return map.get("label").toString();
		}
		else if(map.containsKey("areaName"))
		{
			return map.get("areaName").toString();
		}
		else if(map.containsKey("liveName"))
		{
			return map.get("liveName").toString();
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static String getCurrentName(Map<String,Object> map)
	{
		String result=null;
		try
		{
			result=getName((Map<String,Object>)map.get("label"));
		}
		catch(Exception e)
		{
			try
			{
				List<Map<String, Object>> list = ((List<Map<String,Object>>)map.get("data"));
				if(list.size()==1)
				{
					result=getName(list.get(0));
					if(result.length()>0)
					{
						result+="(海报)";
					}
				}
			}
			catch(Exception e1)
			{
				
			}
		}
		return result;
	}
	
	public static String getPath(Map<String,Object> map)
	{
		String path=null;
		for(Iterator<Object> it=map.values().iterator();it.hasNext();)
		{
		    Object value=it.next();
			if(value!=null&&(value.toString().endsWith(".json")||value.toString().indexOf(".json")>=0))
			{
				path=value.toString();
			}
		}
		return path;
	}
	
	

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		Map<String,String> channelMap=null;
		String token=getToken();
		System.out.println(doHttpGet(genLoginUrl(token)));
		//遍历直播
		Node.channels.clear();
		Node.records.clear();
		Node.realtimes.clear();
		new Node("直播",token,live,3).visit(true);
		for(int i=0;i<Node.channels.size();i++)
		{
		   System.out.println(Node.channels.get(i));
		}
		channelMap=DBHelper.saveChannels(categoryId,LIVE_ID,Node.channels,true);
		DBHelper.saveVideos(categoryId, LIVE_ID, channelMap, Node.realtimes, true);
		//遍历频道
		Node.channels.clear();
		Node.records.clear();
		Node.realtimes.clear();
		new Node("频道",token,channel,3).visit(true);
		List<String> channels=new ArrayList<String>();
		for(int i=0;i<Node.channels.size();i++)
		{
			channels.add(Node.channels.get(i));
		}
		channelMap=DBHelper.saveChannels(categoryId,CHANNEL_ID,channels,false);
		for(int i=0;i<channels.size();i++)
		{
		   String c=(channels.get(i));
		   String[] strs=c.split(",");
		   String[] names=strs[0].split("=");
		   String[] paths=strs[1].split("=");
		   //if(names[1].endsWith("->教育"))
		   {
			   Node.channels.clear();
			   Node.records.clear();
			   Node.realtimes.clear();
			   new Node(names[1],token,paths[1],3).visit(false);
			   DBHelper.saveVideos(categoryId, CHANNEL_ID, channelMap, Node.records, false);
		   }
		}
//		System.out.println(genCollectionUrl("32027354",token));
//		System.out.println(getDownloadUrl("32027354",token));
//		System.out.println(getPlayUrl("","98000000000000000001376526737052",token));
//		System.out.println(getPlayUrl("32027354","",token));
		//直播播节目:name=直播->新视觉->新视觉英超,liveId=98000000000000000001376526737052,freeLiveId=null,productId=1000000131
		//System.out.println(getPlayUrl(String contentId,"98000000000000000001376526737052",token));
		//String baseUrl=getJsonUrl(token);
		//System.out.println(baseUrl);
		//new Node("客户端5X",token,baseUrl,3).visit();
		//visit("频道",token,"/clt4/home/clt4/pd/v201307/index.json");
		//new Node("TV189院线",token,"/clt4/kpcp/szyx/index.json",3).visit();
		//new Node("全能看",token,"/clt4/home/qnk/clt4/index.json",3).visit();
		
		
		//
		
		
	}
}
