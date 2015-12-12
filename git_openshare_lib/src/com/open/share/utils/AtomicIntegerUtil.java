package com.open.share.utils;
/**
 * 
 */


import java.util.concurrent.atomic.AtomicInteger;

/**
 * 整数自增加工具类
 * 
 * @author 杨龙辉
 *
 */
public final class AtomicIntegerUtil {
	
	private static final AtomicInteger mAtomicInteger=new AtomicInteger();
	
	public static int  getIncrementID()
	{
		return mAtomicInteger.getAndIncrement();
	}
}
