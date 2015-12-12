/**
 * 
 */
package com.open.share.qqzone;

/**
 * @author 杨龙辉
 *
 */
public class QQZoneTokenBean {
	
	public String access_token;
	public long expires_in;
	public String openid;
	
	public String name="";
	public String head="";

	@Override
	public String toString() {
		return "access_token=" + access_token+ 
			   "&expires_in=" + expires_in + 
			   "&openid=" + openid+ 
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
			else if(items[i].startsWith("expires_in="))
			{
				expires_in=Long.valueOf(items[i].substring("expires_in=".length()));
			}
			else if(items[i].startsWith("openid="))
			{
				openid=items[i].substring("openid=".length());
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
