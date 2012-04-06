package com.race604.sms.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.widget.Toast;

public class Utility {
	
	
	public static final String DESC_SORT_ORDER = "date DESC";
	public static final String ASC_SORT_ORDER = "date ASC";
	
	public static final String DEFAULT_SORT_ORDER = DESC_SORT_ORDER;
	
	public static List<SmsInfo> getSmsInfo(Context context, Uri uri, String selection, String[] selectionArgs, String sortOrder) {
		List<SmsInfo> smsList = new ArrayList<SmsInfo>();
		String[] projection = new String[] { "_id", "thread_id", "address",
				"person", "date", "protocol", "read", "status", "type", "body" };

		Cursor cusor = context.getContentResolver().query(uri, projection, selection,
				selectionArgs, sortOrder);
		int idCol = cusor.getColumnIndex("_id");
		int thread_idCol = cusor.getColumnIndex("thread_id");
		int addressCol = cusor.getColumnIndex("address");
		int personCol = cusor.getColumnIndex("person");
		int dateCol = cusor.getColumnIndex("date");
		int protocolCol = cusor.getColumnIndex("protocol");
		int readCol = cusor.getColumnIndex("read");
		int statusCol = cusor.getColumnIndex("status");
		int typeCol = cusor.getColumnIndex("type");
		int bodyCol = cusor.getColumnIndex("body");
		if (cusor != null) {
			while (cusor.moveToNext()) {
				SmsInfo smsinfo = new SmsInfo();
				smsinfo.id = cusor.getInt(idCol);
				smsinfo.thread_id = cusor.getInt(thread_idCol);
				smsinfo.address = cusor.getString(addressCol);
				smsinfo.person = cusor.getString(personCol);
				smsinfo.date = cusor.getLong(dateCol);
				smsinfo.protocol = cusor.getInt(protocolCol);
				smsinfo.read = cusor.getInt(readCol);
				smsinfo.status = cusor.getInt(statusCol);
				smsinfo.type = cusor.getInt(typeCol);
				smsinfo.body = cusor.getString(bodyCol);
				smsList.add(smsinfo);
			}
			cusor.close();
		}
		return smsList;
	}
	
	public static List<SmsInfo> getSmsAll(Context context) {	
		return getSmsInfo(context, Uri.parse(SmsInfo.SMS_URI_ALL), null, null, DEFAULT_SORT_ORDER);
	}
	
	public static List<SmsInfo> getSmsInbox(Context context) {
		return getSmsInfo(context, Uri.parse(SmsInfo.SMS_URI_INBOX), null, null, DEFAULT_SORT_ORDER);
	}
	
	public static List<SmsInfo> getSmsSend(Context context) {
		return getSmsInfo(context, Uri.parse(SmsInfo.SMS_URI_SEND), null, null, DEFAULT_SORT_ORDER);
	}
	
	public static List<SmsInfo> getSmsDraft(Context context) {
		return getSmsInfo(context, Uri.parse(SmsInfo.SMS_URI_DRAFT), null, null, DEFAULT_SORT_ORDER);
	}
	
	public static List<SmsThread> getThreadALL(Context context) {
		List<SmsThread> list = new ArrayList<SmsThread>();
		List<SmsInfo> smsList = getSmsAll(context);
		HashMap<Long, Integer> threadIds = new HashMap<Long, Integer>();
		Integer index;
		SmsThread thread;
		for (SmsInfo sms : smsList) {
			index = threadIds.get(sms.thread_id);
			if (index == null) {
				threadIds.put(sms.thread_id, list.size());
				thread = new SmsThread();
				thread.count = 0;
				thread.unread = (sms.read == 0);
				thread.latest = sms;
				list.add(thread);
			} else {
				thread = list.get(index);
			}
			thread.count++;
			thread.unread |= (sms.read == 0);
		}
		return list;
	}
	
	public static List<SmsInfo> getSmsAllByThreadId(Context context, long thread_id) {
		return getSmsInfo(context, Uri.parse(SmsInfo.SMS_URI_ALL), 
				"thread_id = ?", new String[] { String.valueOf(thread_id) },
				Utility.ASC_SORT_ORDER);
	}
	
	public static ContactInfo getCantactByPhone(Context context, String phone) {
		String num = phone;
		if (phone.startsWith("+86")) {
			num = phone.substring(3);
		} 

		ContactInfo contact = new ContactInfo();
		contact.displayName = num;
		String[] projection = { ContactsContract.PhoneLookup.DISPLAY_NAME,
				ContactsContract.CommonDataKinds.Phone.NUMBER,
				ContactsContract.CommonDataKinds.Phone.CONTACT_ID};
		
		ContentResolver cr = context.getContentResolver();
		Cursor pCur = cr.query(
				 ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection,
				 ContactsContract.CommonDataKinds.Phone.NUMBER + " = ?",
				 new String[] { num }, null);
		 if (pCur.moveToFirst()) {
			 contact.displayName = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
			 contact.contactId = pCur.getLong(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
			 pCur.close();
		 }
		 return contact;
	}
	
	public static int sendMms(final Context context, String phone, String message) {
		String SENT = "SMS_SENT";
		String DELIVERED = "SMS_DELIVERED";
        
		int ret = 0;
		if (message == null || message.trim().length() == 0) {
			return 0;
		}
		
		PendingIntent sentPI;
		PendingIntent deliveredPI;
		
		//---when the SMS has been sent---
		context.registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(context, "SMS sent", 
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(context, "Generic failure", 
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(context, "No service", 
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(context, "Null PDU", 
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(context, "Radio off", 
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SENT));
 
        //---when the SMS has been delivered---
		context.registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(context, "SMS delivered", 
                                Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(context, "SMS not delivered", 
                                Toast.LENGTH_SHORT).show();
                        break;                        
                }
            }
        }, new IntentFilter(DELIVERED));  
		
		SmsManager smsManager = SmsManager.getDefault();
		// 如果短信没有超过限制长度，则返回一个长度的List。
		List<String> texts = smsManager.divideMessage(message);

		for (String text : texts) {
			// TODO 写入到发件箱
			sentPI = PendingIntent.getBroadcast(context, 0, new Intent(
					SENT), 0);
			deliveredPI = PendingIntent.getBroadcast(context, 0,
					new Intent(DELIVERED), 0);
			smsManager.sendTextMessage(phone, null, text, sentPI, deliveredPI);
			ret++;
		}
		return ret;
		
	}
}
