package com.open.share.utils;

import java.lang.reflect.Field;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 自定义haredPreference
 * 用此类注意是否对代码进行了混淆
 * 
 * @author 杨龙辉
 *
 */
public final class SharedPreferenceUtil {

	/**
	 * @param context 上下文
	 * @param fileName 文件名
	 * @param in 输入对象
	 */
	public static void store(Context context,String fileName,Object in)
	{
		SharedPreferences settings = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		
		Field[] mField=in.getClass().getFields();
		for(int i=0;i<mField.length;i++)
		{
			mField[i].setAccessible(true);
			String fieldTtype = mField[i].getType().toString();
			try {
				if(fieldTtype.endsWith("String"))
				{
					editor.putString(mField[i].getName(), (String)mField[i].get(in));
				}
				else if(fieldTtype.endsWith("int")|| fieldTtype.endsWith("Integer"))
				{
					editor.putInt(mField[i].getName(), (Integer)(mField[i].get(in)));
				}
				else if(fieldTtype.endsWith("long")|| fieldTtype.endsWith("Long"))
				{
					editor.putLong(mField[i].getName(), (Long)(mField[i].get(in)));
				}
				else if(fieldTtype.endsWith("float")|| fieldTtype.endsWith("Float"))
				{
					editor.putFloat(mField[i].getName(), (Float)(mField[i].get(in)));
				}
				else if(fieldTtype.endsWith("boolean")|| fieldTtype.endsWith("Boolean"))
				{
					editor.putBoolean(mField[i].getName(), (Boolean) mField[i].get(in));
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		
		editor.commit();
	}
	
	/**
	 * 获取对象
	 * @param context 上下文
	 * @param fileName 文件名字
	 * @param out 输出对象
	 */
	public static void Fetch(Context context,String fileName,Object out)
	{
		SharedPreferences settings =context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
		
		Field[] mField=out.getClass().getFields();
		for(int i=0;i<mField.length;i++)
		{
			mField[i].setAccessible(true);
			String fieldTtype = mField[i].getType().toString();
			
			try {
				if(fieldTtype.endsWith("String"))
				{
					mField[i].set(out, settings.getString(mField[i].getName(), ""));
				}
				else if(fieldTtype.endsWith("int")|| fieldTtype.endsWith("Integer"))
				{
					mField[i].set(out, settings.getInt(mField[i].getName(), -1));;
				}
				else if(fieldTtype.endsWith("long")|| fieldTtype.endsWith("Long"))
				{
					mField[i].set(out, settings.getLong(mField[i].getName(), -1));
				}
				else if(fieldTtype.endsWith("float")|| fieldTtype.endsWith("Float"))
				{
					mField[i].set(out, settings.getFloat(mField[i].getName(), -1));
				}
				else if(fieldTtype.endsWith("boolean")|| fieldTtype.endsWith("Boolean"))
				{
					mField[i].set(out, settings.getBoolean(mField[i].getName(), false));
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 清空所有属性
	 * @param context 上下文
	 * @param fileName 文件名字
	 */
	public static void clear(Context context,String fileName)
	{
		SharedPreferences settings = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.clear();
		editor.commit();
	}
}
