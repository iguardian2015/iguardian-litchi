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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class ComposeMessage extends Fragment {

	// ImageView ivIcon;
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
	int result_check = 0;
	// ProgressDialog ringProgressDialog = null;
	String schoolSelected, teacherSelected = "", username, school_db;

	SessionManagement session;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.custom_compose_message,
				container, false);

		getActivity().setTitle("Compose Message");

		button_send = (Button) view.findViewById(R.id.Button_send);
		new_message = (EditText) view.findViewById(R.id.new_message);
		message_subject = (EditText) view.findViewById(R.id.message_subject);
		teacher_spinner = (Spinner) view.findViewById(R.id.teacher_spinner);

		session = new SessionManagement(getActivity());
		// StrictMode.enableDefaults();

		HashMap<String, String> user = session.getUserDetails();
		username = user.get(SessionManagement.KEY_NAME);
		school_db = user.get(SessionManagement.KEY_DB);

		new Composeget().execute();

		teacher_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				int index = arg0.getSelectedItemPosition();
				teacherSelected = teacherID[index];

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}

		});

		button_send.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (teacherSelected.equals("")) {
					teacherAlert();
				}

				else if (message_subject.getText().toString().trim().length() == 0) {
					userAlert2();
				} else if (new_message.getText().toString().trim().length() == 0) {
					userAlert();

				} else {
					// sendData();
					new Composesend().execute();

				}
			}
		}); // b.setOnC

		return view;
	}

	private class Composeget extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			getData();
			return null;
		}

		@Override
		protected void onPostExecute(String result) {

			if (result_check == 1) {
				ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(
						getActivity(), R.layout.compose_message_spinner_item,
						teacherName);
				dataAdapter1
						.setDropDownViewResource(R.layout.compose_message_spinner_item);
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

		void getData() {
			String result = "";
			InputStream isr = null;
			final List<String> list_compose = new ArrayList<String>();
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(
						"http://npaneer.iguardianerp.co.in/compose.php");

				nameValuePairs = new ArrayList<NameValuePair>(2);
				// tv.setText("vhpppi");

				nameValuePairs
						.add(new BasicNameValuePair("username", username));

				nameValuePairs.add(new BasicNameValuePair("dbSelected",
						school_db));

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
				DataAlert();

			}
			try {
				
				if (!result.trim().toString().equals("0"))
				{
					
					result_check = 1;

					JSONArray jArray = new JSONArray(result);
					teacherName = new String[jArray.length()];
					teacherID = new String[jArray.length()];

					for (int i = 0; i < jArray.length(); i++) {
						JSONObject json = jArray.getJSONObject(i);
						teacherName[i] = "" + json.getString("first_name")
								+ " " + json.getString("middle_name") + " "
								+ json.getString("last_name") + "-"
								+ json.getString("subject") + "";
						teacherID[i] = "" + json.getString("EMPLOYEE_ID") + "";
					}

					for (int i = 0; i < teacherName.length; i++) {
						list_compose.add(teacherName[i]);

					}
				}
				else
				{
					DataAlert();
				}

			} catch (Exception e) {

				Log.e("log_tag", "Error Parsing data " + e.toString());
				DataAlert();

			}

		}

	}

	private class Composesend extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			sendData();
			return null;
		}

		@Override
		protected void onPostExecute(String result) {

			dialog.dismiss();
			new_message.setText("");
			message_subject.setText("");
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
			try {

				httpclient = new DefaultHttpClient();
				httppost = new HttpPost(
						"http://npaneer.iguardianerp.co.in/compose_message.php");

				nameValuePairs = new ArrayList<NameValuePair>(5);
				// tv.setText("vhpppi");

				nameValuePairs
						.add(new BasicNameValuePair("username", username)); // $Edittext_value
																			// =
																			// $_POST['Edittext_value'];

				nameValuePairs.add(new BasicNameValuePair("dbSelected",
						school_db));

				nameValuePairs.add(new BasicNameValuePair("new_message",
						new_message.getText().toString().trim()));
				// nameValuePairs.add(new
				// BasicNameValuePair("password",pass.getText().toString().trim()));
				// tv.setText("vhiccc");
				nameValuePairs.add(new BasicNameValuePair("message_subject",
						message_subject.getText().toString().trim()));

				nameValuePairs.add(new BasicNameValuePair("teacherSelected",
						teacherSelected.trim()));
				// tv.setText("vhisjdskdj");
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				// Execute HTTP Post Request
				// response=httpclient.execute(httppost);

				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();

				replyAlert2();
			} // try terminated

			catch (Exception e) {
				dialog.dismiss();
				System.out.println("Exception : " + e.getMessage());
			}

		} // void login

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

	public void teacherAlert() {
		getActivity().runOnUiThread(new Runnable() {
			public void run() {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getActivity());
				builder.setTitle("Can't send the message");
				builder.setMessage("No Data found For teacher.")
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