package com.litchi.iguardian;


import java.lang.reflect.Field;
import java.util.HashMap;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

//import android.support.v7.appcompat.R;






	public class MainActivity extends ActionBarActivity implements OnItemClickListener
	{
		private DrawerLayout drawerLayout;
		private ListView listView;
		ProgressDialog Dialog ;
		Fragment fragment = null;
		//private  ImageView student_image;
		//private String[] Planets;
		private ActionBarDrawerToggle drawerListener;
		private MyAdapter myAdapter;
		//Bundle extras = getIntent().getExtras();
		//String schoolName = extras.getString("schoolSelected");
		//String username = extras.getString("username");
		SessionManagement session;
		protected void onCreate(Bundle savedInstanceState)
		{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_main);
			
			
			
			session = new SessionManagement(getApplicationContext());
			
			  session.checkLogin();
			 
			  /*if (getIntent().getBooleanExtra("EXIT", false)) {
				    finish();
				}		*/
			
			
			//SToast.makeText(getApplicationContext(), "User Login Status from main: " + session.isLoggedIn(), Toast.LENGTH_LONG).show();
			
			//session.checkLogin();
			
			HashMap<String, String> user = session.getUserDetails();
	         
	        // name;
	        String username = user.get(SessionManagement.KEY_NAME);
	        String school = user.get(SessionManagement.KEY_SCHOOL);
	         
	        // email
	      //String email = user.get(SessionManagement.KEY_EMAIL);
	         
	        // displaying user data
	        //lblName.setText(Html.fromHtml("Name: <b>" + name + "</b>"));
	        //lblEmail.setText(Html.fromHtml("Email: <b>" + email + "</b>"));
			//student_image = (ImageView) findViewById(R.id.student_image);
			
			/*new DownloadImageTask((ImageView) findViewById(R.id.student_image))
            .execute("http://npaneer.iguardianerp.co.in/download.jpg");
*/




			
			
			
			
			
			
			
			listView = (ListView) findViewById(R.id.drawerList);
//			listView.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, Planets));
			myAdapter = new MyAdapter(this);
			listView.setAdapter(myAdapter);
			listView.setOnItemClickListener(this);
			drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
			drawerListener = new ActionBarDrawerToggle(this, drawerLayout, R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close);
			
			drawerLayout.setDrawerListener(drawerListener);
			android.support.v7.app.ActionBar actionBar =getSupportActionBar();
	        actionBar.setDisplayHomeAsUpEnabled(true);
			//actionBar.setHomeButtonEnabled(true);
			//getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			
			
			getOverflowMenu();
			
		}
		
		private void getOverflowMenu() {

		     try {
		        ViewConfiguration config = ViewConfiguration.get(this);
		        Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
		        if(menuKeyField != null) {
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
			if(drawerListener.onOptionsItemSelected(item))
			{
				return true;
			}
			
			int itemId = item.getItemId();
			if (itemId == R.id.action_logout) {
				session.logoutUser();
				MainActivity.this.finish();
				return true;
			}
			
			
			return super.onOptionsItemSelected(item);
			
		}
		
		@Override
		public void onConfigurationChanged(Configuration newConfig) {
			// TODO Auto-generated method stub
			super.onConfigurationChanged(newConfig);
			drawerListener.onConfigurationChanged(newConfig);
		}
		
		
		@Override
		protected void onPostCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onPostCreate(savedInstanceState);
			drawerListener.syncState();	
		}

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			// TODO Auto-generated method stub
			//Toast.makeText(this, Planets[position]+" was selected ",Toast.LENGTH_LONG).show();
			selectItem(position);
			//Intent intent = new Intent(this, DisplayMessageActivity.class);
			
			
				//Fragment fragment = null;
			 listView.setItemChecked(position, true);         
			    drawerLayout.closeDrawer(listView);
			    if ( position == 0 ) {
			    	//drawerLayout.closeDrawers();
			       // Intent intent = new Intent(MainActivity.this, Assignment.class);
			        //intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
			        //intent.putExtra("username", username);
			        //intent.putExtra("schoolName",schoolName );
			        //MainActivity.this.startActivity(intent);
			    	fragment = new FragmentOne();
			        /*drawerLayout.closeDrawers();*/
			    	//drawerLayout.closeDrawers();
			    	FragmentManager frgManager = getSupportFragmentManager();
					frgManager.beginTransaction().replace(R.id.mainContent, fragment)
							.commit();
					drawerLayout.closeDrawers();
			    	
			        }
			    if ( position == 1 ) {
			        /*Intent intent1 = new Intent(MainActivity.this, Attendance.class);
			        MainActivity.this.startActivity(intent1);*/
			    	fragment = new FragmentTwo();
			    	FragmentManager frgManager = getSupportFragmentManager();
					frgManager.beginTransaction().replace(R.id.mainContent, fragment)
							.commit();
			    	drawerLayout.closeDrawers();
			        }
			    
			    if ( position == 2 ) {
			    	fragment = new FragmentThree();
			    	FragmentManager frgManager = getSupportFragmentManager();
					frgManager.beginTransaction().replace(R.id.mainContent, fragment)
							.commit();
			    	drawerLayout.closeDrawers();
			        }
			    /*if ( position == 3 ) {
			        Intent intent3 = new Intent(MainActivity.this, Marks.class);
			        MainActivity.this.startActivity(intent3);
			        drawerLayout.closeDrawers();
			        }
			    if ( position == 4 ) {
			        Intent intent4 = new Intent(MainActivity.this, TimeTable.class);
			        MainActivity.this.startActivity(intent4);
			        drawerLayout.closeDrawers();
			        }
			    if ( position == 5 ) {
			        Intent intent5 = new Intent(MainActivity.this, ExamsSchedule.class);
			        MainActivity.this.startActivity(intent5);
			        drawerLayout.closeDrawers();
			        }
			    if ( position == 6 ) {
			        Intent intent6 = new Intent(MainActivity.this, Circulars.class);
			        MainActivity.this.startActivity(intent6);
			        drawerLayout.closeDrawers();
			        }
			*/
			    
			    //fragment.setArguments(args);
				/*FragmentManager frgManager = getFragmentManager();
				frgManager.beginTransaction().replace(R.id.mainContent, fragment)
						.commit();*/

				//mDrawerList.setItemChecked(possition, true);
				//setTitle(dataList.get(possition).getItemName());
//				mDrawerLayout.closeDrawer(mDrawerList);
//				drawerLayout.closeDrawers();
			     
			    
			    
				//listView.setItemChecked(position, true);
				//setTitle(dataList.get(possition).getItemName());
		        
		        
			
		}

		public void selectItem(int position) {
			// TODO Auto-generated method stub
			listView.setItemChecked(position, true);
			//setTitle([position]);
			
		}
		
		
	}
	
	class MyAdapter extends BaseAdapter
	{
		private Context context;
		String[] iguardianmenu;
		int[] images={ R.drawable.assignment_icon, R.drawable.attendance_icon, R.drawable.message_icon, 
				       R.drawable.marks_icon, R.drawable.timetable_icon, R.drawable.timetable_icon, R.drawable.circular_icon};
		
		public MyAdapter(Context context) {
			// TODO Auto-generated constructor stub
			this.context=context;
			iguardianmenu=context.getResources().getStringArray(R.array.imenu);
			
		}
		
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return iguardianmenu.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return iguardianmenu[position];
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View row = null;
			if(convertView==null)
			{
				LayoutInflater inflater =(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				row=inflater.inflate(R.layout.findrow, parent, false);
			}
			else
			{
				row=convertView;
				
			}
			TextView titleTextView = (TextView) row.findViewById(R.id.textView1);
			ImageView titleImageView = (ImageView) row.findViewById(R.id.imageView1);
			titleTextView.setText(iguardianmenu[position]);
			titleImageView.setImageResource(images[position]);
			return row;
		}
		
	}
	
	 /*class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
	    ImageView bmImage;

	    public DownloadImageTask(ImageView bmImage) {
	        this.bmImage = bmImage;
	    }

	    protected Bitmap doInBackground(String... urls) {
	        String urldisplay = urls[0];
	        Bitmap mIcon11 = null;
	        try {
	            InputStream in = new java.net.URL(urldisplay).openStream();
	            mIcon11 = BitmapFactory.decodeStream(in);
	        } catch (Exception e) {
	            Log.e("Error", e.getMessage());
	            e.printStackTrace();
	        }
	        return mIcon11;
	    }

	    protected void onPostExecute(Bitmap result) {
	        bmImage.setImageBitmap(result);
	    }
	}
*/