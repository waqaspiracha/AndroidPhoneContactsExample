package com.android.androidphonecontactsexample;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;



public class UploadToServer extends Activity {

	TextView messageText;
	Button uploadButtonMsgs, uploadButtonConts, uploadButtonApps;
	int serverResponseCode = 0;
	ProgressDialog dialog = null;

	String upLoadServerUri = null;

	/********** File Path *************/
	

	final String uploadFilePath = "data/data/com.android.androidphonecontactsexample/files/";
	final String uploadFileNameCont = "myFileContacts.xml";
	final String uploadFileNameMsgs = "myFileMessages.xml";
	final String uploadFileNameApps = "myFileApps.xml";

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upload_to_server);

		uploadButtonMsgs = (Button) findViewById(R.id.uploadButtonMsgs);
		uploadButtonConts = (Button) findViewById(R.id.uploadButtonConts);
		uploadButtonApps = (Button) findViewById(R.id.uploadButtonApps);
		messageText = (TextView) findViewById(R.id.messageText);

		/************* Php script path ****************/
		upLoadServerUri = "http://mimuz.com/androidd/upload.php";

		uploadButtonMsgs.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) {

				messageText.setText("Uploading file path :- " + uploadFilePath 
						+ uploadFileNameMsgs);
				dialog = ProgressDialog.show(UploadToServer.this, "",
						"Uploading file...", true);

				new Thread(new Runnable() {
					public void run() {
						runOnUiThread(new Runnable() {
							public void run() {
								messageText.setText("uploading started.....");
							}
						});

						uploadFileMsgs(uploadFilePath + "" + uploadFileNameMsgs);

					}
				}).start();
			}
		});
		
		uploadButtonApps.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) {

				messageText.setText("Uploading file path :- " + uploadFilePath 
						+ uploadFileNameApps);
				dialog = ProgressDialog.show(UploadToServer.this, "",
						"Uploading file...", true);

				new Thread(new Runnable() {
					public void run() {
						runOnUiThread(new Runnable() {
							public void run() {
								messageText.setText("uploading started.....");
							}
						});

						uploadFileApps(uploadFilePath + "" + uploadFileNameApps);

					}
				}).start();
			}
		});
		
		uploadButtonConts.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) {

				messageText.setText("Uploading file path :- " + uploadFilePath 
						+ uploadFileNameCont);
				dialog = ProgressDialog.show(UploadToServer.this, "",
						"Uploading file...", true);

				new Thread(new Runnable() {
					public void run() {
						runOnUiThread(new Runnable() {
							public void run() {
								messageText.setText("uploading started.....");
							}
						});

						uploadFileConts(uploadFilePath + "" + uploadFileNameCont);

					}
				}).start();
			}
		});
	}
	
	
	public int uploadFileConts(String sourceFileUri) {

		
		String fileName = sourceFileUri;

		HttpURLConnection conn = null;
		DataOutputStream dos = null;
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 1 * 1024 * 1024;
		File sourceFile = new File(sourceFileUri);

		if (!sourceFile.isFile()) {

			dialog.dismiss();

			Log.e("uploadFile", "Source File not exist :" + uploadFilePath + ""
					+ uploadFileNameCont);

			runOnUiThread(new Runnable() {
				public void run() {
					messageText.setText("Source File not exist :"
							+ uploadFilePath + "" + uploadFileNameCont);
				}
			});

			return 0;

		} else {
			try {

				// open a URL connection to the Servlet
				FileInputStream fileInputStream = new FileInputStream(
						sourceFile);
				URL url = new URL(upLoadServerUri);

				// Open a HTTP connection to the URL
				conn = (HttpURLConnection) url.openConnection();
				conn.setDoInput(true); // Allow Inputs
				conn.setDoOutput(true); // Allow Outputs
				conn.setUseCaches(false); // Don't use a Cached Copy
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Connection", "Keep-Alive");
				conn.setRequestProperty("ENCTYPE", "multipart/form-data");
				conn.setRequestProperty("Content-Type",
						"multipart/form-data;boundary=" + boundary);
				conn.setRequestProperty("uploaded_file", fileName);

				dos = new DataOutputStream(conn.getOutputStream());

				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
						+ fileName + "\"" + lineEnd);

				dos.writeBytes(lineEnd);

				// create a buffer of maximum size
				bytesAvailable = fileInputStream.available();

				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				buffer = new byte[bufferSize];

				// read file and write it into form...
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);

				while (bytesRead > 0) {

					dos.write(buffer, 0, bufferSize);
					bytesAvailable = fileInputStream.available();
					bufferSize = Math.min(bytesAvailable, maxBufferSize);
					bytesRead = fileInputStream.read(buffer, 0, bufferSize);

				}

				// send multipart form data necesssary after file data...
				dos.writeBytes(lineEnd);
				dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

				// Responses from the server (code and message)
				serverResponseCode = conn.getResponseCode();
				String serverResponseMessage = conn.getResponseMessage();

				Log.i("uploadFile", "HTTP Response is : "
						+ serverResponseMessage + ": " + serverResponseCode);

				if (serverResponseCode == 200) {

					runOnUiThread(new Runnable() {
						public void run() {

							String msg = "File Upload Completed.\n\n See uploaded file here : \n\n"
									+ " http://mimuz.com/android/upload.php/"
									+ uploadFileNameCont;

							messageText.setText(msg);
							Toast.makeText(UploadToServer.this,
									"File Upload Complete.", Toast.LENGTH_SHORT)
									.show();
						}
					});
				}

				// close the streams //
				fileInputStream.close();
				dos.flush();
				dos.close();

			} catch (MalformedURLException ex) {

				dialog.dismiss();
				ex.printStackTrace();

				runOnUiThread(new Runnable() {
					public void run() {
						messageText
								.setText("MalformedURLException Exception : check script url.");
						Toast.makeText(UploadToServer.this,
								"MalformedURLException", Toast.LENGTH_SHORT)
								.show();
					}
				});

				Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
			} catch (Exception e) {

				dialog.dismiss();
				e.printStackTrace();

				runOnUiThread(new Runnable() {
					public void run() {
						messageText.setText("Got Exception : see logcat ");
						Toast.makeText(UploadToServer.this,
								"Got Exception : see logcat ",
								Toast.LENGTH_SHORT).show();
					}
				});
				Log.e("Upload file to server Exception",
						"Exception : " + e.getMessage(), e);
			}
			dialog.dismiss();
			return serverResponseCode;

		} // End else block

	}
	
	

	public int uploadFileMsgs(String sourceFileUri) {

		
		String fileName = sourceFileUri;

		HttpURLConnection conn = null;
		DataOutputStream dos = null;
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 1 * 1024 * 1024;
		File sourceFile = new File(sourceFileUri);

		if (!sourceFile.isFile()) {

			dialog.dismiss();

			Log.e("uploadFile", "Source File not exist :" + uploadFilePath + ""
					+ uploadFileNameMsgs);

			runOnUiThread(new Runnable() {
				public void run() {
					messageText.setText("Source File not exist :"
							+ uploadFilePath + "" + uploadFileNameMsgs);
				}
			});

			return 0;

		} else {
			try {

				// open a URL connection to the Servlet
				FileInputStream fileInputStream = new FileInputStream(
						sourceFile);
				URL url = new URL(upLoadServerUri);

				// Open a HTTP connection to the URL
				conn = (HttpURLConnection) url.openConnection();
				conn.setDoInput(true); // Allow Inputs
				conn.setDoOutput(true); // Allow Outputs
				conn.setUseCaches(false); // Don't use a Cached Copy
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Connection", "Keep-Alive");
				conn.setRequestProperty("ENCTYPE", "multipart/form-data");
				conn.setRequestProperty("Content-Type",
						"multipart/form-data;boundary=" + boundary);
				conn.setRequestProperty("uploaded_file", fileName);

				dos = new DataOutputStream(conn.getOutputStream());

				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
						+ fileName + "\"" + lineEnd);

				dos.writeBytes(lineEnd);

				// create a buffer of maximum size
				bytesAvailable = fileInputStream.available();

				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				buffer = new byte[bufferSize];

				// read file and write it into form...
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);

				while (bytesRead > 0) {

					dos.write(buffer, 0, bufferSize);
					bytesAvailable = fileInputStream.available();
					bufferSize = Math.min(bytesAvailable, maxBufferSize);
					bytesRead = fileInputStream.read(buffer, 0, bufferSize);

				}

				// send multipart form data necesssary after file data...
				dos.writeBytes(lineEnd);
				dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

				// Responses from the server (code and message)
				serverResponseCode = conn.getResponseCode();
				String serverResponseMessage = conn.getResponseMessage();

				Log.i("uploadFile", "HTTP Response is : "
						+ serverResponseMessage + ": " + serverResponseCode);

				if (serverResponseCode == 200) {

					runOnUiThread(new Runnable() {
						public void run() {

							String msg = "File Upload Completed.\n\n See uploaded file here : \n\n"
									+ " http://mimuz.com/android/upload.php/"
									+ uploadFileNameMsgs;

							messageText.setText(msg);
							Toast.makeText(UploadToServer.this,
									"File Upload Complete.", Toast.LENGTH_SHORT)
									.show();
						}
					});
				}

				// close the streams //
				fileInputStream.close();
				dos.flush();
				dos.close();

			} catch (MalformedURLException ex) {

				dialog.dismiss();
				ex.printStackTrace();

				runOnUiThread(new Runnable() {
					public void run() {
						messageText
								.setText("MalformedURLException Exception : check script url.");
						Toast.makeText(UploadToServer.this,
								"MalformedURLException", Toast.LENGTH_SHORT)
								.show();
					}
				});

				Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
			} catch (Exception e) {

				dialog.dismiss();
				e.printStackTrace();

				runOnUiThread(new Runnable() {
					public void run() {
						messageText.setText("Got Exception : see logcat ");
						Toast.makeText(UploadToServer.this,
								"Got Exception : see logcat ",
								Toast.LENGTH_SHORT).show();
					}
				});
				Log.e("Upload file to server Exception",
						"Exception : " + e.getMessage(), e);
			}
			dialog.dismiss();
			return serverResponseCode;

		} // End else block

	}
	
	

	public int uploadFileApps(String sourceFileUri) {

		
		String fileName = sourceFileUri;

		HttpURLConnection conn = null;
		DataOutputStream dos = null;
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 1 * 1024 * 1024;
		File sourceFile = new File(sourceFileUri);

		if (!sourceFile.isFile()) {

			dialog.dismiss();

			Log.e("uploadFile", "Source File not exist :" + uploadFilePath + ""
					+ uploadFileNameApps);

			runOnUiThread(new Runnable() {
				public void run() {
					messageText.setText("Source File not exist :"
							+ uploadFilePath + "" + uploadFileNameApps);
				}
			});

			return 0;

		} else {
			try {

				// open a URL connection to the Servlet
				FileInputStream fileInputStream = new FileInputStream(
						sourceFile);
				URL url = new URL(upLoadServerUri);

				// Open a HTTP connection to the URL
				conn = (HttpURLConnection) url.openConnection();
				conn.setDoInput(true); // Allow Inputs
				conn.setDoOutput(true); // Allow Outputs
				conn.setUseCaches(false); // Don't use a Cached Copy
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Connection", "Keep-Alive");
				conn.setRequestProperty("ENCTYPE", "multipart/form-data");
				conn.setRequestProperty("Content-Type",
						"multipart/form-data;boundary=" + boundary);
				conn.setRequestProperty("uploaded_file", fileName);

				dos = new DataOutputStream(conn.getOutputStream());

				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
						+ fileName + "\"" + lineEnd);

				dos.writeBytes(lineEnd);

				// create a buffer of maximum size
				bytesAvailable = fileInputStream.available();

				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				buffer = new byte[bufferSize];

				// read file and write it into form...
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);

				while (bytesRead > 0) {

					dos.write(buffer, 0, bufferSize);
					bytesAvailable = fileInputStream.available();
					bufferSize = Math.min(bytesAvailable, maxBufferSize);
					bytesRead = fileInputStream.read(buffer, 0, bufferSize);

				}

				// send multipart form data necesssary after file data...
				dos.writeBytes(lineEnd);
				dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

				// Responses from the server (code and message)
				serverResponseCode = conn.getResponseCode();
				String serverResponseMessage = conn.getResponseMessage();

				Log.i("uploadFile", "HTTP Response is : "
						+ serverResponseMessage + ": " + serverResponseCode);

				if (serverResponseCode == 200) {

					runOnUiThread(new Runnable() {
						public void run() {

							String msg = "File Upload Completed.\n\n See uploaded file here : \n\n"
									+ " http://mimuz.com/android/upload.php/"
									+ uploadFileNameApps;

							messageText.setText(msg);
							Toast.makeText(UploadToServer.this,
									"File Upload Complete.", Toast.LENGTH_SHORT)
									.show();
						}
					});
				}

				// close the streams //
				fileInputStream.close();
				dos.flush();
				dos.close();

			} catch (MalformedURLException ex) {

				dialog.dismiss();
				ex.printStackTrace();

				runOnUiThread(new Runnable() {
					public void run() {
						messageText
								.setText("MalformedURLException Exception : check script url.");
						Toast.makeText(UploadToServer.this,
								"MalformedURLException", Toast.LENGTH_SHORT)
								.show();
					}
				});

				Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
			} catch (Exception e) {

				dialog.dismiss();
				e.printStackTrace();

				runOnUiThread(new Runnable() {
					public void run() {
						messageText.setText("Got Exception : see logcat ");
						Toast.makeText(UploadToServer.this,
								"Got Exception : see logcat ",
								Toast.LENGTH_SHORT).show();
					}
				});
				Log.e("Upload file to server Exception",
						"Exception : " + e.getMessage(), e);
			}
			dialog.dismiss();
			return serverResponseCode;

		} // End else block

	}

}

