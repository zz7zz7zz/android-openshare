/**
 * 
 */
package com.open.share.net;

import com.open.share.utils.AtomicIntegerUtil;


/**
 * @author 杨龙辉
 *
 */
public abstract class AbstractRunnable implements Runnable{
	
	private final int mTaskToken=AtomicIntegerUtil.getIncrementID();//一个消息的唯一标识
	private boolean isCanceled=false;//是否取消了消息的执行
	
	public boolean isCanceled() {
		return isCanceled;
	}
	public void setCanceled(boolean isCanceled) {
		this.isCanceled = isCanceled;
	}
	
	public int getmTaskToken() {
		return mTaskToken;
	}
	
}
