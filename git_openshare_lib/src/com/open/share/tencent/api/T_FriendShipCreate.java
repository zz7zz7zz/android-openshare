/**
 * 
 */
package com.open.share.tencent.api;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.text.TextUtils;

import com.open.share.ContextMgr;
import com.open.share.OpenAppConstant;
import com.open.share.OpenManager;
import com.open.share.interfaces.IOpenResponse;
import com.open.share.net.OpenResponse;
import com.open.share.tencent.TFormBodyUtil;
import com.open.share.tencent.TenShareImpl;
import com.open.share.tencent.TenTokenBean;
import com.open.share.utils.IpUtils;
import com.open.share.utils.LogUtil;
import com.open.share.utils.SharedPreferenceUtil;

/**
 * 关注用户
 * @author Administrator
 *
 */
public class T_FriendShipCreate extends TenNetRunnable {

	private Bundle mBundle;
	
	public T_FriendShipCreate(Bundle mBundle,IOpenResponse listener)
	{
		this.mBundle=mBundle;
		this.listener=listener;
	}
	
	@Override
	public void loadData() {
		this.reqMethod=Method.POST;
		this.reqUrl=TenShareImpl.URL_SERVER+"/friends/add";
		
		TenTokenBean mTencentWeiboTokenBean=new TenTokenBean();
		SharedPreferenceUtil.Fetch(ContextMgr.getContext(), OpenManager.getInstatnce().getSpName(OpenManager.TENCENT_WEIBO), mTencentWeiboTokenBean);
		
		String uid=mBundle.getString(OpenManager.BUNDLE_KEY_UID);
		if(TextUtils.isEmpty(uid))
		{
			uid=OpenAppConstant.DEFAULT_SINA_APP_UID;
		}
		
		this.formMultParamList.add(new BasicNameValuePair("oauth_consumer_key", TenShareImpl.APP_KEY));
		this.formMultParamList.add(new BasicNameValuePair("access_token", mTencentWeiboTokenBean.access_token));
		this.formMultParamList.add(new BasicNameValuePair("openid", mTencentWeiboTokenBean.openid));
		this.formMultParamList.add(new BasicNameValuePair("clientip", IpUtils.getPsdnIp()));
		this.formMultParamList.add(new BasicNameValuePair("oauth_version","2.a"));
		this.formMultParamList.add(new BasicNameValuePair("scope","all"));
		
		this.formMultParamList.add(new BasicNameValuePair("format","json"));
		this.formMultParamList.add(new BasicNameValuePair("name",uid));
		
		byte[] postBody=TFormBodyUtil.getPostData(reqHeards, formMultParamList);
		this.reqPostDate=postBody;
	}

	/* (non-Javadoc)
	 * @see com.open.share.net.AbstractNetMessage#handleData()
	 */
	@Override
	public void handleData() {
		String str=com.open.share.utils.HttpEntityReadUtil.getString(this.readObj);
LogUtil.v("T_FriendShipCreateMsg handleData():", "---"+str);
		if(!TextUtils.isEmpty(str))
		{
			OpenResponse ret=new OpenResponse();
			try {
				JSONObject object = new JSONObject(str);
				if(null!=object)
				{
					ret.ret=object.optInt("ret",OpenResponse.RET_FAILED);
					if(ret.ret==OpenResponse.RET_OK)
					{

					}
					else
					{
						ret.errcode=object.optInt("error_code",-1);
						ret.errmsg=object.optString("error","");
					}
				}
				this.resObj=ret;
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

	}
}
