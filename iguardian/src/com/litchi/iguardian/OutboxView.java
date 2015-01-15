package com.litchi.iguardian;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewConfiguration;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

//import android.support.v7.appcompat.R;

public class OutboxView extends ActionBarActivity {

	TextView mes, nam, mes_sub, dat;
	EditText reply;
	ProgressDialog Dialog;
	Button button_send_message;

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
		setContentView(R.layout.custom_outbox_view);

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
		// reply = (EditText) findViewById(R.id.reply_message);
		// button_send_message = (Button) findViewById(R.id.send_message);

		nam.setText("" + name + "");
		dat.setText("" + date + "");
		mes_sub.setText("" + message_subject + "");
		mes.setText(message);
		getOverflowMenu();

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
			OutboxView.this.finish();
			return true;
		}
		if (itemId == R.id.action_add_account) {
			Intent intent = new Intent(OutboxView.this, LoginActivity.class);
			OutboxView.this.startActivity(intent);

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

}
