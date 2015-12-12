package com.um.album.wxapi;

import com.open.share.OpenAppConstant;
import com.open.share.R;
import com.tencent.mm.sdk.openapi.BaseReq;
import com.tencent.mm.sdk.openapi.BaseResp;
import com.tencent.mm.sdk.openapi.ConstantsAPI;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler{
	
	public static final int TIMELINE_SUPPORTED_VERSION = 0x21020001;
    private IWXAPI api;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
    	api = WXAPIFactory.createWXAPI(this, OpenAppConstant.WEIXIN_APP_ID, false);
    	api.registerApp(OpenAppConstant.WEIXIN_APP_ID); 
        api.handleIntent(getIntent(), this);
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) 
	{
Log.v("WXEntryActivity", "onReq");
		switch (req.getType()) 
		{
			case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
				break;
			case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
				break;
			default:
				break;
		}
	}

	@Override
	public void onResp(BaseResp resp) 
	{
Log.v("WXEntryActivity", "onResp");
		int result = 0;
		switch (resp.errCode) 
		{
			case BaseResp.ErrCode.ERR_OK:
				result = R.string.weixin_errcode_success;
				break;
			case BaseResp.ErrCode.ERR_USER_CANCEL:
				result = R.string.weixin_errcode_cancel;
				break;
			case BaseResp.ErrCode.ERR_AUTH_DENIED:
				result = R.string.weixin_errcode_deny;
				break;
			default:
				result = R.string.weixin_errcode_unknown;
				break;
		}
		Toast.makeText(this, result, Toast.LENGTH_LONG).show();
		finish();
	}
}