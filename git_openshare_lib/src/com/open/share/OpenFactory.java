package com.open.share;

import com.open.share.douban.DoubanShareImpl;
import com.open.share.interfaces.IShare;
import com.open.share.qqzone.QQZoneShareImpl;
import com.open.share.renren.RenrenShareImpl;
import com.open.share.sina.SinaShareImpl;
import com.open.share.tencent.TenShareImpl;

/**
 * @author 杨龙辉
 *
 */
public class OpenFactory {

	public static IShare produce(int destType)
	{
		IShare ret=null;
		
		switch(destType)
		{
			case OpenManager.SINA_WEIBO:
				ret=new SinaShareImpl();
				break;
				
			case OpenManager.TENCENT_WEIBO:
				ret=new TenShareImpl();
				break;
				
			case OpenManager.RENREN:
				ret=new RenrenShareImpl();
				break;
				
			case OpenManager.QQZONE:
				ret=new QQZoneShareImpl();
				break;
				
			case OpenManager.DOUBAN:
				ret=new DoubanShareImpl();
				break;
				
			case OpenManager.WEIXIN:
				break;
				
			case OpenManager.FACEBOOK:
				break;
				
			case OpenManager.TWITTER:
				break;
				
			case OpenManager.MYSPACE:
				break;
		}

		return ret;
	}
}
