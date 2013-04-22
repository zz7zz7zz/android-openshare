package com.open.share.tencent.api;

import org.apache.http.message.BasicNameValuePair;

import android.os.Bundle;
import android.util.Log;

import com.open.share.ContextMgr;
import com.open.share.OpenManager;
import com.open.share.interfaces.IOpenResponse;
import com.open.share.tencent.TenShareImpl;
import com.open.share.tencent.TenTokenBean;
import com.open.share.utils.IpUtils;
import com.open.share.utils.SharedPreferenceUtil;

public class T_Friends extends TenNetRunnable {

	private Bundle mBundle;
	
	public T_Friends(Bundle mBundle,IOpenResponse listener)
	{
		this.mBundle=mBundle;
		this.listener=listener;
	}
	
	@Override
	public void loadData() {
		this.reqMethod=Method.GET;
		this.reqUrl=TenShareImpl.URL_SERVER+"/friends/idollist_s";
		
		TenTokenBean mTencentWeiboTokenBean=new TenTokenBean();
		SharedPreferenceUtil.Fetch(ContextMgr.getContext(),OpenManager.getInstatnce().getSpName(OpenManager.TENCENT_WEIBO), mTencentWeiboTokenBean);
		
		this.reqGetParams.add(new BasicNameValuePair("oauth_consumer_key", TenShareImpl.APP_KEY));
		this.reqGetParams.add(new BasicNameValuePair("access_token", mTencentWeiboTokenBean.access_token));
		this.reqGetParams.add(new BasicNameValuePair("openid", mTencentWeiboTokenBean.openid));
		this.reqGetParams.add(new BasicNameValuePair("clientip", IpUtils.getPsdnIp()));
		this.reqGetParams.add(new BasicNameValuePair("oauth_version","2.a"));
		this.reqGetParams.add(new BasicNameValuePair("scope","all"));
		
		int pageIndex=mBundle.getInt(OpenManager.BUNDLE_KEY_PAGEINDEX);
		int perPageSize=mBundle.getInt(OpenManager.BUNDLE_KEY_PERPAGESIZE);
		
		this.reqGetParams.add(new BasicNameValuePair("format","json"));
		this.reqGetParams.add(new BasicNameValuePair("reqnum",""+perPageSize));
		this.reqGetParams.add(new BasicNameValuePair("startindex",""+pageIndex));
		this.reqGetParams.add(new BasicNameValuePair("install","0"));
		this.reqGetParams.add(new BasicNameValuePair("mode","0"));
	}

	@Override
	public void handleData() {
		String str=com.open.share.utils.HttpEntityReadUtil.getString(this.readObj);
		this.resObj=str;
Log.v("T_Friends handleData():", "---"+str);
	}

}
