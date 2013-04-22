/**
 * 
 */
package com.open.share.net;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * 网络消息池
 * 
 * @author 杨龙辉
 *
 */
public class NetPool extends AbstractPool<AbstractRunnable>{
	
	private ExecutorService netTaskHandlerExecutorService = Executors.newFixedThreadPool(1);
	private static NetPool instance=null;
	
	@Override
	protected void handleMsg(AbstractRunnable msg) {
		if(null!=msg)
		{
			netTaskHandlerExecutorService.execute(msg);
		}
	}

	public static NetPool getInstance()
	{
	    if (null==instance||(null!=instance&&instance.netTaskHandlerExecutorService.isShutdown())) 
	    {
	        instance = new NetPool();
	    }
	    return instance;
	}
	
	 public static void destory()
	 {
		 if(null!=instance)
		 {
			 instance.cancelAllMsg();
			 if(!instance.netTaskHandlerExecutorService.isShutdown())
			 {
				 instance.netTaskHandlerExecutorService.shutdown();
			 }
		 }
		 instance=null;
	 }
}
