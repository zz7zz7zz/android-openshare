/**
 * 
 */
package com.open.share;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.open.share.interfaces.IOpenResponse;
import com.open.share.net.AbstractRunnable;
import com.open.share.utils.LogUtil;
import com.open.share.utils.SharedPreferenceUtil;

/**
 * 第三方开放平台管理类
 * 
 * @author 杨龙辉
 *
 */
public final class OpenManager {

	//国内开放平台 与  国外开放平台
	public static final int SINA_WEIBO=1;//新浪微博
	public static final int TENCENT_WEIBO=2;//腾讯微博
	public static final int RENREN=5;//人人网
	public static final int QQZONE=3;//QQ空间
	public static final int DOUBAN=4;//豆瓣
	public static final int WEIXIN=6;//微信好友
	public static final int WEIXIN_pengyouquan=8;//微信好友圈
	public static final int FACEBOOK=11;//Facebook
	public static final int TWITTER=12;//Twitter
	public static final int MYSPACE=13;//Myspace
	public static final int MOKA=7;//分享到摩卡时光
	
	//Bundle->key名字
	public static final String BUNDLE_KEY_TYPE="bundle_key_type";//分享的目的地
	public static final String BUNDLE_KEY_TEXT="bundle_key_text";//分享的文字
	public static final String BUNDLE_KEY_IMGPATH="bundle_key_imgpath";//图片路径
	public static final String BUNDLE_KEY_VOICEPATH="bundle_key_voicepath";//声音路径
	public static final String BUNDLE_KEY_LATITUDE="bundle_key_latitude";//纬度
	public static final String BUNDLE_KEY_LONGITUDE="bundle_key_longitude";//经度
	public static final String BUNDLE_KEY_LOCATION="bundle_key_location";//详细地址
	public static final String BUNDLE_KEY_PICURL="bundle_key_picurl";//关注
	public static final String BUNDLE_KEY_UID="bundle_key_uid";//关注
	public static final String BUNDLE_KEY_PAGEINDEX="bundle_key_pageIndex";//关注
	public static final String BUNDLE_KEY_PERPAGESIZE="bundle_key_perPageSize";//关注
	
	//授权的界面接收到的属性
	public static final String EXTRA_AUTHORIZE_URL="extra_open_oauthUrl";
	public static final String EXTRA_OPEN_DEST="extra_open_dest";
	
	//授权结果
	public static final int OAUTH_SUCCESS=Activity.RESULT_OK;
	public static final int OAUTH_FAILED=Activity.RESULT_CANCELED+1;
	public static final int OAUTH_CANCELED=Activity.RESULT_CANCELED;
	
	//广播结果相关
	public static final String BUNDLE_KEY_OPEN="bundle_key_open";//关注
	public static final String AUTH_RESULT_ACTION="com.open.share";
	
	//access_token 提前失效时间
	public static final long EARLY_INVAILD_TIME =  10 * 60*1000;
	
	private final String openName_sina="新浪微博";
	private final String openName_tencent="腾讯微博";
	private final String openName_renren="人人网";
	private final String openName_qqzone="QQ空间";
	private final String openName_douban="豆瓣";
	private final String openName_weixin="微信";
	private final String openName_facebook="Facebook";
	private final String openName_twitter="Twitter";
	private final String openName_myspace="Myspace";
	private final String openName_moka="时光轴";
	
	private final String iconPath_sina = "resource/open_icon_sina.png";
	private final String iconPath_tencent = "resource/open_icon_tencent.png";
	private final String iconPath_renren = "resource/open_icon_renren.png";
	private final String iconPath_qqzone = "resource/open_icon_qqzone.png";
	private final String iconPath_douban = "resource/open_icon_douban.png";
	private final String iconPath_weixin = "resource/open_icon_weixin.png";
	private final String iconPath_facebook = "null.png";
	private final String iconPath_twitter = "null.png";
	private final String iconPath_myspace = "null.png";
	private final String iconPath_moka = "resource/open_icon_moka.png";
	
