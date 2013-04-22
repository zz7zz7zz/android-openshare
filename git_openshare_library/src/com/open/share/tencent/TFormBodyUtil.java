package com.open.share.tencent;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.message.BasicNameValuePair;


public  class TFormBodyUtil {
	
	public static byte[] getPostData(ArrayList<BasicNameValuePair> heards,ArrayList<BasicNameValuePair> getParamList)
	{
		ByteArrayOutputStream bos = new ByteArrayOutputStream(1024 * 50);
        try {
        	
        	heards.add(new BasicNameValuePair("Content-Type", "application/x-www-form-urlencoded"));
//    		String postParam = getPostString(getParamList);
        	
        	String postParam = QStrOperate.getQueryString(getParamList);//2012.10.29
			bos.write(postParam.getBytes());
		
        } catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bos.toByteArray();
	}
	
	private static String getPostString(ArrayList<BasicNameValuePair> getParamList)
	{
		if(getParamList.size()>0)
		{
			StringBuilder result = new StringBuilder();
			for (BasicNameValuePair param : getParamList) 
			{
				if (result.length() != 0) 
				{
					result.append("&");
				}
				
				result.append(param.getName());
				result.append("=");
				result.append(param.getValue());
			}
			return result.toString();
		}
		return null;
	}
	
//    public static String paramEncode(String paramDecString)
//    {
//        if (TextUtils.isEmpty(paramDecString)) 
//        {
//            return "";
//        }
//        try {
//            return URLEncoder.encode(paramDecString, "UTF-8").replace("+", "%20")
//                    .replace("*", "%2A").replace("%7E", "~")
//                    .replace("#", "%23");
//        } catch (UnsupportedEncodingException e) {
//            throw new RuntimeException(e.getMessage(), e);
//        }
//    }
}
