/**
 * 
 */
package com.open.share.renren.api;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.text.TextUtils;

import com.open.share.OpenManager;
import com.open.share.interfaces.IOpenResponse;
import com.open.share.renren.RenrenShareImpl;
import com.open.share.renren.RenrenTokenBean;
import com.open.share.sina.SFormBodyUtil;
import com.open.share.utils.LogUtil;

/**
 * @author Administrator
 *
 */
public class R_SessionKey extends RenNetRunnable {

	private String accessToken;
	
	public R_SessionKey(String accessToken,IOpenResponse listener)
	{
		this.accessToken=accessToken;
		this.listener=listener;
	}
	
	@Override
	public void loadData() {
		
		this.reqMethod=Method.POST;
		this.reqUrl=RenrenShareImpl.URL_SESSION_KEY;
		
		this.postParamList.add(new BasicNameValuePair("oauth_token", accessToken));
		
		byte[] postBody=SFormBodyUtil.getData(reqHeards, postParamList, null);
		this.reqPostDate=postBody;
	}

	@Override
	public void handleData() {

		String str=com.open.share.utils.HttpEntityReadUtil.getString(this.readObj);
LogUtil.v("R_SessionKeyMsg handleData():", "---"+str);
		if(!TextUtils.isEmpty(str))
		{
			RenrenTokenBean ret=new RenrenTokenBean();
			try {
				
				JSONObject obj = new JSONObject(str);
				String error=obj.optString("error");
				if (!TextUtils.isEmpty(error)) 
				{
					throw new Exception(obj.toString());
				}
				
				ret.access_token=accessToken;
				ret.session_key = obj.getJSONObject("renren_token").getString("session_key");
				ret.session_secret = obj.getJSONObject("renren_token").getString("session_secret");
				ret.uid=obj.getJSONObject("user").getLong("id");
				long expires = obj.getJSONObject("renren_token").getLong("expires_in") * 1000;
				ret.session_expires_in=System.currentTimeMillis()+expires-OpenManager.EARLY_INVAILD_TIME;
				
				this.resObj=ret;
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	
	}

}
