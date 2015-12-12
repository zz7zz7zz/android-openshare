package com.open.share.sina;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;

import com.open.share.AuthorizeActivity;
import com.open.share.OpenAppConstant;
import com.open.share.OpenManager;
import com.open.share.interfaces.IOpenResponse;
import com.open.share.interfaces.IShare;
import com.open.share.net.AbstractRunnable;
import com.open.share.sina.api.S_FriendShipCreate;
import com.open.share.sina.api.S_Friends;
import com.open.share.sina.api.S_StatusUpdate;
import com.open.share.sina.api.S_StatusUpload;
import com.open.share.sina.api.S_StatusUploadUrl;
import com.open.share.sina.api.S_UserInfo;
import com.open.share.utils.SharedPreferenceUtil;
import com.sina.sso.RemoteSSO;


public class SinaShareImpl implements IShare
{
	public static final String CLIENT_ID=OpenAppConstant.SINA_App_Key;
	public static final String CLIENT_SECRET=OpenAppConstant.SINA_App_Secret;
	
	public static final String URL_CALLBACK_CONFRIMPAGE=OpenAppConstant.SINA_URL_CALLBACK_CONFRIMPAGE;//确定授权
	public static final String URL_CALLBACK_CANCELPAGE=OpenAppConstant.SINA_URL_CALLBACK_CANCELPAGE;//取消授权
	
	public static final String URL_OAUTH2_AUTHORIZE = "https://api.weibo.com/oauth2/authorize?client_id=%s&response_type=token&redirect_uri=%s&display=mobile";//oauth2.0 第一步
	public static final String URL_OAUTH2_ACCESS_TOKEN = "https://api.weibo.com/oauth2/access_token";//oauth2.0 第二步
	
	public static final String URL_USERS_SHOW="https://api.weibo.com/2/users/show.json";//用户信息
	public static final String URL_STATUSES="https://api.weibo.com/2/statuses/update.json";//发表文字微博
	public static final String URL_STATUSES_UPLOAD="https://api.weibo.com/2/statuses/upload.json";//上传图片微博
	public static final String URL_STATUSES_UPLOAD_URL="https://api.weibo.com/2/statuses/upload_url_text.json";//上传图片微博,带图片地址
	public static final String URL_FRIENDSHIPS_CREATE="https://api.weibo.com/2/friendships/create.json";//关注其他用户
	
	public static final String URL_FRIENDSHIPS_FRIENDS="https://api.weibo.com/2/friendships/friends.json";//关注其他用户
	
	@Override
	public boolean isVaild(Context context) 
	{
		SinaTokenBean mSinaTokenStore=new SinaTokenBean();
		SharedPreferenceUtil.Fetch(context, OpenManager.getInstatnce().getSpName(OpenManager.SINA_WEIBO), mSinaTokenStore);
		
		boolean isVaild=mSinaTokenStore.expires_in>System.currentTimeMillis();
		if(!isVaild)
		{
			SharedPreferenceUtil.clear(context, OpenManager.getInstatnce().getSpName(OpenManager.SINA_WEIBO));
		}
		return isVaild;
	}

	@Override
	public void authorize(int requestCode, Activity activity) 
	{
		mAuthActivity = activity;
		mAuthPermissions = new String[] {};
		mAuthActivityCode = requestCode;
		
		boolean bindSucced = bindRemoteSSOService(activity);//先用SSO授权方式
		if(!bindSucced)
		{
			originalAuthorize(mAuthActivityCode,mAuthActivity);
		}
	}
	
	public static void originalAuthorize(int requestCode, Activity activity)
	{
		 String authorizeUrl=String.format(URL_OAUTH2_AUTHORIZE, CLIENT_ID,URL_CALLBACK_CONFRIMPAGE);
		 
		 Intent mIntent=new Intent(activity, AuthorizeActivity.class);
		 mIntent.putExtra(OpenManager.EXTRA_AUTHORIZE_URL, authorizeUrl);
		 mIntent.putExtra(OpenManager.EXTRA_OPEN_DEST, OpenManager.SINA_WEIBO);
		 activity.startActivity(mIntent);
	}

	@Override
	public AbstractRunnable getUserInfo(final Bundle bundle, IOpenResponse listener) {
		return new S_UserInfo(bundle,listener);
	}

	@Override
	public AbstractRunnable shareStatus(Bundle bundle,IOpenResponse listener) {
		return new S_StatusUpdate(bundle, listener);
	}

	@Override
	public AbstractRunnable sharePhoto(Bundle bundle,IOpenResponse listener) {
		return new S_StatusUpload(bundle, listener);
	}
	
	public AbstractRunnable sharePhotoPicUrl(Bundle bundle,IOpenResponse listener) {
		return new S_StatusUploadUrl(bundle, listener);
	}

	@Override
	public AbstractRunnable friendShipsCreate(Bundle bundle,IOpenResponse listener) {
		return new S_FriendShipCreate(bundle, listener);
	}
	