/*
 * HttpURLConnection connection = null; DataOutputStream outputStream = null;
 * DataInputStream inputStream = null;
 * 
 * String pathToOurFile = "path of the image.jpeg"; String urlServer =
 * "http://mimuz.com/android/upload.php"; String lineEnd = "\r\n"; String
 * twoHyphens = "--"; String boundary = "*****";
 * 
 * int bytesRead, bytesAvailable, bufferSize; byte[] buffer; int maxBufferSize =
 * 1*1024*1024; {
 * 
 * 
 * try { FileInputStream fileInStream = new FileInputStream(new
 * File(pathToOurFile) );
 * 
 * URL url = new URL(urlServer); connection = (HttpURLConnection)
 * url.openConnection();
 * 
 * // Allow Inputs & Outputs connection.setDoInput(true);
 * connection.setDoOutput(true); connection.setUseCaches(false);
 * 
 * // Enable POST method connection.setRequestMethod("POST");
 * 
 * connection.setRequestProperty("Connection", "Keep-Alive");
 * connection.setRequestProperty("Content-Type",
 * "multipart/form-data;boundary="+boundary);
 * 
 * outputStream = new DataOutputStream( connection.getOutputStream() );
 * outputStream.writeBytes(twoHyphens + boundary + lineEnd);
 * outputStream.writeBytes
 * ("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" +
 * pathToOurFile +"\"" + lineEnd); outputStream.writeBytes(lineEnd);
 * 
 * bytesAvailable = fileInStream.available(); bufferSize =
 * Math.min(bytesAvailable, maxBufferSize); buffer = new byte[bufferSize];
 * 
 * // Read file bytesRead = fileInStream.read(buffer, 0, bufferSize);
 * 
 * while (bytesRead > 0) { outputStream.write(buffer, 0, bufferSize);
 * bytesAvailable = fileInStream.available(); bufferSize =
 * Math.min(bytesAvailable, maxBufferSize); bytesRead =
 * fileInStream.read(buffer, 0, bufferSize); }
 * 
 * outputStream.writeBytes(lineEnd); outputStream.writeBytes(twoHyphens +
 * boundary + twoHyphens + lineEnd);
 * 
 * // Responses from the server (code and message) serverResponseCode =
 * connection.getResponseCode(); serverResponseMessage =
 * connection.getResponseMessage();
 * 
 * fileInputStream.close(); outputStream.flush(); outputStream.close(); } catch
 * (Exception ex) { //Exception handling }
 */

