package com.akazam.wap.iterator.test;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Test2
{
	public final static String UA = "Mozilla/5.0 (Linux; U; Android 2.3.7; zh-cn; MB525 Build/GWK74; CyanogenMod-72) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1";

	public static String doHttpGet(String urlString, String token)
	{
		String result = null;
		for (int i = 0; i < 3; i++)
		{
			HttpURLConnection conn = null;
			InputStream is = null;
			ByteArrayOutputStream outStream = null;
			try
			{
				//System.out.println("GETING:"+urlString);
				URL url = new URL(urlString);
				conn = (HttpURLConnection) url.openConnection();
				conn.setDoOutput(true);
				conn.setRequestProperty("User-Agent", UA);
				conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
				conn.setRequestProperty("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
				conn.setRequestProperty("Accept-Encoding", "gzip, deflate");
				//if (!isEmpty(token))
				{
					conn.setRequestProperty("Cookie", token);
				}
				conn.setRequestProperty("Connection", "keep-alive");
				conn.setRequestProperty("Cache-Control", "no-cache");
				int statusCode = conn.getResponseCode();
				if (statusCode == HttpURLConnection.HTTP_MOVED_TEMP 
						|| conn.getResponseCode() == HttpURLConnection.HTTP_MOVED_PERM)
				{
					//System.out.println("REDIRECT:"+statusCode);
					return doHttpGet(conn.getHeaderField("location"),token);
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

					outStream = new ByteArrayOutputStream();
					byte[] buffer = new byte[8192];
					int len = 0;
					while ((len = is.read(buffer)) != -1)
					{
						outStream.write(buffer, 0, len);
					}
					result = new String(outStream.toByteArray(), "utf-8");
					//System.out.println("RESPONSE:"+result);
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
		return result;
	}
	
	public static void main(String[] args)
	{
		System.out.println(doHttpGet("http://v.vnet.mobi/portal/480//live/99999000000460000000011699502001/index.jsp", "JSESSIONID=83923E849B24F1C831693865B234E7D3.net-wap3_02; COOKIEID=90B6E6AE-E868-CBA4-EF6C-2FD7251677FE; Hm_lpvt_c0613b8e4751e75f05122d0b45e0f006=1384998536; Hm_lvt_c0613b8e4751e75f05122d0b45e0f006=1384998536; com.tianyi.tv189.accountNoPas=\"18914034115:]]]cb9d557be38fbcff\"; com.tianyi.tv189.loginType=1; JSESSIONID=3125B61B45577CA8A996FEBDFA352AA9.net-wap3_02"));
	}
}
