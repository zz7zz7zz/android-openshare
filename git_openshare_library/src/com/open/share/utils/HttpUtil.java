/**
 * 
 */
package com.open.share.utils;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.text.TextUtils;

import com.open.share.ContextMgr;
import com.open.share.net.NetRunnable;

/**
 * Http工具类
 * 
 * @author 杨龙辉 2012.04.14
 *
 */
public final class HttpUtil
{
	public static final int CONN_TIMEOUT =15*1000;//连接超时
	public static final int READ_TIMEOUT = 15*1000;//读取超时
	
	/**
	 * 微博
	 * @return
	 */
	private static HttpClient getHttpClient()
	{
		HttpClient client=null;
		try {
		       KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
		       trustStore.load(null, null);

		       SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
		       sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

		       HttpParams params = new BasicHttpParams();
		       HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		       HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
	           HttpConnectionParams.setConnectionTimeout(params, CONN_TIMEOUT);
	           HttpConnectionParams.setSoTimeout(params, READ_TIMEOUT);

		       SchemeRegistry registry = new SchemeRegistry();
		       registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		       registry.register(new Scheme("https", sf, 443));

		       ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

		       client= new DefaultHttpClient(ccm, params);
		   } catch (Exception e) {
LogUtil.v("HttpUtil getHttpClient()", "------");   
			   client=new DefaultHttpClient();
		   } 
		
	       WifiManager wifiManager = (WifiManager) ContextMgr.getContext().getSystemService(Context.WIFI_SERVICE);
           if (!wifiManager.isWifiEnabled()) 
           {
                Uri uri = Uri.parse("content://telephony/carriers/preferapn");
                Cursor mCursor = ContextMgr.getContext().getContentResolver().query(uri, null, null, null, null);
                if (mCursor != null && mCursor.moveToFirst())
                {
                    String proxyStr = mCursor.getString(mCursor.getColumnIndex("proxy"));
                    int port = mCursor.getInt(mCursor.getColumnIndex("port"));
                    if (!TextUtils.isEmpty(proxyStr)&&port>0) 
                    {
                        HttpHost proxy = new HttpHost(proxyStr, port);
                        client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
                    }
                    mCursor.close();
                }
            }
           
		   return client;
	}
	
	public static HttpEntity handleGet(NetRunnable msg)
	{
		try {
			
			HttpClient mHttpClient = getHttpClient();
			String requestUrl=HttpParamUtil.getRequestUrl(msg.reqUrl, msg.reqGetParams);
			HttpGet httpGet = new HttpGet(NetUtil.getFinalReqUrl(requestUrl)); 
			
//			httpGet.setHeader("accept-encoding", "gzip");
			if(msg.reqHeards.size()>0)
			{
				for (BasicNameValuePair param : msg.reqHeards) 
				{
					httpGet.setHeader(param.getName(), param.getValue());
				}
			}
			
			if(NetUtil.isProxy)
			{
				String []url=NetUtil.getUrlSegments(msg.reqUrl);
				if(null!=url)
				{
					httpGet.setHeader("X-Online-Host",url[0]);
				}
			}
			
	    	HttpResponse response = mHttpClient.execute(httpGet);
	    	StatusLine mStatusLine=response.getStatusLine();
	    	if(null!=mStatusLine)
	    	{
	        	int code =mStatusLine.getStatusCode();
	        	if(code == HttpStatus.SC_OK)
	        	{
	        		return response.getEntity();
	        	}
	    	}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	public static HttpEntity handlePost(NetRunnable msg)
	{
		try {
			
			HttpClient mHttpClient = getHttpClient();
			HttpPost mHttpPost = new HttpPost(NetUtil.getFinalReqUrl(msg.reqUrl));
			
//			mHttpPost.setHeader("accept-encoding", "gzip");
			if(msg.reqHeards.size()>0)
			{
				for (BasicNameValuePair param : msg.reqHeards) 
				{
					mHttpPost.setHeader(param.getName(), param.getValue());
				}
			}
			
			if(NetUtil.isProxy)
			{
				String []url=NetUtil.getUrlSegments(msg.reqUrl);
				if(null!=url)
				{
					mHttpPost.setHeader("X-Online-Host",url[0]);
				}
			}
			
			HttpEntity httpentity= new ByteArrayEntity(msg.reqPostDate);
			mHttpPost.setEntity(httpentity);
			
			HttpResponse response = mHttpClient.execute(mHttpPost);  
			StatusLine mStatusLine=response.getStatusLine();
			
			if(null!=mStatusLine)
			{
				if(mStatusLine.getStatusCode() == HttpStatus.SC_OK) 
				{
					return response.getEntity();
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
    public static class MySSLSocketFactory extends SSLSocketFactory {
        SSLContext sslContext = SSLContext.getInstance("TLS");

        public MySSLSocketFactory(KeyStore truststore) throws NoSuchAlgorithmException,
                KeyManagementException, KeyStoreException, UnrecoverableKeyException {
            super(truststore);

            TrustManager tm = new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain, String authType)
                        throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] chain, String authType)
                        throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };

            sslContext.init(null, new TrustManager[] { tm }, null);
        }

        @Override
        public Socket createSocket(Socket socket, String host, int port, boolean autoClose)
                throws IOException, UnknownHostException {
            return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
        }

        @Override
        public Socket createSocket() throws IOException {
            return sslContext.getSocketFactory().createSocket();
        }
    }
}