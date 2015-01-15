package com.litchi.iguardian;

import android.content.Context;
import android.content.Intent;

public final class CommonUtilities {

	/**
	 * Base URL of the Demo Server (such as http://my_host:8080/gcm-server)
	 * 
	 * This link helps you to save or register your Registration-id in server. I
	 * used .net service here.
	 */

	//static final String SERVER_URL = "http://10.0.2.2:10739/GCM-server.asmx/PostRegistration-Id";

	/**
	 * Google API project id is your SENDER_ID and it is use to register with
	 * GCM.
	 */
	static final String SENDER_ID = "761390888847";

	/**
	 * Tag used on log messages.
	 */
	static final String TAG = "Prerit Test";

	/**
	 * Intent used to display a message in the screen.
	 */
	static final String DISPLAY_MESSAGE_ACTION =

	" com.litchi.iguardian.DISPLAY_MESSAGE";

	/**
	 * Intent's extra that contains the message to be displayed.
	 */
	static final String EXTRA_MESSAGE = "test_message";

	/**
	 * Notifies UI to display a message.
	 * <p>
	 * This method is defined in the common helper because it's used both by the
	 * UI and the background service.
	 * 
	 * @param context
	 *            application's context.
	 * @param message
	 *            message to be displayed.
	 */
	static void displayMessage(Context context, String message) {
		Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
		intent.putExtra(EXTRA_MESSAGE, message);
		context.sendBroadcast(intent);
	}
}
