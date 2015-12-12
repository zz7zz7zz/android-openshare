/**
 * 
 */
package com.open.share.net;

import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.message.BasicNameValuePair;

import com.open.share.interfaces.IOpenResponse;
import com.open.share.utils.HttpUtil;
import com.open.share.utils.NetUtil;

/**
 *网络消息基类
 * 
 * @author 杨龙辉 2012.04.14
 */
public abstract class NetRunnable extends AbstractRunnable {
	
	public enum Method
	{
		GET,
		POST,
	}
	
	public static final int maxRequestCount=2;//最多的请求次数
	
	public Method reqMethod=Method.GET;//①HTTP请求类型,默认为请求
	public String reqUrl;//②请求的Uri
	public ArrayList<BasicNameValuePair> reqHeards=new ArrayList<BasicNameValuePair>(0);//③设置请求头
	public ArrayList<BasicNameValuePair> reqGetParams=new ArrayList<BasicNameValuePair>(0);//④get请求的参数
	public byte []reqPostDate=null;//④post请求的参数
	
	public HttpEntity readObj;//⑤读取到的对象
	public Object resObj = null;//⑥回调对象
	public IOpenResponse listener=null;//⑦回调的监听器
	
	public int mRequestCount=0;//⑧当前请求次数
	
	public void init()
	{
		NetUtil.init();
		
		reqMethod=Method.GET;
		reqUrl="";
		reqHeards.clear();
		reqGetParams.clear();
		reqPostDate=null;
		
		readObj=null;
		resObj=null;
	}
	
	/**
	 * 在这个方法里需要设置如下数据:</br></br>
	 * 
	 * ①请求类型 reqMethod,为Method.Get或者Method.Post</br></br>
	 * ②请求的 reqUrl </br></br>
	 * ③设置请求头 reqHeards ，有则设,没有则不设置   </br></br> 
	 * ④get请求的参数 reqGetParams/post请求的数据 reqPostDate,有则设,没有则不设置   
	 */
	public abstract void loadData();
	
	public void execute()
	{
		while(mRequestCount<maxRequestCount)
		{
			mRequestCount++;
			
			if(reqMethod==Method.GET)
			{
				this.readObj=HttpUtil.handleGet(this);
			}
			else if(reqMethod==Method.POST)
			{
				this.readObj=HttpUtil.handlePost(this);
			}
			
			if(this.readObj!=null)//如果获取了数据则跳出循环
			{
				break;
			}
		}
	
	}
	
	public abstract void handleData();
	
	public void callback()
	{
		if(null!=listener)
		{
			boolean isTaskHandled=false;
			if(null==resObj)
			{
				OpenResponse obj=new OpenResponse();
				obj.ret=OpenResponse.RET_FAILED;
				resObj=obj;
			}
			
			isTaskHandled=listener.response(this.getmTaskToken(),resObj);
			if(!isTaskHandled)
			{
				//TODO 消息管理器自己进行额外的处理,比如网络超时弹出提示框等
			}
		}
		
		NetPool.getInstance().cancelMsgByToken(this.getmTaskToken());
	};
	
	
	@Override
	public void run() 
	{
		/*
		 * 原始这五步骤
		init();
		loadData();
		execute();
		handleData();
		callback();
		 */
		
		try {
			
			if(NetUtil.isNetAvailable())
			{
				if(!isCanceled())
				{
					init();
				}
				
				if(!isCanceled())
				{
					loadData();
				}
				
				if(!isCanceled())
				{
					execute();
				}
				
				if(!isCanceled())
				{
					handleData();
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally
		{
			try {
				if(!isCanceled())
				{
					callback();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
}
