package com.litchi.iguardian;

import android.app.Activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

public class Test extends Activity {
	
	Button downloadqwqwq ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);
		
		downloadqwqwq =  (Button) findViewById(R.id.download);
		
		
	}
}
