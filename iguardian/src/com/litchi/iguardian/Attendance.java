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
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class Attendance extends Fragment {

	// ImageView ivIcon;
	int result_check = 0;
	InputStream isr = null;
	String result = "";
	String Stu_at;
	TextView tvItemName;
	ListView assignments;
	// TextView assignments;
	HttpPost httppost;
	StringBuffer buffer;
	HttpResponse response;
	HttpClient httpclient;
	List<NameValuePair> nameValuePairs;
	private ProgressDialog dialog = null;
	String[] assign;
	// Spinner spinner1;
	// publicString schoolSelected;
	SessionManagement session;
	String username, school_db;
	ArrayList<HashMap<String, String>> list =

	new ArrayList<HashMap<String, String>>();

	// public static final String IMAGE_RESOURCE_ID = "iconResourceID";
	// public static final String ITEM_NAME = "itemName";

	public Attendance() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.assignment, container, false);

		getActivity().setTitle("Attendance");

		// ivIcon = (ImageView) view.findViewById(R.id.frag1_icon);
		tvItemName = (TextView) view.findViewById(R.id.frag1_text);

		// tvItemName.setText("Hello");
		// ivIcon.setImageDrawable(view.getResources().getDrawable(
		// getArguments().getInt(IMAGE_RESOURCE_ID)));
		assignments = (ListView) view.findViewById(R.id.listView1);

		session = new SessionManagement(getActivity());
		// StrictMode.enableDefaults();

		HashMap<String, String> user = session.getUserDetails();
		username = user.get(SessionManagement.KEY_NAME);
		school_db = user.get(SessionManagement.KEY_DB);
		// name
		// String username = user.get(SessionManagement.KEY_NAME);
		// tvItemName.setText(school);
		// tvItemName.setText(username);
		// getData(school,username);
		new attendancedata().execute();

		return view;
	}

	private class attendancedata extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			getData(school_db, username);
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			if (result_check == 1) {
				SimpleAdapter adapter = new SimpleAdapter(getActivity(), list,
						R.layout.custom_assignment, new String[] {
								"attendance", "date" }, new int[] { R.id.text6,
								R.id.text5 }

				);

				assignments.setAdapter(adapter);
			}

			dialog.dismiss();

		}

		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(getActivity(), "Wait", "Connecting..");

		}

		@Override
		protected void onProgressUpdate(Void... values) {
		}

	}

	void getData(String school_db, String username) {
		try {
			httpclient = new DefaultHttpClient();
			httppost = new HttpPost(
					"http://npaneer.iguardianerp.co.in/attendance2.php");

			nameValuePairs = new ArrayList<NameValuePair>(2);
			// tv.setText("vhpppi");

			nameValuePairs.add(new BasicNameValuePair("username", username)); // $Edittext_value
																				// =
																				// $_POST['Edittext_value'];
			// nameValuePairs.add(new
			// BasicNameValuePair("schoolName",schoolName));
			// tv.setText("vhiccc");
			nameValuePairs.add(new BasicNameValuePair("dbSelected", school_db));
			// tv.setText("vhisjdskdj");
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			// Execute HTTP Post Request
			// response=httpclient.execute(httppost);

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
					// assign[i]=json.getString("ASSIGNMENT_TEXT");
					HashMap<String, String> temp = new HashMap<String, String>();

					if (json.getString("STATUS").equals("p")) {
						Stu_at = "Present";
					} else if (json.getString("STATUS").equals("a")) {
						Stu_at = "Absent";
					} else if (json.getString("STATUS").equals("lc")) {
						Stu_at = "Late Comer";
					} else {
						Stu_at = "Holiday";
					}

					temp.put("attendance", "" + Stu_at + "");
					// temp.put("",""+json.getString("ASSIGNMENT_TEXT")+"");
					temp.put("date", "" + json.getString("Date") + "");
					list.add(temp);

					// listItems.add(""+json.getString("ASSIGNMENT_TEXT")+"");
				}
			}

		} catch (Exception e) {
			Log.e("log_tag", "Error Parsing data " + e.toString());
			DataAlert();
		}

	} // voi

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