	public AbstractRunnable getFriends(Bundle bundle,IOpenResponse listener) {
		return new S_Friends(bundle, listener);
	}

	//-------------------添加SSO授权相关代码-----------------------
	private String ssoPackageName = "";// "com.sina.weibo";
	private String ssoActivityName = "";// "com.sina.weibo.MainTabActivity";
	private ServiceConnection conn = null;
	
	public final String WEIBO_SIGNATURE = "30820295308201fea00302010202044b4ef1bf300d"
			+ "06092a864886f70d010105050030818d310b300906035504061302434e3110300e0603550408130"
			+ "74265694a696e673110300e060355040713074265694a696e67312c302a060355040a132353696e"
			+ "612e436f6d20546563686e6f6c6f677920284368696e612920436f2e204c7464312c302a0603550"
			+ "40b132353696e612e436f6d20546563686e6f6c6f677920284368696e612920436f2e204c746430"
			+ "20170d3130303131343130323831355a180f32303630303130323130323831355a30818d310b300"
			+ "906035504061302434e3110300e060355040813074265694a696e673110300e0603550407130742"
			+ "65694a696e67312c302a060355040a132353696e612e436f6d20546563686e6f6c6f67792028436"
			+ "8696e612920436f2e204c7464312c302a060355040b132353696e612e436f6d20546563686e6f6c"
			+ "6f677920284368696e612920436f2e204c746430819f300d06092a864886f70d010101050003818"
			+ "d00308189028181009d367115bc206c86c237bb56c8e9033111889b5691f051b28d1aa8e42b66b7"
			+ "413657635b44786ea7e85d451a12a82a331fced99c48717922170b7fc9bc1040753c0d38b4cf2b2"
			+ "2094b1df7c55705b0989441e75913a1a8bd2bc591aa729a1013c277c01c98cbec7da5ad7778b2fa"
			+ "d62b85ac29ca28ced588638c98d6b7df5a130203010001300d06092a864886f70d0101050500038"
			+ "181000ad4b4c4dec800bd8fd2991adfd70676fce8ba9692ae50475f60ec468d1b758a665e961a3a"
			+ "edbece9fd4d7ce9295cd83f5f19dc441a065689d9820faedbb7c4a4c4635f5ba1293f6da4b72ed3"
			+ "2fb8795f736a20c95cda776402099054fccefb4a1a558664ab8d637288feceba9508aa907fc1fe2"
			+ "b1ae5a0dec954ed831c0bea4";
	
	public static final String TOKEN = "access_token";
	public static final String EXPIRES = "expires_in";
	public static final String UID = "uid";
	public final String REFRESHTOKEN = "refresh_token";
	
	private Activity mAuthActivity;
	private String[] mAuthPermissions;
	private int mAuthActivityCode;
	
