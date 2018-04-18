package com.akazam.wap.framework.core.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Constants base class, all value read from config file. Sub-class should put
 * blow code at first, example:
 * 
 * <pre>
 * static
 * {
 * 	init(&quot;conf/aggr.properties&quot;);
 * }
 * </pre>
 * 
 * @author akazam
 */
public abstract class ConfigurableConstants
{
	private static Properties	p		= new Properties();
	public static final String	Blank	= "null";


	/**
	 * read config file and set vlaue to Properties p
	 * 
	 * @param propertyFileName
	 *            file name
	 */
	protected static void init(String propertyFileName)
	{
		//Assert.hasText("property file name can't be null for configurable constants");

		InputStream in = null;
		try
		{
			in = ConfigurableConstants.class.getClassLoader().getResourceAsStream(propertyFileName);
			if (in != null)
				p.load(in);
		}
		catch (IOException e)
		{
			System.out.println("load " + propertyFileName + " into Constants error!");
		}
		finally
		{
			if (in != null)
			{
				try
				{
					in.close();
				}
				catch (IOException e)
				{
					System.out.println("close " + propertyFileName + " error!");
				}
			}
		}
	}

	/**
	 * @param key
	 *            property key
	 * @return property value
	 */
	protected static String getValue(String key)
	{
		String value = p.getProperty(key);
		if (value == null)
		{
			System.out.println("'" + key + "' not fount!");
		}
		return value;
	}
}
