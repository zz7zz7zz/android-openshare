/**
 * 
 */
package com.open.share.renren.api;

import java.util.ArrayList;
import java.util.Collections;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import android.util.Log;

import com.open.share.ContextMgr;
import com.open.share.OpenManager;
import com.open.share.interfaces.IOpenResponse;
import com.open.share.renren.RFormBodyUtil;
import com.open.share.renren.RenrenComparator;
import com.open.share.renren.RenrenShareImpl;
import com.open.share.renren.RenrenTokenBean;
import com.open.share.renren.RenrenUserInfo;
import com.open.share.renren.RenrenUtil;
import com.open.share.utils.SharedPreferenceUtil;

/**
 * @author Administrator
 *
 */
public class R_UserInfo extends RenNetRunnable {
	
    private String[] uids;
    
    public R_UserInfo(String[] uids,IOpenResponse listener)
    {
    	this.uids=uids;
    	this.listener=listener;
    }

	@Override
	public void loadData() {
		this.reqMethod=Method.POST;
		this.reqUrl=RenrenShareImpl.URL_RESTSERVER;
		
		RenrenTokenBean mRenrenTokenBean=new RenrenTokenBean();
		SharedPreferenceUtil.Fetch(ContextMgr.getContext(), OpenManager.getInstatnce().getSpName(OpenManager.RENREN), mRenrenTokenBean);
		
		this.postParamList.add(new BasicNameValuePair("method", "users.getInfo"));
		this.postParamList.add(new BasicNameValuePair("fields", "uid,name,headurl"));
		if (uids != null&&uids.length>0) 
		{
	        StringBuffer sb = new StringBuffer();
	        for (String uid : uids) 
	        {
	            sb.append(uid).append(",");
	        }
	        sb.deleteCharAt(sb.length() - 1);
	        this.postParamList.add(new BasicNameValuePair("uids", sb.toString()));
        }
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
		
		this.reqPostDate=RFormBodyUtil.getPostData(reqHeards, this.postParamList, null);
	}

	@Override
	public void handleData() {
		
		String response=com.open.share.utils.HttpEntityReadUtil.getString(this.readObj);
Log.v("T_UserInfoMsg handleData():", "---"+response);

        JSONArray array;
		try {
			array = new JSONArray(response);
	        if (array != null) 
	        {
	        	ArrayList<RenrenUserInfo> users = new ArrayList<RenrenUserInfo>();
	            int size = array.length();
	            for (int i = 0; i < size; i++) 
	            {
	                RenrenUserInfo info = new RenrenUserInfo();
	                info.parse(array.optJSONObject(i));
	                if (info != null) {
	                    users.add(info);
	                }
	            }
	            this.resObj=users;
	        }
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
