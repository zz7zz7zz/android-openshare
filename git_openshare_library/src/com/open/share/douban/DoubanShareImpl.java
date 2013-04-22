/**
 * 
 */
package com.open.share.douban;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.open.share.AuthorizeActivity;
import com.open.share.OpenAppConstant;
import com.open.share.OpenManager;
import com.open.share.douban.api.D_Friendships;
import com.open.share.douban.api.D_Status;
import com.open.share.douban.api.D_UserInfo;
import com.open.share.interfaces.IOpenResponse;
import com.open.share.interfaces.IShare;
import com.open.share.net.AbstractRunnable;
import com.open.share.utils.SharedPreferenceUtil;


/**
 * 
 * @author 杨龙辉
 *
 */
public class DoubanShareImpl implements IShare{
	
	public final static String APP_KEY=OpenAppConstant.DOUBAN_APP_KEY;
	public final static String KEY_SECRET=OpenAppConstant.DOUBAN_KEY_SECRET;
	public final static String URL_CALLBACK=OpenAppConstant.DOUBAN_URL_CALLBACK;
	public final static String SCOPE=OpenAppConstant.DOUBAN_SCOPE;
	
	public final static String OAUTH="https://www.douban.com/service/auth2/auth?client_id=%s&redirect_uri=%s&response_type=code&scope=%s";
	public final static String OAUTH2="https://www.douban.com/service/auth2/token";
	
	public final static String URL_API_USERIFNO="https://api.douban.com/v2/use/%s";
	public final static String URL_API_FRIENDSHIP="https://api.douban.com/shuo/v2/friendships/create";
	public final static String URL_API_STATUS="https://api.douban.com/shuo/v2/statuses/";
	
	public boolean isVaild(Context context) {
		
		DoubanTokenBean mDouban=new DoubanTokenBean();
		SharedPreferenceUtil.Fetch(context, OpenManager.getInstatnce().getSpName(OpenManager.DOUBAN), mDouban);
		
		boolean isVaild=mDouban.expires_in>System.currentTimeMillis();
		if(!isVaild)
		{
			SharedPreferenceUtil.clear(context, OpenManager.getInstatnce().getSpName(OpenManager.DOUBAN));
		}
		return isVaild;
	}

	public void authorize(int requestCode, Activity activity) {
		
		 String authorizeUrl=String.format(OAUTH, APP_KEY,URL_CALLBACK,SCOPE);
		 
		 Intent mIntent=new Intent(activity, AuthorizeActivity.class);
		 mIntent.putExtra(OpenManager.EXTRA_AUTHORIZE_URL, authorizeUrl);
		 mIntent.putExtra(OpenManager.EXTRA_OPEN_DEST, OpenManager.DOUBAN);
		 activity.startActivityForResult(mIntent, requestCode);
	}

	@Override
	public AbstractRunnable getUserInfo(Bundle bundle,final IOpenResponse listener) {
		return new D_UserInfo(bundle,listener);
	}

	public AbstractRunnable shareStatus(Bundle bundleIn,final IOpenResponse listener) {
		return new D_Status(bundleIn, listener);
	}

	public AbstractRunnable sharePhoto(Bundle bundleIn,final IOpenResponse listener) {
		
		return new D_Status(bundleIn, listener);
	}

	public AbstractRunnable friendShipsCreate(Bundle bundle,IOpenResponse listener) {
		return new D_Friendships(bundle,listener);
	}

}
