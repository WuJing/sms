package com.race604.sms;

import com.race604.sms.model.SmsInfo;
import com.race604.sms.model.Utility;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

public class SmsReceiver extends BroadcastReceiver {

	public static final int NOTIFY_NEW_SMS_RECEIVED = 1;
	
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
            SmsInfo sms = Utility.getASmsInfo(context, last);
            long thread_id = sms.thread_id;
            
            Context appContext = SmsApplication.get();
            NotificationManager notificationManager = (NotificationManager) appContext.getSystemService(Context.NOTIFICATION_SERVICE);
            
            int icon = R.drawable.ic_notify_new;
            CharSequence tickerText = message;
            long when = System.currentTimeMillis();
            Notification notification = new Notification(icon, tickerText, when);
            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            
            CharSequence contentTitle = Utility.getCantactByPhone(appContext, sms.address).toString();
            CharSequence contentText = message;
            Intent notificationIntent = new Intent(appContext, ThreadActivity.class);
            notificationIntent.putExtra("id", thread_id);
            PendingIntent contentIntent = PendingIntent.getActivity(appContext, 0, notificationIntent, PendingIntent.FLAG_ONE_SHOT);
            
            notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
            notificationManager.notify(NOTIFY_NEW_SMS_RECEIVED, notification);

            TextView textView = new TextView(context);
            textView.setText(message);
            WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                    WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                    PixelFormat.TRANSLUCENT);
            params.gravity = Gravity.RIGHT | Gravity.TOP;
            params.setTitle("Load Average");
            WindowManager wm = (WindowManager) SmsApplication.get().getSystemService(Service.WINDOW_SERVICE);
            wm.addView(textView, params);
            
            Activity curActivity = SmsApplication.get().getCurrentActivity();
            
//            if (curActivity != null && curActivity instanceof ThreadActivity) {
//            	ThreadActivity threadAcitivity = (ThreadActivity)curActivity;
//            	if (thread_id == threadAcitivity.getThreadId()) {
//            		threadAcitivity.addSmsInfo(smsInfos[smsInfos.length-1]);
//            	} else {
//            		threadAcitivity.showThread(thread_id);
//            	}
//            } 
            
//            else {
//            	Intent threadItent = new Intent(SmsApplication.get(), ThreadActivity.class);
//            	threadItent.putExtra("id", thread_id);
//            	threadItent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            	context.startActivity(threadItent);
//            }
            // 禁止其他短信接收软件
            // abortBroadcast();
        }     
		
	}

}
