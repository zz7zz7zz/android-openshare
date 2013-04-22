/**
 * 
 */
package com.open.share.qqzone;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.open.share.ContextMgr;
import com.open.share.OpenAppConstant;
import com.open.share.OpenManager;
import com.open.share.interfaces.IOpenResponse;
import com.open.share.interfaces.IShare;
import com.open.share.net.AbstractRunnable;
import com.open.share.qqzone.api.Q_GetUserInfo;
import com.open.share.qqzone.api.Q_PhotoRunnable;
import com.open.share.utils.SharedPreferenceUtil;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;


/**
 * 
 * @author 杨龙辉
 *
 */
public class QQZoneShareImpl implements IShare{
	
	public final static String APP_ID=OpenAppConstant.QQZONE_APP_ID;
	public final static String APP_KEY=OpenAppConstant.QQZONE_APP_KEY;
	public final static String URL_CALLBACK=OpenAppConstant.QQZONE_URL_CALLBACK;
	public final static String SCOPE=OpenAppConstant.QQZONE_SCOPE;
	
	public Tencent mTencent;
	public final static String CANCLE_KEYWORD="usercancel";
	{
		try {
			QQZoneTokenBean mQqZoneTokenBean=new QQZoneTokenBean();
			SharedPreferenceUtil.Fetch(ContextMgr.getContext(), OpenManager.getInstatnce().getSpName(OpenManager.QQZONE), mQqZoneTokenBean);
			mTencent = Tencent.createInstance(APP_ID, ContextMgr.getContext());
			mTencent.setOpenId(mQqZoneTokenBean.openid);
			mTencent.setAccessToken(mQqZoneTokenBean.access_token, ""+(mQqZoneTokenBean.expires_in-System.currentTimeMillis())/1000);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public boolean isVaild(Context context) {
		return mTencent.isSessionValid();
	}

	public void authorize(int requestCode, final Activity activity) {
        IUiListener listener = new IUiListener() {
			
			@Override
			public void onError(UiError arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onComplete(JSONObject arg0) {
				try {
					String openId=arg0.getString("openid");
					String access_token=arg0.getString("access_token");
					String expires_in=arg0.getString("expires_in");
					
					final QQZoneTokenBean savaBean=new QQZoneTokenBean();
					savaBean.openid=openId;
					savaBean.access_token=access_token;
					savaBean.expires_in=System.currentTimeMillis()+Long.valueOf(expires_in)*1000;
					SharedPreferenceUtil.store(activity, OpenManager.getInstatnce().getSpName(OpenManager.QQZONE), savaBean);
					
					Intent intent = new Intent();
					intent.setAction(OpenManager.AUTH_RESULT_ACTION);
					intent.putExtra(OpenManager.BUNDLE_KEY_OPEN, OpenManager.QQZONE);
					activity.sendBroadcast(intent);
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			
			@Override
			public void onCancel() {
				// TODO Auto-generated method stub
				
			}
		};
		mTencent.login(activity, SCOPE, listener);
	}

	@Override
	public AbstractRunnable getUserInfo(Bundle bundle, final IOpenResponse listener) {
		return new Q_GetUserInfo(bundle,listener);
	}

	public AbstractRunnable shareStatus(Bundle bundleIn, final IOpenResponse listener) {
		return null;
	}

	public AbstractRunnable sharePhoto(Bundle bundleIn, final IOpenResponse listener) {
		
		return new Q_PhotoRunnable(bundleIn,listener);
	}

	public AbstractRunnable friendShipsCreate(Bundle bundle,final IOpenResponse listener) {
		return null;
	}

}
