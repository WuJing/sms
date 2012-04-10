package com.race604.sms;

import java.util.List;

import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.race604.sms.model.SmsInfo;
import com.race604.sms.model.Utility;

public class NewSmsActivity extends SherlockActivity implements OnClickListener {

	private Button mSendBtn;
	private Button mSelectBtn;
	private EditText mAddressEt;
	private EditText mContentEt;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_sms_activity);
		
		mSendBtn = (Button) findViewById(R.id.bt_send);
		mSelectBtn = (Button) findViewById(R.id.bt_select);
		mSendBtn.setOnClickListener(this);
		mSelectBtn.setOnClickListener(this);
		
		mAddressEt = (EditText) findViewById(R.id.et_address);
		mContentEt = (EditText) findViewById(R.id.et_content);
		
		getSupportActionBar().setTitle(R.string.newsms);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.clear();
		menu.add(0, R.string.search, 0, R.string.search)
				.setActionView(R.layout.action_search)
				.setShowAsAction(
						MenuItem.SHOW_AS_ACTION_IF_ROOM
								| MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		int id = item.getItemId();
		switch (id) {
		case android.R.id.home: // home button
			finish();
			break;
		case R.string.search:
			break;
		}
		return true;
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.bt_select:
			break;
		case R.id.bt_send: {
			String phone = mAddressEt.getText().toString();
			String message = mContentEt.getText().toString();
			if(phone == null || phone.length() <= 0
					|| message == null || message.length() <= 0 ) {
				return;
			}
			
			SmsManager smsManager = SmsManager.getDefault();
			// 如果短信没有超过限制长度，则返回一个长度的List。
			List<String> texts = smsManager.divideMessage(message);

			PendingIntent sentPI;
			PendingIntent deliveredPI;

			Uri uri = null;
			for (String text : texts) {
				uri = Utility.saveSentSms(this, phone, text);

				Bundle bundle = new Bundle();
				bundle.putParcelable(SmsSendStatusReceiver.SMS_URI, uri);
				
				Intent intent = new Intent(SmsSendStatusReceiver.SENT);
				intent.putExtras(bundle);
				sentPI = PendingIntent.getBroadcast(this, 0,
						intent, PendingIntent.FLAG_UPDATE_CURRENT);
				
				intent = new Intent(SmsSendStatusReceiver.DELIVERED);
				intent.putExtras(bundle);
				deliveredPI = PendingIntent.getBroadcast(this,
						0, intent, PendingIntent.FLAG_ONE_SHOT);

				smsManager.sendTextMessage(phone, null, text, sentPI,
						deliveredPI);
			}
			// 隐藏键盘
//			mContentEt.setText("");
//			mContentEt.clearFocus();
//			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//			imm.hideSoftInputFromWindow(mContentEt.getWindowToken(), 0);
			
			Intent intent = new Intent(NewSmsActivity.this, ThreadActivity.class);
			SmsInfo sms = Utility.getASmsInfo(this, uri);
			intent.putExtra("id", sms.thread_id);
			startActivity(intent);
			finish();
			break;
		}
		default:
			break;
		}
		
	}

}
