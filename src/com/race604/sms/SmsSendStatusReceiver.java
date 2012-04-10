package com.race604.sms;

import com.race604.sms.model.SmsInfo;
import com.race604.sms.model.Utility;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telephony.SmsManager;
import android.widget.Toast;

public class SmsSendStatusReceiver extends BroadcastReceiver {

	public static String SENT = "com.race604.sms.SENT";
	public static String DELIVERED = "com.race604.sms.DELIVERED";
	public static String SMS_URI = "sms_uri";

	@Override
	public void onReceive(Context ctx, Intent intent) {
		Uri uri = intent.getParcelableExtra(SMS_URI);
		Context context = SmsApplication.get();
		String action = intent.getAction();
		if (action.equals(SENT)) {
			switch (getResultCode()) {
			case Activity.RESULT_OK:
				Toast.makeText(context, R.string.sent, Toast.LENGTH_SHORT)
						.show();
				Utility.updateSmsStatus(context, uri, SmsInfo.STATUS_PENDING);
				break;
			case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
				Toast.makeText(context, R.string.failure_generic,
						Toast.LENGTH_SHORT).show();
				Utility.updateSmsStatus(context, uri, SmsInfo.STATUS_FAILED);
				break;
			case SmsManager.RESULT_ERROR_NO_SERVICE:
				Toast.makeText(context, R.string.failure_noservice,
						Toast.LENGTH_SHORT).show();
				Utility.updateSmsStatus(context, uri, SmsInfo.STATUS_FAILED);
				break;
			case SmsManager.RESULT_ERROR_NULL_PDU:
				Toast.makeText(context, R.string.failure_nullpdu,
						Toast.LENGTH_SHORT).show();
				Utility.updateSmsStatus(context, uri, SmsInfo.STATUS_FAILED);
				break;
			case SmsManager.RESULT_ERROR_RADIO_OFF:
				Toast.makeText(context, R.string.failure_radiooff,
						Toast.LENGTH_SHORT).show();
				Utility.updateSmsStatus(context, uri, SmsInfo.STATUS_FAILED);
				break;
			}
		} else if (action.equals(DELIVERED)) {
			switch (getResultCode()) {
			case Activity.RESULT_OK:
				Toast.makeText(context, R.string.delivered, Toast.LENGTH_SHORT)
						.show();
				Utility.updateSmsStatus(context, uri, SmsInfo.STATUS_COMPLETED);
				break;
			case Activity.RESULT_CANCELED:
				Toast.makeText(context, R.string.failure_canceled,
						Toast.LENGTH_SHORT).show();
				Utility.updateSmsStatus(context, uri, SmsInfo.STATUS_FAILED);
				break;
			}
		}
	}

}
