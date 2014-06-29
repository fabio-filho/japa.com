package com.filhossi.japa.com;

import global_values.Functions;
import global_values.MyDatabase;
import global_values.PHP;
import global_values.PostValues;
import global_values.Values;

import java.util.ArrayList;

import objectsOfLists.AdapterRestauranteListView;
import objectsOfLists.RestaurantItensListView;
import objectsOfLists.Places;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class RestaurantsActivity extends Activity{
	
	
	private AdapterRestauranteListView mAdapter; 
	private ListView list;
	//Threads.
	Thread thread,timerThread;
	//These TextViews will show titles.
	private TextView txtJAPA,txtChoose;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_places_restaurants);
	
		if (Build.VERSION.SDK_INT>=14){
			getActionBar().setDisplayHomeAsUpEnabled(true);
			setTitle(Values.TEMP_NAME_NEIGHBORHOOD);
			//Load title font.
			int titleId = getResources().getIdentifier("action_bar_title", "id","android");
		    TextView txt = (TextView) findViewById(titleId);
		    Typeface tf = Typeface.createFromAsset(getAssets(), Values.FONT_PATH);
		    txt.setTypeface(tf);
			
		}
		
		
		//Get the object from UI . 
		list = (ListView) findViewById(R.id.activity_places_restaurants_listView);
		txtChoose = (TextView)findViewById(R.id.txtChoose);
		txtJAPA = (TextView) findViewById(R.id.txtJAPA);
		
		//Parameters of query's request.
		final PostValues[] parameters = new PostValues[3];
		parameters[1] = new PostValues();
		parameters[1].field = Values.TEMP_FIELD_CITY;
		parameters[1].value = Values.TEMP_COD_CITY;
		
		parameters[0] = new PostValues();
		parameters[0].field = Values.TEMP_FIELD_STATE;
		parameters[0].value = Values.TEMP_COD_STATE;
		
		parameters[2] = new PostValues();
		parameters[2].field = Values.TEMP_FIELD_NEIGHBORHOOD;
		parameters[2].value = Values.TEMP_COD_NEIGHBORHOOD;
		
		
	
      //SHOW WARMMING BOX PLEASE WAIT
		Functions.showWammingDialog(this, Values.WARNING_PLEASE_WAIT_PT);
		thread = new Thread()
			{
				@Override
				public void run() {
					
					//GETTING THE LIST
					final String result = MyDatabase.getFromPHP(PHP.php_GET_RESTAURANTS,parameters);
					
					runOnUiThread(new Runnable() {
						public void run() {
						
							try{
								loadingArrayList(Functions.convertJsonToArray(result));						
							}
							catch(Exception e){
								Log.i("FABIO ERRO",e.toString());
							}
							finally{Functions.closeWammingDialog();
							checkIfHasRegistreAndOut();
							}	
						}
					});
					
				}
			};
				
			timerThread = new Thread(){
				
				@SuppressWarnings("deprecation")
				public void run() {
					try{							
						sleep(6000);							
						runOnUiThread(new Runnable() {
							public void run() {
								if (Values.STATE_CONNECTING )
								{
									try {
										if (!Values.STATE_CONNECTING){
												thread.join();
												finish();
												Toast.makeText(getApplicationContext(), 
														Values.WARNING_FAIL_CONNECTION_PT, 
														Toast.LENGTH_SHORT).show();													
											}
										
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							}
						});	
					}
					catch(Exception o){}
				};
				
			};
			//Check if has internet and block the app if there ins't.
			checkInternet();
			
			//Setting font on UI objects.
			setFont();
		
	}
	
	
	private void setFont()
	{	
		//Instance font object.
		Typeface tf = Typeface.createFromAsset(getAssets(), Values.FONT_PATH);
		//Setting font created for every TextView from UI.
		txtChoose.setTypeface(tf);
		txtJAPA.setTypeface(tf);
	
	}
	
	//Check if has connection with internet and block the app if there ins't.
	private void checkInternet()
	{
		//Checks if has connection with internet.
		if (!Functions.checkConnectionIntenet((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE))) 
		{
			Toast.makeText(getApplicationContext(), Values.WARNING_NO_CONNECTION_PT, Toast.LENGTH_LONG).show();
			finish();
		}
		else
		{	
			//Starting the Query'Thread.
			thread.start();
			timerThread.start();
		}
		
	}
	
	private void checkIfHasRegistreAndOut()
	{
		
		if (mAdapter.getCount()==0){
		
			Toast.makeText(getApplicationContext(), 
					Values.WARNING_NOT_REGISTER_YET, 
					Toast.LENGTH_LONG).show();
			
			finish();
		}	
		
	}
	
	//OnClickRestaurants
	public void onClick(View view)
	{
		RestaurantItensListView item =mAdapter.getItem(list.getPositionForView(view));	
		
		Values.TEMP_COD_RESTAURANT = item.getCod();
		Values.TEMP_NAME_RESTAURANT = item.getName();
		
		Intent i = new Intent("android.intent.action.DETAILS");
		startActivity(i);
	}
	
	
	private void cleanTempDataDetails()
	{
		Values.TEMP_COD_NEIGHBORHOOD = "";
		Values.TEMP_NAME_NEIGHBORHOOD = "";
		
		finish();
	}
								
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		
		cleanTempDataDetails();		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home)
		{			
			cleanTempDataDetails();
		}
			return super.onOptionsItemSelected(item);
	}
	
	
	public void loadingArrayList(ArrayList<RestaurantItensListView> itens)
	{
		mAdapter = new AdapterRestauranteListView
			(this,itens,getAssets());
		
		list.setAdapter(mAdapter);
		
	}

}
