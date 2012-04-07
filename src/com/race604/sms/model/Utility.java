package com.race604.sms.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.race604.sms.ThreadActivity;

import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

public class Utility {
	
	
	public static final String DESC_SORT_ORDER = "date DESC";
	public static final String ASC_SORT_ORDER = "date ASC";
	
	public static final String DEFAULT_SORT_ORDER = DESC_SORT_ORDER;
	
	public static String[] SMS_PROJECTION = new String[] { "_id", "thread_id", "address",
			"person", "date", "protocol", "read", "status", "type", "body" };
	
	public static List<SmsInfo> getSmsInfo(Context context, Uri uri, String selection, String[] selectionArgs, String sortOrder, int maxCount) {
		List<SmsInfo> smsList = new ArrayList<SmsInfo>();
		

		Cursor cusor = context.getContentResolver().query(uri, SMS_PROJECTION, selection,
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
		
		int count = 0;
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
				
				count++;
				if(maxCount > 0 && count >= maxCount) {
					break;
				}
			}
			cusor.close();
		}
		return smsList;
	}
	
	public static List<SmsInfo> getSmsInfo(Context context, Uri uri, String selection, String[] selectionArgs, String sortOrder) {
		return getSmsInfo(context, uri, selection, selectionArgs, sortOrder, 0);
	}
	
	public static SmsInfo getASmsInfo(Context context, Uri uri) {
		List<SmsInfo> list = getSmsInfo(context, uri, null, null, null);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
		
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
	

	public static long getThreadIdByPhone(Context context, String phone) {
		List<SmsInfo> list = getSmsInfo(context, Uri.parse(SmsInfo.SMS_URI_ALL),
				"address = ? ", new String[] {phone}, Utility.DESC_SORT_ORDER);
		if (list.size() <= 0) {
			return -1;
		} else {
			return list.get(0).thread_id;
		}
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
	
	public static Uri saveSentSms(Context context, String address, String body) {
		ContentValues values = new ContentValues(); 
		values.put("address", address); 
		values.put("body", body);
		values.put("status", SmsInfo.STATUS_NONE);
		return context.getContentResolver().insert(Uri.parse(SmsInfo.SMS_URI_SEND), values);
	}
	
	public static Uri saveReceivedSms(Context context, String address, String body) {
		ContentValues values = new ContentValues(); 
		values.put("address", address); 
		values.put("body", body);
		values.put("status", SmsInfo.STATUS_NONE);
		return context.getContentResolver().insert(Uri.parse(SmsInfo.SMS_URI_INBOX), values);
	}
	
	public static int updateSmsStatus(Context context, Uri uri, int status) {
		ContentValues values = new ContentValues();
		values.put("status", status);
		return context.getContentResolver().update(uri, values, null, null);
	}
	
	public static SmsInfo parseSmsMessage(SmsMessage message) {
		SmsInfo sms = new SmsInfo();
		sms.address = message.getOriginatingAddress();
		sms.body = message.getMessageBody();
		
		return sms;
	}
	
}
