package com.open.share.douban.api;

import java.net.URLEncoder;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.text.TextUtils;

import com.open.share.ContextMgr;
import com.open.share.OpenManager;
import com.open.share.douban.DoubanShareImpl;
import com.open.share.douban.DoubanTokenBean;
import com.open.share.interfaces.IOpenResponse;
import com.open.share.net.OpenResponse;
import com.open.share.sina.SFormBodyUtil;
import com.open.share.sina.api.SinaNetRunnable;
import com.open.share.utils.LogUtil;
import com.open.share.utils.SharedPreferenceUtil;

public class D_Status extends SinaNetRunnable {

	private Bundle mBundle;
	
	public D_Status(Bundle mBundle,IOpenResponse listener)
	{
		this.mBundle=mBundle;
		this.listener=listener;
	}
	
	@Override
	public void loadData() {
		
		this.reqMethod=Method.POST;
		this.reqUrl=DoubanShareImpl.URL_API_STATUS;
		
		DoubanTokenBean out=new DoubanTokenBean();
		SharedPreferenceUtil.Fetch(ContextMgr.getContext(), OpenManager.getInstatnce().getSpName(OpenManager.DOUBAN), out);
		
		this.reqHeards.add(new BasicNameValuePair("Authorization","Bearer "+out.access_token));
		this.formMultParamList.add(new BasicNameValuePair("source",DoubanShareImpl.APP_KEY));
		this.formMultParamList.add(new BasicNameValuePair("text", URLEncoder.encode(mBundle.getString(OpenManager.BUNDLE_KEY_TEXT))));
		
		byte[] postBody=SFormBodyUtil.getData(reqHeards, formMultParamList, null);
		this.reqPostDate=postBody;
	}

	@Override
	public void handleData() {
		String str=com.open.share.utils.HttpEntityReadUtil.getString(this.readObj);
LogUtil.v("D_Status handleData():", "---"+str);
		if(!TextUtils.isEmpty(str))
		{
			OpenResponse ret=new OpenResponse();
			this.resObj=ret;
			try {
				JSONObject object = new JSONObject(str);
				if(null!=object)
				{
					String id=object.optString("id");
					String msg=object.optString("msg");
					ret.ret=id.length()>0?OpenResponse.RET_OK:OpenResponse.RET_FAILED;
					if(!TextUtils.isEmpty(msg)&&msg.indexOf("重复")!=-1)
					{
						ret.ret=OpenResponse.RET_REPEATE;
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

}
