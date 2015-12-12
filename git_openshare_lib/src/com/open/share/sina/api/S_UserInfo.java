package com.open.share.sina.api;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.open.share.ContextMgr;
import com.open.share.OpenManager;
import com.open.share.interfaces.IOpenResponse;
import com.open.share.sina.SinaShareImpl;
import com.open.share.sina.SinaTokenBean;
import com.open.share.sina.SinaUserInfo;
import com.open.share.utils.SharedPreferenceUtil;

/**
 * 获取用户资料
 * @author Administrator
 *
 */

public class S_UserInfo extends SinaNetRunnable {
	
	private Bundle mBundle;
	
	public S_UserInfo(Bundle mBundle,IOpenResponse listener)
	{
		this.mBundle=mBundle;
		this.listener=listener;
	}
	
	@Override
	public void loadData() {
		this.reqMethod=Method.GET;
		this.reqUrl=SinaShareImpl.URL_USERS_SHOW;
		
		SinaTokenBean out=new SinaTokenBean();
		SharedPreferenceUtil.Fetch(ContextMgr.getContext(), OpenManager.getInstatnce().getSpName(OpenManager.SINA_WEIBO), out);
		
		String uid=mBundle.getString(OpenManager.BUNDLE_KEY_UID);
		if(TextUtils.isEmpty(uid))
		{
			uid=out.uid;
		}
		
		this.reqGetParams.add(new BasicNameValuePair("access_token", out.access_token));
		this.reqGetParams.add(new BasicNameValuePair("uid", uid));
	}

	@Override
	public void handleData() {
		String str=com.open.share.utils.HttpEntityReadUtil.getString(this.readObj);
Log.v("UserInfoMsg handleData():", "---"+str);
		if(!TextUtils.isEmpty(str))
		{
			SinaUserInfo ret=new SinaUserInfo();
			try {
				
				JSONObject obj = new JSONObject(str);
				ret.name=obj.optString("name");
				ret.head=obj.optString("avatar_large");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			this.resObj=ret;
		}
		
	}
}
