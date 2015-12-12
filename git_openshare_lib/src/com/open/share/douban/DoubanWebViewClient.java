package com.open.share.douban;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.text.TextUtils;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.open.share.OpenManager;
import com.open.share.douban.api.D_AccessToken;
import com.open.share.interfaces.IOpenResponse;
import com.open.share.net.NetPool;
import com.open.share.utils.LogUtil;
import com.open.share.utils.SharedPreferenceUtil;

public class DoubanWebViewClient extends WebViewClient {

	private final String TAG="DoubanWebViewClient";
	Activity activity;
	
	public DoubanWebViewClient(Activity activity)
	{
		this.activity=activity;
	}
	
	@Override
	public void onPageStarted(WebView view, String url, Bitmap favicon) {
LogUtil.v(TAG, "onPageStarted():"+url);
		if(handle(view, url))
		{

		}
		else
		{
			super.onPageStarted(view, url, favicon);
		}
	}

	@Override
	public void onReceivedSslError(WebView view, SslErrorHandler handler,
			SslError error) {
LogUtil.v(TAG, "onReceivedSslError():"+view.getUrl());
		handler.proceed();
	}
	
	private boolean handle(WebView view,String url)
	{
		if(url == null)
		{
			return false;
		}
		else if(url.startsWith(DoubanShareImpl.URL_CALLBACK))
		{
//			view.stopLoading();
			view.destroyDrawingCache();
			view.destroy();
			
			Uri uri = Uri.parse(url);
			String error = uri.getQueryParameter("error");
			String code = uri.getQueryParameter("code");
			
			if(!TextUtils.isEmpty(error))
			{
				view.stopLoading();
				view.destroy();
				activity.finish();
			}
			
			if(!TextUtils.isEmpty(code))
			{
				getAccessToken(code);
				return false;
			}
			else
			{
				activity.finish();
			}
		}
		return false;
	}
	
	private void getAccessToken(String code)
	{
		D_AccessToken run=new D_AccessToken(code, new IOpenResponse() {
			
			@Override
			public boolean response(int token, Object obj) {
				if(null!=obj&&obj instanceof DoubanTokenBean)
				{
					DoubanTokenBean ret=(DoubanTokenBean)obj;
					if(!TextUtils.isEmpty(ret.access_token))
					{
						SharedPreferenceUtil.store(activity, OpenManager.getInstatnce().getSpName(OpenManager.DOUBAN), ret);
						
						Intent intent = new Intent();
						intent.setAction(OpenManager.AUTH_RESULT_ACTION);
						intent.putExtra(OpenManager.BUNDLE_KEY_OPEN, OpenManager.DOUBAN);
						activity.sendBroadcast(intent);
					}
				}
				
				activity.finish();
				return true;
			}
		});
		NetPool.getInstance().push(run);
	}
	
	
//	private void getUserInfo(String userID)
//	{
//		Bundle mBundle=new Bundle();
//		mBundle.putString(OpenManager.BUNDLE_KEY_UID, userID);
//		
//		D_UserInfo run=new D_UserInfo(mBundle, new IOpenResponse() {
//			
//			@Override
//			public boolean response(int token, Object obj) {
//				
//				if(null!=obj&&obj instanceof DoubleUserInfo)
//				{
//					DoubleUserInfo ret=(DoubleUserInfo)obj;
//					
//					DoubanTokenBean out=new DoubanTokenBean();
//					SharedPreferenceUtil.Fetch(ContextMgr.getContext(), OpenManager.getInstatnce().getSpName(OpenManager.DOUBAN), out);
//					out.name=(null==ret.name?"":ret.name);
//					
//					SharedPreferenceUtil.store(activity, OpenManager.getInstatnce().getSpName(OpenManager.DOUBAN), out);
//				}
//				
//				Intent intent = new Intent();
//				intent.setAction(OpenManager.AUTH_RESULT_ACTION);
//				intent.putExtra(OpenManager.BUNDLE_KEY_OPEN, OpenManager.DOUBAN);
//				activity.sendBroadcast(intent);
//				activity.finish();
//				
//				return false;
//			}
//		});
//		NetPool.getInstance().push(run);
//	}
}
