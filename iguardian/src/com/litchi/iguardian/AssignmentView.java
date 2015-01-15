package com.litchi.iguardian;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;

import android.app.Notification;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

public class AssignmentView extends ActionBarActivity {

	TextView assignment_subject, assignment_date, assignment_view;
	EditText reply;
	ProgressDialog Dialog;
	Button download_button;
	Context context;
	String i, assignment, assignment_link = "", date, subject, username,
			school;
	ListView assignments;
	ProgressDialog mProgressDialog;
	HttpPost httppost;
	StringBuffer buffer;
	HttpResponse response;
	HttpClient httpclient;
	List<NameValuePair> nameValuePairs;
	ProgressDialog dialog = null;
	String[] assign;
	String assignment_download_url;
	// int i ;

	SessionManagement session;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_assignment_view);
		// Log.i("break","1" );
		context = getApplicationContext();
		
	
		download_button = (Button) findViewById(R.id.download);
		assignment_subject = (TextView) findViewById(R.id.assignment_subject);
		assignment_date = (TextView) findViewById(R.id.assignment_date);
		assignment_view = (TextView) findViewById(R.id.assignment);
		Intent intent = getIntent();
		// Log.i("break","3" );
		subject = intent.getStringExtra("subject");
		date = intent.getStringExtra("date");
		assignment = intent.getStringExtra("assign");
		assignment_link = intent.getStringExtra("assignment_link");
		// i = intent.getStringExtra("i");

		// download_button.setVisibility(View.INVISIBLE);

		// Log.i("ass",assignment_link );
		if (assignment_link.equals("")) {

			download_button.setVisibility(View.INVISIBLE);

		}
		System.out.println(assignment);
		// message_id = intent.getStringExtra("message_id");
		// Log.i("break","4" );

		// assignment_ = (TextView) findViewById(R.id.name);
		// reply = (EditText) findViewById(R.id.reply_message);
		// Log.i("break","5" );

		assignment_subject.setText("" + subject + "");
		assignment_date.setText("" + date + "");
		assignment_view.setText("" + assignment + "");
		assignment_download_url = "http://npaneer.iguardianerp.co.in/"
				+ "dbms2.pdf";
		// Log.i("break","6" );
		// mes.setText(message);
		getOverflowMenu();
		// Log.i("break","7" );

		mProgressDialog = new ProgressDialog(AssignmentView.this);
		mProgressDialog.setMessage("Downloading...");
		mProgressDialog.setIndeterminate(true);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		mProgressDialog.setCancelable(true);

		download_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				final DownloadTask downloadTask = new DownloadTask(
						AssignmentView.this);
				downloadTask.execute(assignment_download_url);

				mProgressDialog
						.setOnCancelListener(new DialogInterface.OnCancelListener() {
							@Override
							public void onCancel(DialogInterface dialog) {
								downloadTask.cancel(true);
							}
						});

			}
		}); // b.setOnC
		// Log.i("break","8" );

	}

	private void getOverflowMenu() {

		try {
			ViewConfiguration config = ViewConfiguration.get(this);
			Field menuKeyField = ViewConfiguration.class
					.getDeclaredField("sHasPermanentMenuKey");
			if (menuKeyField != null) {
				menuKeyField.setAccessible(true);
				menuKeyField.setBoolean(config, false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_activity_actions, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		/*
		 * if(drawerListener.onOptionsItemSelected(item)) { return true; }
		 */

		int itemId = item.getItemId();
		if (itemId == R.id.action_logout) {
			session.logoutUser();
			AssignmentView.this.finish();
			return true;
		}
		if (itemId == R.id.action_add_account) {
			Intent intent = new Intent(AssignmentView.this, LoginActivity.class);
			AssignmentView.this.startActivity(intent);

		}


		return super.onOptionsItemSelected(item);

	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		// drawerListener.onConfigurationChanged(newConfig);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onPostCreate(savedInstanceState);
		// drawerListener.syncState();
	}

	private class DownloadTask extends AsyncTask<String, Integer, String> {

		private Context context;
		private PowerManager.WakeLock mWakeLock;

		public DownloadTask(Context context) {
			this.context = context;
			Log.i("break", "1");
		}

		@Override
		protected String doInBackground(String... sUrl) {
			InputStream input = null;
			OutputStream output = null;
			HttpURLConnection connection = null;
			Log.i("break", "5");
			try {
				URL url = new URL(sUrl[0]);
				connection = (HttpURLConnection) url.openConnection();
				connection.connect();
				Log.i("break", "6");
				// expect HTTP 200 OK, so we don't mistakenly save error report
				// instead of the file
				if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
					return "Server returned HTTP "
							+ connection.getResponseCode() + " "
							+ connection.getResponseMessage();
				}

				// this will be useful to display download percentage
				// might be -1: server did not report the length
				int fileLength = connection.getContentLength();

				// download the file
				input = connection.getInputStream();
				output = new FileOutputStream("/sdcard/dbms2.pdf");

				byte data[] = new byte[4096];
				long total = 0;
				int count;
				while ((count = input.read(data)) != -1) {
					// allow canceling with back button
					if (isCancelled()) {
						input.close();
						return null;
					}
					total += count;
					// publishing the progress....
					if (fileLength > 0) // only if total length is known
						publishProgress((int) (total * 100 / fileLength));
					output.write(data, 0, count);
				}
			} catch (Exception e) {
				return e.toString();
			} finally {
				try {
					if (output != null)
						output.close();
					if (input != null)
						input.close();
				} catch (IOException ignored) {
				}

				if (connection != null)
					connection.disconnect();
			}
			return null;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			Log.i("break", "2");
			// take CPU lock to prevent CPU from going off if the user
			// presses the power button during download
			PowerManager pm = (PowerManager) context
					.getSystemService(Context.POWER_SERVICE);
			mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
					getClass().getName());
			mWakeLock.acquire();
			Log.i("break", "3");
			mProgressDialog.show();
			Log.i("break", "4");
		}

		@Override
		protected void onProgressUpdate(Integer... progress) {
			super.onProgressUpdate(progress);
			Log.i("break", "oka");
			// if we get here, length is known, now set indeterminate to false
			mProgressDialog.setIndeterminate(false);
			mProgressDialog.setMax(100);
			mProgressDialog.setProgress(progress[0]);
		}

		@Override
		protected void onPostExecute(String result) {
			mWakeLock.release();
			mProgressDialog.dismiss();
			if (result != null) {
				Toast.makeText(context, "Download error: " + result,
						Toast.LENGTH_LONG).show();
				Log.d("error", result);
			} else
				Toast.makeText(context, "File downloaded", Toast.LENGTH_SHORT)
						.show();
		}
	}
	
	

}
