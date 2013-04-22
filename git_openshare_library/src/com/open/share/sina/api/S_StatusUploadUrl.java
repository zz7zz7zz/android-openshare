/**
 * 
 */
package com.open.share.sina.api;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.text.TextUtils;

import com.open.share.ContextMgr;
import com.open.share.OpenManager;
import com.open.share.interfaces.IOpenResponse;
import com.open.share.net.OpenResponse;
import com.open.share.sina.SFormBodyUtil;
import com.open.share.sina.SinaShareImpl;
import com.open.share.sina.SinaTokenBean;
import com.open.share.utils.LogUtil;
import com.open.share.utils.SharedPreferenceUtil;

/**
 * @author Administrator
 *
 */
public class S_StatusUploadUrl extends SinaNetRunnable {

	private Bundle mBundle;
	
	public S_StatusUploadUrl(Bundle mBundle,IOpenResponse listener)
	{
		this.mBundle=mBundle;
		this.listener=listener;
	}
	
	@Override
	public void loadData() {
		
		this.reqMethod=Method.POST;
		this.reqUrl=SinaShareImpl.URL_STATUSES_UPLOAD_URL;
		
		SinaTokenBean out=new SinaTokenBean();
		SharedPreferenceUtil.Fetch(ContextMgr.getContext(), OpenManager.getInstatnce().getSpName(OpenManager.SINA_WEIBO), out);
		
		this.formMultParamList.add(new BasicNameValuePair("access_token", out.access_token));
		this.formMultParamList.add(new BasicNameValuePair("status", mBundle.getString(OpenManager.BUNDLE_KEY_TEXT)));
		this.formMultParamList.add(new BasicNameValuePair("url", mBundle.getString(OpenManager.BUNDLE_KEY_PICURL)));
//		this.formMultParamList.add(new BasicNameValuePair("lat", mBundle.getString(OpenManager.BUNDLE_KEY_LATITUDE)));
//		this.formMultParamList.add(new BasicNameValuePair("long", mBundle.getString(OpenManager.BUNDLE_KEY_LONGITUDE)));
		this.formMultParamList.add(new BasicNameValuePair("lat","0.0"));
		this.formMultParamList.add(new BasicNameValuePair("long", "0.0"));
		
		byte[] postBody=SFormBodyUtil.getData(reqHeards, formMultParamList, null);
		this.reqPostDate=postBody;
	}

	/* (non-Javadoc)
	 * @see com.open.share.net.AbstractNetMessage#handleData()
	 */
	@Override
	public void handleData() {
		String str=com.open.share.utils.HttpEntityReadUtil.getString(this.readObj);
LogUtil.v("StatusUploadUrlMsg handleData():", "---"+str);
		if(!TextUtils.isEmpty(str))
		{
			OpenResponse ret=new OpenResponse();
			try {
				JSONObject object = new JSONObject(str);
				if(null!=object)
				{
					ret.ret=object.optInt("error_code",OpenResponse.RET_OK);
					if(ret.ret==OpenResponse.RET_OK)
					{

					}
					else
					{
						ret.errcode=object.optInt("error_code",-1);
						ret.errmsg=object.optString("error","");
						
						if(!TextUtils.isEmpty(ret.errmsg)&&(ret.errmsg.indexOf("repeat")!=-1))
						{
							ret.ret=OpenResponse.RET_REPEATE;
						}
					}
					this.resObj=ret;
					return ;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

	}

}
