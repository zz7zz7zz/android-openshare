/**
 * 
 */
package com.open.share.utils;

/**
 * 时间日期的工具类
 * 
 * @author 杨龙辉 2012.04.07
 *
 */
public final class DateUtil {
	
	/**
	 * 获取本地时间
	 * @param pattern yyyyMMdd或者yyyy-MM-dd-HH-mm:ss:ms 类型的时间格式
	 * @return 
	 */
	public static String getStringTime(long time,String pattern)
	{
		java.text.SimpleDateFormat format = new java.text.SimpleDateFormat(pattern);
		java.util.Date date = new java.util.Date(time);
		return format.format(date);
	}
	
}
