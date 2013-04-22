package com.open.share.renren.api;

import java.util.ArrayList;

import org.apache.http.message.BasicNameValuePair;

import com.open.share.net.NetRunnable;
import com.open.share.renren.RenHttpUtil;

public abstract class RenNetRunnable extends NetRunnable {
	
	public ArrayList<BasicNameValuePair> postParamList=new ArrayList<BasicNameValuePair>(0);//④FormMult请求的参数

	public void execute()
	{
		while(mRequestCount<maxRequestCount)
		{
			mRequestCount++;
			
			if(reqMethod==Method.GET)
			{
				this.readObj=RenHttpUtil.handleGet(this);
			}
			else if(reqMethod==Method.POST)
			{
				this.readObj=RenHttpUtil.handlePost(this);
			}
			
			if(this.readObj!=null)//如果获取了数据则跳出循环
			{
				break;
			}
		}
		
		postParamList.clear();
	}
	
}
