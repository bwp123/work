package com.akazam.wap.iterator.test;

import com.akazam.wap.client.base.BaseSendReq;
import com.akazam.wap.framework.Constant;

public class Test1234
{
	public static void main(String[] args)
	{
		try
		{
			String response=BaseSendReq.sendJSon(Constant.URL_External, "{\"method\":\"getTestRate\",\"registerId\":\""+128+"\",\"bsid\":\""+1+"\",\"categoryid\":\"5\"}");
			System.out.println(response);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

}
