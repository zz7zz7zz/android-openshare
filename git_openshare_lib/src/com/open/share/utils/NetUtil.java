/**
 * 
 */
package com.open.share.utils;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import com.open.share.ContextMgr;

/**
 * 与网络相关的类,用于网络的检测
 * 
 * @author 杨龙辉 2012.04.07
 * 
 */
public final class NetUtil
{

	private static ConnectivityManager mConnectivityManager = (ConnectivityManager) ContextMgr.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
	private static TelephonyManager mTelephonyManager = (TelephonyManager) ContextMgr.getContext().getSystemService(Context.TELEPHONY_SERVICE);

	public static final int Unknown_Net=-1;//未知网络
	public static final int _WIFI = 1;
	public static final int _CMNET = 2;
	public static final int _CMWAP = 3;
	public static final int _CTWAP = 4;
	public static final int _UNIWAP = 5;
	public static final int _3GWAP = 6;
	public static int currentNet = _WIFI;//网络类型
	
	public static final int Unknown_Operator=-10;//未知运营商
	public static final int ChinaMobile = 10;
	public static final int ChinaUnicom = 11;
	public static final int ChinaTelecom = 12;
	public static int currentSimOperator = ChinaMobile;//网络运营商: 11为移动,12为联通,13为电信,-10未知
	
	public static final int Unknow_NetType=-21;
	public static final int GSM = 21;// 2G网络
	public static final int THIRD_GENERATION = 22;//3G 网络
	public static final int currentNetType = GSM;//网络 2G还是3G,还是未知

	public static boolean isProxy = false;//是否使用了代理
	
	public static final String HTTP_SCHEMA = "http://";
	public static final String CMWAP_PROXY_HOST = "10.0.0.172:80";// 移动的代理
	public static final String UNIWAP_PROXY_HOST = "10.0.0.172:80";// 联通的代理
	public static final String CTWAP_PROXY_HOST = "10.0.0.200:80";// 电信的代理

	/**
	 * 确定网络类型,是否使用了代理
	 */
	public static void init()
	{
		if (isWIFI())
		{
			currentNet = _WIFI;
			isProxy = false;
		} else if (isCMWap())
		{
			currentNet = _CMWAP;
			isProxy = true;
		} else if (isCTWAP())
		{
			currentNet = _CTWAP;
			isProxy = true;
		} else if (isUNIWAP())
		{
			currentNet = _UNIWAP;
			isProxy = true;
		}
		else if (is3GWAP())
		{
			currentNet = _3GWAP;
			isProxy = true;
		}
		else
		{
			currentNet = _CMNET;
			isProxy = false;
		}
	}

	/**
	 * 检查网络是否可用
	 * 
	 * @return true表示可用，false 表示不可用
	 */
	public static boolean isNetAvailable()
	{
		if (null != mConnectivityManager)
		{
			NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
			if (null != mNetworkInfo)
			{
				return mNetworkInfo.isConnected();
			}
		}
		return false;
	}

	/**
	 * 判断网络是否为Wifi
	 * 
	 * @return 是wifi返回true,否则返回false
	 */
	public static boolean isWIFI()
	{
		if (null != mConnectivityManager)
		{
			NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
			if (null != mNetworkInfo)
			{
				return (mNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI);
			}
		}
		return false;
	}

	/**
	 * 判断网络是否为CMWap
	 * 
	 * @return 是CMWap返回true,否则返回false
	 */
	public static boolean isCMWap()
	{
		if (null != mConnectivityManager)
		{
			NetworkInfo info = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if (info != null)
			{
				String netInfo = info.getExtraInfo();
				if (StringUtil.isNonEmpty(netInfo))
				{
					int type = getOperatorByNetworkInfo();
					if (type == ChinaMobile)
					{
						return (netInfo.indexOf("cmwap") != -1);
					}
				}
			}
		}
		return false;
	}

