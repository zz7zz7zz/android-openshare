/**
 * 
 */
package com.open.share.tencent;

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
public class TenWebViewClient extends WebViewClient {
	
	private final String TAG="TenWebViewClient";
	
	AuthorizeActivity activity;
	
	public TenWebViewClient(AuthorizeActivity activity)
	{
		this.activity=activity;
	}
	
	
	@Override
	public void onPageStarted(WebView view, String url, Bitmap favicon) {
LogUtil.v(TAG, "onPageStarted():"+url);
		handle(view, url);
		super.onPageStarted(view, url, favicon);
	}

    /*
     * TODO Android2.2及以上版本才能使用该方法 
     * 目前https://open.t.qq.com中存在http资源会引起sslerror，待网站修正后可去掉该方法
     */
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) 
    {
LogUtil.v(TAG, "onReceivedSslError():"+view.getUrl());
		handler.proceed();// 接受证书
    }

	private boolean handle(WebView view, final String url)
	{
		if (url == null)
		{
			return false;
		}
		else if(url.startsWith(TenShareImpl.URL_REDIRECT))
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
			String openid=uri.getQueryParameter("openid");
			String openkey=uri.getQueryParameter("openkey");
			if(!TextUtils.isEmpty(access_token))
			{
				TenTokenBean mTenWeiboToken=new TenTokenBean();
				mTenWeiboToken.access_token=access_token;
				mTenWeiboToken.expires_in=expires_in;
				mTenWeiboToken.openid=openid;
				mTenWeiboToken.openkey=openkey;
				
				SharedPreferenceUtil.store(activity,OpenManager.getInstatnce().getSpName(OpenManager.TENCENT_WEIBO), mTenWeiboToken);
				
				Intent intent = new Intent();
				intent.setAction(OpenManager.AUTH_RESULT_ACTION);
				intent.putExtra(OpenManager.BUNDLE_KEY_OPEN, OpenManager.TENCENT_WEIBO);
				activity.sendBroadcast(intent);
			}
			
			activity.finish();
			return true;
		}
		return false;
	}
}
