/**
 * 
 */
package com.open.share.renren;

/**
 * @author 杨龙辉
 *
 */
public class RenrenTokenBean {
	
	public String access_token;
	public String session_key;
	public String session_secret;
	public long session_expires_in;
	public long uid;
	
	//用户名称
	public String name="";
	public String head="";

	@Override
	public String toString() {
		return "access_token=" + access_token+ 
			   "&session_key=" + session_key + 
			   "&session_secret="+ session_secret + 
			   "&session_expires_in=" + session_expires_in+ 
			   "&uid=" + uid + 
			   "&name=" + name+
			   "&head=" + head;
	}
	
	public void parse(String tokenIn)
	{
		String []items=tokenIn.split("&");
		for(int i=0;i<items.length;i++)
		{
			if(items[i].startsWith("access_token="))
			{
				access_token=items[i].substring("access_token=".length());
			}
			else if(items[i].startsWith("session_key="))
			{
				session_key=items[i].substring("session_key=".length());
			}
			else if(items[i].startsWith("session_secret="))
			{
				session_secret=items[i].substring("session_secret=".length());
			}
			else if(items[i].startsWith("session_expires_in="))
			{
				session_expires_in=Long.valueOf(items[i].substring("session_expires_in=".length()));
			}
			else if(items[i].startsWith("uid="))
			{
				uid=Long.valueOf(items[i].substring("uid=".length()));
			}
			else if(items[i].startsWith("name="))
			{
				name=items[i].substring("name=".length());
			}
			else if(items[i].startsWith("head="))
			{
				head=items[i].substring("head=".length());
			}
		}
	}
}
