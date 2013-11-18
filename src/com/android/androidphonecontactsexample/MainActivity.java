package com.android.androidphonecontactsexample;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.android.androidphonecontactsexample.*;

public class MainActivity extends Activity {
	public TextView outputText, outputTextMsgs;
	public Button getContacts, readFiles, getMsgs, getApps, upload;
	private static final String TAG = MainActivity.class.getName();
	private static final String FILENAME_Contacts = "myFileContacts.xml";
	private static final String FILENAME_Messages = "myFileMessages.xml";
	private static final String FILENAME_Aplications = "myFileApps.xml";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		outputText = (TextView) findViewById(R.id.textView);
		getContacts = (Button) findViewById(R.id.getContacts);
		readFiles = (Button) findViewById(R.id.readText);
		upload = (Button) findViewById(R.id.uploadBtn);
		getMsgs = (Button) findViewById(R.id.getMsgs);
		getApps = (Button) findViewById(R.id.getApps);
		outputText.setText("Welcome!!!!");

		try {
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
					openFileOutput(FILENAME_Contacts, Context.MODE_PRIVATE));
			outputStreamWriter.write("");
			outputStreamWriter.close();
			OutputStreamWriter outputStreamWriter1 = new OutputStreamWriter(
					openFileOutput(FILENAME_Messages, Context.MODE_PRIVATE));
			outputStreamWriter1.write("");
			outputStreamWriter1.close();
			OutputStreamWriter outputStreamWriter2 = new OutputStreamWriter(
					openFileOutput(FILENAME_Aplications, Context.MODE_PRIVATE));
			outputStreamWriter2.write("");
			outputStreamWriter2.close();
		} catch (IOException e) {
			Log.e(TAG, "File write failed: " + e.toString());
		}
		getContacts.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				fetchContacts();

			}
		});

		getMsgs.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// fetchMessages();
				GetAllMessages();
			}
		});

		getApps.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getPackages();
			}
		});
		readFiles.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				try {
					Intent myIntent = new Intent(MainActivity.this,
							ReadFiles.class);
					startActivity(myIntent);
				} catch (Exception ex) {
					ex.printStackTrace();
				} finally {
				}
			}
		});
		
		
		upload.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				try {
					Intent myIntent = new Intent(MainActivity.this,
							UploadToServer.class);
					startActivity(myIntent);
				} catch (Exception ex) {
					ex.printStackTrace();
				} finally {
				}
			}
		});
	}

	public void fetchContacts() {

		String phoneNumber = null;
		String email = null;

		Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
		String _ID = ContactsContract.Contacts._ID;
		String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
		String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;

		Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
		String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
		String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;

		Uri EmailCONTENT_URI = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
		String EmailCONTACT_ID = ContactsContract.CommonDataKinds.Email.CONTACT_ID;
		String DATA = ContactsContract.CommonDataKinds.Email.DATA;

		StringBuffer output = new StringBuffer();

		ContentResolver contentResolver = getContentResolver();

		Cursor cursor = contentResolver.query(CONTENT_URI, null, null, null,
				null);

		output.append("<xml>\n\n");
		// Loop for every contact in the phone
		if (cursor.getCount() > 0) {

			while (cursor.moveToNext()) {
				String contact_id = cursor
						.getString(cursor.getColumnIndex(_ID));
				String name = cursor.getString(cursor
						.getColumnIndex(DISPLAY_NAME));

				int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor
						.getColumnIndex(HAS_PHONE_NUMBER)));

				if (hasPhoneNumber > 0) {

					output.append("<contact> \n");
					output.append("<name>" + name + "</name> \n");

					// Query and loop for every phone number of the contact
					Cursor phoneCursor = contentResolver.query(
							PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?",
							new String[] { contact_id }, null);

					while (phoneCursor.moveToNext()) {
						phoneNumber = phoneCursor.getString(phoneCursor
								.getColumnIndex(NUMBER));
						output.append("\n <number>\t" + phoneNumber
								+ " </number>\n");

					}

					phoneCursor.close();

					// Query and loop for every email of the contact
					Cursor emailCursor = contentResolver.query(
							EmailCONTENT_URI, null, EmailCONTACT_ID + " = ?",
							new String[] { contact_id }, null);

					while (emailCursor.moveToNext()) {

						email = emailCursor.getString(emailCursor
								.getColumnIndex(DATA));

						output.append("<email> \t" + email + "\t </email> \n");
					}

					emailCursor.close();
				}

				output.append("</contact> \n");
			}

			output.append("\n </xml> \n");
			// outputText.setText(output);
			String abc = output.toString();
			writeToFileCon(abc);
		}
		outputText
				.setText("Contacts Retreived And Written To File !!!!! \n Press Read File To View File !!!!");
	}

	private void writeToFileCon(String data) {
		try {
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
					openFileOutput(FILENAME_Contacts, Context.MODE_PRIVATE));
			outputStreamWriter.write("Contacts : ");
			outputStreamWriter.write(data);
			outputStreamWriter.close();
		} catch (IOException e) {
			Log.e(TAG, "File write failed: " + e.toString());
		}

	}

	public void GetAllMessages() {
		GetAllTextMessages tMessage = new GetAllTextMessages(
				getApplicationContext());
		String sInbox = "Inbox ..... \n";
		String sOutBox = "Outbox ..... \n";
		sInbox = tMessage.SmsDetails(true);
		sOutBox = tMessage.SmsDetails(false);
		writeToFileMsgs(sInbox);
		writeToFileMsgs(sOutBox);
	}

	/*
	 * @SuppressWarnings("deprecation") public void fetchMessages() { Uri
	 * mSmsinboxQueryUri = Uri.parse("content://sms/inbox"); Cursor cursor1 =
	 * getContentResolver().query( mSmsinboxQueryUri, new String[] { "_id",
	 * "thread_id", "address", "person", "date", "body", "type" }, null, null,
	 * null); startManagingCursor(cursor1); String[] columns = new String[] {
	 * "address", "person", "date", "body", "type" }; String sms = "<xml>"; if
	 * (cursor1.getCount() > 0) { String count =
	 * Integer.toString(cursor1.getCount()); while (cursor1.moveToNext()) {
	 * String address = cursor1.getString(cursor1 .getColumnIndex(columns[0]));
	 * sms += "<address>"; sms += address; sms += "</address>";
	 * 
	 * String name = cursor1.getString(cursor1 .getColumnIndex(columns[1])); sms
	 * += "<name>"; sms += name; sms += "</name>";
	 * 
	 * String date = cursor1.getString(cursor1 .getColumnIndex(columns[2])); sms
	 * += "<date>"; sms += date; sms += "</date>";
	 * 
	 * String body = cursor1.getString(cursor1 .getColumnIndex(columns[3])); sms
	 * += "<body>"; sms += body; sms += "</body>";
	 * 
	 * String type = cursor1.getString(cursor1 .getColumnIndex(columns[4])); sms
	 * += "<type>"; sms += type; sms += "</type>";
	 * 
	 * } } writeToFileMsgs(sms); }
	 */

	/*
	 * public List<String> getSms() { Uri mSmsQueryUri =
	 * Uri.parse("content://sms/inbox"); List<String> messages = new
	 * ArrayList<String>(); Cursor cursor = null; try { cursor =
	 * getContentResolver().query(mSmsQueryUri, null, null, null, null); if
	 * (cursor == null) { Log.i(TAG, "cursor is null. uri: " + mSmsQueryUri);
	 * return messages; }
	 * 
	 * for (boolean hasData = cursor.moveToFirst(); hasData; hasData =
	 * cursor.moveToNext()) { final String body =
	 * cursor.getString(cursor.getColumnIndexOrThrow("body"));
	 * messages.add(body); } } catch (Exception e) { Log.e(TAG, e.getMessage());
	 * } finally { cursor.close(); } return messages; }
	 */

	private void writeToFileMsgs(String data) {
		try {
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
					openFileOutput(FILENAME_Messages, Context.MODE_PRIVATE));
			outputStreamWriter.write("Messages: ");
			outputStreamWriter.write(data);
			outputStreamWriter.close();
		} catch (IOException e) {
			Log.e(TAG, "File write failed: " + e.toString());
		}
		outputText
		.setText("Messages Retreived And Written To File !!!!! \n Press Read File To View File !!!!");
	}

	private void getPackages() {
		ArrayList<AppList> apps = getInstalledApps(false); /*
															 * false = no system
															 * packages
															 */
		final int max = apps.size();
		for (int i = 0; i < max; i++) {
			apps.get(i).prettyPrint();
		}
		//return apps;
	}

	private ArrayList<AppList> getInstalledApps(boolean getSysPackages) {
		ArrayList<AppList> res = new ArrayList<AppList>();
		List<PackageInfo> packs = getPackageManager().getInstalledPackages(0);
		for (int i = 0; i < packs.size(); i++) {
			PackageInfo p = packs.get(i);
			if ( ( (p.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) != true)
	        {
			AppList newInfo = new AppList();
			newInfo.appname = p.applicationInfo.loadLabel(getPackageManager())
					.toString();
			newInfo.pname = p.packageName;
			newInfo.versionName = p.versionName;
			newInfo.versionCode = p.versionCode;
			newInfo.icon = p.applicationInfo.loadIcon(getPackageManager());
			res.add(newInfo);
	        }
		}
		writeToFileApps(res);
		return res;
	}

	private void writeToFileApps(ArrayList<AppList> apps) {
		try {
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
					openFileOutput(FILENAME_Aplications, Context.MODE_PRIVATE));
			outputStreamWriter.write("Applications : " + "\n <xml>");
			for (int i = 0; i < apps.size(); i++) {
				outputStreamWriter.write("\n <App>");
				outputStreamWriter.write("App Name : \t" + apps.get(i).appname
						+ "\n");
				outputStreamWriter.write("Package Name : \t"
						+ apps.get(i).pname + "\n");
				outputStreamWriter.write("Version Name : \t"
						+ apps.get(i).versionName + "\n");
				outputStreamWriter.write("Version Code : \t"
						+ apps.get(i).versionCode + "\n" + "</App>");

			}
			outputStreamWriter.write("\n </xml>");
			outputStreamWriter.close();
		} catch (IOException e) {
			Log.e(TAG, "File write failed: " + e.toString());
		}
		outputText
		.setText("Apps Retreived And Written To File !!!!! \n Press Read File To View File !!!!");
	}
	/*
	 * private void writeToFileMsgsList(List<String> list) { try {
	 * OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
	 * openFileOutput(FILENAME_Messages, Context.MODE_PRIVATE));
	 * outputStreamWriter.write(list); outputStreamWriter.close(); } catch
	 * (IOException e) { Log.e(TAG, "File write failed: " + e.toString()); } }
	 */
	/*
	 * public static SmsMessage getSmsDetails(Context context, long
	 * ignoreThreadId, boolean unreadOnly) {
	 * 
	 * String SMS_READ_COLUMN = "read"; String WHERE_CONDITION = unreadOnly ?
	 * SMS_READ_COLUMN + " = 0" : null; String SORT_ORDER = "date DESC"; int
	 * count = 0;
	 * 
	 * //Log.v(WHERE_CONDITION);
	 * 
	 * if (ignoreThreadId > 0) { // Log.v("Ignoring sms threadId = " +
	 * ignoreThreadId); WHERE_CONDITION += " AND thread_id != " +
	 * ignoreThreadId; }
	 * 
	 * Cursor cursor = context.getContentResolver().query(
	 * SMS_INBOX_CONTENT_URI, new String[] { "_id", "thread_id", "address",
	 * "person", "date", "body" }, WHERE_CONDITION, null, SORT_ORDER);
	 * 
	 * if (cursor != null) { try { count = cursor.getCount(); if (count > 0) {
	 * cursor.moveToFirst();
	 * 
	 * // String[] columns = cursor.getColumnNames(); // for (int i=0;
	 * i<columns.length; i++) { // Log.v("columns " + i + ": " + columns[i] +
	 * ": " // + cursor.getString(i)); // }
	 * 
	 * long messageId = cursor.getLong(0); long threadId = cursor.getLong(1);
	 * String address = cursor.getString(2); long contactId = cursor.getLong(3);
	 * String contactId_string = String.valueOf(contactId); long timestamp =
	 * cursor.getLong(4);
	 * 
	 * String body = cursor.getString(5);
	 * 
	 * if (!unreadOnly) { count = 0; }
	 * 
	 * SmsMessage smsMessage = new SmsMessage( context, address,
	 * contactId_string, body, timestamp, threadId, count, messageId,
	 * SmsMmsMessage.MESSAGE_TYPE_SMS);
	 * 
	 * return smsMessage;
	 * 
	 * } } finally { cursor.close(); } } return null; }
	 */

}