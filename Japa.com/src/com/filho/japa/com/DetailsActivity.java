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

	
	private TextView txtName,txtInfo;
	private ArrayAdapter<Telephone> mAdapter;
	private List<Telephone> mItens;
	private Spinner spn;
	//Threads.
	Thread thread,timerThread,thread1;
	private Telephone telephoneSelected;
	private String address;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_details);
		
		if (Build.VERSION.SDK_INT>=14){
			getActionBar().setDisplayHomeAsUpEnabled(true);
			int titleId = getResources().getIdentifier("action_bar_title", "id","android");
		    TextView txt = (TextView) findViewById(titleId);
		    Typeface tf = Typeface.createFromAsset(getAssets(), Values.FONT_PATH);
		    txt.setTypeface(tf);
			
		}
		 
		spn = (Spinner) findViewById(R.id.spnTelephone);
		
		spn.setOnItemSelectedListener(new OnItemSelectedListener(){
			
	
			public void onItemSelected(
					android.widget.AdapterView<?> arg0,
					View arg1, int arg2, long arg3) {
				
				telephoneSelected = mAdapter.getItem(arg2);
			};
			
			public void onNothingSelected(android.widget.AdapterView<?> arg0) {
				
				
			};
			
		}	
		);
		
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
											thread.join();
											thread1.join();
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
		thread1 = new Thread()
			{
				@Override
				public void run() {
					
					//GETTING THE LIST
					final String result = MyDatabase.getFromPHP(PHP.php_GET_RESTAURANT_DETAILS,parameters);
					
					runOnUiThread(new Runnable() {
						public void run() {
						
							try{								 
								loadingData(Functions.convertJsonToList_To_Details(result));
							}
							catch(Exception e){
								Log.i("FABIO ERRO",e.toString());
							}
							finally{
							
								Functions.closeWammingDialog();
								
								//Loading Spinner Telephone
						        openThread(PHP.php_GET_TELEPHONE, spn, parameters);
						        thread.start();
														 
							}	
						}
					});
					
				}
			};
			
			
			
			//Checks if has connection with internet.
			if (!Functions.checkConnectionIntenet((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE))) 
			{
				Toast.makeText(getApplicationContext(), Values.WARNING_NO_CONNECTION_PT, Toast.LENGTH_LONG).show();
				finish();
			}
			else
			{					
				thread1.start();
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
	private void openThread(final String php,final Spinner spn,final PostValues[] parameters)
		{
			{
				 //SHOW WARMMING BOX PLEASE WAIT
				Functions.showWammingDialog(this, Values.WARNING_PLEASE_WAIT_PT);
				thread = new Thread()
					{
						@Override
						public void run() {
							
							//GETTING THE LIST
							final String result = MyDatabase.getFromPHP(php,parameters);
							
							runOnUiThread(new Runnable() {
								public void run() {
								
									try{
										//GET RESULT FINAL OF QUERY
										mItens = Functions.convertJsonToList_To_Telephone(result);
										//ADD RESULT TO OBJETO SCREEN
										creatingBaseForSpinner(spn,mItens);							
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
		

		
		
	//INSTANCE THE ADAPTER AND ITENS ARRAY
	private void creatingBaseForSpinner(Spinner spn,List<Telephone> mItens)
	{			
			mAdapter = new ArrayAdapter<Telephone>(this,R.layout.activity_spinner_itens,mItens);	
			spn.setAdapter(mAdapter);
    }
	
	
	
	
	private void loadingData(Places resultPlace)
	{
		txtName = (TextView) findViewById(R.id.txtName);
		
		txtName.setText(Values.TEMP_NAME_RESTAURANT);
		
		txtInfo = (TextView) findViewById(R.id.txtInfo);
		
		address = Values.TEMP_NAME_STATE+", "+Values.TEMP_NAME_CITY+", "+
				Values.TEMP_NAME_NEIGHBORHOOD+", "+resultPlace.getAddress();
		
		txtInfo.setText(Values.TEMP_NAME_STATE+", "+Values.TEMP_NAME_CITY+
				",\n"+Values.TEMP_NAME_NEIGHBORHOOD+", "+resultPlace.getAddress());
		
		
		/*txtAddress.setText();
		txtCity.setText();
		txtNeighborhood.setText();
		txtState.setText();
		*/
	}
	
}
