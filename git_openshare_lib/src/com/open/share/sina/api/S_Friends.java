package com.open.share.sina.api;

import org.apache.http.message.BasicNameValuePair;

import android.os.Bundle;
import android.text.TextUtils;

import com.open.share.ContextMgr;
import com.open.share.OpenManager;
import com.open.share.interfaces.IOpenResponse;
import com.open.share.sina.SinaShareImpl;
import com.open.share.sina.SinaTokenBean;
import com.open.share.utils.LogUtil;
import com.open.share.utils.SharedPreferenceUtil;

/**
 * 
 * @author Administrator
 *
 */
public class S_Friends extends SinaNetRunnable {

	private Bundle mBundle;
	
	public S_Friends(Bundle mBundle,IOpenResponse listener)
	{
		this.mBundle=mBundle;
		this.listener=listener;
	}
	
	@Override
	public void loadData() {
		this.reqMethod=Method.GET;
		this.reqUrl=SinaShareImpl.URL_FRIENDSHIPS_FRIENDS;
		
		SinaTokenBean out=new SinaTokenBean();
		SharedPreferenceUtil.Fetch(ContextMgr.getContext(), OpenManager.getInstatnce().getSpName(OpenManager.SINA_WEIBO), out);
		
		int pageIndex=mBundle.getInt(OpenManager.BUNDLE_KEY_PAGEINDEX);
		int perPageSize=mBundle.getInt(OpenManager.BUNDLE_KEY_PERPAGESIZE);
		String uid=mBundle.getString(OpenManager.BUNDLE_KEY_UID);
		if(TextUtils.isEmpty(uid))
		{
			uid=out.uid;
		}
		
		this.reqGetParams.add(new BasicNameValuePair("access_token", out.access_token));
		this.reqGetParams.add(new BasicNameValuePair("uid",uid));
		this.reqGetParams.add(new BasicNameValuePair("count",""+perPageSize));
		this.reqGetParams.add(new BasicNameValuePair("cursor",""+pageIndex));
	}

	@Override
	public void handleData() {
		String str=com.open.share.utils.HttpEntityReadUtil.getString(this.readObj);
		this.resObj=str;
LogUtil.v("S_Friends handleData():", "---"+str);

	}

}
