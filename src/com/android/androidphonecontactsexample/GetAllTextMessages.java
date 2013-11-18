package com.android.androidphonecontactsexample;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.telephony.PhoneNumberUtils;
import android.content.ContentResolver;

public class GetAllTextMessages {

	private Context mContext;

	public GetAllTextMessages(Context context) {
		mContext = context;
	}

	public static final Uri SMS_CONTENT_URI = Uri.parse("content://sms");
	public static final Uri SMS_INBOX_CONTENT_URI = Uri.withAppendedPath(
			SMS_CONTENT_URI, "all");
	public static final Uri SMS_Inbox = Uri.parse("content://sms/inbox");
	public static final Uri SMS_Failed = Uri.parse("content://sms/failed");
	public static final Uri SMS_Queued = Uri.parse("content://sms/queued");
	public static final Uri SMS_Sent = Uri.parse("content://sms/sent");
	public static final Uri SMS_Draft = Uri.parse("content://sms/draft");
	public static final Uri SMS_Outbox = Uri.parse("content://sms/outbox");
	public static final Uri SMS_Undelivered = Uri
			.parse("content://sms/undelivered");
	public static final Uri SMS_All = Uri.parse("content://sms/all");
	public static final Uri SMS_Conversations = Uri
			.parse("content://sms/conversations");

	public String SmsDetails(boolean inbox) {
		StringBuilder sb = new StringBuilder();
		
		try {
			String sMessageType = "0";
			String SMS_READ_COLUMN = "read";
			String SORT_ORDER = " _id ASC";
			int count = 0;
			Cursor cursor;
			if (inbox == true) {
				cursor = mContext.getContentResolver().query(
						SMS_Inbox,
						new String[] { "_id", "thread_id", "address", "person",
								"date", "body" },
						" _id &gt; " + String.valueOf("0"), null, SORT_ORDER);
				sMessageType = "1";
			} else {
				cursor = mContext.getContentResolver().query(
						SMS_Sent,
						new String[] { "_id", "thread_id", "address", "person",
								"date", "body" },
						" _id &gt; " + String.valueOf("0"), null, SORT_ORDER);
				sMessageType = "2";
			}
			String sDetail = "";
			int iCnt = 0;
			int iLimit = 1000;
			if (cursor != null) {
				try {
					if (cursor.moveToFirst()) {
						String body = "";
						do {
							iCnt = iCnt + 1;
							if (iCnt > iLimit) {
								break;
							}
							long messageId = cursor.getLong(0);
							long threadId = cursor.getLong(1);
							String address = cursor.getString(2);
							long contactId = cursor.getLong(3);
							String contactId_string = String.valueOf(contactId);
							long timestamp = cursor.getLong(4);
							String sBody = cursor.getString(5);
							sDetail = "";
							sDetail += String.valueOf(messageId) + "^";// messageId
							sDetail += String.valueOf(threadId) + "^";// threadId
							sDetail += address + "^";// address
							sDetail += String.valueOf(contactId) + "^";// contactId
							sDetail += GetPersonNameDetails(contactId_string,
									address) + "^";//
							sDetail += String.valueOf(timestamp) + "^";// timestamp
							sDetail += sBody + "^";// body
							sDetail += sMessageType + "^";// Message Type
							sb.append(sDetail + "|");
						} while (cursor.moveToNext());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				cursor.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// return null;
		return sb.toString();
	}

	public String GetPersonNameDetails(String ID, String address) {
		String sReturn = "UNK";
		try {
			sReturn = PhoneNumberUtils.formatNumber(address);
			Uri contactUri = ContentUris.withAppendedId(
					ContactsContract.Contacts.CONTENT_URI, Integer.valueOf(ID));
			Cursor cursor = mContext
					.getContentResolver()
					.query(contactUri,
							new String[] { ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME },
							null, null, null);
			if (cursor != null) {
				if (cursor.getCount() > 0) {
					cursor.moveToFirst();
					String name = cursor.getString(0);
					sReturn = name;
				}
				cursor.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sReturn;
	}
}