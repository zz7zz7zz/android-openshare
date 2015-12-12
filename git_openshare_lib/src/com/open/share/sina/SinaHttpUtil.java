/**
 * 
 */
package com.open.share.sina;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.InetAddress;
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
import org.apache.http.HttpVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnRouteParams;
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

import com.open.share.ContextMgr;
import com.open.share.sina.api.SinaNetRunnable;
import com.open.share.utils.HttpParamUtil;
import com.open.share.utils.LogUtil;

/**
 * Http工具类
 * 
 * @author 杨龙辉 2012.04.14
 *
 */
public final class SinaHttpUtil
{
	public static final int CONN_TIMEOUT =20*1000;//连接超时
	public static final int READ_TIMEOUT = 20*1000;//读取超时
	
	private static HttpClient getHttpClient()
	{
		HttpClient client=null;
		 try {
	        	KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
	            trustStore.load(null, null);
	            SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
	            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

	            SchemeRegistry registry = new SchemeRegistry();
	            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
	            registry.register(new Scheme("https", sf, 443));
	            
	            HttpParams params = new BasicHttpParams();
	            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
	            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
	            HttpConnectionParams.setConnectionTimeout(params, CONN_TIMEOUT);
	            HttpConnectionParams.setSoTimeout(params, READ_TIMEOUT);
	            
	            HttpProtocolParams.setUseExpectContinue(params, false);
	            HttpClientParams.setCookiePolicy(params, CookiePolicy.BROWSER_COMPATIBILITY);
	            
	            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);
	            client = new DefaultHttpClient(ccm, params);
	            
	            
	        } catch (Exception e) {
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
                 if (proxyStr != null && proxyStr.trim().length() > 0&&port>0) 
                 {
                     HttpHost proxy = new HttpHost(proxyStr, port);
                     client.getParams().setParameter(ConnRouteParams.DEFAULT_PROXY, proxy);
                 }
                 mCursor.close();
             }
         }
         
         return client;
	}
	
	public static HttpEntity handleGet(SinaNetRunnable msg)
	{
		try {
			
			HttpClient mHttpClient = getHttpClient();
			HttpGet httpGet = new HttpGet(HttpParamUtil.getRequestUrl(msg.reqUrl, msg.reqGetParams)); 
LogUtil.v("handleGet", HttpParamUtil.getRequestUrl(msg.reqUrl, msg.reqGetParams));
			if(msg.reqHeards.size()>0)
			{
				for (BasicNameValuePair param : msg.reqHeards) 
				{
					httpGet.setHeader(param.getName(), param.getValue());
				}
			}
			
	    	HttpResponse response = mHttpClient.execute(httpGet);
	    	StatusLine mStatusLine=response.getStatusLine();
	    	if(null!=mStatusLine)
	    	{
	        	int code =mStatusLine.getStatusCode();
LogUtil.v("SinaHttpUtil handleGet()", "code"+code);
//	        	if(code == HttpStatus.SC_OK)
//	        	{
//	        		return response.getEntity();
//	        	}
	    	}
	    	
	    	return response.getEntity();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	public static HttpEntity handlePost(SinaNetRunnable msg)
	{
		try {
			
			HttpClient mHttpClient = getHttpClient();
			HttpPost mHttpPost = new HttpPost(HttpParamUtil.getRequestUrl(msg.reqUrl, msg.reqGetParams));
LogUtil.v("handlePost", HttpParamUtil.getRequestUrl(msg.reqUrl, msg.reqGetParams));
			
			if(msg.reqHeards.size()>0)
			{
				for (BasicNameValuePair param : msg.reqHeards) 
				{
					mHttpPost.setHeader(param.getName(), param.getValue());
				}
			}
			
			if(null!=msg.reqPostDate)
			{
				HttpEntity httpentity= new ByteArrayEntity(msg.reqPostDate);
				mHttpPost.setEntity(httpentity);
			}
			
			HttpResponse response = mHttpClient.execute(mHttpPost);  
			StatusLine mStatusLine=response.getStatusLine();
			
			if(null!=mStatusLine)
			{
				int code=mStatusLine.getStatusCode();
LogUtil.v("SinaHttpUtil handlePost()", "code"+code);
//				if(code == HttpStatus.SC_OK) 
//				{
//					return response.getEntity();
//				}
			}
			return response.getEntity();
			
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

            sslContext.init(null, new TrustManager[] { tm }, new java.security.SecureRandom());
        }

        @Override
        public Socket createSocket(Socket socket, String host, int port, boolean autoClose)
                throws IOException, UnknownHostException {
        	 injectHostname(socket, host);
            return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
        }

        @Override
        public Socket createSocket() throws IOException {
            return sslContext.getSocketFactory().createSocket();
        }
        
        private void injectHostname(Socket socket, String host) {
            try {
                Field field = InetAddress.class.getDeclaredField("hostName");
                field.setAccessible(true);
                field.set(socket.getInetAddress(), host);
            } catch (Exception ignored) {
            }
        }
    }
}