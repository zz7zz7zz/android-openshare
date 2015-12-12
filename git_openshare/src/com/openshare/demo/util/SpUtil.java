package com.openshare.demo.util;

import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * sharedPerference 文件工具类
 * 
 * @author yanglonghui 2012.07.10
 *
 */
public final class SpUtil {
	
	public static final String OPENSHARE_SETTING="openshare_setting";//文件名
	public static final String OPENSHARE_SETTING_ATT_ISSELECTED="_isSelected";
	
	public static final String OPEN_DYNAMIC="openshare_setting_dynamic";//文件名
	public static final String OPEN_DYNAMIC_ATT_OPENTHEME="opentheme";
	public static final String OPEN_DYNAMIC_ATT_ISFOLLOW="_isFollow";
	public static final String OPEN_DYNAMIC_ATT_ISSHOW="_isShow";
	public static final String OPEN_DYNAMIC_ATT_ISBIND="_isBind";
	public static final String OPEN_DYNAMIC_ATT_DEFAULTSHARESTR="defaultShareString";
	
	public static final String MOKA="moka";//文件名
	public static final String MOKA_ATT_CURRENTID="currentID";
	public static final String MOKA_ATT_REFRESH_Timeline="refresh_timeline";
	public static final String MOKA_ATT_REFRESH_TOTHER="refresh_tother";
	public static final String MOKA_ATT_REFRESH_SQUARE="refresh_square";
	public static final String MOKA_ATT_REFRESH_Matrial="refresh_matrial";
	public static final String MOKA_ATT_LOCALTIME="localTime";//本地时间
	public static final String MOKA_ATT_SERVERTIME="serverTime";//服务器时间
	public static final String MOKA_ATT_TmpIMG_CreateTime="img_createTime";//时光广场记录时间
	public static final String NOTIFICATION_FRIEND_UPDATE = "friend_update_";// 好友更新情况
	
	
	public static final String SP_ACCOUNT="sp_account";

	public static boolean getBoolean(String fileName,String key,boolean defValue)
	{
		SharedPreferences settings = OpenDemoApp.getInstance().getSharedPreferences(fileName, Context.MODE_PRIVATE);
		return settings.getBoolean(key, defValue);
	}
	
	public static void putBoolean(String fileName,String key,boolean value)
	{
		SharedPreferences settings = OpenDemoApp.getInstance().getSharedPreferences(fileName, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}
	
	public static String getString(String fileName,String key,String defValue)
	{
		SharedPreferences settings = OpenDemoApp.getInstance().getSharedPreferences(fileName, Context.MODE_PRIVATE);
		return settings.getString(key, defValue);
	}
	
	public static void putString(String fileName,String key,String value)
	{
		SharedPreferences settings = OpenDemoApp.getInstance().getSharedPreferences(fileName, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(key, value);
		editor.commit();
	}
	
	public static int getInt(String fileName,String key,int defValue)
	{
		SharedPreferences settings = OpenDemoApp.getInstance().getSharedPreferences(fileName, Context.MODE_PRIVATE);
		return settings.getInt(key, defValue);
	}
	
	public static void putInt(String fileName,String key,int value)
	{
		SharedPreferences settings = OpenDemoApp.getInstance().getSharedPreferences(fileName, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt(key, value);
		editor.commit();
	}
	
	public static long getLong(String fileName,String key,long defValue)
	{
		SharedPreferences settings = OpenDemoApp.getInstance().getSharedPreferences(fileName, Context.MODE_PRIVATE);
		return settings.getLong(key, defValue);
	}
	
	public static void putLong(String fileName,String key,long value)
	{
		SharedPreferences settings = OpenDemoApp.getInstance().getSharedPreferences(fileName, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putLong(key, value);
		editor.commit();
	}
	
	public static float getFloat(String fileName,String key,float defValue)
	{
		SharedPreferences settings = OpenDemoApp.getInstance().getSharedPreferences(fileName, Context.MODE_PRIVATE);
		return settings.getFloat(key, defValue);
	}
	
	public static void putFloat(String fileName,String key,float value)
	{
		SharedPreferences settings = OpenDemoApp.getInstance().getSharedPreferences(fileName, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putFloat(key, value);
		editor.commit();
	}
	
	public static Map<String, ?> putFloat(String fileName)
	{
		SharedPreferences settings = OpenDemoApp.getInstance().getSharedPreferences(fileName, Context.MODE_PRIVATE);
		return settings.getAll();
	}

	public static void clear(String fileName)
	{
		SharedPreferences settings = OpenDemoApp.getInstance().getSharedPreferences(fileName, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.clear();
		editor.commit();
	}
}
