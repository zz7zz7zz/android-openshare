/**
 * 
 */
package com.open.share;

import android.webkit.WebViewClient;

import com.open.share.douban.DoubanWebViewClient;
import com.open.share.renren.RenrenWebViewClient;
import com.open.share.sina.SinaWebViewClient;
import com.open.share.tencent.TenWebViewClient;

/**
 * @author 杨龙辉
 *
 */
public class WebViewClientFactory {
	
	public static WebViewClient produce(int destType,AuthorizeActivity activity)
	{
		WebViewClient ret=null;
		
		switch(destType)
		{
			case OpenManager.SINA_WEIBO:
				ret=new SinaWebViewClient(activity);
				break;
				
			case OpenManager.TENCENT_WEIBO:
				ret=new TenWebViewClient(activity);
				break;
				
			case OpenManager.RENREN:
				ret=new RenrenWebViewClient(activity);
				break;
				
			case OpenManager.QQZONE:
				break;
				
			case OpenManager.DOUBAN:
				ret=new DoubanWebViewClient(activity);
				break;
		}
		
		return ret;
	}
	
}
