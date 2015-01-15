package com.litchi.iguardian;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class Circulars extends Fragment {

	int result_check = 0;
	TextView tvItemName;
	ListView assignments;

	InputStream isr = null;
	String result = "";

	HttpPost httppost;
	StringBuffer buffer;
	HttpResponse response;
	HttpClient httpclient;
	List<NameValuePair> nameValuePairs;
	ArrayList<HashMap<String, String>> list =

	new ArrayList<HashMap<String, String>>();
	String[] assign;
	String school_db, username;
	SessionManagement session;
	ProgressDialog dialog;

	public Circulars() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.assignment, container, false);

		getActivity().setTitle("Circulars");

		assignments = (ListView) view.findViewById(R.id.listView1);

		session = new SessionManagement(getActivity());
		// StrictMode.enableDefaults();

		HashMap<String, String> user = session.getUserDetails();
		username = user.get(SessionManagement.KEY_NAME);
		school_db = user.get(SessionManagement.KEY_DB);

		// getData();
		new circulardata().execute();

		assignments
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int position, long arg3) {
						Intent intent = new Intent(getActivity(),
								CircularView.class);
						Log.i("df", "ok");

						HashMap<String, String> item = list.get(position);
						intent.putExtra("circular_title",
								item.get("circular_title").toString());
						intent.putExtra("date", item.get("date").toString());
						intent.putExtra("circular_text",
								item.get("circular_text").toString());
						intent.putExtra("circular_link",
								item.get("circular_link").toString());
						// intent.putExtra("message_id",
						// item.get("message_id").toString());
						getActivity().startActivity(intent);
					}
				});

		return view;
	}

	private class circulardata extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			getData();
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			if (result_check == 1) {
				SimpleAdapter adapter = new SimpleAdapter(getActivity(), list,
						R.layout.custom_circular_view, new String[] {
								"circular_title", "circular_text", "date" },
						new int[] { R.id.circular_title, R.id.circular_text,
								R.id.circular_date }

				);

				assignments.setAdapter(adapter);
			}

			dialog.dismiss();
		}

		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(getActivity(), "", "Please Wait..",
					true);

		}

		@Override
		protected void onProgressUpdate(Void... values) {
		}

	}

	void getData() {

		try {
			httpclient = new DefaultHttpClient();
			httppost = new HttpPost(
					"http://npaneer.iguardianerp.co.in/circulars.php");

			nameValuePairs = new ArrayList<NameValuePair>(2);

			nameValuePairs.add(new BasicNameValuePair("username", username));

			nameValuePairs.add(new BasicNameValuePair("dbSelected", school_db));

			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			isr = entity.getContent();

		} // try terminated

		catch (Exception e) {
			// dialog.dismiss();
			System.out.println("Exception : " + e.getMessage());
			InternetAlert();
		}
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					isr, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");

			}
			isr.close();

			result = sb.toString();

		} catch (Exception e) {
			Log.e("log_tag", "Error coverting result " + e.toString());

		}
		try {
			if (!result.trim().toString().equals("0"))
			{
				
				result_check = 1;

				JSONArray jArray = new JSONArray(result);

				for (int i = 0; i < jArray.length(); i++) {
					JSONObject json = jArray.getJSONObject(i);

					HashMap<String, String> temp = new HashMap<String, String>();

					temp.put("circular_title",
							"" + json.getString("CIRCULAR_TITLE") + "");
					temp.put("circular_text",
							"" + json.getString("CIRCULAR_TEXT") + "");
					temp.put("date", "" + json.getString("Date") + "");
					temp.put("circular_link",
							"" + json.getString("CIRCULAR_LINK") + "");
					list.add(temp);

				}
			}

		} catch (Exception e) {
			Log.e("log_tag", "Error Parsing data " + e.toString());
			DataAlert();
		}

	} // void

	public void InternetAlert() {
		getActivity().runOnUiThread(new Runnable() {
			public void run() {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getActivity());
				builder.setTitle("Connection Error");
				builder.setMessage("Check Internet Connection")
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

	public void DataAlert() {
		getActivity().runOnUiThread(new Runnable() {
			public void run() {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getActivity());
				builder.setTitle("Error");
				builder.setMessage("No Data Found")
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