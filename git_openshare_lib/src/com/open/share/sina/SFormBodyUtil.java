package com.open.share.sina;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.http.message.BasicNameValuePair;

import android.text.TextUtils;

import com.open.share.utils.FileUtil;


public  class SFormBodyUtil {
	
	
    private static final String BOUNDARY = "7cd4a6d158c";
    private static final String MP_BOUNDARY = "--" + BOUNDARY;
    private static final String END_MP_BOUNDARY = "--" + BOUNDARY + "--";
    
	public static byte[] getData(ArrayList<BasicNameValuePair> reqHeards,ArrayList<BasicNameValuePair> postParamList,String filePath)
	{
		ByteArrayOutputStream bos = new ByteArrayOutputStream(1024 * 50);
		try {
			if (!TextUtils.isEmpty(filePath)) 
			{
				writeParamData(bos,postParamList);
				reqHeards.add(new BasicNameValuePair("Content-Type", "multipart/form-data; boundary=" + BOUNDARY));
				writeImageData(bos,filePath);
			}
			else
			{
				reqHeards.add(new BasicNameValuePair("Content-Type", "application/x-www-form-urlencoded"));
                String postParam = encodeParameters(postParamList);
                bos.write(postParam.getBytes("UTF-8"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bos.toByteArray();
	}
	
	
    private static void  writeParamData(OutputStream out,ArrayList<BasicNameValuePair> getParamList) throws IOException
    {
        for (int i = 0; i < getParamList.size(); i++) 
        {
            StringBuilder sb = new StringBuilder();
            sb.append(MP_BOUNDARY).append("\r\n");
            sb.append("content-disposition: form-data; name=\"").append(getParamList.get(i).getName()).append("\"\r\n\r\n");
            sb.append(getParamList.get(i).getValue()).append("\r\n");
            
            byte[]ParamData = sb.toString().getBytes();
            out.write(ParamData);
        }
    }
    
    private static void writeImageData(OutputStream out,String filePath) throws IOException
    {
        StringBuilder sb = new StringBuilder();
        sb.append(MP_BOUNDARY).append("\r\n");
        sb.append("Content-Disposition: form-data; name=\"pic\"; filename=\"").append("news_image").append("\"\r\n");
        sb.append("Content-Type: ").append("image/jpg").append("\r\n\r\n");
        byte[] res = sb.toString().getBytes();
        
        out.write(res);
        out.write(FileUtil.readFile(filePath));
        out.write("\r\n".getBytes());
        out.write(("\r\n" + END_MP_BOUNDARY).getBytes());
    }
    
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
}
