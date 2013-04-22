package com.open.share.douban.api;

import org.apache.http.message.BasicNameValuePair;

import android.os.Bundle;

import com.open.share.OpenManager;
import com.open.share.douban.DoubanShareImpl;
import com.open.share.interfaces.IOpenResponse;
import com.open.share.sina.SFormBodyUtil;
import com.open.share.sina.api.SinaNetRunnable;
import com.open.share.utils.LogUtil;

public class D_Friendships extends SinaNetRunnable {

	private Bundle mBundle;
	
	public D_Friendships(Bundle mBundle,IOpenResponse listener)
	{
		this.mBundle=mBundle;
		this.listener=listener;
	}
	
	@Override
	public void loadData() {
		this.reqMethod=Method.POST;
		this.reqUrl=DoubanShareImpl.URL_API_FRIENDSHIP;
		
		this.formMultParamList.add(new BasicNameValuePair("source", DoubanShareImpl.APP_KEY));
		this.formMultParamList.add(new BasicNameValuePair("user_id", mBundle.getString(OpenManager.BUNDLE_KEY_UID)));
		
		byte[] postBody=SFormBodyUtil.getData(reqHeards, formMultParamList, null);
		this.reqPostDate=postBody;
	}

	@Override
	public void handleData() {
		String str=com.open.share.utils.HttpEntityReadUtil.getString(this.readObj);
LogUtil.v("D_Friendships handleData():", "---"+str);
	}

}
