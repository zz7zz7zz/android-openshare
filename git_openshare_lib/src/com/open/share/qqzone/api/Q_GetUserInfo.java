/**
 * 
 */
package com.open.share.qqzone.api;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;

import com.open.share.ContextMgr;
import com.open.share.OpenManager;
import com.open.share.interfaces.IOpenResponse;
import com.open.share.net.AbstractRunnable;
import com.open.share.net.NetPool;
import com.open.share.qqzone.QQZoneShareImpl;
import com.open.share.qqzone.QQZoneTokenBean;
import com.open.share.utils.SharedPreferenceUtil;
import com.tencent.tauth.Constants;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.bean.UserInfo;

/**
 * @author Administrator
 *
 */
public class Q_GetUserInfo extends AbstractRunnable {

	private IOpenResponse listener;
	
	public Q_GetUserInfo(Bundle bundleIn,final IOpenResponse listener)
	{
		this.listener=listener;
	}
	
	@Override
	public void run() {
		
		final UserInfo res=new UserInfo("", "", "", "");
		try {
			QQZoneTokenBean mQqZoneTokenBean=new QQZoneTokenBean();
			SharedPreferenceUtil.Fetch(ContextMgr.getContext(), OpenManager.getInstatnce().getSpName(OpenManager.QQZONE), mQqZoneTokenBean);
			Tencent mTencent = Tencent.createInstance(QQZoneShareImpl.APP_ID, ContextMgr.getContext());
			mTencent.setOpenId(mQqZoneTokenBean.openid);
			mTencent.setAccessToken(mQqZoneTokenBean.access_token, ""+(mQqZoneTokenBean.expires_in-System.currentTimeMillis())/1000);
			
			JSONObject arg0=mTencent.request(Constants.GRAPH_SIMPLE_USER_INFO, null,
                    Constants.HTTP_GET);
			try {
				String nickName = arg0.getString("nickname");
				String figureurl_2=arg0.getString("figureurl_2");
				res.setNickName(nickName);
				res.setIcon_100(figureurl_2);
			} catch (JSONException e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(!isCanceled())
			{
				if(null!=listener)
				{
					listener.response(getmTaskToken(), res);
				}
			}
			NetPool.getInstance().cancelMsgByToken(Q_GetUserInfo.this.getmTaskToken());
		}
	}
}
