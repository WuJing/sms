package com.race604.sms;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.zip.Inflater;

import com.race604.sms.model.SmsInfo;

import android.app.Activity;
import android.content.Context;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ThreadActivityAdapter  extends ArrayAdapter<SmsInfo>{


	private List<SmsInfo> mList;
	private Activity mContext;
	private LayoutInflater mInflater;
	private String mName;
	
	public ThreadActivityAdapter(Activity context, List<SmsInfo> smsList) {
		super(context, R.layout.thread_item, smsList);
		mList = smsList;
		mContext = context;
		mInflater = mContext.getLayoutInflater();
	}
	
	public void setContactName(String name) {
		mName = name;
	}
	
	public String getContactName() {
		return mName;
	}
	
	@Override
	public void add(SmsInfo object) {
		mList.add(object);
	}

	@Override
	public void addAll(Collection<? extends SmsInfo> collection) {
		mList.addAll(collection);
	}

	@Override
	public void clear() {
		super.clear();
		mList.clear();
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public SmsInfo getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rawView = convertView;
		if (rawView == null) {
			rawView = mInflater.inflate(R.layout.thread_item, null);
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.name = (TextView) rawView.findViewById(R.id.name_tv);
			viewHolder.time = (TextView) rawView.findViewById(R.id.time_tv);
			viewHolder.sms = (TextView) rawView.findViewById(R.id.sms_tv);
			rawView.setTag(viewHolder);
		}
		
		ViewHolder holder = (ViewHolder) rawView.getTag();
		SmsInfo sms = mList.get(position);
		if (sms.type == 1)  { // 接收到的短信
			holder.name.setText(mName);
		} else { // 自己发送的短信
			holder.name.setText(R.string.me);
		}
		Time time = new Time();
		time.set(sms.date);
		holder.time.setText(time.format("%Y-%m-%d %H:%M"));
		holder.sms.setText(sms.body);
		
		return rawView;
	}
	
	private static class ViewHolder {
		public TextView name;
		public TextView time;
		public TextView sms;
	}

}
