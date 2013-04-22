package com.open.share.tencent.api;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.util.Log;

import com.open.share.ContextMgr;
import com.open.share.OpenManager;
import com.open.share.interfaces.IOpenResponse;
import com.open.share.tencent.TenShareImpl;
import com.open.share.tencent.TenTokenBean;
import com.open.share.tencent.TenUserInfo;
import com.open.share.utils.IpUtils;
import com.open.share.utils.SharedPreferenceUtil;

/**
 * 获取用户资料
 * @author Administrator
 *
 */

public class T_UserInfo extends TenNetRunnable {
	
	public T_UserInfo(IOpenResponse listener)
	{
		this.listener=listener;
	}
	
	@Override
	public void loadData() {
		this.reqMethod=Method.GET;
		this.reqUrl=TenShareImpl.URL_SERVER+"/user/info";
		
		TenTokenBean mTencentWeiboTokenBean=new TenTokenBean();
		SharedPreferenceUtil.Fetch(ContextMgr.getContext(),OpenManager.getInstatnce().getSpName(OpenManager.TENCENT_WEIBO), mTencentWeiboTokenBean);
		
		this.reqGetParams.add(new BasicNameValuePair("oauth_consumer_key", TenShareImpl.APP_KEY));
		this.reqGetParams.add(new BasicNameValuePair("access_token", mTencentWeiboTokenBean.access_token));
		this.reqGetParams.add(new BasicNameValuePair("openid", mTencentWeiboTokenBean.openid));
		this.reqGetParams.add(new BasicNameValuePair("clientip", IpUtils.getPsdnIp()));
		this.reqGetParams.add(new BasicNameValuePair("oauth_version","2.a"));
		this.reqGetParams.add(new BasicNameValuePair("scope","all"));
		this.reqGetParams.add(new BasicNameValuePair("format","json"));
	}

	@Override
	public void handleData() {
		String str=com.open.share.utils.HttpEntityReadUtil.getString(this.readObj);
Log.v("T_UserInfoMsg handleData():", "---"+str);
		try {
			JSONObject object = new JSONObject(str);
			if(null!=object)
			{
				TenUserInfo ret=new TenUserInfo();
				ret.name=object.getJSONObject("data").optString("name", "");
				ret.head=object.getJSONObject("data").optString("head", "")+"/100";
				this.resObj=ret;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
