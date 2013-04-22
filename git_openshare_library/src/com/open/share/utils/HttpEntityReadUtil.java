/**
 * 
 */
package com.open.share.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;

import com.open.share.net.AbstractRunnable;

/**
 * 
 * @author 杨龙辉 2012.05.05
 *
 */
public final class HttpEntityReadUtil {

	public static byte[] getBytes(HttpEntity entity,AbstractRunnable msg)
	{
		if(null==entity)
		{
			return null;
		}
		
		InputStream in=getInputStream(entity);
		if(null!=in)
		{
			ByteArrayOutputStream os=null;
			try
			{
				os = new ByteArrayOutputStream();
				
				int readCount = 0;
				int len = 1024;
				byte[] buffer = new byte[len];
				while ((readCount = in.read(buffer)) != -1&&!msg.isCanceled())
				{
					os.write(buffer, 0, readCount);
				}

				in.close();
				in = null;

				return os.toByteArray();

			} catch (IOException e)
			{
				e.printStackTrace();
			}finally{
				if(null!=os)
				{
					try {
						os.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					os = null;
				}
			}
			return null;
		}
		return null;
	}
	
	public static InputStream getInputStream(HttpEntity entity)
	{
		if(null==entity)
		{
			return null;
		}
		
		try {
			return entity.getContent();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getString(HttpEntity entity)
	{
		if(null==entity)
		{
			return null;
		}
		
		try {
			return EntityUtils.toString(entity, "UTF-8");
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		};
		return null;
	}
}
