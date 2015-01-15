package com.litchi.iguardian;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.TextView;
//import com.litchi.iguardian.ComposeMessage.Composesend;

//import android.support.v7.appcompat.R;

public class InboxView extends ActionBarActivity {

	TextView mes, nam, mes_sub, dat, message_view;
	EditText reply;
	ProgressDialog Dialog;
	Button button_send_message;
	Context context;
	String message, name, message_subject, date, message_id, username, school_db;
	ListView assignments;

	HttpPost httppost;
	StringBuffer buffer;
	HttpResponse response;
	HttpClient httpclient;
	List<NameValuePair> nameValuePairs;
	ProgressDialog dialog = null;
	String[] assign;

	SessionManagement session;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.custom_inbox_view);
		context = InboxView.this;
		// getActionBar().setTitle("Compose Message");

		session = new SessionManagement(getApplicationContext());

		HashMap<String, String> user = session.getUserDetails();
		username = user.get(SessionManagement.KEY_NAME);
		school_db = user.get(SessionManagement.KEY_DB);
		// session = new SessionManagement(getApplicationContext());

		Intent intent = getIntent();
		message = intent.getStringExtra("message");
		date = intent.getStringExtra("date");
		message_subject = intent.getStringExtra("message_subject");
		name = intent.getStringExtra("name");
		message_id = intent.getStringExtra("message_id");

		mes = (TextView) findViewById(R.id.mes);
		mes_sub = (TextView) findViewById(R.id.message_subject);
		dat = (TextView) findViewById(R.id.date);
		nam = (TextView) findViewById(R.id.name);
		message_view = (TextView) findViewById(R.id.message);
		reply = (EditText) findViewById(R.id.reply_message);
		button_send_message = (Button) findViewById(R.id.send_message);

		nam.setText("" + name + "");
		dat.setText("" + date + "");
		mes_sub.setText("" + message_subject + "");
		mes.setText(message);
		getOverflowMenu();

		button_send_message.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (reply.getText().toString().trim().length() == 0) {
					userAlert();
				} else {

					new Inboxsend().execute();
				}
			}
		}); // b.setOnClickLi

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
			InboxView.this.finish();
			return true;
		}
		if (itemId == R.id.action_add_account) {
			Intent intent = new Intent(InboxView.this, LoginActivity.class);
			InboxView.this.startActivity(intent);

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

	public class Inboxsend extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			Log.i("hello", "helloq");
			sendData();
			return null;
		}

		@Override
		protected void onPostExecute(String result) {

			dialog.dismiss();
			reply.setText("");
		}

		@Override
		protected void onPreExecute() {
			Log.i("hello", "hellov");
			dialog = ProgressDialog.show(context, "", "Please Wait..", true);

		}

		@Override
		protected void onProgressUpdate(Void... values) {
		}

		void sendData() {

			try { /*
				 * dialog = ProgressDialog.show(LoginActivity.this, "",
				 * "Validating user...", true); runOnUiThread(new Runnable() {
				 * public void run() { //tv.setText(response);
				 * //dialog.dismiss(); dialog =
				 * ProgressDialog.show(Assignment.this, "",
				 * "Validating user...", true); } });
				 */
				Log.i("hello", "hello");
				httpclient = new DefaultHttpClient();
				httppost = new HttpPost(
						"http://npaneer.iguardianerp.co.in/message_reply.php");
				Log.i("hello", "hello2");
				nameValuePairs = new ArrayList<NameValuePair>(5);
				// tv.setText("vhpppi");

				nameValuePairs
						.add(new BasicNameValuePair("username", username)); // $Edittext_value
																			// =
																			// $_POST['Edittext_value'];
				// nameValuePairs.add(new
				// BasicNameValuePair("schoolName",schoolName));
				// tv.setText("vhiccc");
				nameValuePairs.add(new BasicNameValuePair("dbSelected",
						school_db));
				nameValuePairs.add(new BasicNameValuePair("message", reply
						.getText().toString().trim()));
				nameValuePairs.add(new BasicNameValuePair("message_subject",
						message_subject));
				nameValuePairs.add(new BasicNameValuePair("message_id",
						message_id));
				// nameValuePairs.add(new BasicNameValuePair("read_flag","N"));
				Log.i("hello", "hello3");
				// tv.setText("vhisjdskdj");
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				// Execute HTTP Post Request
				// response=httpclient.execute(httppost);
				Log.i("hello", "hello4");
				HttpResponse response = httpclient.execute(httppost);
				Log.i("hello", "hello5");
				HttpEntity entity = response.getEntity();
				// isr = entity.getContent();
				Log.i("hello", "hello6");
				// Toast.makeText(MessageView.this, "Message Sent",
				// Toast.LENGTH_SHORT).show();
				replyAlert2();
			}

			// try terminated

			catch (Exception e) {
				// dialog.dismiss();
				// System.out.println("Exception : " + e.printStackTrace());
				e.printStackTrace();
				// mes.setText("good");
				replyAlert();
				// Toast.makeText(MessageView.this, "",
				// Toast.LENGTH_SHORT).show();
			}

		}

	}

	public void userAlert() {
		InboxView.this.runOnUiThread(new Runnable() {
			public void run() {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						InboxView.this);
				builder.setTitle("Error.");
				builder.setMessage("Message Can't be empty")
						.setCancelable(false)
						.setPositiveButton("OK",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
									}
								});
				AlertDialog alert = builder.create();
				alert.show();
			}
		}); // on click terminated
	}//

	public void replyAlert() {
		InboxView.this.runOnUiThread(new Runnable() {
			public void run() {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						InboxView.this);
				builder.setTitle("Message not sent");
				builder.setMessage("Check Internet connection")
						.setCancelable(false)
						.setPositiveButton("OK",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
									}
								});
				AlertDialog alert = builder.create();
				alert.show();
			}
		}); // on click terminated
	}//

	public void replyAlert2() {
		InboxView.this.runOnUiThread(new Runnable() {
			public void run() {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						InboxView.this);
				builder.setTitle("Status");
				builder.setMessage("Message Sent")
						.setCancelable(false)
						.setPositiveButton("OK",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
									}
								});
				AlertDialog alert = builder.create();
				alert.show();
			}
		}); // on click terminated
	}//

}
