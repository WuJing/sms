package com.race604.sms;

import java.util.List;

import com.race604.sms.model.SmsInfo;
import com.race604.sms.model.SmsInfoUtility;

import android.app.Activity;
import android.app.ListActivity;
import android.net.Uri;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ListView;

public class MainActivity extends ListActivity {
    
	ListView mThreadLv;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        mThreadLv = getListView();
        
        List<SmsInfo> smsList = SmsInfoUtility.getSmsInfo(this, Uri.parse(SmsInfo.SMS_URI_ALL));
        
        setListAdapter(new MainActivityAdapter(this, smsList));
                
    }
}