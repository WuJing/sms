package com.race604.sms;

import java.util.List;

import com.race604.sms.model.ContactInfo;
import com.race604.sms.model.SmsInfo;
import com.race604.sms.model.SmsThread;
import com.race604.sms.model.Utility;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author Wu Jing wujing@jike.com
 * @version Create at：2012-3-28 下午6:00:05
 * 
 **/
public class MainActivityAdapter extends ArrayAdapter<SmsThread>{

	private final Activity mContext;
	private final List<SmsThread> mThreadList;
	
	public MainActivityAdapter(Activity context, List<SmsThread> threadList) {
		super(context, R.layout.thread_item, threadList);
		mThreadList = threadList;
		mContext = context;
	}

	@Override
	public void add(SmsThread object) {
		super.add(object);
		mThreadList.add(object);
	}
	
	public void addAll(List<SmsThread> threadList) {
		mThreadList.addAll(threadList);
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
			viewHolder.photo = (ImageView) rowView.findViewById(R.id.headIv);
			rowView.setTag(viewHolder);
		}
		
		ViewHolder holder = (ViewHolder) rowView.getTag();
		SmsThread thread = mThreadList.get(position);
		ContactInfo contact = Utility.getCantactByPhone(mContext, thread.latest.address);
		String from = contact.displayName;
		if (from == null) {
			from = thread.latest.address;
		}
		
		Bitmap photo = contact.getPhoto(mContext);
		holder.photo.setImageBitmap(photo);
		
		from += " (" + thread.count + ")";
		holder.from.setText(from);
		holder.body.setText(thread.latest.body);
		
		return rowView;
	}
	
	public class ViewHolder {
		public TextView from;
		public TextView body;
		public ImageView photo;
	}	
	
}