	private final String savaFileName_sina = "openshare_sina";
	private final String savaFileName_tencent = "openshare_tencent";
	private final String savaFileName_renren = "openshare_renren";
	private final String savaFileName_qqzone = "openshare_qqzone";
	private final String savaFileName_douban = "openshare_douban";
	private final String savaFileName_weixin = "openshare_weixin";
	private final String savaFileName_facebook = "openshare_facebook";
	private final String savaFileName_twitter = "openshare_twitter";
	private final String savaFileName_myspace = "openshare_myspace";
	private final String savaFileName_moka = "openshare_moka"; 
	
	private static OpenManager instance; 
	
	public static OpenManager getInstatnce()
	{
		if(null==instance)
		{
			instance=new OpenManager();
		}
		return instance;
	}
	
	public static void destory()
	{
		instance=null;
	}
	
	//第三方开放平台名称集合
	public HashMap<Integer, OpenBean> openMap=new HashMap<Integer, OpenBean>();
	{
		openMap.put(SINA_WEIBO, new OpenBean(SINA_WEIBO,openName_sina,iconPath_sina,savaFileName_sina));
		openMap.put(TENCENT_WEIBO, new OpenBean(TENCENT_WEIBO,openName_tencent,iconPath_tencent,savaFileName_tencent));
		openMap.put(RENREN, new OpenBean(RENREN,openName_renren,iconPath_renren,savaFileName_renren));
		openMap.put(QQZONE, new OpenBean(QQZONE,openName_qqzone,iconPath_qqzone,savaFileName_qqzone));
		openMap.put(DOUBAN, new OpenBean(DOUBAN,openName_douban,iconPath_douban,savaFileName_douban));
		openMap.put(WEIXIN, new OpenBean(WEIXIN,openName_weixin,iconPath_weixin,savaFileName_weixin));
		openMap.put(FACEBOOK,new OpenBean(FACEBOOK,openName_facebook,iconPath_facebook,savaFileName_facebook));
		openMap.put(TWITTER, new OpenBean(TWITTER,openName_twitter,iconPath_twitter,savaFileName_twitter));
		openMap.put(MYSPACE, new OpenBean(MYSPACE,openName_myspace,iconPath_myspace,savaFileName_myspace));
		openMap.put(MOKA, new OpenBean(MOKA,openName_moka,iconPath_moka,savaFileName_moka));
	}
	
	public static class OpenBean
	{
		public int openID;//平台ID
		public String openName;//名字
		public String iconPath;//小图标
		public String savaFileName;//信息保存的文件名
		
		public OpenBean(int openID, String openName, String iconPath,String savaFileName) 
		{
			this.openID = openID;
			this.openName = openName;
			this.iconPath = iconPath;
			this.savaFileName = savaFileName;
		}
	}
	
