package com.race604.sms;

import java.util.Collection;
import java.util.List;

import com.race604.sms.model.ContactInfo;
import com.race604.sms.model.SmsInfo;
import com.race604.sms.model.SmsThread;
import com.race604.sms.model.Utility;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
	private Bitmap mDefaultHead;
	
	public MainActivityAdapter(Activity context, List<SmsThread> threadList) {
		super(context, R.layout.main_item, threadList);
		mThreadList = threadList;
		mContext = context;
	}
	
	@Override
	public SmsThread getItem(int position) {
		return mThreadList.get(position);
	}

	@Override
	public void add(SmsThread object) {
		super.add(object);
		mThreadList.add(object);
	}

	@Override
	public void addAll(Collection<? extends SmsThread> collection) {
		mThreadList.addAll(collection);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		if (rowView == null) {
			LayoutInflater inflater = mContext.getLayoutInflater();
			rowView = inflater.inflate(R.layout.main_item, null);
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
		if (photo == null) {
			if (mDefaultHead == null) {
				mDefaultHead = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_contact);
			}
			photo = mDefaultHead;
		}
		
		holder.photo.setImageBitmap(photo);
		
		from += " (" + thread.count + ")";
		holder.from.setText(from);
		holder.body.setText(thread.latest.body);
		
		return rowView;
	}
	
	public static class ViewHolder {
		public TextView from;
		public TextView body;
		public ImageView photo;
	}
	
}
