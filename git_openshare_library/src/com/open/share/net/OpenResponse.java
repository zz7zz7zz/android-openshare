package com.open.share.net;

public class OpenResponse
{
	public static final int RET_OK=0;//成功
	public static final int RET_FAILED=1;//失败
	public static final int RET_REPEATE=2;//重复
	
	public int ret=RET_FAILED;
	public int errcode;
	public String errmsg;
	public Object backObj;//成功时的回调对象
	
	@Override
	public String toString() {
		return "OpenResponse [ret=" + ret + ", errcode=" + errcode
				+ ", errmsg=" + errmsg + ", backObj=" + backObj + "]";
	}
}