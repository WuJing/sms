package com.race604.sms.model;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class Utility {
	
	public static final String DEFAULT_SORT_ORDER = "date DESC";
	
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
		String sortOrder = "date desc";
		return getSmsInfo(context, Uri.parse(SmsInfo.SMS_URI_INBOX), null, null, DEFAULT_SORT_ORDER);
	}
	
	public static List<SmsInfo> getSmsSend(Context context) {
		return getSmsInfo(context, Uri.parse(SmsInfo.SMS_URI_SEND), null, null, DEFAULT_SORT_ORDER);
	}
	
	public static List<SmsInfo> getSmsDraft(Context context) {
		return getSmsInfo(context, Uri.parse(SmsInfo.SMS_URI_DRAFT), null, null, DEFAULT_SORT_ORDER);
	}
	
	public static List<Thread> getThreadList() {
		List<Thread> list = new ArrayList<Thread>();
		
		return list;
	}
}
