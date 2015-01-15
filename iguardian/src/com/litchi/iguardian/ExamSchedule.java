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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

public class ExamSchedule extends Fragment {

	int result_check = 0;
	ListView assignments;
	Button button_send;
	EditText new_message, message_subject;
	// TextView tv;
	HttpPost httppost;
	StringBuffer buffer;
	HttpResponse response;
	HttpClient httpclient;
	List<NameValuePair> nameValuePairs;
	ProgressDialog dialog = null;
	String[] teacherName, teacherID;
	Spinner teacher_spinner;
	// ProgressDialog ringProgressDialog = null;
	String schoolSelected, teacherSelected, username, school_db;

	SessionManagement session;

	public ExamSchedule() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.exam_schedule, container, false);

		getActivity().setTitle("Exam Schedule");

		assignments = (ListView) view.findViewById(R.id.listView1);

		teacher_spinner = (Spinner) view.findViewById(R.id.teacher_spinner);

		session = new SessionManagement(getActivity());
		// StrictMode.enableDefaults();

		HashMap<String, String> user = session.getUserDetails();
		username = user.get(SessionManagement.KEY_NAME);
		school_db = user.get(SessionManagement.KEY_DB);

		// getData();
		new ExamScheduleget().execute();

		teacher_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				int index = arg0.getSelectedItemPosition();
				teacherSelected = teacherID[index];
				// sendData();
				new ExamSchedulesend().execute();

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}

		});

		return view;
	}

	public void userAlert() {
		getActivity().runOnUiThread(new Runnable() {
			public void run() {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getActivity());
				builder.setTitle("Error.");
				builder.setMessage("Message can't be empty")
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
	}

	public void userAlert2() {
		getActivity().runOnUiThread(new Runnable() {
			public void run() {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getActivity());
				builder.setTitle("Error.");
				builder.setMessage("Subject can't be empty")
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
	}

	public void replyAlert2() {
		getActivity().runOnUiThread(new Runnable() {
			public void run() {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getActivity());
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

	private class ExamScheduleget extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			getData();
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			if (result_check == 1) {
				ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(
						getActivity(), R.layout.spinner_item, teacherName);
				dataAdapter1.setDropDownViewResource(R.layout.spinner_item);
				teacher_spinner.setAdapter(dataAdapter1);

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

		public void getData() {
			String result = "";
			InputStream isr = null;
			final List<String> list1 = new ArrayList<String>();
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(
						"http://npaneer.iguardianerp.co.in/exam_schedule_type.php");

				nameValuePairs = new ArrayList<NameValuePair>(2);
				// tv.setText("vhpppi");

				nameValuePairs
						.add(new BasicNameValuePair("username", username));
				nameValuePairs.add(new BasicNameValuePair("dbSelected",
						school_db));
				// tv.setText("vhisjdskdj");
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();
				isr = entity.getContent();

			} catch (Exception e) {
				Log.e("log_tag", "Error in http connection" + e.toString());
				InternetAlert();

			}

			try {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(isr, "iso-8859-1"), 8);
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
				if (!result.trim().toString().equals("0")) {

					result_check = 1;

					JSONArray jArray = new JSONArray(result);
					teacherName = new String[jArray.length()];
					teacherID = new String[jArray.length()];

					for (int i = 0; i < jArray.length(); i++) {
						JSONObject json = jArray.getJSONObject(i);
						teacherName[i] = "" + json.getString("exam_type") + "";
						teacherID[i] = "" + json.getString("exam_type_id") + "";
					}

					for (int i = 0; i < teacherName.length; i++) {
						list1.add(teacherName[i]);

					}
				}
			} catch (Exception e) {
				DataAlert();
				Log.e("log_tag", "Error Parsing data " + e.toString());

			}

		}
	}

	private class ExamSchedulesend extends AsyncTask<Void, Void, String> {

		ArrayList<HashMap<String, String>> list_exam =

		new ArrayList<HashMap<String, String>>();

		@Override
		protected String doInBackground(Void... params) {
			sendData();
			return null;
		}

		@Override
		protected void onPostExecute(String result) {

			SimpleAdapter adapter = new SimpleAdapter(getActivity(), list_exam,
					R.layout.custom_exam_schedule, new String[] { "date",
							"subject" }, new int[] { R.id.text_exam_date,
							R.id.text_exam_subject }

			);

			assignments.setAdapter(adapter);

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

		void sendData() {
			InputStream isr = null;
			String result = "";

			try {
				httpclient = new DefaultHttpClient();
				httppost = new HttpPost(
						"http://npaneer.iguardianerp.co.in/exam_schedule.php");

				nameValuePairs = new ArrayList<NameValuePair>(3);

				nameValuePairs
						.add(new BasicNameValuePair("username", username));
				nameValuePairs.add(new BasicNameValuePair("teacherSelected",
						teacherSelected));

				nameValuePairs.add(new BasicNameValuePair("dbSelected",
						school_db));

				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();
				isr = entity.getContent();

			} // try terminated

			catch (Exception e) {
				// dialog.dismiss();
				System.out.println("Exception : " + e.getMessage());
			}

			try {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(isr, "iso-8859-1"), 8);
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

				JSONArray jArray = new JSONArray(result);

				for (int i = 0; i < jArray.length(); i++) {
					JSONObject json = jArray.getJSONObject(i);

					HashMap<String, String> temp = new HashMap<String, String>();

					temp.put("date", "" + json.getString("Date") + "");
					temp.put("subject", "" + json.getString("subject_name")
							+ "");

					list_exam.add(temp);

				}

			} catch (Exception e) {
				Log.e("log_tag", "Error Parsing data " + e.toString());
				// Log.i("parse", "ok");

			}
		}

	}

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