package com.android.androidphonecontactsexample;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class ReadFiles extends Activity {

	Button viewMsgs, viewContacts, viewApps;
	TextView viewFilesTxt;
	private static final String FILENAME_Contacts = "myFileContacts.xml";
	private static final String FILENAME_Messages = "myFileMessages.xml";
	private static final String FILENAME_Aplications = "myFileApps.xml";
	private static final String TAG = ReadFiles.class.getName();

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_files);
		viewMsgs = (Button) findViewById(R.id.viewMsgs);
		viewContacts = (Button) findViewById(R.id.viewContacts);
		viewApps = (Button) findViewById(R.id.viewApps);
		viewFilesTxt = (TextView) findViewById(R.id.viewFilesText);
		viewFilesTxt.setMovementMethod(new ScrollingMovementMethod());

		viewMsgs.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				viewFilesTxt.setText(readFromFileMsgs().toString());
			}
		});

		viewContacts.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				viewFilesTxt.setText(readFromFileCont().toString());

			}
		});

		viewApps.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				viewFilesTxt.setText(readFromFileApps().toString());
			}
		});
	}

	
	private String readFromFileCont() {

		String ret = "Contacts In File ............... \n\n";

		try {
			InputStream inputStream = openFileInput(FILENAME_Contacts);

			if (inputStream != null) {
				InputStreamReader inputStreamReader = new InputStreamReader(
						inputStream);
				BufferedReader bufferedReader = new BufferedReader(
						inputStreamReader);
				String receiveString = "";
				StringBuilder stringBuilder = new StringBuilder();

				while ((receiveString = bufferedReader.readLine()) != null) {
					stringBuilder.append(receiveString);
				}

				inputStream.close();
				ret += stringBuilder.toString();
			}
		} catch (FileNotFoundException e) {
			Log.e(TAG, "File not found: " + e.toString());
		} catch (IOException e) {
			Log.e(TAG, "Can not read file: " + e.toString());
		} finally {
		}
		return ret;
	}

	
	private String readFromFileMsgs() {
		String ret = "Messages In File ............... \n\n";
		try {
			InputStream inputStream1 = openFileInput(FILENAME_Messages);

			if (inputStream1 != null) {
				InputStreamReader inputStreamReader1 = new InputStreamReader(
						inputStream1);
				BufferedReader bufferedReader1 = new BufferedReader(
						inputStreamReader1);
				String receiveString1 = "";
				StringBuilder stringBuilder1 = new StringBuilder();

				while ((receiveString1 = bufferedReader1.readLine()) != null) {
					stringBuilder1.append(receiveString1);
				}
				inputStream1.close();
				ret += stringBuilder1.toString();
			}
		} catch (FileNotFoundException e) {
			Log.e(TAG, "File not found: " + e.toString());
		} catch (IOException e) {
			Log.e(TAG, "Can not read file: " + e.toString());
		}
		return ret;
	}

	
	private String readFromFileApps() {
		String ret = "Applications In File ............... \n\n";
		try {
			InputStream inputStream1 = openFileInput(FILENAME_Aplications);

			if (inputStream1 != null) {
				InputStreamReader inputStreamReader1 = new InputStreamReader(
						inputStream1);
				BufferedReader bufferedReader1 = new BufferedReader(
						inputStreamReader1);
				String receiveString1 = "";
				StringBuilder stringBuilder1 = new StringBuilder();

				while ((receiveString1 = bufferedReader1.readLine()) != null) {
					stringBuilder1.append(receiveString1);
				}
				inputStream1.close();
				ret += stringBuilder1.toString();
			}
		} catch (FileNotFoundException e) {
			Log.e(TAG, "File not found: " + e.toString());
		} catch (IOException e) {
			Log.e(TAG, "Can not read file: " + e.toString());
		}
		return ret;
	}
}
