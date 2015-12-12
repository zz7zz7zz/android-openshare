/**
 * 
 */
package com.open.share.interfaces;

/**
 * 网络回调接口
 * @author 杨龙辉 2012.04.07
 *
 */
public interface IOpenResponse {
	
	public static final int DEFAULT_NET_TOKEN=-1;
	
	/**
	 * @param token 消息的唯一标示
	 * @param backObj 回调对象
	 * @return 自己已经处理返回true,没有处理则返回false,如果没有处理,会由消息回调管理器继续处理
	 */
	public boolean response(int token,Object obj);
}
