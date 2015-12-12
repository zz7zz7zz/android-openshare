/**
 * 
 */
package com.open.share.renren;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.open.share.AuthorizeActivity;
import com.open.share.OpenAppConstant;
import com.open.share.OpenManager;
import com.open.share.interfaces.IOpenResponse;
import com.open.share.interfaces.IShare;
import com.open.share.net.AbstractRunnable;
import com.open.share.renren.api.R_StatusUpdate;
import com.open.share.renren.api.R_StatusUpload;
import com.open.share.renren.api.R_UserInfo;
import com.open.share.utils.SharedPreferenceUtil;

/**
 * @author 杨龙辉
 *
 */
public class RenrenShareImpl implements IShare{
	
	public static final String APP_ID = OpenAppConstant.RENREN_APP_ID;
	public static final String API_KEY = OpenAppConstant.RENREN_API_KEY;
	public static final String SECRET_KEY = OpenAppConstant.RENREN_SECRET_KEY;
	
	public static final String URL_DEFAULT_REDIRECT = OpenAppConstant.RENREN_URL_CALLBACK;//回调页
	public static final String RESPONSE_FORMAT_JSON = "json";
	public static final String[] DEFAULT_PERMISSIONS = { "publish_feed","create_album", "photo_upload", "read_user_album", "status_update" };
	
	public static final String URL_AUTHORIZE = "https://graph.renren.com/oauth/authorize";//第一步
	public static final String URL_SESSION_KEY = "http://graph.renren.com/renren_api/session_key";//第二步获取accesstoken后进行的操作
	
	public static final String URL_RESTSERVER = "http://api.renren.com/restserver.do";//服务器地址
	
	@Override
	public boolean isVaild(Context context) 
	{
		RenrenTokenBean mRenrenTokenBean=new RenrenTokenBean();
		SharedPreferenceUtil.Fetch(context, OpenManager.getInstatnce().getSpName(OpenManager.RENREN), mRenrenTokenBean);
		
		boolean isVaild=mRenrenTokenBean.session_expires_in>System.currentTimeMillis();
		if(!isVaild)
		{
			SharedPreferenceUtil.clear(context, OpenManager.getInstatnce().getSpName(OpenManager.RENREN));
		}
		return isVaild;
	}

	@Override
	public void authorize(int requestCode,Activity activity) {
		
		Bundle params = new Bundle();
		params.putString("client_id", API_KEY);
		params.putString("redirect_uri", URL_DEFAULT_REDIRECT);
		params.putString("response_type", "token");
		params.putString("display", "touch");
		
		if (DEFAULT_PERMISSIONS != null && DEFAULT_PERMISSIONS.length > 0) 
		{
			String scope = TextUtils.join(" ", DEFAULT_PERMISSIONS);
			params.putString("scope", scope);
		}
		String url = URL_AUTHORIZE + "?" + RFormBodyUtil.encodeUrl(params);
		
		Intent mIntent=new Intent(activity, AuthorizeActivity.class);
		mIntent.putExtra(OpenManager.EXTRA_AUTHORIZE_URL, url);
		mIntent.putExtra(OpenManager.EXTRA_OPEN_DEST, OpenManager.RENREN);
		activity.startActivity(mIntent);
	}

	@Override
	public AbstractRunnable getUserInfo(final Bundle bundle,final IOpenResponse listener) 
	{
		return  new R_UserInfo(new String[]{}, listener);
	}

	@Override
	public AbstractRunnable sharePhoto(final Bundle bundle, final IOpenResponse listener) {
		return   new R_StatusUpload(bundle, listener);
	}

	@Override
	public AbstractRunnable shareStatus(final Bundle bundle, final IOpenResponse listener) {
		return  new R_StatusUpdate(bundle, listener);
	}

	@Override
	public AbstractRunnable friendShipsCreate(Bundle bundle,IOpenResponse listener) {
		return null;
	}
}
