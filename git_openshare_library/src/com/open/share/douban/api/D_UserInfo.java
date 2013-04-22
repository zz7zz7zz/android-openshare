package com.open.share.douban.api;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.open.share.OpenManager;
import com.open.share.douban.DoubanShareImpl;
import com.open.share.douban.DoubleUserInfo;
import com.open.share.interfaces.IOpenResponse;
import com.open.share.sina.api.SinaNetRunnable;
import com.open.share.utils.StringUtil;

public class D_UserInfo extends SinaNetRunnable {

	private Bundle mBundle;
	
	public D_UserInfo(Bundle mBundle,IOpenResponse listener)
	{
		this.mBundle=mBundle;
		this.listener=listener;
	}
	
	@Override
	public void loadData() {
		this.reqMethod=Method.GET;
		this.reqUrl=String.format(DoubanShareImpl.URL_API_USERIFNO, mBundle.getString(OpenManager.BUNDLE_KEY_UID));
	}

	@Override
	public void handleData() {
		String str=com.open.share.utils.HttpEntityReadUtil.getString(this.readObj);
Log.v("D_UserInfo handleData():", "---"+str);
		if(!TextUtils.isEmpty(str))
		{
			DoubleUserInfo ret=new DoubleUserInfo();
			ret.name=StringUtil.truncateString(str, "<title>", "</title>");
			this.resObj=ret;
		}

	}

}
