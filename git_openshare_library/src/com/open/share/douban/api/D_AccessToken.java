/**
 * 
 */
package com.open.share.douban.api;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.open.share.OpenManager;
import com.open.share.douban.DoubanShareImpl;
import com.open.share.douban.DoubanTokenBean;
import com.open.share.interfaces.IOpenResponse;
import com.open.share.sina.SFormBodyUtil;
import com.open.share.sina.api.SinaNetRunnable;
import com.open.share.utils.LogUtil;

/**
 * 获取AccessToken
 * @author Administrator
 *
 */
public class D_AccessToken extends SinaNetRunnable{
	
	private String code="";
	public D_AccessToken(String code,IOpenResponse listener)
	{
		this.code=code;
		this.listener=listener;
	}

	@Override
	public void loadData() {
		
		this.reqMethod=Method.POST;
		this.reqUrl=DoubanShareImpl.OAUTH2;
		
		this.formMultParamList.add(new BasicNameValuePair("client_id", DoubanShareImpl.APP_KEY));
		this.formMultParamList.add(new BasicNameValuePair("client_secret", DoubanShareImpl.KEY_SECRET));
		this.formMultParamList.add(new BasicNameValuePair("redirect_uri", DoubanShareImpl.URL_CALLBACK));
		this.formMultParamList.add(new BasicNameValuePair("grant_type", "authorization_code"));
		this.formMultParamList.add(new BasicNameValuePair("code", this.code));
		byte[] postBody=SFormBodyUtil.getData(reqHeards, formMultParamList, null);
		
		this.reqPostDate=postBody;
	}

	@Override
	public void handleData() 
	{
		String str=com.open.share.utils.HttpEntityReadUtil.getString(this.readObj);
LogUtil.v("D_AccessToken handleData():", "---"+str);
		if(!TextUtils.isEmpty(str))
		{
			DoubanTokenBean ret=new DoubanTokenBean();
			try {
				
				JSONObject obj = new JSONObject(str);
				ret.access_token=obj.optString("access_token");
				
				String expires_in=obj.optString("expires_in");
				if(!TextUtils.isEmpty(expires_in))
				{
					ret.expires_in=System.currentTimeMillis()+Long.valueOf(expires_in)*1000-OpenManager.EARLY_INVAILD_TIME;
				}
				
				ret.uid=obj.optString("douban_user_id");
				ret.name=obj.optString("douban_user_name");
				this.resObj=ret;
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
}
