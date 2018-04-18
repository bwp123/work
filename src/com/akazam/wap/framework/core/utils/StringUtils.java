package com.akazam.wap.framework.core.utils;

import java.util.ArrayList;
import java.util.List;

public class StringUtils {

	public static boolean isEmpty(String str)
	{
		return str==null||str.trim().length()==0;
	}
	
	public static List<String> array2List(String[] strs)
	{
		List<String> result = new ArrayList<String>(0);
		try
		{
			for (int i = 0; i < strs.length; i++)
			{
				result.add(strs[i]);
			}
		}
		catch (Exception e)
		{
		}
		return result;
	}
	/**
	 * 通过key去掉URL需要去掉的参数
	 * @param url
	 * @param key
	 * @return
	 */
	public static String filterParamByKey(String url,String key){
		int index=url.indexOf("?");
		String sub=url.substring(index+1);
		String [] array=sub.split("&");
		StringBuilder append = new StringBuilder();
		for(int i=0;i<array.length;i++){
			String temp=array[i];
			if(!temp.contains(key)){
				if(i==array.length-1){
					append.append(temp);
				}else{
					append.append(temp).append("&");
				}
			}
		}
		String result=url.substring(0, index+1)+append.toString();
		return result;
	}
}
