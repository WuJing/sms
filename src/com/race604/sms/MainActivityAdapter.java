package com.race604.sms;

import java.io.InputStream;
import java.util.List;

import com.race604.sms.model.ContactInfo;
import com.race604.sms.model.SmsThread;
import com.race604.sms.model.Utility;
import com.race604.sms.utils.ImageLoader;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author Wu Jing wujing@jike.com
 * @version Create at：2012-3-28 下午6:00:05
 * 
 **/
public class MainActivityAdapter extends BaseAdapter{

	private final Activity mContext;
	private final List<SmsThread> mThreadList;
	private HeadPhotoLoader mPhotoLoader;
	
	public MainActivityAdapter(Activity context, List<SmsThread> threadList) {
		super();
		mThreadList = threadList;
		mContext = context;
		mPhotoLoader = new HeadPhotoLoader(mContext, R.drawable.ic_contact);
	}
	
	public SmsThread getItem(int position) {
		return mThreadList.get(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final SmsThread thread = mThreadList.get(position);
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
		final String phone = Utility.getCleanPhone(thread.latest.address);
		ContactInfo contact = Utility.getCantactByPhone(mContext, phone);
		String from = contact.displayName;
		if (from == null) {
			from = thread.latest.address;
		}
		holder.photo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (v.getId() == R.id.headIv) {
					Intent intent =new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+phone));
					mContext.startActivity(intent);
				}
			}
		});
		mPhotoLoader.DisplayImage(String.valueOf(contact.contactId), holder.photo);
		
		from += " (" + thread.count + ")";
		holder.from.setText(from);
		holder.body.setText(thread.latest.body);
		
		return rowView;
	}
	
	
	
	@Override
	public int getCount() {
		return mThreadList.size();
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	
	public static class ViewHolder {
		public TextView from;
		public TextView body;
		public ImageView photo;
	}
	
	class HeadPhotoLoader extends ImageLoader {

		public HeadPhotoLoader(Context context, int defualt_drawable) {
			super(context, defualt_drawable);
		}

		@Override
		protected Bitmap getExtactBitmap(String arg) {
			long id = Long.parseLong(arg);
			Uri uri = ContentUris.withAppendedId(
					ContactsContract.Contacts.CONTENT_URI, id);

			InputStream input = ContactsContract.Contacts
					.openContactPhotoInputStream(mContext.getContentResolver(), uri);

			if (input != null) {
				Bitmap bitmap = BitmapFactory.decodeStream(input);
				return bitmap;
			} else {
				return null;
			}
		}
		
	}

}
