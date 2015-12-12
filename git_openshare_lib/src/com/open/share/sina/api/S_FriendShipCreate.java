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
import com.open.share.OpenAppConstant;
import com.open.share.OpenManager;
import com.open.share.interfaces.IOpenResponse;
import com.open.share.net.OpenResponse;
import com.open.share.sina.SFormBodyUtil;
import com.open.share.sina.SinaShareImpl;
import com.open.share.sina.SinaTokenBean;
import com.open.share.utils.LogUtil;
import com.open.share.utils.SharedPreferenceUtil;

/**
 * 关注用户
 * @author Administrator
 *
 */
public class S_FriendShipCreate extends SinaNetRunnable {

	private Bundle mBundle;
	
	public S_FriendShipCreate(Bundle mBundle,IOpenResponse listener)
	{
		this.mBundle=mBundle;
		this.listener=listener;
	}
	
	@Override
	public void loadData() {
		this.reqMethod=Method.POST;
		this.reqUrl=SinaShareImpl.URL_FRIENDSHIPS_CREATE;
		
		SinaTokenBean out=new SinaTokenBean();
		SharedPreferenceUtil.Fetch(ContextMgr.getContext(), OpenManager.getInstatnce().getSpName(OpenManager.SINA_WEIBO), out);
		String uid=mBundle.getString(OpenManager.BUNDLE_KEY_UID);
		if(TextUtils.isEmpty(uid))
		{
			uid=OpenAppConstant.DEFAULT_SINA_APP_UID;
		}
		
		this.formMultParamList.add(new BasicNameValuePair("access_token", out.access_token));
		this.formMultParamList.add(new BasicNameValuePair("uid",uid));
		
		byte[] postBody=SFormBodyUtil.getData(reqHeards, formMultParamList, null);
		this.reqPostDate=postBody;
	}

	/* (non-Javadoc)
	 * @see com.open.share.net.AbstractNetMessage#handleData()
	 */
	@Override
	public void handleData() {
		String str=com.open.share.utils.HttpEntityReadUtil.getString(this.readObj);
LogUtil.v("S_FriendShipCreate handleData():", "---"+str);
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
					}
				}
				this.resObj=ret;
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

	}

}
