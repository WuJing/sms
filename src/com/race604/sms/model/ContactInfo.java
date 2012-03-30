package com.race604.sms.model;

import java.io.InputStream;

import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;

/**
 * @author Wu Jing wujing@jike.com
 * @version Create at：2012-3-30 下午1:10:55
 * 
 **/
public class ContactInfo {
	public long contactId; // CONTACT_ID
	public String displayName; // DISPLAY_NAME

	public Bitmap getPhoto(Context context) {
		Uri uri = ContentUris.withAppendedId(
				ContactsContract.Contacts.CONTENT_URI, contactId);

		InputStream input = ContactsContract.Contacts
				.openContactPhotoInputStream(context.getContentResolver(), uri);

		if (input != null) {
			Bitmap bitmap = BitmapFactory.decodeStream(input);
			return bitmap;
		} else {
			return null;
		}

	}

}
