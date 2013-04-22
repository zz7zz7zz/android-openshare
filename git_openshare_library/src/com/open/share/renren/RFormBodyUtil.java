package com.open.share.renren;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.http.message.BasicNameValuePair;

import android.os.Bundle;
import android.text.TextUtils;

import com.open.share.utils.FileUtil;
import com.open.share.utils.HttpParamUtil;


public  class RFormBodyUtil {
	
	
    private static final String BOUNDARY = "-----------------------------114975832116442893661388290519";
    private static final String MP_BOUNDARY = "--" + BOUNDARY;
    private static final String END_MP_BOUNDARY = "--" + BOUNDARY + "--";
    
	public static byte[] getPostData(ArrayList<BasicNameValuePair> heards,ArrayList<BasicNameValuePair> getParamList,String filePath)
	{
		ByteArrayOutputStream bos = new ByteArrayOutputStream(1024 * 50);
		try {
			if (!TextUtils.isEmpty(filePath)) 
			{
				writeParamData(bos,getParamList);
				heards.add(new BasicNameValuePair("Content-Type", "multipart/form-data; boundary=" + BOUNDARY));
				writeImageData(bos,filePath);
			}
			else
			{
				heards.add(new BasicNameValuePair("Content-Type", "application/x-www-form-urlencoded"));
                String postParam = HttpParamUtil.encodeParameters(getParamList);
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
        sb.append("Content-Disposition: form-data; name=\"upload\"; filename=\"").append(System.currentTimeMillis()+".jpg").append("\"\r\n");
        sb.append("Content-Type: ").append("image/jpg").append("\r\n\r\n");
        byte[] res = sb.toString().getBytes();
        
        out.write(res);
        out.write(FileUtil.readFile(filePath));
        out.write("\r\n".getBytes());
        out.write(("\r\n" + END_MP_BOUNDARY).getBytes());
    }
    
    
    /**
     * 将Key-value转换成用&号链接的URL查询参数形式。
     * 
     * @param parameters
     * @return
     */
    public static String encodeUrl(Bundle parameters) {
        if (parameters == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (String key : parameters.keySet()) {
            if (first) {
                first = false;
            } else {
                sb.append("&");
            }
            sb.append(key + "=" + URLEncoder.encode(parameters.getString(key)));
            //sb.append(key + "=" + parameters.getString(key));
        }
        return sb.toString();
    }

    /**
     * 将用&号链接的URL参数转换成key-value形式。
     * 
     * @param s
     * @return
     */
    public static Bundle decodeUrl(String s) {
        Bundle params = new Bundle();
        if (s != null) {
            params.putString("url", s);
            String array[] = s.split("&");
            for (String parameter : array) {
                String v[] = parameter.split("=");
                if (v.length > 1) {
                    params.putString(v[0], URLDecoder.decode(v[1]));
                }
            }
        }
        return params;
    }

    /**
     * 解析URL中的查询串转换成key-value
     * 
     * @param url
     * @return
     */
    public static Bundle parseUrl(String url) {
        url = url.replace("rrconnect", "http");
        url = url.replace("#", "?");
        try {
            URL u = new URL(url);
            Bundle b = decodeUrl(u.getQuery());
            b.putAll(decodeUrl(u.getRef()));
            return b;
        } catch (MalformedURLException e) {
            return new Bundle();
        }
    }
}
