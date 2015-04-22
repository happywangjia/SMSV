package com.wangjia.captche;


import com.thinkland.sdk.util.CommonFun;

import android.app.Application;

public class CaptchaApplication extends Application{

	@Override
	public void onCreate() {
		super.onCreate();
		CommonFun.initialize(getApplicationContext(), false);
		
		
	}
	
}