	/**
	 * accessToken 是否有效
	 * @param dest
	 * @param activity
	 * @return
	 */
	public boolean isVaild(int dest,Context context)
	{
		try
		{
			return OpenFactory.produce(dest).isVaild(context);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return false;
	}
	
	/**
	 * 授权操作
	 * @param dest 要授哪个平台
	 * {@link #SINA_WEIBO}、{@link #TENCENT_WEIBO}、{@link #RENREN}
	 * 、{@link #QQZONE}、{@link #DOUBAN}、{@link #WEIXIN}
	 * 、{@link #FACEBOOK}、{@link #TWITTER}、{@link #MYSPACE}
	 * @param context 上下文
	 */
	public void authorize(int dest,Activity activity)
	{
		try
		{
			OpenFactory.produce(dest).authorize(dest,activity);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * 分享文字微博
	 * @param dest 要分享到哪里
	 * @param bundle 数据包裹,包括你要分享的类型，文字,分享的图片路径,
	 *  key的参考信息如下：
	 *  {@link #BUNDLE_KEY_TYPE }、
	 *  {@link #BUNDLE_KEY_TEXT }、
	 *  {@link #BUNDLE_KEY_IMGPATH }
	 * @param activity
	 * @param listener
	 */
	public AbstractRunnable shareStatus(final int dest,final Bundle bundle,final IOpenResponse listener)
	{
		try
		{
			return OpenFactory.produce(dest).shareStatus(bundle,listener);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * 分享图片
	 * @param dest 要分享到哪里
	 * @param bundle 数据包裹,包括你要分享的类型，文字,分享的图片路径,
	 *  key的参考信息如下：
	 *  {@link #BUNDLE_KEY_TYPE }、
	 *  {@link #BUNDLE_KEY_TEXT }、
	 *  {@link #BUNDLE_KEY_IMGPATH }
	 * @param activity
	 */
	public AbstractRunnable sharePhoto(final int dest,final Bundle bundle,final IOpenResponse listener)
	{
		try
		{
			return OpenFactory.produce(dest).sharePhoto(bundle,listener);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
		
	}
	
	/**
	 * 关注接口
	 * @param dest 要关注哪里
	 * @param bundle 数据包裹,包括你要分享的类型，文字,分享的图片路径,
	 *  key的参考信息如下：
	 *  {@link #BUNDLE_KEY_TYPE }、
	 *  {@link #BUNDLE_KEY_TEXT }、
	 *  {@link #BUNDLE_KEY_IMGPATH }
	 * @param activity
	 * @param listener
	 */
	public AbstractRunnable friendShipCreate(final int dest,final Bundle bundle,final IOpenResponse listener)
	{
		try
		{
			return OpenFactory.produce(dest).friendShipsCreate(bundle,listener);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	
	public AbstractRunnable getUserInfo(final int dest,final Bundle bundle,final IOpenResponse listener)
	{
		try
		{
			return OpenFactory.produce(dest).getUserInfo(bundle, listener);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	

	/**
	 * 解绑操作
	 * @param dest 要解绑哪个平台
	 * @param context 上下文
	 */
	public void unBind(int dest,Activity activity)
	{
		SharedPreferenceUtil.clear(activity, getSpName(dest));
	}
	
	
	/**
	 * 获取名字
	 * @param dest
	 * @return
	 */
	public String getName(int dest)
	{
		return openMap.get(dest).openName;
	}
	
	public String getSpName(int dest)
	{
		return openMap.get(dest).savaFileName;
	}
	
	public Bitmap getIcon(int dest)
	{
		return getBitmap(openMap.get(dest).iconPath);
	}
	
	private Bitmap getBitmap(String picName)
	{
		Bitmap bitmap = null;
        synchronized (lock) 
        {
            if (_cache.containsKey(picName)) 
            {
            	WeakReference<Bitmap> ref = _cache.get(picName);
                if ( ref != null ) 
                {
                    bitmap =ref.get();
                    if (bitmap == null)
                    {
LogUtil.v("OpenManager", "getBitmapFromMemoryCache() bitmap==null : url:"+picName);
                    	 _cache.remove(picName);
                    }
                    else if(bitmap.isRecycled())
                    {
LogUtil.v("OpenManager", "getBitmapFromMemoryCache() bitmap isRecycled : url:"+picName);
                    	bitmap=null;
                    	_cache.remove(picName);
                    }
                }
            }
        }
        
        if(null==bitmap)
        {
    		InputStream is=getClass().getResourceAsStream(picName);
    		try {
    			if(null!=is)
    			{
    				bitmap = BitmapFactory.decodeStream(is);
    				if(bitmap!=null)
    				{
    					bitmap.setDensity(Bitmap.DENSITY_NONE);
    					 synchronized (lock) 
    					 {
    			             _cache.put(picName, new WeakReference<Bitmap>(bitmap));
    			         }
    				}
    				is.close();
    			}
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
        }
		return bitmap;
	}
	
	private final Object lock = new Object();
	private HashMap<String, WeakReference<Bitmap>> _cache = new HashMap<String, WeakReference<Bitmap>>();
}
