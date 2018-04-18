package com.akazam.wap.framework.core.utils;


public class LogUtils {
public final static boolean isDebug = false;
	
	public static void println(String msg){
		if(isDebug){
			System.out.println(msg);
		}
	}
}
