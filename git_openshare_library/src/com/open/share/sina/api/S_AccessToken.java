/**
 * 
 */
package com.open.share.sina.api;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.open.share.OpenManager;
import com.open.share.interfaces.IOpenResponse;
import com.open.share.sina.SFormBodyUtil;
import com.open.share.sina.SinaShareImpl;
import com.open.share.sina.SinaTokenBean;
import com.open.share.utils.LogUtil;

/**
 * 获取AccessToken
 * @author Administrator
 *
 */
public class S_AccessToken extends SinaNetRunnable{
	
	private String code="";
	public S_AccessToken(String code,IOpenResponse listener)
	{
		this.code=code;
		this.listener=listener;
	}

	@Override
	public void loadData() {
		
		this.reqMethod=Method.POST;
		this.reqUrl=SinaShareImpl.URL_OAUTH2_ACCESS_TOKEN;
		
		this.formMultParamList.add(new BasicNameValuePair("client_id", SinaShareImpl.CLIENT_ID));
		this.formMultParamList.add(new BasicNameValuePair("client_secret", SinaShareImpl.CLIENT_SECRET));
		this.formMultParamList.add(new BasicNameValuePair("grant_type", "authorization_code"));
		this.formMultParamList.add(new BasicNameValuePair("code", this.code));
		this.formMultParamList.add(new BasicNameValuePair("redirect_uri", SinaShareImpl.URL_CALLBACK_CONFRIMPAGE));
		
		byte[] postBody=SFormBodyUtil.getData(reqHeards, formMultParamList, null);
		this.reqPostDate=postBody;
	}

	@Override
	public void handleData() 
	{
		String str=com.open.share.utils.HttpEntityReadUtil.getString(this.readObj);
LogUtil.v("AccessTokenMsg handleData():", "---"+str);
		if(!TextUtils.isEmpty(str))
		{
			SinaTokenBean ret=new SinaTokenBean();
			try {
				
				JSONObject obj = new JSONObject(str);
				ret.access_token=obj.optString("access_token");
				
				String expires_in=obj.optString("expires_in");
				if(!TextUtils.isEmpty(expires_in))
				{
					ret.expires_in=System.currentTimeMillis()+Long.valueOf(expires_in)*1000-OpenManager.EARLY_INVAILD_TIME;
				}
				
				ret.uid=obj.optString("uid");
				this.resObj=ret;
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

}