	/**
	 * 判断网络是否为UNIWAP
	 * 
	 * @return 是UNIWAP返回true,否则返回false
	 */
	public static boolean isUNIWAP()
	{
		if (null != mConnectivityManager)
		{
			NetworkInfo info = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if (info != null)
			{
				String netInfo = info.getExtraInfo();
				if (StringUtil.isNonEmpty(netInfo))
				{
					int type = getOperatorByNetworkInfo();
					if (type == ChinaUnicom)
					{
						return (netInfo.indexOf("uniwap") != -1);
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * 判断网络是否为3GWAP
	 * 
	 * @return 是UNIWAP返回true,否则返回false
	 */
	public static boolean is3GWAP()
	{
		if (null != mConnectivityManager)
		{
			NetworkInfo info = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if (info != null)
			{
				String netInfo = info.getExtraInfo();
				if (StringUtil.isNonEmpty(netInfo))
				{
					int type = getOperatorByNetworkInfo();
					if (type == ChinaUnicom)
					{
						return (netInfo.indexOf("3gwap") != -1);
					}
				}
			}
		}
		return false;
	}

	/**
	 * 判断网络是否为CTWAP
	 * 
	 * @return 是CTWAP返回true,否则返回false
	 */
	public static boolean isCTWAP()
	{
		if (null != mConnectivityManager)
		{
			NetworkInfo info = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if (info != null)
			{
				String netInfo = info.getExtraInfo();
				if (StringUtil.isNonEmpty(netInfo))
				{
					int type = getOperatorByNetworkInfo();
					if (type == ChinaTelecom)
					{
						return (netInfo.indexOf("ctwap") != -1);
					}
				}
			}
		}
		return false;
	}

	/**
	 * 返回运营商
	 * @return 网络运营商: 11为移动,12为联通,13为电信
	 */
	public static int getOperator()
	{
		int type = getOperatorByMccMnc();
		
		if (type == Unknown_Operator)
		{
			type=getOperatorByNetworkInfo();
		}
		
		if (type == Unknown_Operator)
		{
			type=getOperatorByNetworkType();
		}
		
		return type;
	}
	/**
	 * 通过MNC判断
	 * <p>
	 * 获取SIM卡的IMSI码SIM卡唯一标识：IMSI 国际移动用户识别码（IMSI：International Mobile Subscriber
	 * Identification Number）是区别移动用户的标志， 储存在SIM卡中，可用于区别移动用户的有效信息。
	 * <p>
	 * IMSI由MCC、MNC、MSIN组成，其中MCC为移动国家号码，由3位数字组成，唯一地识别移动客户所属的国家，我国为460；MNC为网络id，
	 * 由2位数字组成，
	 * 用于识别移动客户所归属的移动网络，中国移动为00，中国联通为01,中国电信为03；MSIN为移动客户识别码，采用等长11位数字构成。
	 * 因为移动网络编号46000下的IMSI已经用完，所以虚拟了一个46002编号，134/159号段使用了此编号
	 * <p>
	 * 唯一地识别国内GSM移动通信网中移动客户。所以要区分是移动还是联通，只需取得SIM卡中的MNC字段即可
	 * 
	 * @return 返回运营商的类型,是移动，联通，还是电信
	 */
	private static int getOperatorByMccMnc()
	{
		if (null != mTelephonyManager)
		{
			String simOperator = mTelephonyManager.getSimOperator();
			if (StringUtil.isNonEmpty(simOperator))
			{
				if (simOperator.equals("46000") || simOperator.equals("46002"))
				{
					return ChinaMobile;
				} else if (simOperator.equals("46001"))
				{
					return ChinaUnicom;
				} else if (simOperator.equals("46003"))
				{
					return ChinaTelecom;
				}
			}

			String imsi = mTelephonyManager.getSubscriberId();
			if (StringUtil.isNonEmpty(imsi))
			{
				if (imsi.startsWith("46000") || imsi.startsWith("46002"))
				{
					return ChinaMobile;
				} else if (imsi.startsWith("46001"))
				{
					return ChinaUnicom;
				} else if (imsi.startsWith("46003"))
				{
					return ChinaTelecom;
				}
			}
		}
		return Unknown_Operator;
	}

	private static int getOperatorByNetworkInfo()
	{
		if (null != mConnectivityManager)
		{
			NetworkInfo info = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if (null != info)
			{
				String extraInfo = info.getExtraInfo();
				if (StringUtil.isNonEmpty(extraInfo))
				{
					if (extraInfo.indexOf("cmnet") != -1 || extraInfo.indexOf("cmwap") != -1)
					{
						return ChinaMobile;
					}
					else if (extraInfo.indexOf("3gnet") != -1 || extraInfo.indexOf("uninet") != -1 || extraInfo.indexOf("uniwap") != -1)
					{
						return ChinaUnicom;
					} 
					else if (extraInfo.indexOf("ctnet") != -1 || extraInfo.indexOf("#777") != -1 || extraInfo.indexOf("ctwap") != -1)// #777、ctnet
					{
						return ChinaTelecom;
					}
				}

				String subtypeName = info.getSubtypeName();
				if (StringUtil.isNonEmpty(subtypeName))
				{
					if (subtypeName.indexOf("EDGE") != -1)
					{
//						return ChinaMobile;
					} 
					else if (subtypeName.indexOf("UMTS") != -1 || subtypeName.indexOf("HSDPA") != -1)
					{
						return ChinaUnicom;
					} 
					else if (subtypeName.indexOf("CDMA") != -1)
					{
						return ChinaTelecom;
					}
				}
			}
		}
		return Unknown_Operator;
	}

	private static int getOperatorByNetworkType()
	{
		if (null != mTelephonyManager)
		{
			int type = mTelephonyManager.getNetworkType();
			int ret=Unknown_Operator;
			
			//在中国，联通的3G为UMTS或HSDPA，移动和联通的2G为GPRS或EGDE，电信的2G为CDMA，电信的3G为EVDO
			
			switch (type)
			{
				case TelephonyManager.NETWORK_TYPE_UMTS:
				case TelephonyManager.NETWORK_TYPE_HSDPA:
					ret = ChinaUnicom;// 类型为UMTS定义为wcdma的USIM卡
					break;
				
				case TelephonyManager.NETWORK_TYPE_GPRS:
				case TelephonyManager.NETWORK_TYPE_EDGE:
//					ret = ChinaMobile;
					break;
				
				case TelephonyManager.NETWORK_TYPE_CDMA:
					ret = ChinaTelecom;
					break;
			}
			
			return ret;
		}
		
		return Unknown_Operator;
	}

	
	/**
	 * 获取Mcc,Mnc号
	 * @return
	 */
	public static int[] getMccMnc()
	{
		int []MccMnc=new int[2];
		
		MccMnc[0]=460;
		MccMnc[0]=0;
		
		if (null != mTelephonyManager)
		{
			String simOperator = mTelephonyManager.getSimOperator();
			if (StringUtil.isNonEmpty(simOperator))
			{
				if(simOperator.length()>3)
				{
					MccMnc[0] = Integer.valueOf(simOperator.substring(0,3));
				}
				
				if(simOperator.length()>=5)
				{
					MccMnc[1] = Integer.valueOf(simOperator.substring(3,5));
				}
			}
		}
		return MccMnc;
	}
	
	
	/**
	 * 获取最终的请求Url
	 * @param originalRequrl
	 * @return
	 */
	public static String getFinalReqUrl(String originalRequrl)
	{
		if (isProxy)
		{
			String[] retUrls = getUrlSegments(originalRequrl);
			if (currentNet == _CMWAP)// 中国移动的代理
			{
				return (HTTP_SCHEMA + CMWAP_PROXY_HOST + retUrls[1]).replace(" ", "%20");
			} 
			else if (currentNet == _UNIWAP||currentNet==_3GWAP)// 中国联通的代理
			{
				return (HTTP_SCHEMA + UNIWAP_PROXY_HOST + retUrls[1]).replace(" ", "%20");
			}
			else if (currentNet == _CTWAP)// 中国电信的代理
			{
				return (HTTP_SCHEMA + CTWAP_PROXY_HOST + retUrls[1]).replace(" ", "%20");
			}
			
			return null;
		} 
		else
		{
			return originalRequrl.replace(" ", "%20");
		}
	}

	/**
	 * 根据请求的Url，获取请求的前缀与后缀
	 * @param originalRequrl
	 * @return
	 */
	public static String[] getUrlSegments(String originalRequrl)
	{
		/**
		 * 从http://www.baidu.com/img/baidu_logo.gif 变成 http://10.0.0.172/img/baidu_logo.gif
		 * URL url = new URL("http://10.0.0.172/img/baidu_logo.gif"); 
		 * HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		 * conn.setRequestProperty("X-Online-Host", "www.baidu.com");
		 */
		String[] retUrls = new String[2];

		int start = originalRequrl.toLowerCase().indexOf(HTTP_SCHEMA);
		if (start == -1)
		{
			return null;
		}

		int shemaLength = HTTP_SCHEMA.length();
		int end = originalRequrl.indexOf("/", start + shemaLength);

		if (end == -1)
		{
			retUrls[0] = originalRequrl.substring(shemaLength);
			retUrls[1] = "/";
		} 
		else
		{
			retUrls[0] = originalRequrl.substring(shemaLength, end);
			retUrls[1] = originalRequrl.substring(end);
		}
		return retUrls;
	}

//	 public static boolean isNetAvailable()
//	 {
//		 if (null!=mConnectivityManager)
//		 {
//			 NetworkInfo[] info = mConnectivityManager.getAllNetworkInfo();
//			 if (info != null)
//			 {
//				 for (int i = 0; i < info.length; i++)
//				 {
//					 if (info[i].getState() == NetworkInfo.State.CONNECTED)
//					 {
//						 return true;
//					 }
//				 }
//			 }
//		 }
//		 return false;
//	 }

//	 public static boolean isWIFI()
//	 {
//		 if(null!=mConnectivityManager)
//		 {
//			 NetworkInfo[] info = mConnectivityManager.getAllNetworkInfo();
//			 if (info != null)
//			 {
//				 for(int i=0;i<info.length;i++)
//				 {
//					 int nNetType = info[i].getType();
//					 if (nNetType == ConnectivityManager.TYPE_WIFI)
//					 {
//					 return info[i].isConnected();
//					 }
//				 }
//			 }
//		 }
//		 return false;
//	 }

}
