package com.open.share.tencent.api;

import java.util.ArrayList;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.message.BasicNameValuePair;

import com.open.share.net.NetRunnable;
import com.open.share.tencent.TenHttpUtil;

public abstract class TenNetRunnable extends NetRunnable {
	
	public ArrayList<BasicNameValuePair> formMultParamList=new ArrayList<BasicNameValuePair>(0);//④FormMult请求的参数
	
	public void execute()
	{
		while(mRequestCount<maxRequestCount)
		{
			mRequestCount++;
			
			if(reqMethod==Method.GET)
			{
				this.readObj=TenHttpUtil.handleGet(this);
			}
			else if(reqMethod==Method.POST)
			{
				this.readObj=TenHttpUtil.handlePost(this);
			}
			
			if(this.readObj!=null)//如果获取了数据则跳出循环
			{
				break;
			}
		}
		formMultParamList.clear();
	}
	
	public MultipartEntity getUploadEntity()
	{
		return null;
	}
}
