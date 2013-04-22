/**
 * 
 */
package com.open.share;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.open.share.utils.WebViewCookieUtil;

/**
 * 第三方授权界面用
 * 
 * @author 杨龙辉
 *
 */
public class AuthorizeActivity extends Activity{
	
	private WebView mWebView;
	private ProgressDialog pd;

	private String extra_uri;//外部传入的uri
	private int extra_open_Dest;
	
	public final int showProDlg=1;
	public final int closeProDlg=2;
	
	public Handler mHandler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch(msg.what)
			{
				case showProDlg:
					if(null!=pd&&!pd.isShowing())
					{
						pd.show();
					}
					break;
					
				case closeProDlg:
					pd.hide();
					break;
			}
			super.handleMessage(msg);
		}
		
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN); 
		
		initData();
		initView();
	}
	
	private void initData()
	{
		extra_uri=this.getIntent().getStringExtra(OpenManager.EXTRA_AUTHORIZE_URL);
		extra_open_Dest=this.getIntent().getIntExtra(OpenManager.EXTRA_OPEN_DEST,-1);
	}
	
	private void initView()
	{
		LinearLayout contentView=new LinearLayout(this);
		contentView.setBackgroundColor(Color.WHITE);
		LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
		mWebView=new WebView(this);
		contentView.addView(mWebView,params);
		setContentView(contentView);
		
		WebViewCookieUtil.clearCookies(this);
		
		mWebView.setHorizontalScrollBarEnabled(true);
		mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY) ;//去掉右边白色边框
		
		WebSettings mWebSettings = mWebView.getSettings();
		mWebSettings.setJavaScriptEnabled(true);
		mWebSettings.setSupportZoom(true);
		mWebSettings.setBuiltInZoomControls(true);// 设置支持缩放
		mWebSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		
		pd = new ProgressDialog(AuthorizeActivity.this);
		pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pd.setMessage("数据加载中,请稍后!");
		pd.show();
		
		WebViewClient client=WebViewClientFactory.produce(extra_open_Dest,AuthorizeActivity.this);
		mWebView.setWebViewClient(client);
		
		mWebView.setWebChromeClient(new WebChromeClient()
		{

			@Override
			public void onProgressChanged(WebView view, int newProgress)
			{
				if (newProgress == 100 && pd != null)
				{
					pd.hide();
				}
				super.onProgressChanged(view, newProgress);
			}
		});
		mWebView.loadUrl(extra_uri);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack())
		{
			try {
				mWebView.goBack();
			} catch (Exception e) {
				e.printStackTrace();
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onResume() {
		super.onResume();
//		CookieSyncManager.getInstance().startSync();
	}

	@Override
	protected void onStop() {
		super.onStop();
//		CookieSyncManager.getInstance().stopSync();
	}

	@Override
	public void finish() {
		if(null!=pd&&pd.isShowing())
		{
			pd.dismiss();
			pd=null;
		}
		mHandler.removeMessages(showProDlg);
		mHandler.removeMessages(closeProDlg);
		super.finish();
	}
}