	{
		conn = new ServiceConnection() {
			@Override
			public void onServiceDisconnected(ComponentName name) {
			}

			@Override
			public void onServiceConnected(ComponentName name, IBinder service) {
				RemoteSSO remoteSSOservice = RemoteSSO.Stub.asInterface(service);
				try {
					ssoPackageName = remoteSSOservice.getPackageName();
					ssoActivityName = remoteSSOservice.getActivityName();
					boolean singleSignOnStarted = startSingleSignOn(mAuthActivity, CLIENT_ID, mAuthPermissions,mAuthActivityCode);
					if (!singleSignOnStarted) 
					{
//						startDialogAuth(mAuthActivity, mAuthPermissions);
						originalAuthorize(mAuthActivityCode,mAuthActivity);
					}
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		};
	}
	
	private boolean bindRemoteSSOService(Activity activity) {
		Context context = activity.getApplicationContext();
		Intent intent = new Intent("com.sina.weibo.remotessoservice");
		return context.bindService(intent, conn, Context.BIND_AUTO_CREATE);
	}
	
	private boolean startSingleSignOn(Activity activity, String applicationId,
			String[] permissions, int activityCode) {
		boolean didSucceed = true;
		Intent intent = new Intent();
		intent.setClassName(ssoPackageName, ssoActivityName);
		intent.putExtra("appKey", applicationId);// applicationId //"2745207810"
		intent.putExtra("redirectUri", URL_CALLBACK_CONFRIMPAGE);

		if (permissions.length > 0) {
			intent.putExtra("scope", TextUtils.join(",", permissions));
		}

		// validate Signature
		if (!validateAppSignatureForIntent(activity, intent)) {
			return false;
		}

		try {
			activity.startActivityForResult(intent, activityCode);
		} catch (ActivityNotFoundException e) {
			didSucceed = false;
		}

		activity.getApplication().unbindService(conn);
		return didSucceed;
	}
	
	
	private boolean validateAppSignatureForIntent(Activity activity,
			Intent intent) {
		ResolveInfo resolveInfo = activity.getPackageManager().resolveActivity(
				intent, 0);
		if (resolveInfo == null) {
			return false;
		}

		String packageName = resolveInfo.activityInfo.packageName;
		try {
			PackageInfo packageInfo = activity.getPackageManager()
					.getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
			for (Signature signature : packageInfo.signatures) {
				if (WEIBO_SIGNATURE.equals(signature.toCharsString())) {
					return true;
				}
			}
		} catch (NameNotFoundException e) {
			return false;
		}

		return false;
	}
	
	public boolean authorizeCallBack(int requestCode, int resultCode, Intent data) {
		if (requestCode == mAuthActivityCode) {

			// Successfully redirected.
			if (resultCode == Activity.RESULT_OK) {

				// Check OAuth 2.0/2.10 error code.
				String error = data.getStringExtra("error");
				if (error == null) {
					error = data.getStringExtra("error_type");
				}

				// error occurred.
				if (error != null) {
//					if (error.equals("access_denied")
//							|| error.equals("OAuthAccessDeniedException")) {
//						Log.d("Weibo-authorize", "Login canceled by user.");
//						mAuthDialogListener.onCancel();
//					} else {
//						String description = data
//								.getStringExtra("error_description");
//						if (description != null) {
//							error = error + ":" + description;
//						}
//						Log.d("Weibo-authorize", "Login failed: " + error);
//						mAuthDialogListener.onError(new DialogError(error,
//								resultCode, description));
//					}

					// No errors.
				} else {
//					if (null == mAccessToken) {
//						mAccessToken = new Token();
//					}
//					mAccessToken.setToken(data.getStringExtra(TOKEN));
//					mAccessToken.setExpiresTime(data.getStringExtra(EXPIRES));
//					mAccessToken.setRefreshToken(data
//							.getStringExtra(REFRESHTOKEN));
//					if (isSessionValid()) {
//						Log.d("Weibo-authorize",
//								"Login Success! access_token="
//										+ mAccessToken.getToken() + " expires="
//										+ mAccessToken.getExpiresTime()
//										+ "refresh_token="
//										+ mAccessToken.getRefreshToken());
//						mAuthDialogListener.onComplete(data.getExtras());
//					} else {
//						Log.d("Weibo-authorize",
//								"Failed to receive access token by SSO");
//						startDialogAuth(mAuthActivity, mAuthPermissions);
//					}
					
					
					SinaTokenBean ret=new SinaTokenBean();
					ret.access_token=data.getStringExtra(TOKEN);
					
					String expires_in=data.getStringExtra(EXPIRES);
					if(!TextUtils.isEmpty(expires_in))
					{
						ret.expires_in=System.currentTimeMillis()+Long.valueOf(expires_in)*1000-OpenManager.EARLY_INVAILD_TIME;
					}
					
					if (isSessionValid(ret)) 
					{
						SharedPreferenceUtil.store(mAuthActivity, OpenManager.getInstatnce().getSpName(OpenManager.SINA_WEIBO), ret);
						return true;
					}
					else
					{
						originalAuthorize(mAuthActivityCode,mAuthActivity);
					}
				}

				// An error occurred before we could be redirected.
			} else if (resultCode == Activity.RESULT_CANCELED) {

//				// An Android error occured.
//				if (data != null) {
//					Log.d("Weibo-authorize",
//							"Login failed: " + data.getStringExtra("error"));
//					mAuthDialogListener.onError(new DialogError(data
//							.getStringExtra("error"), data.getIntExtra(
//							"error_code", -1), data
//							.getStringExtra("failing_url")));
//
//					// User pressed the 'back' button.
//				} else {
//					Log.d("Weibo-authorize", "Login canceled by user.");
//					mAuthDialogListener.onCancel();
//				}
			}
		}
		
		return false;
	}
	
	public static boolean getSsoAuthorizeResult(int requestCode, int resultCode, Intent data,Activity activity)
	{
		SinaTokenBean ret=new SinaTokenBean();
		ret.access_token=data.getStringExtra(TOKEN);
		
		String expires_in=data.getStringExtra(EXPIRES);
		if(!TextUtils.isEmpty(expires_in))
		{
			ret.expires_in=System.currentTimeMillis()+Long.valueOf(expires_in)*1000-OpenManager.EARLY_INVAILD_TIME;
		}
		
		ret.uid=data.getStringExtra(UID);
		
		if (!TextUtils.isEmpty(ret.access_token)&& ret.expires_in>System.currentTimeMillis()) 
		{
			SharedPreferenceUtil.store(activity, OpenManager.getInstatnce().getSpName(OpenManager.SINA_WEIBO), ret);
			return true;
		}
		else
		{
			SinaShareImpl.originalAuthorize(OpenManager.SINA_WEIBO,activity);
		}
		return false;
	}
	
	public boolean isSessionValid(SinaTokenBean ret) 
	{
			return !TextUtils.isEmpty(ret.access_token)&& ret.expires_in>System.currentTimeMillis();
	}
}
