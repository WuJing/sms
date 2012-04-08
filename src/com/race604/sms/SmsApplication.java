package com.race604.sms;

import android.app.Activity;
import android.app.Application;

public class SmsApplication extends Application {


	private Activity mCurrentActivity = null;
	private static SmsApplication mTheApp;
	
	@Override
	public void onCreate() {
		super.onCreate();
		mTheApp = SmsApplication.this;

	}
	
	public static SmsApplication get() {
		return mTheApp;
	}

	public void setCurrentActivity(Activity activity) {
		this.mCurrentActivity = activity;
	}
	
	public Activity getCurrentActivity() {
		return this.mCurrentActivity;
	}
}
