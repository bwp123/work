package com.akazam.wap.client.base;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class BaseSendReq {
	public static String sendJSon(String url ,String json) 
	{
		String result =null;
		HttpURLConnection conn = null;
		InputStream instream = null;
		BufferedReader br =null;
		System.out.println("send json "+json+" to "+ url);
		
		for (int retry = 0; retry < 3; retry++) 
		{
			try 
			{
				//json=URLEncoder.encode(json,"utf-8");
				URL u = new URL(url);
				conn = (HttpURLConnection)u.openConnection();
				conn.setConnectTimeout(30000);
				conn.setReadTimeout(30000);
				conn.setDoInput(true);
				conn.setDoOutput(true);
				conn.setRequestProperty("Content-Type","text/html;charset=UTF-8");
				conn.setRequestProperty("Content-Language","zh-CN");
				conn.setRequestProperty("User-Agent","Apache-HttpClient/UNAVAILABLE (java 1.4)");
				conn.setRequestProperty("Connection", "close");
				//conn.setRequestProperty("request", json);
				conn.setRequestMethod("POST");
				OutputStream os = (OutputStream) conn.getOutputStream();
				OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
				osw.write(json);
				osw.flush();
				osw.close();
				instream = conn.getInputStream();
				StringBuffer sb = new StringBuffer(); 
				String bufferString=null;
				br = new BufferedReader(new InputStreamReader(instream,"UTF-8"));
				while ((bufferString=br.readLine())!=null)
				{
					sb.append(bufferString);
				}
				if(sb.length()>0&&sb.indexOf("\"isError\":false")>=0){
					result=sb.toString();
					break;
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
					if (instream != null)
						instream.close();
				}
				catch (Exception e) 
				{
				}
				try
				{
					if (br != null)
						br.close();
				}
				catch (Exception e) 
				{
				}
			}
		}
		
		return result;
	}
}
