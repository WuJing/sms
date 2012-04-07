package com.race604.sms;

import com.race604.sms.model.SmsInfo;
import com.race604.sms.model.Utility;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class SmsReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();        
		SmsInfo[] smsInfos = null;
		String message = "";
        if (bundle != null)
        {
            //接收短信
            Object[] pdus = (Object[]) bundle.get("pdus");
            smsInfos = new SmsInfo[pdus.length];
            Uri last = null;
            for (int i=0; i<smsInfos.length; i++){
            	SmsMessage msg = SmsMessage.createFromPdu((byte[])pdus[i]);                
            	smsInfos[i] = Utility.parseSmsMessage(msg);
            	last = Utility.saveReceivedSms(context, msg.getOriginatingAddress(),
                		msg.getMessageBody());
            	message += smsInfos[i].address + ": " + smsInfos[i].body + "\n";
            }
            //---display the new SMS message---
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            
            Activity curActivity = SmsApplication.get().getCurrentActivity();
            long thread_id = Utility.getASmsInfo(context, last).thread_id;
            if (curActivity != null && curActivity instanceof ThreadActivity) {
            	ThreadActivity threadAcitivity = (ThreadActivity)curActivity;
            	if (thread_id == threadAcitivity.getThreadId()) {
            		threadAcitivity.addSmsInfo(smsInfos[smsInfos.length-1]);
            	} else {
            		threadAcitivity.showThread(thread_id);
            	}
            } else {
            	Intent threadItent = new Intent(SmsApplication.get(), ThreadActivity.class);
            	threadItent.putExtra("id", thread_id);
            	threadItent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            	context.startActivity(threadItent);
            }
        }     
		
	}

}
