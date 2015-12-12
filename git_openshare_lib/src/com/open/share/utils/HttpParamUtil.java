/**
 * 
 */
package com.open.share.utils;

import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.http.message.BasicNameValuePair;

import android.text.TextUtils;

/**
 * 网络请求的参数
 * 
 * @author 杨龙辉
 *
 */
public class HttpParamUtil {
	
//	private static String getSuffixUrl(ArrayList<Parameter> params)
//	{
//		if(params.size()>0)
//		{
//			StringBuilder result = new StringBuilder();
//			for (int i = 0; i < params.size(); i++) 
//            {
//                if (i != 0) 
//                {
//                    result.append("&");
//                }
//                result.append(params.get(i).getName()).append("=").append(params.get(i).getValue());
//            }
//			return result.toString();
//		}
//		return "";
//	}
	
    public static String encodeParameters(ArrayList<BasicNameValuePair> params) 
    {
    	if(params.size()>0)
    	{
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < params.size(); i++) 
            {
                if (i != 0) 
                {
                    result.append("&");
                }
                try {
                    result.append(URLEncoder.encode(params.get(i).getName(), "UTF-8")).append("=").append(URLEncoder.encode(params.get(i).getValue(), "UTF-8"));
                } catch (java.io.UnsupportedEncodingException neverHappen) {
                }
            }
            return result.toString();
    	}

        return "";
    }
	
	/**
	 * 返回最终的请求url
	 */
	public static String getRequestUrl(String requestUrl,ArrayList<BasicNameValuePair> getParamList)
	{
		String finalUrl=requestUrl;
		
		String suffix=encodeParameters(getParamList);
		if(!TextUtils.isEmpty(suffix))
		{
			finalUrl+=("?"+suffix);
		}
		
		return finalUrl;
	}
}
