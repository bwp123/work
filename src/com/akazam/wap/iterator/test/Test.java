package com.akazam.wap.iterator.test;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Test
{
	public final static String URL = "http://client31.v.vnet.mobi/";

	public static Gson gson = new GsonBuilder().create();

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
		String result = null;
		for (int i = 0; i < 3; i++)
		{
			HttpURLConnection conn = null;
			InputStream is = null;
			ByteArrayOutputStream outStream = null;
			try
			{
				// System.out.println(urlString);
				URL url = new URL(urlString);
				conn = (HttpURLConnection) url.openConnection();
				conn.setDoInput(true);
				conn.setDoOutput(true);
				conn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
				conn.setRequestProperty("User-Agent", "Java/1.6.0_33");
				conn.setRequestProperty("Accept", "text/html, image/gif, image/jpeg, *; q=.2, */*; q=.2");
				// conn.setRequestProperty("Connection", "close");
				int statusCode = conn.getResponseCode();
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
				// System.out.println(result);
				break;
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
		return result;
	}
	
	public static String[] getVodDetailUrls(String name, String path, String token)
	{
		String response = doHttpGet(URL + path + (path.indexOf("?") >= 0 ? "&" : "?") + "UUID=" + token);
		Document doc = Jsoup.parse(response);
		Elements hiddens = doc.select("input[type=hidden]");
		String pid = "";
		String detailurl = "";
		String playmode = "";
		String cid = "";
		String oid = "";
		String blockid = "";
		String collectid = "";
		// System.out.println("name="+name);
		for (int j = 0; j < hiddens.size(); j++)
		{
			Element input = hiddens.get(j);
			if ("pid".equals(input.attr("id")))
			{
				pid = input.attr("value");
			}
			if ("detailurl".equals(input.attr("id")))
			{
				detailurl = input.attr("value");
			}
			if ("playmode".equals(input.attr("id")))
			{
				playmode = input.attr("value");
			}
			if ("cid".equals(input.attr("id")))
			{
				cid = input.attr("value");
			}
			if ("oid".equals(input.attr("id")))
			{
				oid = input.attr("value");
			}
			if ("blockid".equals(input.attr("id")))
			{
				blockid = input.attr("value");
			}
			if ("collectid".equals(input.attr("id")))
			{
				collectid = input.attr("value");
			}
			//System.out.println(input.attr("id")+"="+input.attr("value"));
		}
		List<ParamsNameValue> params = new ArrayList<ParamsNameValue>();
		params.add(new ParamsNameValue("clientid", token));
		params.add(new ParamsNameValue("xmlPath", detailurl));
		params.add(new ParamsNameValue("playtype", "HDUrl"));
		params.add(new ParamsNameValue("playmode", playmode));
		params.add(new ParamsNameValue("contentID", cid));
		params.add(new ParamsNameValue("objectID", oid));
		params.add(new ParamsNameValue("productID", pid));
		params.add(new ParamsNameValue("BlockID", blockid));
		String res = (doHttpGet(URL + "action.php?a=playIt&" + encodeUrl(params, true)));
		String result = "";
		try
		{
			Map<String, Object> values = (Map<String, Object>) gson.fromJson(res, Object.class);
			result = (String)((Map<String, Object>) values.get("msg")).get("result");
			doc = Jsoup.parse(result);
            Elements els=doc.select("input[type=hidden]");
            List<String> names=new ArrayList<String>();
            List<String> pnames=new ArrayList<String>();
            List<String> fees=new ArrayList<String>();
            List<String> purchasetypes=new ArrayList<String>();
            for(int j=0;j<els.size();j++)
            {
            	Element e=els.get(j);
            	if("name".equals(e.attr("class")))
            	{
            		names.add(e.attr("value"));
            	}
            	else if("pname".equals(e.attr("class")))
            	{
            		pnames.add(e.attr("value"));
            	}
            	else if("fee".equals(e.attr("class")))
            	{
            		fees.add(e.attr("value"));
            	}
            	else if("purchasetype".equals(e.attr("class")))
            	{
            		purchasetypes.add(e.attr("value"));
            	}
            }

            for(int i=0;i<names.size();i++)
            {
            	System.out.println("name="+names.get(i)+","+"pname="+pnames.get(i)+","+"fee="+fees.get(i)+","+"purchasetype="+purchasetypes.get(i));
            }
		}
		catch (Exception e)
		{
		}
		//System.out.println(result);
		return null;
	}
	
	public static void main(String[] args)
	{
//		String token = getToken();
//		System.out.println(doLogin(token));
		String token="5289bf673e3d5";
		String[] result=getVodDetailUrls("南京大屠杀幸存者指证元凶", "show.php?path=/ppb/dfys/xwzx/shwx/n88079d169c20163030.xml", token);
		
		
	}

}
