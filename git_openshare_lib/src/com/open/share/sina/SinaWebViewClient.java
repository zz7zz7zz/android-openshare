/**
 * 
 */
package com.open.share.sina;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Message;
import android.text.TextUtils;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.open.share.AuthorizeActivity;
import com.open.share.OpenManager;
import com.open.share.utils.LogUtil;
import com.open.share.utils.SharedPreferenceUtil;

/**
 * 
 * @author 杨龙辉
 *
 */
public class SinaWebViewClient extends WebViewClient {
	
	private final String TAG="SinaWebViewClient";
	
	AuthorizeActivity activity;
	
	public SinaWebViewClient(AuthorizeActivity activity)
	{
		this.activity=activity;
	}
	
	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url)
	{
LogUtil.v(TAG, "shouldOverrideUrlLoading():"+url);
		return super.shouldOverrideUrlLoading(view, url);
		
	}

	@Override
	public void onPageStarted(WebView view, String url, Bitmap favicon) {
LogUtil.v(TAG, "onPageStarted():"+url);
		handle(view, url);
	}
	
	
	@Override
	public void onReceivedSslError(WebView view, SslErrorHandler handler,
			SslError error) {
LogUtil.v(TAG, "onReceivedSslError():"+view.getUrl());
		handler.proceed();// 接受证书 
	}

	private boolean handle(WebView view,String url)
	{
		if (url == null)
		{
			return false;
		}
		else if(url.startsWith(SinaShareImpl.URL_CALLBACK_CANCELPAGE))
		{
			view.stopLoading();
			activity.finish();
			return true;
		}
		else if(url.startsWith(SinaShareImpl.URL_CALLBACK_CONFRIMPAGE))
		{
			Message msg=activity.mHandler.obtainMessage(activity.showProDlg);
			activity.mHandler.sendMessage(msg);
			
			view.stopLoading();
            
			String temp=url.replaceFirst("#", "?");
			Uri uri = Uri.parse(temp);
			String access_token = uri.getQueryParameter("access_token");
			long expires_in=0;
			try {
				
				expires_in=System.currentTimeMillis()+Long.valueOf(uri.getQueryParameter("expires_in"))*1000-OpenManager.EARLY_INVAILD_TIME;
			} catch (Exception e) {
			}
			String uid=uri.getQueryParameter("uid");

			if(!TextUtils.isEmpty(access_token))
			{
				SinaTokenBean ret=new SinaTokenBean();
				ret.access_token=access_token;
				ret.expires_in=expires_in;
				ret.uid=uid;
				SharedPreferenceUtil.store(activity, OpenManager.getInstatnce().getSpName(OpenManager.SINA_WEIBO), ret);
				
				Intent intent = new Intent();
				intent.setAction(OpenManager.AUTH_RESULT_ACTION);
				intent.putExtra(OpenManager.BUNDLE_KEY_OPEN, OpenManager.SINA_WEIBO);
				activity.sendBroadcast(intent);
			}
			
			activity.finish();
			return true;
		}
//		else if(url.startsWith(SinaShareImpl.URL_CALLBACK_CONFRIMPAGE))
//		{
//			Message msg=activity.mHandler.obtainMessage(activity.showProDlg);
//			activity.mHandler.sendMessage(msg);
//			
//			view.stopLoading();
//			
//			Uri uri = Uri.parse(url);
//			final String code = uri.getQueryParameter("code");
//			
//			if(!TextUtils.isEmpty(code))
//			{
//				getAccessToken(code);
//			}
//			else
//			{
//				activity.finish();
//			}
//			
//			return true;
//		}
		return false;
	}
	
	
//	private void getAccessToken(String code)
//	{
//		S_AccessToken msg=new S_AccessToken(code, new SimpleResponse(){
//			@Override
//			public boolean response(int token,Object obj){
//				if(null!=obj&&obj instanceof SinaTokenBean)
//				{
//					SinaTokenBean ret=(SinaTokenBean)obj;
//					if(!TextUtils.isEmpty(ret.access_token))
//					{
//						SharedPreferenceUtil.store(activity, OpenManager.getInstatnce().getSpName(OpenManager.SINA_WEIBO), ret);
//						
//						Intent intent = new Intent();
//						intent.setAction(OpenManager.AUTH_RESULT_ACTION);
//						intent.putExtra(OpenManager.BUNDLE_KEY_OPEN, OpenManager.SINA_WEIBO);
//						activity.sendBroadcast(intent);
//					}
//				}
//				
//				activity.finish();
//				return false;
//			}
//		});
//		NetPool.getInstance().push(msg);
//	}
}
