package com.open.share.utils;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class IpUtils {
	
	public static String getPsdnIp() 
	{  
	    try {  
	        for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {  
	            NetworkInterface intf = en.nextElement();  
	            for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {  
	                InetAddress inetAddress = enumIpAddr.nextElement();  
	                if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {  
	                //if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet6Address) {   
	                    return inetAddress.getHostAddress().toString();  
	                }  
	            }  
	        }  
	    } catch (Exception e) {  
	    }  
	    return "";  
	}
	
//	public static String intToIp(int i) 
//	{    
//	   
//	   return ((i >> 24 ) & 0xFF ) + "." +    
//	               ((i >> 16 ) & 0xFF) + "." +    
//	               ((i >> 8 ) & 0xFF) + "." +    
//	               ( i & 0xFF) ;    
//	}
//	
//	
//	public static String getIp(Context context)
//	{
//		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);   
//		if(wifiManager.isWifiEnabled())
//		{
//			WifiInfo wifiInfo = wifiManager.getConnectionInfo();    
//			int ipAddress = wifiInfo.getIpAddress(); 
//			return intToIp(ipAddress) ;
//		}
//		else
//		{
//			return getPsdnIp();
//		}
//	}
}
