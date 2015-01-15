package com.litchi.iguardian;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MainStudent extends Fragment {

	ImageView student_image;
	TextView student_name, class_name, student_birth_date, student_gender,
			student_address, student_father_name, student_mother_name,
			no_of_communication, transport, swipe_anywhere;
	ListView assignments;
	int result_check = 0;
	HttpPost httppost;
	StringBuffer buffer;
	HttpResponse response;
	HttpClient httpclient;
	List<NameValuePair> nameValuePairs;
	int i;
	String[] assign;
	SessionManagement session;
	String school_db, username, image_id;
	InputStream isr = null;
	String result = "";
	ProgressDialog mProgressDialog;

	public ProgressDialog simpleWaitDialog, simpleWaitDialog2;
	String downloadUrl;

	private String TAG = "** pushAndroidActivity **";

	// private TextView mDisplay;

	TextView RuA, paC, DEF;
	TextView Defense_Tackles[];

	AsyncTask<Void, Void, Void> mRegisterTask;

	public MainStudent() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.costum_main_student, container,
				false);

		
		getActivity().setTitle("Student Profile");
		student_image = (ImageView) view.findViewById(R.id.student_image);
		student_name = (TextView) view.findViewById(R.id.student_name);
		class_name = (TextView) view.findViewById(R.id.student_class);
		student_birth_date = (TextView) view
				.findViewById(R.id.student_birth_date);
		student_gender = (TextView) view.findViewById(R.id.student_gender);
		student_address = (TextView) view.findViewById(R.id.student_address);
		student_father_name = (TextView) view
				.findViewById(R.id.student_father_name);
		student_mother_name = (TextView) view
				.findViewById(R.id.student_mother_name);
		no_of_communication = (TextView) view
				.findViewById(R.id.no_of_communication);
		transport = (TextView) view.findViewById(R.id.transport);
		swipe_anywhere = (TextView) view.findViewById(R.id.swipe_anywhere);

		// download = (Button) view.findViewById(R.id.download);

		session = new SessionManagement(getActivity());

		// HashMap<Integer , Integer> num = session.getUserNumber();
		// i = num.get(SessionManagement.i);
		HashMap<String, String> user = session.getUserDetails();
		username = user.get(SessionManagement.KEY_NAME);
		school_db = user.get(SessionManagement.KEY_DB);
		// i = user.get(SessionManagement.i);

		// image_id= "8";
		final dataget data = new dataget();
		data.execute();

		/*
		 * final ImageDownloader download = new ImageDownloader();
		 * download.execute
		 * ("http://npaneer.iguardianerp.co.in/student_images/239.jpg");
		 */

		// new ImageDownloader().execute(downloadUrl);
		// class_name.setText(class_name1);
		// new dataget().execute();
		return view;
	}

	public class dataget extends AsyncTask<Void, Void, String> {

		String student_name1, class_name1, student_birth_date1,
				student_gender1, student_address1, student_father_name1,
				student_mother_name1, no_of_communication1, transport1,
				 download_url;
		String swipe_anywhere1 = "Swipe anywhere on the screen from left to right to get the menu.";

		// private Context context;
		// private PowerManager.WakeLock mWakeLock;

		/*
		 * public dataget(Context context) { this.context = context;
		 * Log.i("break", "1"); }
		 */

		@Override
		protected String doInBackground(Void... params) {
			getData(school_db, username);

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			simpleWaitDialog2.dismiss();

			if (result_check == 1) {
				student_name.setText(student_name1);
				student_birth_date.setText(student_birth_date1);
				class_name.setText(class_name1);
				student_gender.setText(student_gender1);
				student_address.setText(student_address1);
				student_father_name.setText(student_father_name1);
				student_mother_name.setText(student_mother_name1);
				no_of_communication.setText(no_of_communication1);
				transport.setText(transport1);
				swipe_anywhere.setText(swipe_anywhere1);

				final ImageDownloader download = new ImageDownloader();
				download.execute(download_url);
			}

			// mWakeLock.release();
		}

		@Override
		protected void onPreExecute() {

			/*
			 * PowerManager pm = (PowerManager) context
			 * .getSystemService(Context.POWER_SERVICE); mWakeLock =
			 * pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
			 * getClass().getName()); mWakeLock.acquire();
			 */
			simpleWaitDialog2 = ProgressDialog.show(getActivity(), "Wait",
					"Connecting..");

		}

		@Override
		protected void onProgressUpdate(Void... values) {
		}

		void getData(String school_db, String username) {

			try {
				httpclient = new DefaultHttpClient();
				httppost = new HttpPost(
						"http://npaneer.iguardianerp.co.in/student_main.php");

				nameValuePairs = new ArrayList<NameValuePair>(2);
				// tv.setText("vhpppi");

				nameValuePairs
						.add(new BasicNameValuePair("username", username));

				nameValuePairs.add(new BasicNameValuePair("dbSelected",
						school_db));
				Log.i("db", school_db);
				// tv.setText("vhisjdskdj");
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();
				isr = entity.getContent();

			} // try terminated

			catch (Exception e) {
				// dialog.dismiss();
				InternetAlert();
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
				// InternetAlert();

			}
			try {
				if (!result.trim().toString().equals("0")) {

					result_check = 1;
					JSONArray jArray = new JSONArray(result);

					for (int i = 0; i < jArray.length(); i++) {
						JSONObject json = jArray.getJSONObject(i);

						student_name1 = "" + json.getString("First_name") + " "
								+ json.getString("middle_name") + " "
								+ json.getString("last_name") + "";

						class_name1 = json.getString("class_name");
						// Log.i("class :", "dgffg");
						student_birth_date1 = json.getString("birth_date");
						student_gender1 = json.getString("gender");
						student_address1 = "" + json.getString("address_line1")
								+ " " + json.getString("city") + " "
								+ json.getString("state") + "";
						student_father_name1 = json.getString("father_name");
						student_mother_name1 = json.getString("mother_name");
						no_of_communication1 = json
								.getString("no_of_communication");
						transport1 = "" + json.getString("route_name") + " at "
								+ json.getString("bus_stop_name") + "";
						image_id = "" + json.getString("student_id") + "";
						Log.i("image :", image_id);
						download_url = "http://npaneer.iguardianerp.co.in/student_images/"
								+ image_id + ".jpg";
						/*
						 * final ImageDownloader download = new
						 * ImageDownloader(); download.execute(downloadUrl);
						 */

					}
				}

			} catch (Exception e) {

				DataAlert();
				// InternetAlert();

			}
		}
	}

	private class ImageDownloader extends AsyncTask<String, Void, Bitmap> {

		@Override
		protected Bitmap doInBackground(String... param) {
			// TODO Auto-generated method stub

			return downloadBitmap(param[0]);

		}

		@Override
		protected void onPreExecute() {
			Log.i("Async-Example", "onPreExecute Called");
			simpleWaitDialog = ProgressDialog.show(getActivity(), "Wait",
					"Connecting..");

		}

		@Override
		protected void onPostExecute(Bitmap result) {
			simpleWaitDialog.dismiss();
			Log.i("Async-Example", "onPostExecute Called");
			// simpleWaitDialog.dismiss();
			student_image.setImageBitmap(result);

		}

		private Bitmap downloadBitmap(String url) {
			// initilize the default HTTP client object
			final DefaultHttpClient client = new DefaultHttpClient();

			// forming a HttoGet request
			final HttpGet getRequest = new HttpGet(url);
			try {

				HttpResponse response = client.execute(getRequest);

				// check 200 OK for success
				final int statusCode = response.getStatusLine().getStatusCode();

				if (statusCode != HttpStatus.SC_OK) {
					Log.w("ImageDownloader", "Error " + statusCode
							+ " while retrieving bitmap from " + url);
					return null;

				}

				final HttpEntity entity = response.getEntity();
				if (entity != null) {
					InputStream inputStream = null;
					try {
						// getting contents from the stream
						inputStream = entity.getContent();

						// decoding stream data back into image Bitmap that
						// android understands
						final Bitmap bitmap = BitmapFactory
								.decodeStream(inputStream);

						return bitmap;
					} finally {
						if (inputStream != null) {
							inputStream.close();
						}
						entity.consumeContent();
					}
				}
			} catch (Exception e) {
				// You Could provide a more explicit error message for
				// IOException
				getRequest.abort();
				InternetAlert();
				Log.e("ImageDownloader", "Something went wrong while"
						+ " retrieving bitmap from " + url + e.toString());
			}

			return null;
		}

	}

	// void

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