package com.race604.sms;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.race604.sms.model.SmsInfo;
import com.race604.sms.model.Utility;

public class ThreadActivity extends SherlockListActivity implements
		OnClickListener {

	private long thread_id;
	private List<SmsInfo> mList;
	private ThreadActivityAdapter mAdapter;
	private ListView mSmsLv;
	private Button mSentBtn;
	private EditText mMessageEt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.thread_activity);

		thread_id = getIntent().getExtras().getLong("id");
		mList = Utility.getSmsAllByThreadId(this, thread_id);
		mAdapter = new ThreadActivityAdapter(this, mList);

		if (mList.size() > 0) {
			mAdapter.setContactName(Utility.getCantactByPhone(this,
					mList.get(0).address).displayName);
		}

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
		}
		return true;
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.bt_send: {
			String message = mMessageEt.getText().toString();
			String phone = mList.get(0).address;
			Utility.sendMms(phone, message);
			
			mMessageEt.setText("");
			mMessageEt.clearFocus();
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(mMessageEt.getWindowToken(), 0);
			break;
		}
			
		}
		
	}
}
