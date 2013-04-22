/**
 * 
 */
package com.open.share.douban;

/**
 * @author 杨龙辉
 *
 */
public class DoubanTokenBean {
	
	public String access_token;
	public long expires_in;
	public String uid;
	
	//用户名称
	public String name;

	@Override
	public String toString() {
		return  "access_token=" + access_token + 
				"&expires_in="+ expires_in + 
				"&uid=" + uid + 
				"&name=" + name;
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
			else if(items[i].startsWith("expires_in="))
			{
				expires_in=Long.valueOf(items[i].substring("expires_in=".length()));
			}
			else if(items[i].startsWith("uid="))
			{
				uid=items[i].substring("uid=".length());
			}
			else if(items[i].startsWith("name="))
			{
				name=items[i].substring("name=".length());
			}
		}
	}
}
