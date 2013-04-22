package com.openshare.demo.util;

import android.app.Application;

import com.open.share.ContextMgr;

public class OpenDemoApp extends Application
{
	private static OpenDemoApp instance;
	
	
	@Override
	public void onCreate()
	{
		super.onCreate();
		instance = this;
		
		ContextMgr.setContext(this);
	}

	public static OpenDemoApp getInstance()
	{
		return instance;
	}

}
