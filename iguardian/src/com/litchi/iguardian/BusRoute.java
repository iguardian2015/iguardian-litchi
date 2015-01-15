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

public class BusRoute extends Fragment {

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
	String[] route_name, bus_route_id;
	Spinner teacher_spinner;
	// ProgressDialog ringProgressDialog = null;
	String schoolSelected, routeSelected, username, school_db;

	SessionManagement session;

	public BusRoute() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.marks, container, false);

		getActivity().setTitle("Bus Routes");

		assignments = (ListView) view.findViewById(R.id.listView1);
		// button_send = (Button) view.findViewById(R.id.Button_send);
		// new_message = (EditText) view.findViewById(R.id.new_message);
		// message_subject = (EditText)view.findViewById(R.id.message_subject);
		teacher_spinner = (Spinner) view.findViewById(R.id.teacher_spinner);
		// tv = (TextView) view.findViewById(R.id.status);

		session = new SessionManagement(getActivity());
		// StrictMode.enableDefaults();

		HashMap<String, String> user = session.getUserDetails();
		username = user.get(SessionManagement.KEY_NAME);
		school_db = user.get(SessionManagement.KEY_DB);

		// getData();

		new Marksget().execute();
		teacher_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				int index = arg0.getSelectedItemPosition();
				routeSelected = bus_route_id[index];
				// sendData();
				new Markssend().execute();
				// tv.setText(schoolSelected);

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}

		});

		return view;
	}

	private class Marksget extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			getData();
			return null;
		}

		@Override
		protected void onPostExecute(String result) {

			if (result_check == 1) {
				ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(
						getActivity(), R.layout.spinner_item, route_name);
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
			// final List<String> list1 = new ArrayList<String>();
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(
						"http://npaneer.iguardianerp.co.in/bus_route.php");

				nameValuePairs = new ArrayList<NameValuePair>(2);
				// tv.setText("vhpppi");

				nameValuePairs
						.add(new BasicNameValuePair("username", username)); // $Edittext_value
																			// =
																			// $_POST['Edittext_value'];

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
				if (!result.trim().toString().equals("0"))
				{
					
					result_check = 1;

					JSONArray jArray = new JSONArray(result);

					/*
					 * route_name = new String[jArray.length()];
					 */
					route_name = new String[jArray.length()];
					bus_route_id = new String[jArray.length()];

					for (int i = 0; i < jArray.length(); i++) {

						JSONObject json = jArray.getJSONObject(i);

						route_name[i] = "" + json.getString("route_name") + "";
						bus_route_id[i] = "" + json.getString("bus_route_id")
								+ "";

						// teacherID[i] = "" + json.getString("exam_type_id") +
						// "";
					}
				}

			} catch (Exception e) {
				
				Log.e("log_tag", "Error Parsing data " + e.toString());
				DataAlert();
			}

		}

	}

	private class Markssend extends AsyncTask<Void, Void, String> {

		ArrayList<HashMap<String, String>> list_marks =

		new ArrayList<HashMap<String, String>>();

		@Override
		protected String doInBackground(Void... params) {
			sendData();

			return null;
		}

		@Override
		protected void onPostExecute(String result) {

			SimpleAdapter adapter = new SimpleAdapter(getActivity(),
					list_marks, R.layout.custom_bus_stop,
					new String[] { "bus_stop_name" },
					new int[] { R.id.text_subject }

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
						"http://npaneer.iguardianerp.co.in/bus_stops.php");

				nameValuePairs = new ArrayList<NameValuePair>(3);
				// tv.setText("vhpppi");

				nameValuePairs
						.add(new BasicNameValuePair("username", username));
				nameValuePairs.add(new BasicNameValuePair("routeSelected",
						routeSelected));

				nameValuePairs.add(new BasicNameValuePair("dbSelected",
						school_db));
				// tv.setText("vhisjdskdj");
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
				// String s = "";
				// ArrayList<String> listItems=new ArrayList<String>();

				JSONArray jArray = new JSONArray(result);

				for (int i = 0; i < jArray.length(); i++) {
					JSONObject json = jArray.getJSONObject(i);
					// assign[i]=json.getString("ASSIGNMENT_TEXT");
					HashMap<String, String> temp = new HashMap<String, String>();

					temp.put("bus_stop_name",
							"" + json.getString("bus_stop_name") + "");
					/*
					 * temp.put("marks", "" + json.getString("marks_attained") +
					 * " / " + json.getString("total_marks") + "");
					 */

					list_marks.add(temp);

					// listItems.add(""+json.getString("ASSIGNMENT_TEXT")+"");
				}

			} catch (Exception e) {
				Log.e("log_tag", "Error Parsing data " + e.toString());

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

	public void Alert2() {
		getActivity().runOnUiThread(new Runnable() {
			public void run() {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getActivity());
				builder.setTitle("Status");
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