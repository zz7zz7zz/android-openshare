package com.open.share.sina.api;

import java.util.ArrayList;

import org.apache.http.message.BasicNameValuePair;

import com.open.share.net.NetRunnable;
import com.open.share.sina.SinaHttpUtil;

public abstract class SinaNetRunnable extends NetRunnable {
	
	public ArrayList<BasicNameValuePair> formMultParamList=new ArrayList<BasicNameValuePair>(0);//④FormMult请求的参数
	
	public void execute()
	{
		while(mRequestCount<maxRequestCount)
		{
			mRequestCount++;
			
			if(reqMethod==Method.GET)
			{
				this.readObj=SinaHttpUtil.handleGet(this);
			}
			else if(reqMethod==Method.POST)
			{
				this.readObj=SinaHttpUtil.handlePost(this);
			}
			
			if(this.readObj!=null)//如果获取了数据则跳出循环
			{
				break;
			}
		}
		formMultParamList.clear();
	}
	
}
