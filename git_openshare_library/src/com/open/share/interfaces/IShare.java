package com.open.share.interfaces;



import com.open.share.OpenManager;
import com.open.share.net.AbstractRunnable;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

/**
 * 分享接口
 * 
 * @author 杨龙辉
 *
 */
public interface IShare {
	
	/**
	 * accesss_Token 是否有效，还在有效返回true,否则返回false
	 * @param context
	 */
	public abstract boolean isVaild(Context context);
	
	/**
	 * 授权操作
	 * @param context 上下文
	 */
	public abstract void authorize(int requestCode,Activity activity);
	
	/**
	 * 查询用户信息
	 * @param activity
	 */
	public abstract AbstractRunnable getUserInfo(Bundle bundle,IOpenResponse listener);
	
	/**
	 * 分享文字
	 * 
	 * @param bundle
	 *  key的参考信息如下：
	 *  {@link OpenManager#BUNDLE_KEY_TEXT }、
	 * @param activity 上下文
	 */
	public abstract AbstractRunnable shareStatus(Bundle bundle,IOpenResponse listener);
	
	/**
	 * 分享图片
	 * 
	 * @param bundle 包含图片描述,分享的图片路径,
	 *  key的参考信息如下：
	 *  {@link OpenManager#BUNDLE_KEY_TEXT }
	 *  {@link OpenManager#BUNDLE_KEY_IMGPATH }
	 * @param activity 上下文
	 */
	public abstract AbstractRunnable sharePhoto(Bundle bundle,IOpenResponse listener);
	
	/**
	 * 添加关注
	 * key的参考信息如下：
	 *  {@link OpenManager#BUNDLE_KEY_ATTENTION }
	 * @return 成功返回true,失败返回false
	 */
	public abstract AbstractRunnable friendShipsCreate(Bundle bundle,IOpenResponse listener);
}
