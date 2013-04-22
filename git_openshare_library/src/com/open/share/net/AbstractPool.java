/**
 * 
 */
package com.open.share.net;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author 杨龙辉 2012.05.05
 *
 */
public abstract class AbstractPool <T extends AbstractRunnable>{
	
	private ArrayList<T> queen=new ArrayList<T>();
	
	protected abstract void handleMsg(T msg);
	
	public int push(T msg) 
	{
		synchronized (queen) 
		{
			queen.add(msg);
		}
		handleMsg(msg);//处理任务
		return msg.getmTaskToken();
	}
	
	public void push(ArrayList<T> list)
	{
		synchronized (queen) 
		{
			for(int i=0;i<list.size();i++)
			{
				queen.add(list.get(i));
			}
		}
		for(int i=0;i<list.size();i++)
		{
			handleMsg(list.get(i));
		}
	}
	
	public T getMsgWithToken(int mToken)
	{
		synchronized (queen) 
		{
			for(int i=0;i<queen.size();i++)
			{
				if(queen.get(i).getmTaskToken()==mToken)
				{
					return queen.get(i);
				}
			}
		}
		return null;
	}
	
	public boolean cancelMsgByToken(int mToken) 
	{
		synchronized (queen) 
		{
			Iterator<T> it = queen.iterator();
			while (it.hasNext())
			{
				T msg = it.next();
				if (msg.getmTaskToken() == mToken)
				{
					msg.setCanceled(true);
					it.remove();
					return true;
				}
			}
		}
		return false;
	}
	
	public void cancelAllMsg() 
	{
		synchronized (queen) 
		{
			Iterator<T> it = queen.iterator();
			while (it.hasNext())
			{
				T msg = it.next();
				msg.setCanceled(true);
				it.remove();
			}
		}
	}
}
