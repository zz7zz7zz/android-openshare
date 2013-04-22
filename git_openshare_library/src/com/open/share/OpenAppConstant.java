/**
 * 
 */
package com.open.share;

/**
 * 
 * 应用在第三方平台的相关信息，比如App_Key,App_Secret等信息
 * 
 * @author 杨龙辉
 *
 */
public class OpenAppConstant 
{
	/*
	 * 新浪开发相关信息
	 * App Key： 2085605553 
	 * App Secret： d4e6c096864a3282ce1eedf9dbd40d89
	 */
	public static final String SINA_App_Key="2085605553";
	public static final String SINA_App_Secret="d4e6c096864a3282ce1eedf9dbd40d89";
	public static final String SINA_URL_CALLBACK_CONFRIMPAGE="http://www.sxmobi.com/product/detail/id/28/sina/confrimauthorize";//确定授权回调
	public static final String SINA_URL_CALLBACK_CANCELPAGE="http://www.sxmobi.com/product/detail/id/28/sina/cancelauthorize";//取消授权回调
	
	/*
	 * 腾讯开发相关信息
	 * App Key:801143182
	 * App Secret:a8d8351e1f99596adb58e2c9c1723c9f
	 */
	public static final String Tencent_APP_KEY="801143182";
	public static final String Tencent_App_Secret="a8d8351e1f99596adb58e2c9c1723c9f";
	public static final String Tencent_URL_CALLBACK="http://www.sxmobi.com/product/detail/id/28";//回调页面
	
	
	/*
	 * 人人网
	 * 应用ID：198696
	 * API Key：550900cbddb644528f7ad0759c71994b
	 * Secret Key：928feff901614cea9f02cf0a6da50b61
	 */
	public static final String RENREN_APP_ID = "198696";
	public static final String RENREN_API_KEY = "550900cbddb644528f7ad0759c71994b";
	public static final String RENREN_SECRET_KEY = "928feff901614cea9f02cf0a6da50b61";
	public static final String RENREN_URL_CALLBACK = "http://graph.renren.com/oauth/login_success.html";//回调页
	
	
	/*
	 * QQ空间相关信息
	 * App Key:100282939
	 * App Secret:4f85116d0abbdf66d05951daf809930b
	 */
	public static final String QQZONE_APP_ID="100282939";
	public static final String QQZONE_APP_KEY="4f85116d0abbdf66d05951daf809930b";
	public static final String QQZONE_URL_CALLBACK="http://sxmobi.com";//回调页
	public static final String QQZONE_SCOPE="all";//授权权限列表
	
	/*
	 * 豆瓣
	 * App 43a2f13c340a00aa
	 * App 0475e962d98a67400d61bc288c9f35c0
	 */
	public static final String DOUBAN_APP_KEY="0206d212dd7e877f06147638987b967d";
	public static final String DOUBAN_KEY_SECRET="a020b1d3ecf426b0";
	public static final String DOUBAN_URL_CALLBACK="http://www.baidu.com";
	public static final String DOUBAN_SCOPE="";
	
	/*
	 * 微信
	 * App wx3945ec3991782833
	 * App Secret:bd5dfe28a80afee10d05e06a4fa57c3d
	 */
	public static final String WEIXIN_APP_ID="wxe73c48c0fe7b8118";
	public static final String WEIXIN_APP_KEY="c61cdf558cb87de50a0cedfe48134a66";
	
	
	//默认的关注用户
	public static final String DEFAULT_SINA_APP_UID="2740040780";//摩卡时光UID
	public static final String DEFAULT_Tencent_App_UID="memorycamera";//摩卡时光UID
}
