package com.race604.sms;

import java.util.List;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.race604.sms.model.SmsInfo;
import com.race604.sms.model.Utility;

public class ThreadActivity extends SherlockListActivity implements
		OnClickListener {

	private long thread_id;
	private ThreadActivityAdapter mAdapter;
	private ListView mSmsLv;
	private Button mSentBtn;
	private EditText mMessageEt;
	private String mPhone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.thread_activity);

		thread_id = getIntent().getExtras().getLong("id", -1);
		if (thread_id == -1) {
			finish();
			return;
		}

		showThread(thread_id);

		ActionBar action_bar = getSupportActionBar();
		action_bar.setTitle(mAdapter.getContactName());

		mSmsLv = getListView();
		mSmsLv.setAdapter(mAdapter);

		mSentBtn = (Button) findViewById(R.id.bt_send);
		mSentBtn.setOnClickListener(this);

		mMessageEt = (EditText) findViewById(R.id.et_smsinput);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.clear();
		menu.add(0, R.string.search, 0, R.string.search)
				.setActionView(R.layout.action_search)
				.setShowAsAction(
						MenuItem.SHOW_AS_ACTION_IF_ROOM
								| MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
		menu.add(0, R.string.call, 0, R.string.call)
				.setIcon(R.drawable.ic_call)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
		case android.R.id.home: // home button
			finish();
			break;
		case R.string.search:
			break;
		case R.string.call: {
			Intent intent =new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+mPhone));
			startActivity(intent);
			break;
		}
		}
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_send: {
			String message = mMessageEt.getText().toString();

			if (message == null || message.length() == 0) {
				return;
			}

			String phone = mAdapter.getItem(0).address;

			SmsManager smsManager = SmsManager.getDefault();
			// 如果短信没有超过限制长度，则返回一个长度的List。
			List<String> texts = smsManager.divideMessage(message);

			PendingIntent sentPI;
			PendingIntent deliveredPI;

			for (String text : texts) {
				Uri uri = Utility.saveSentSms(ThreadActivity.this, phone, text);

				SmsInfo sms = Utility.getASmsInfo(ThreadActivity.this, uri);
				mAdapter.add(sms);
				Bundle bundle = new Bundle();
				bundle.putParcelable(SmsSendStatusReceiver.SMS_URI, uri);
				
				Intent intent = new Intent(SmsSendStatusReceiver.SENT);
				intent.putExtras(bundle);
				sentPI = PendingIntent.getBroadcast(ThreadActivity.this, 0,
						intent, PendingIntent.FLAG_UPDATE_CURRENT);
				
				intent = new Intent(SmsSendStatusReceiver.DELIVERED);
				intent.putExtras(bundle);
				deliveredPI = PendingIntent.getBroadcast(ThreadActivity.this,
						0, intent, PendingIntent.FLAG_ONE_SHOT);

				smsManager.sendTextMessage(phone, null, text, sentPI,
						deliveredPI);
			}
			mAdapter.notifyDataSetChanged();

			mMessageEt.setText("");
			mMessageEt.clearFocus();
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(mMessageEt.getWindowToken(), 0);
			break;
		}

		}

	}

	public long getThreadId() {
		return this.thread_id;
	}

	public void addSmsInfo(SmsInfo sms) {
		mAdapter.add(sms);
		mAdapter.notifyDataSetChanged();
	}

	public void showThread(long thread_id) {
		this.thread_id = thread_id;

		if (mAdapter == null) {
			mAdapter = new ThreadActivityAdapter(this, null);
		}


		List<SmsInfo> list = Utility.getSmsAllByThreadId(this, thread_id);
		if (list.size() <= 0) {
			finish();
			return;
		}
		mPhone = Utility.getCleanPhone(list.get(0).address);
		mAdapter.setContactName(Utility.getCantactByPhone(this,
				mPhone).displayName);

		mAdapter.addAll(list);
		mAdapter.notifyDataSetChanged();
	}

	@Override
	protected void onPause() {
		super.onPause();
		SmsApplication.get().setCurrentActivity(null);
	}

	@Override
	protected void onResume() {
		super.onResume();
		SmsApplication.get().setCurrentActivity(ThreadActivity.this);
	}

}
