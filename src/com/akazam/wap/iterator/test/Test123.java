package com.akazam.wap.iterator.test;
import com.akazam.wap.iterator.common.DBHelper;
public class Test123
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
//		String token=Main5.getToken();
//		System.out.println(Main5.doHttpGet(Main5.genLoginUrl(token)));
//		String url=Main5.getLiveVodPlayUrl("934368", "11111111000000050000000000101802", token);
//		System.out.println(url);
		DBHelper.saveErrorLog("mobile", "error", "testData", "url", "testModel", 3, "category");
		
		
	}

}
