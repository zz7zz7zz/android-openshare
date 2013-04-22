package com.open.share.renren.api;

import java.util.Collections;

import org.apache.http.message.BasicNameValuePair;

import android.os.Bundle;

import com.open.share.ContextMgr;
import com.open.share.OpenManager;
import com.open.share.interfaces.IOpenResponse;
import com.open.share.net.OpenResponse;
import com.open.share.renren.RFormBodyUtil;
import com.open.share.renren.RenRenStatusSetResponseBean;
import com.open.share.renren.RenrenComparator;
import com.open.share.renren.RenrenShareImpl;
import com.open.share.renren.RenrenTokenBean;
import com.open.share.renren.RenrenUtil;
import com.open.share.utils.LogUtil;
import com.open.share.utils.SharedPreferenceUtil;

public class R_StatusUpdate extends RenNetRunnable {

	private Bundle mBundle;
	
	public R_StatusUpdate(Bundle mBundle,IOpenResponse listener)
	{
		this.mBundle=mBundle;
		this.listener=listener;
	}
	
	@Override
	public void loadData() {
		
		this.reqMethod=Method.POST;
		this.reqUrl=RenrenShareImpl.URL_RESTSERVER;
		
		RenrenTokenBean mRenrenTokenBean=new RenrenTokenBean();
		SharedPreferenceUtil.Fetch(ContextMgr.getContext(), OpenManager.getInstatnce().getSpName(OpenManager.RENREN), mRenrenTokenBean);
		
		this.postParamList.add(new BasicNameValuePair("method", "status.set"));
		this.postParamList.add(new BasicNameValuePair("status", mBundle.getString(OpenManager.BUNDLE_KEY_TEXT)));
		this.postParamList.add(new BasicNameValuePair("format", "json"));
		this.postParamList.add(new BasicNameValuePair("session_key",mRenrenTokenBean.session_key));
		
		this.postParamList.add(new BasicNameValuePair("api_key",RenrenShareImpl.API_KEY));
		this.postParamList.add(new BasicNameValuePair("v","1.0"));
		this.postParamList.add(new BasicNameValuePair("call_id",String.valueOf(System.currentTimeMillis())));
		this.postParamList.add(new BasicNameValuePair("xn_ss","1"));
		
		Collections.sort(postParamList, new RenrenComparator());
		StringBuffer sb = new StringBuffer();
		for (BasicNameValuePair item:postParamList) 
		{
			sb.append(item.getName());
			sb.append("=");
			sb.append(item.getValue());
		}
		sb.append(mRenrenTokenBean.session_secret);
		this.postParamList.add(new BasicNameValuePair("sig",RenrenUtil.md5(sb.toString())));
		
		this.reqPostDate=RFormBodyUtil.getPostData(reqHeards, this.postParamList,null);
	}

	@Override
	public void handleData() {
		String str=com.open.share.utils.HttpEntityReadUtil.getString(this.readObj);
LogUtil.v("R_StatusUpdateMsg handleData():", "---"+str);
		RenRenStatusSetResponseBean mResponseBean = new RenRenStatusSetResponseBean(str);
		
		OpenResponse ret=new OpenResponse();
		if(null!=mResponseBean&&mResponseBean.getResult()==1)
		{
			ret.ret=OpenResponse.RET_OK;
		}
		this.resObj=ret;
	}

}
