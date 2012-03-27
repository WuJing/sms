package com.race604.sms.model;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class SmsInfoUtility {
	public static List<SmsInfo> getSmsInfo(Context context, Uri uri) {
		List<SmsInfo> smsList = new ArrayList<SmsInfo>();
		String[] projection = new String[] { "_id", "thread_id", "address",
				"person", "date", "protocol", "read", "status", "type", "body" };

		Cursor cusor = context.getContentResolver().query(uri, projection, null,
				null, "date desc");

		int idCol = cusor.getColumnIndex("_id");
		int thread_idCol = cusor.getColumnIndex("thread_id");
		int addressCol = cusor.getColumnIndex("address");
		int personCol = cusor.getColumnIndex("person");
		int dateCol = cusor.getColumnIndex("date");
		int protocolCol = cusor.getColumnIndex("protocol");
		int readCol = cusor.getColumnIndex("read");
		int statusCol = cusor.getColumnIndex("status");
		int typeCol = cusor.getColumnIndex("body");
		if (cusor != null) {
			while (cusor.moveToNext()) {
				SmsInfo smsinfo = new SmsInfo();
				smsinfo.id = cusor.getInt(idCol);
				smsinfo.thread_id = cusor.getInt(thread_idCol);
				smsinfo.address = cusor.getString(addressCol);
				smsinfo.date = cusor.getLong(dateCol);
				smsinfo.protocol = cusor.getInt(protocolCol);
				smsinfo.read = cusor.getInt(readCol);
				smsinfo.status = cusor.getInt(statusCol);
				smsinfo.type = cusor.getInt(typeCol);
				smsinfo.setDate(cusor.getString(dateColumn));
				smsinfo.setPhoneNumber(cusor.getString(phoneNumberColumn));
				smsinfo.setSmsbody(cusor.getString(smsbodyColumn));
				smsinfo.setType(cusor.getString(typeColumn));
				infos.add(smsinfo);
			}
			cusor.close();
		}

		
		cusor.getInt(idCol);
		
		
		
		return smsList;
	}
}
