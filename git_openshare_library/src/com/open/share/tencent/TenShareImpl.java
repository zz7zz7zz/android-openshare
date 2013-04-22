/**
 * 
 */
package com.open.share.tencent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.open.share.AuthorizeActivity;
import com.open.share.OpenAppConstant;
import com.open.share.OpenManager;
import com.open.share.interfaces.IOpenResponse;
import com.open.share.interfaces.IShare;
import com.open.share.net.AbstractRunnable;
import com.open.share.tencent.api.T_FriendShipCreate;
import com.open.share.tencent.api.T_Friends;
import com.open.share.tencent.api.T_StatusUpdate;
import com.open.share.tencent.api.T_StatusUpload;
import com.open.share.tencent.api.T_StatusUploadUrl;
import com.open.share.tencent.api.T_UserInfo;
import com.open.share.utils.SharedPreferenceUtil;

/**
 * @author 杨龙辉
 *
 */
public class TenShareImpl implements IShare {
	
	public static final String APP_KEY=OpenAppConstant.Tencent_APP_KEY;
	public static final String App_Secret=OpenAppConstant.Tencent_App_Secret;
	
	public static final String URL_REDIRECT=OpenAppConstant.Tencent_URL_CALLBACK;//回调页面
	
	public static final String URL_OAUTH2_AUTHORIZE="https://open.t.qq.com/cgi-bin/oauth2/authorize?client_id=%s&response_type=token&redirect_uri=%s";//授权第一步
	public static final String URL_OAUTH2_ACCESS_TOKEN="https://open.t.qq.com/cgi-bin/oauth2/access_token";//授权第二部
	
	public static final String URL_SERVER="https://open.t.qq.com/api";//服务器地址

	public boolean isVaild(Context context) 
	{
		TenTokenBean mTenWeiboTokenStore=new TenTokenBean();
		SharedPreferenceUtil.Fetch(context, OpenManager.getInstatnce().getSpName(OpenManager.TENCENT_WEIBO), mTenWeiboTokenStore);
		
		boolean isVaild=mTenWeiboTokenStore.expires_in>System.currentTimeMillis();
		if(!isVaild)
		{
			SharedPreferenceUtil.clear(context, OpenManager.getInstatnce().getSpName(OpenManager.TENCENT_WEIBO));
		}
		return isVaild;
	}

	public void authorize(int requestCode,Activity activity) 
	{
		 String authorizeUrl=String.format(URL_OAUTH2_AUTHORIZE, APP_KEY,URL_REDIRECT);
		 
		 Intent mIntent=new Intent(activity, AuthorizeActivity.class);
		 mIntent.putExtra(OpenManager.EXTRA_AUTHORIZE_URL, authorizeUrl);
		 mIntent.putExtra(OpenManager.EXTRA_OPEN_DEST, OpenManager.TENCENT_WEIBO);
		 activity.startActivity(mIntent);
	}

	public AbstractRunnable getUserInfo(final Bundle bundle,final IOpenResponse listener) {
		return new T_UserInfo(listener);
	}

	public AbstractRunnable sharePhoto(final Bundle bundle, final IOpenResponse listener) {
		return new T_StatusUpload(bundle, listener);
	}
	
	public AbstractRunnable sharePhotoWithPicUrl(final Bundle bundle, final IOpenResponse listener) {
		return  new T_StatusUploadUrl(bundle, listener);
	}

	@Override
	public AbstractRunnable shareStatus(final Bundle bundle, final IOpenResponse listener) {
		return  new T_StatusUpdate(bundle, listener);
	}

	@Override
	public AbstractRunnable friendShipsCreate(final Bundle bundle,final IOpenResponse listener) {
		return new T_FriendShipCreate(bundle, listener);
	}
	
	public AbstractRunnable getFriends(final Bundle bundle,final IOpenResponse listener) {
		return new T_Friends(bundle, listener);
	}
}
