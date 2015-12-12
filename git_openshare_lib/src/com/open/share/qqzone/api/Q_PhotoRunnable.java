package com.open.share.qqzone.api;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;

import com.open.share.ContextMgr;
import com.open.share.OpenManager;
import com.open.share.interfaces.IOpenResponse;
import com.open.share.net.AbstractRunnable;
import com.open.share.net.NetPool;
import com.open.share.net.OpenResponse;
import com.open.share.qqzone.QQZoneShareImpl;
import com.open.share.qqzone.QQZoneTokenBean;
import com.open.share.utils.DateUtil;
import com.open.share.utils.FileUtil;
import com.open.share.utils.LogUtil;
import com.open.share.utils.SharedPreferenceUtil;
import com.tencent.tauth.Constants;
import com.tencent.tauth.Tencent;

public class Q_PhotoRunnable extends AbstractRunnable {

	private Bundle bundleIn;
	private IOpenResponse listener;
	
	public Q_PhotoRunnable(Bundle bundleIn,final IOpenResponse listener)
	{
		this.bundleIn=bundleIn;
		this.listener=listener;
	}
	
	@Override
	public void run() {
		
		QQZoneTokenBean mQqZoneTokenBean=new QQZoneTokenBean();
		SharedPreferenceUtil.Fetch(ContextMgr.getContext(),  OpenManager.getInstatnce().getSpName(OpenManager.QQZONE), mQqZoneTokenBean);
		
		String time=DateUtil.getStringTime(System.currentTimeMillis(), "yyyy-MM-dd_HH-mm-ss");
		Bundle bundle= new Bundle();
		bundle.putByteArray("picture", FileUtil.readFile(bundleIn.getString(OpenManager.BUNDLE_KEY_IMGPATH)));//必须.上传照片的文件名以及图片的内容（在发送请求时，图片内容以二进制数据流的形式发送，见下面的请求示例），注意照片名称不能超过30个字符。
		bundle.putString("photodesc", bundleIn.getString(OpenManager.BUNDLE_KEY_TEXT));//照片描述，注意照片描述不能超过200个字符。 
		bundle.putString("title", time+ ".jpg");//照片的命名，必须以.jpg, .gif, .png, .jpeg, .bmp此类后缀结尾。
//		bundle.putString("albumid", "");//相册id，不填则传到默认相册
		bundle.putString("mobile", "1");//如果传1，则当albumid为空时，图片会上传到手机相册；
//		bundle.putString("x", bundleIn.getString(OpenManager.BUNDLE_KEY_LONGITUDE));//照片拍摄时的地理位置的经度。请使用原始数据（纯经纬度，0-360）。
//		bundle.putString("y", bundleIn.getString(OpenManager.BUNDLE_KEY_LATITUDE));//照片拍摄时的地理位置的纬度。请使用原始数据（纯经纬度，0-360）。
		bundle.putString("x", "0.0");//照片拍摄时的地理位置的经度。请使用原始数据（纯经纬度，0-360）。
		bundle.putString("y", "0.0");//照片拍摄时的地理位置的纬度。请使用原始数据（纯经纬度，0-360）。
		
		bundle.putString("format", "json");
		bundle.putString("access_token", mQqZoneTokenBean.access_token);
		bundle.putString("oauth_consumer_key", QQZoneShareImpl.APP_ID);
		bundle.putString("openid",  mQqZoneTokenBean.openid);

		final OpenResponse mOpenResponse=new OpenResponse();
		try {
			SharedPreferenceUtil.Fetch(ContextMgr.getContext(), OpenManager.getInstatnce().getSpName(OpenManager.QQZONE), mQqZoneTokenBean);
			Tencent mTencent = Tencent.createInstance(QQZoneShareImpl.APP_ID, ContextMgr.getContext());
			mTencent.setOpenId(mQqZoneTokenBean.openid);
			mTencent.setAccessToken(mQqZoneTokenBean.access_token, ""+(mQqZoneTokenBean.expires_in-System.currentTimeMillis())/1000);
			
			JSONObject obj=mTencent.request(Constants.GRAPH_UPLOAD_PIC, bundle,
                    Constants.HTTP_POST);
			try {
				int ret = 0;
				String msg = "";
				
				try {
					
					ret = obj.getInt("ret");
					msg = obj.getString("msg");
				
				} catch (Exception e) {
				}
				
				if(ret==0)
				{
					mOpenResponse.ret=OpenResponse.RET_OK;
					mOpenResponse.backObj=obj.getString("large_url");
					
	LogUtil.v("Q_PhotoRunnable large_url", ""+mOpenResponse.backObj);
				}
				else
				{
					mOpenResponse.ret=OpenResponse.RET_FAILED;
					mOpenResponse.errmsg=msg;
					
					if(ret==100013||ret==100014||ret==100015||ret==100016)
					{
						SharedPreferenceUtil.clear(ContextMgr.getContext(),OpenManager.getInstatnce().getSpName(OpenManager.QQZONE));
						
						Intent intent = new Intent();
						intent.setAction(OpenManager.AUTH_RESULT_ACTION);
						intent.putExtra(OpenManager.BUNDLE_KEY_OPEN, OpenManager.QQZONE);
						ContextMgr.getContext().sendBroadcast(intent);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(!isCanceled())
			{
				if(null!=listener)
				{
					listener.response(getmTaskToken(), mOpenResponse);
				}
			}
			NetPool.getInstance().cancelMsgByToken(this.getmTaskToken());
		}
	}
	
}
