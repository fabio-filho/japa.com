package com.filho.japa.com;

import global_values.Functions;
import global_values.MyDatabase;
import global_values.PHP;
import global_values.PostValues;
import global_values.Values;

import java.util.List;

import objectsOfLists.Places;
import objectsOfLists.Telephone;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class DetailsActivity extends Activity{

	
	private TextView txtName,txtInfo,txtTelephone,txtCallTo;
	//Threads.
	Thread threadTelephone,timerThread,threadDetails;
	private Telephone telephoneSelected;
	private String address;
	private Places resultPlace;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_details);
		
		if (Build.VERSION.SDK_INT>=14){
			getActionBar().setDisplayHomeAsUpEnabled(true);
			//Load title font.
			int titleId = getResources().getIdentifier("action_bar_title", "id","android");
		    TextView txt = (TextView) findViewById(titleId);
		    Typeface tf = Typeface.createFromAsset(getAssets(), Values.FONT_PATH);
		    txt.setTypeface(tf);
			
		}
		
		txtName = (TextView) findViewById(R.id.txtName);
		txtInfo = (TextView) findViewById(R.id.txtInfo);
		txtTelephone = (TextView) findViewById(R.id.txtTelephone);
		txtCallTo = (TextView) findViewById(R.id.txtCallTo);
		
		ImageView btnLike = (ImageView) findViewById(R.id.btnLike);
		ImageView btnInstagram = (ImageView) findViewById(R.id.btnInstagram);
		ImageView btnTwitter = (ImageView) findViewById(R.id.btnTwitter);
		ImageView btnMaps = (ImageView) findViewById(R.id.btnMaps);
		ImageButton btnCall = (ImageButton) findViewById(R.id.imageDirectCall);
		
			
		
		btnMaps.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			
				String url = Values.URL_GOOGLE_MAPS+Functions.tranformToGoogleMapsURL(address);
				openBrowser(url);
				
			}
		});
		
		btnLike.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				openBrowser(Values.URL_FACEBOOK);				
				
			}
		});
		
		btnInstagram.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				openBrowser(Values.URL_INSTAGRAM);
				
				
			}
		});


		btnTwitter.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				openBrowser(Values.URL_TWITTER);
				
			}
		});


		btnCall.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				try {
					
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:"+telephoneSelected.getNumberCall()));
                    startActivity(callIntent);
                    
                } catch (ActivityNotFoundException activityException) {
                    Log.e("Calling a Phone Number", "Call failed", activityException);
                }
				
			}
		});
		
		
		Log.i("Tudo OK", "ATE AQUI OK");
		
		timerThread = new Thread(){
			
			@SuppressWarnings("deprecation")
			public void run() {
				try{							
					sleep(10000);							
					runOnUiThread(new Runnable() {
						public void run() {
							if (Values.STATE_CONNECTING )
							{
								try {
									if (!Values.STATE_CONNECTING){
											threadTelephone.join();
											threadDetails.join();
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
				
		
		final PostValues[] parameters = new PostValues[1];
		
		parameters[0] = new PostValues();
		parameters[0].field = "cod";
		parameters[0].value = Values.TEMP_COD_RESTAURANT;
		
		
		 //SHOW WARMMING BOX PLEASE WAIT
		Functions.showWammingDialog(this, Values.WARNING_PLEASE_WAIT_PT);
		threadDetails = new Thread()
			{
				@Override
				public void run() {
					
					//GETTING THE LIST
					final String result = MyDatabase.getFromPHP(PHP.php_GET_RESTAURANT_DETAILS,parameters);
					
					runOnUiThread(new Runnable() {
						public void run() {
						
							try{								 
								resultPlace = Functions.convertJsonToList_To_Details(result);
							}
							catch(Exception e){
								Log.i("FABIO ERRO",e.toString());
							}
							finally{
							
															
								//Loading Spinner Telephone
						        openThread(PHP.php_GET_TELEPHONE,parameters);
						        threadTelephone.start();
														 
							}	
						}
					});
					
				}
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
		txtInfo.setTypeface(tf);
		txtName.setTypeface(tf);
		txtTelephone.setTypeface(tf);	
		txtCallTo.setTypeface(tf);
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
			threadDetails.start();
			timerThread.start();
		}
		
	}
	
	private void cleanTempDataDetails()
	{
		Values.TEMP_NAME_RESTAURANT = "";
		Values.TEMP_COD_RESTAURANT = "";
		
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
	
	
	public void openBrowser(String url) {

		Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse(url));
		startActivity(intent);
	}
	
	
	//OPEN THREAD
	private void openThread(final String php,final PostValues[] parameters)
		{
			{
				 //SHOW WARMMING BOX PLEASE WAIT			
				threadTelephone = new Thread()
					{
						@Override
						public void run() {
							
							//GETTING THE LIST
							final String result = MyDatabase.getFromPHP(php,parameters);
							
							runOnUiThread(new Runnable() {
								public void run() {
								
									try{
										//GET RESULT FINAL OF QUERY
										telephoneSelected = Functions.convertJsonTo_One_Telephone(result);	
										loadingData();
									}
									catch(Exception e){
										Log.i("FABIO ERRO",e.toString());
									}
									finally{Functions.closeWammingDialog();
									}	
								}
							});
							
						}
						
					};
					}
		}
		

	
	//Putting data collected in the activity.
	private void loadingData()
	{	
		
		txtName.setText(Values.TEMP_NAME_RESTAURANT);
			
		address = Values.TEMP_NAME_STATE+", "+Values.TEMP_NAME_CITY+", "+
				Values.TEMP_NAME_NEIGHBORHOOD+", "+resultPlace.getAddress();
		
		txtInfo.setText(Values.TEMP_NAME_STATE+", "+Values.TEMP_NAME_CITY+
				",\n"+Values.TEMP_NAME_NEIGHBORHOOD+", "+resultPlace.getAddress());
		
		txtTelephone.setText(telephoneSelected.toString());
		
	
	}
	
}
