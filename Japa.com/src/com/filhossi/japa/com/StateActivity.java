package com.filhossi.japa.com;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import global_values.Functions;
import global_values.MyDatabase;
import global_values.PHP;
import global_values.Values;
import objectsOfLists.AdapterPlacesListView;
import objectsOfLists.Places;


public class StateActivity extends Activity {

	//This array adapter will be the heart of the list.
	private AdapterPlacesListView mAdapter;
	//This ListView will show the States. 
	private ListView listState;
	//Threads.
	Thread thread,timerThread;
	//Information about phone.
	private TelephonyManager telephonyManager ;
	//These TextViews will show titles.
	private TextView txtWelcome,txtJapa,txtState,txtQuestion,txtChoose;
	private ImageView imgAD;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_places_state);
		
		//Check phone's SDK version.
		if (Build.VERSION.SDK_INT>=14){
			//Load title font.				
			int titleId = getResources().getIdentifier("action_bar_title", "id","android");
		    TextView txt = (TextView) findViewById(titleId);
			Typeface tf = Typeface.createFromAsset(getAssets(), Values.FONT_PATH);
		    txt.setTypeface(tf);					
		}	
		
		
		//Get the object from UI . 
		listState = (ListView) findViewById(R.id.activity_places_state_listView);
		txtWelcome = (TextView) findViewById(R.id.txtWelcome);
		txtState = (TextView)findViewById(R.id.txtState);
		txtJapa = (TextView)findViewById(R.id.txtJapa);
		txtChoose = (TextView)findViewById(R.id.txtChoose);
		txtQuestion = (TextView)findViewById(R.id.txtQuestion);
		imgAD = (ImageView)findViewById(R.id.imgAd);
		
		
		imgAD.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				openBrowser(Values.URL_SITE_APP_JAPA);
			};
		});
		
		
	        //Show warning box of Wait.
			Functions.showWammingDialog(this, Values.WARNING_PLEASE_WAIT_PT);
			//This thread will make the query in background.
			thread = new Thread()
				{
					@Override
					public void run() {
						Values.STATE_CONNECTING = true;
						//Getting result of query.
						//final String result = MyDatabase.getFromPHP(PHP.php_GET_STATE,null);
						final String result = MyDatabase.getFromPHP(PHP.php_GET_STATE,null);
						runOnUiThread(new Runnable() {
							public void run() {							
								try{
									//Converting the result to readable for listView.
									loadingArrayList(Functions.convertJsonToList_Places(result));	
									Values.STATE_CONNECTING = true;
								}
								catch(Exception e){
									Log.i("onCreate",e.toString());
								}
								finally{Functions.closeWammingDialog();
								}	
							}
						});
						
					}
				};
				
			//Thread Time.
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
			
			//Get IMEI of the phone
			getIMEI();
		}
	
	private void setFont()
	{	
		//Instance font object.
		Typeface tf = Typeface.createFromAsset(getAssets(), Values.FONT_PATH);
		//Setting font created for every TextView from UI.
		txtWelcome.setTypeface(tf);
		txtState.setTypeface(tf);
		txtChoose.setTypeface(tf);
		txtJapa.setTypeface(tf);
		txtQuestion.setTypeface(tf);	
	
	}
	
	
	public void openBrowser(String url) {

		Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse(url));
		startActivity(intent);
	}
	
	//Get IMEI of the phone.
	private void getIMEI(){
			//Getting IMEI of the phone.	
			telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
			Values.TEMP_IMEI = telephonyManager.getDeviceId();
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
		
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		finish();
	}
	
	public void onClick(View view)
	{
		//Get the state object selected.
		Places state = mAdapter.getItem(listState.getPositionForView(view));	
		//Setting the temp values for the program.
		Values.TEMP_COD_STATE = state.getCod();
		Values.TEMP_NAME_STATE = state.getName();
		//Call the new Activity.
		Intent i = new Intent("android.intent.action.CITY");
		startActivity(i);
	}
		
	//Loading listView.
	public void loadingArrayList(ArrayList<Places> itens)
		{
			//Setting the itens and layout to adapter.
			mAdapter = new AdapterPlacesListView(this,itens,getAssets());
			//Setting the adapter to listView.
			listState.setAdapter(mAdapter);
			
		}

	

	
	
}
