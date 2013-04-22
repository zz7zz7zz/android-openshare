/**
 * 
 */
package com.open.share.renren;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.open.share.AuthorizeActivity;
import com.open.share.OpenManager;
import com.open.share.interfaces.IOpenResponse;
import com.open.share.net.NetPool;
import com.open.share.renren.api.R_SessionKey;
import com.open.share.utils.LogUtil;
import com.open.share.utils.SharedPreferenceUtil;

/**
 * 
 * @author 杨龙辉
 *
 */
public class RenrenWebViewClient extends WebViewClient {
	
	private final String TAG="RenrenWebViewClient";
	
	AuthorizeActivity activity;
	
	public RenrenWebViewClient(AuthorizeActivity activity)
	{
		this.activity=activity;
	}
	
	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url)
	{
//		if(handler(view, url))
//		{
//			return true;
//		}
		return super.shouldOverrideUrlLoading(view, url);
	}
	
	
	@Override
	public void onReceivedSslError(WebView view, SslErrorHandler handler,
			SslError error) {
LogUtil.v(TAG, "onReceivedSslError():"+view.getUrl());
		handler.proceed();
	}
	
	@Override
	public void onPageStarted(WebView view, String url, Bitmap favicon) {
		if(handler(view, url))
		{
			
		}
		else
		{
			super.onPageStarted(view, url, favicon);
		}
		
	}

	private boolean handler(WebView view, String url)
	{
		if (url == null)
		{
			return false;
		}
//		else if(url.contains("display"))
//		{
//			return true;
//		}
//		else if(url.startsWith("http://graph.renren.com/login_deny/"))
//		{
//			return true;
//		}
		else if(url.startsWith(RenrenShareImpl.URL_DEFAULT_REDIRECT))
		{
			Message msg=activity.mHandler.obtainMessage(activity.showProDlg);
			activity.mHandler.sendMessage(msg);
			
			 view.stopLoading();
			 
			 final Bundle values = RFormBodyUtil.parseUrl(url);
             String error = values.getString("error");//OAuth返回的错误代码
             if (error != null) 
             {
                 if ("access_denied".equalsIgnoreCase(error)) 
                 {
                	 
                 } 
                 else if ("login_denied".equalsIgnoreCase(error)) 
                 {
                	 
                 } 
                 else
                 {
                     String desc = values.getString("error_description");
                     String errorUri = values.getString("error_uri");
LogUtil.v("RenrenWebViewClient", "desc:"+desc+"\terrorUri:"+errorUri);
                 }
             } 
             else 
             {
            	 String accessToken = values.getString("access_token");
            	 if(!TextUtils.isEmpty(accessToken))
            	 {
                	 getAccessTokenAtts(accessToken);
                	 return true;
            	 }
             }
			
			activity.finish();
			return true;
		}
		return false;
	}
	
	
	private void getAccessTokenAtts(final String accessToken)
	{
		
		R_SessionKey msg=new R_SessionKey(accessToken, new IOpenResponse(){

			@Override
			public boolean response(int token, Object obj) {
				if(null!=obj&&obj instanceof RenrenTokenBean)
				{
					RenrenTokenBean ret=(RenrenTokenBean)obj;
					SharedPreferenceUtil.store(activity, OpenManager.getInstatnce().getSpName(OpenManager.RENREN), ret);
					
					Intent intent = new Intent();
					intent.setAction(OpenManager.AUTH_RESULT_ACTION);
					intent.putExtra(OpenManager.BUNDLE_KEY_OPEN, OpenManager.RENREN);
					activity.sendBroadcast(intent);
				}
				
				activity.finish();
				return true;
			}

		});
		NetPool.getInstance().push(msg);
	}
}
