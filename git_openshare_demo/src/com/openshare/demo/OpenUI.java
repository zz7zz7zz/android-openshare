package com.openshare.demo;

import java.util.ArrayList;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.open.share.OpenAppConstant;
import com.open.share.OpenManager;
import com.open.share.interfaces.IOpenResponse;
import com.open.share.net.AbstractRunnable;
import com.open.share.net.NetPool;
import com.open.share.qqzone.QQZoneShareImpl;
import com.open.share.qqzone.QQZoneTokenBean;
import com.open.share.renren.RenrenTokenBean;
import com.open.share.renren.RenrenUserInfo;
import com.open.share.sina.SinaShareImpl;
import com.open.share.sina.SinaTokenBean;
import com.open.share.sina.SinaUserInfo;
import com.open.share.tencent.TenTokenBean;
import com.open.share.tencent.TenUserInfo;
import com.open.share.utils.SharedPreferenceUtil;
import com.openshare.demo.util.OpenDemoApp;
import com.openshare.demo.util.SpUtil;
import com.tencent.tauth.bean.UserInfo;

public class OpenUI extends Activity {

	private ArrayList<Integer> openList=new ArrayList<Integer>();
	private LinearLayout openParentLayout;
	private final int TAG_EXTRA_VALUE=100;
	private QQZoneShareImpl qqzoneImpl=new QQZoneShareImpl();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.open_ui);
		
		initData();
		initView();
	}
	
	private void initData()
	{
		openList.add(OpenManager.SINA_WEIBO);
		openList.add(OpenManager.TENCENT_WEIBO);
		openList.add(OpenManager.RENREN);
		openList.add(OpenManager.QQZONE);
		
		IntentFilter filter = new IntentFilter();
		filter.addAction(OpenManager.AUTH_RESULT_ACTION);
		registerReceiver(authResultReceiver, filter);
	}
	
	private void initView()
	{
		
		openParentLayout=(LinearLayout)findViewById(R.id.openPlatforms_parent);
		
		int size=openList.size();
		openParentLayout.setVisibility(size>0?View.VISIBLE:View.GONE);
		LinearLayout item=null;
		for(int i=0;i<size;i++)
		{
			item=(LinearLayout)this.getLayoutInflater().inflate(R.layout.open_ui_item, null);
			TextView name=(TextView)item.findViewById(R.id.open_platform_name);
			TextView username=(TextView)item.findViewById(R.id.open_account_username);
			Button bindBtn=(Button)item.findViewById(R.id.open_bindBtn);
			ImageView logoView = (ImageView) item.findViewById(R.id.open_platform_logo);
			
			item.setTag(openList.get(i)+TAG_EXTRA_VALUE);
			bindBtn.setTag(openList.get(i));
			
			boolean isValid=OpenManager.getInstatnce().isVaild(openList.get(i), this);
			
			name.setText(OpenManager.getInstatnce().getName(openList.get(i)));
			
			String sUsername=SpUtil.getString(OpenManager.getInstatnce().getSpName(openList.get(i)), "name", getString(R.string.shareMgr_authed));
			if(TextUtils.isEmpty(sUsername))
			{
				sUsername=getString(R.string.shareMgr_authed);
			}
			
			username.setText(isValid?sUsername:getString(R.string.shareMgr_nobind));
			bindBtn.setText(getString(isValid?R.string.shareMgr_logout:R.string.shareMgr_bind));
			bindBtn.setBackgroundResource(isValid?R.drawable.selector_btn_unbind:R.drawable.selector_btn_bind);
			bindBtn.setOnClickListener(listener);
			switch (openList.get(i))
			{
				case OpenManager.SINA_WEIBO :
					logoView.setImageResource(R.drawable.img_share_management_sinaicon);
					break;
				case OpenManager.TENCENT_WEIBO :
					logoView.setImageResource(R.drawable.img_share_management_tencenticon);
					break;
				case OpenManager.RENREN :
					logoView.setImageResource(R.drawable.img_share_management_renrenicon);
					break;
				case OpenManager.QQZONE :
					logoView.setImageResource(R.drawable.img_share_management_qqzoneicon);
					break;
			}
			
			openParentLayout.addView(item);
			
			if(i!=(size-1))
			{
				TextView line=new TextView(this);
				line.setHeight(1);
				line.setBackgroundResource(R.drawable.list_item_ganline);
				openParentLayout.addView(line);
			}
		}
	}
	
	
	private View.OnClickListener listener=new View.OnClickListener() {
		
		@Override
		public void onClick(View v) 
		{
			int tag=(Integer) v.getTag();
			if(!OpenManager.getInstatnce().isVaild(tag, OpenUI.this))
			{
				if(tag==OpenManager.QQZONE)
				{
					qqzoneImpl.authorize(1111, OpenUI.this);
					return;
				}
				OpenManager.getInstatnce().authorize(tag, OpenUI.this);
			}
			else
			{
				OpenManager.getInstatnce().unBind(tag, OpenUI.this);
				reInit(tag);
			}
		}
	};
	
	private void reInit(int dest)
	{
		int size=openList.size();
		for(int i=0;i<size;i++)
		{
			if(dest==openList.get(i))
			{
				boolean isValid=OpenManager.getInstatnce().isVaild(dest, this);
				View view=openParentLayout.findViewWithTag(dest+TAG_EXTRA_VALUE);
				if(null!=view)
				{
					String sUsername=SpUtil.getString(OpenManager.getInstatnce().getSpName(openList.get(i)), "name", getString(R.string.shareMgr_authed));
					if(TextUtils.isEmpty(sUsername))
					{
						sUsername=getString(R.string.shareMgr_authed);
					}
					
					((TextView)(view.findViewById(R.id.open_account_username))).setText(isValid?sUsername:getString(R.string.shareMgr_nobind));
					((Button)(view.findViewById(R.id.open_bindBtn))).setText(getString(isValid?R.string.shareMgr_logout:R.string.shareMgr_bind));
					((Button)(view.findViewById(R.id.open_bindBtn))).setBackgroundResource(isValid?R.drawable.selector_btn_unbind:R.drawable.selector_btn_bind);
				}
				break;
			}
		}
	}
	
	private void follow(int openType)
	{
		if(openType == OpenManager.SINA_WEIBO)
		{
				Bundle bundle=new Bundle();
				bundle.putString(OpenManager.BUNDLE_KEY_UID, OpenAppConstant.DEFAULT_SINA_APP_UID);
				AbstractRunnable msg=OpenManager.getInstatnce().friendShipCreate(OpenManager.SINA_WEIBO, bundle, null);
				NetPool.getInstance().push(msg);
		}
		else if(openType == OpenManager.TENCENT_WEIBO)
		{
				Bundle bundle=new Bundle();
				bundle.putString(OpenManager.BUNDLE_KEY_UID, OpenAppConstant.DEFAULT_Tencent_App_UID);
				AbstractRunnable msg=OpenManager.getInstatnce().friendShipCreate(OpenManager.TENCENT_WEIBO, bundle, null);
				NetPool.getInstance().push(msg);
		}
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{  
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK)
		{
			if(requestCode == OpenManager.SINA_WEIBO)
			{
				boolean ssoResult=SinaShareImpl.getSsoAuthorizeResult(requestCode, resultCode, data, OpenUI.this);
				if(ssoResult)
				{
					initInfo(OpenManager.SINA_WEIBO);
				}
			}
			else
			{
				//QQ空间SSO登录
				qqzoneImpl.mTencent.onActivityResult(requestCode, resultCode, data);
			}
		}
	}

	private BroadcastReceiver authResultReceiver = new BroadcastReceiver()
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			if (OpenManager.AUTH_RESULT_ACTION.equals(intent.getAction()))
			{
				Bundle bundle=intent.getExtras();
				int openType=bundle.getInt(OpenManager.BUNDLE_KEY_OPEN, 0);
				initInfo(openType);
				switch (openType)
				{
					case OpenManager.SINA_WEIBO :
						break;

					case OpenManager.TENCENT_WEIBO :
						break;
						
					case OpenManager.RENREN :
						break;
						
					case OpenManager.QQZONE :
						break;
						
					case OpenManager.WEIXIN :
						break;
				}
			}
		}
	};
	
	private void initInfo(final int openType)
	{
		reInit(openType);
		getUseInfo(openType);
		follow(openType);
	}
	
	private void getUseInfo(final int openType)
	{
			AbstractRunnable msg=OpenManager.getInstatnce().getUserInfo(openType, new Bundle(), new IOpenResponse(){
				@Override
				public boolean response(final int token,Object obj){
					if(null==obj)
					{
						return true;
					}
					else if(obj instanceof SinaUserInfo)//新浪
					{
						SinaUserInfo ret=(SinaUserInfo)obj;
						
						SinaTokenBean out=new SinaTokenBean();
						SharedPreferenceUtil.Fetch(OpenDemoApp.getInstance(), OpenManager.getInstatnce().getSpName(OpenManager.SINA_WEIBO), out);
						
						out.name=ret.name;
						out.head=ret.head;
						SharedPreferenceUtil.store(OpenDemoApp.getInstance(), OpenManager.getInstatnce().getSpName(OpenManager.SINA_WEIBO), out);
					}
					else if(obj instanceof TenUserInfo)//腾讯微博
					{
						TenUserInfo ret=(TenUserInfo)obj;
						
						TenTokenBean out=new TenTokenBean();
						SharedPreferenceUtil.Fetch(OpenDemoApp.getInstance(), OpenManager.getInstatnce().getSpName(OpenManager.TENCENT_WEIBO), out);
						
						out.name=ret.name;
						out.head=ret.head;
						SharedPreferenceUtil.store(OpenDemoApp.getInstance(), OpenManager.getInstatnce().getSpName(OpenManager.TENCENT_WEIBO), out);
					}
					else if(obj instanceof ArrayList<?>)//人人网
					{
						@SuppressWarnings("unchecked")
						ArrayList<RenrenUserInfo> list=(ArrayList<RenrenUserInfo>)obj;
						
						if(list.size()>0)
						{
							RenrenUserInfo ret=list.get(0);
							
							RenrenTokenBean out=new RenrenTokenBean();
							SharedPreferenceUtil.Fetch(OpenDemoApp.getInstance(), OpenManager.getInstatnce().getSpName(OpenManager.RENREN), out);
							
							out.name=ret.getName();
							out.head=ret.getHeadurl();
							SharedPreferenceUtil.store(OpenDemoApp.getInstance(), OpenManager.getInstatnce().getSpName(OpenManager.RENREN), out);
						}
					}
					else if(obj instanceof UserInfo)//QQ空间
					{
						UserInfo ret=(UserInfo)obj;
						
						QQZoneTokenBean out=new QQZoneTokenBean();
						SharedPreferenceUtil.Fetch(OpenDemoApp.getInstance(), OpenManager.getInstatnce().getSpName(OpenManager.QQZONE), out);
						
						out.name=ret.getNickName();
						out.head=ret.getIcon_100();
						SharedPreferenceUtil.store(OpenDemoApp.getInstance(),  OpenManager.getInstatnce().getSpName(OpenManager.QQZONE), out);
					}
					
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							reInit(openType);
						}
					});
					return true;
				}
			});
			NetPool.getInstance().push(msg);
	}
	
	@Override
	public void finish() {
		unregisterReceiver(authResultReceiver);
		super.finish();
	}

}
