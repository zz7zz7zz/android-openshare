/**
 * 
 */
package com.open.share.utils;

/**
 * 字符串的工具类
 * 
 * @author 杨龙辉 2012.04.07
 * 
 */
public final class StringUtil
{

	/**
	 * 判断字符串是否 不为空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNonEmpty(String str)
	{
		return (null != str && str.length() > 0);
	}

	/**
	 * @param resource
	 *            原始字符串
	 * @param startTag
	 *            要截取目标字符串的开始标签
	 * @param endTag
	 *            要截取目标字符串的结束标签
	 * @return
	 */
	public static String truncateString(String resource, String startTag, String endTag)
	{
		int start = resource.indexOf(startTag);
		int end = resource.indexOf(endTag, start + startTag.length());
		if (start != -1 && end != -1)
		{
			resource = resource.substring(start + startTag.length(), end);
			return resource;
		}
		return null;
	}

	/**
	 * 半角转为全角
	 * 
	 * @param input
	 *            输入
	 * @return
	 */
	public static String ToDBC(String input)
	{
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++)
		{
			if (c[i] == 12288)
			{
				c[i] = (char) 32;
				continue;
			}
			if (c[i] > 65280 && c[i] < 65375)
				c[i] = (char) (c[i] - 65248);
		}
		return new String(c);
	}

}
