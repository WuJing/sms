package com.race604.sms;

import java.util.List;

import com.race604.sms.model.SmsInfo;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * @author Wu Jing wujing@jike.com
 * @version Create at：2012-3-28 下午6:00:05
 * 
 **/
public class MainActivityAdapter extends ArrayAdapter<SmsInfo>{

	private final Activity mContext;
	private final List<SmsInfo> mSmsList;
	
	public MainActivityAdapter(Activity context, List<SmsInfo> smsList) {
		super(context, R.layout.thread_item, smsList);
		mSmsList = smsList;
		mContext = context;
	}

	@Override
	public void add(SmsInfo object) {
		super.add(object);
		mSmsList.add(object);
	}
	
	public void addAll(List<SmsInfo> smsList) {
		mSmsList.addAll(smsList);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		if (rowView == null) {
			LayoutInflater inflater = mContext.getLayoutInflater();
			rowView = inflater.inflate(R.layout.thread_item, null);
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.from = (TextView) rowView.findViewById(R.id.fromTv);
			viewHolder.body = (TextView) rowView.findViewById(R.id.bodyTv);
			rowView.setTag(viewHolder);
		}
		
		ViewHolder holder = (ViewHolder) rowView.getTag();
		SmsInfo sms = mSmsList.get(position);
		holder.from.setText(sms.address);
		holder.body.setText(sms.body);
		
		return rowView;
	}
	
	class ViewHolder {
		public TextView from;
		public TextView body;
	}	
	
}
