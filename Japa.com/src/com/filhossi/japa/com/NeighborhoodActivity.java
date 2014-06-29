package com.filhossi.japa.com;

import java.util.ArrayList;

import objectsOfLists.AdapterPlacesListView;
import objectsOfLists.Places;
import global_values.Functions;
import global_values.MyDatabase;
import global_values.PHP;
import global_values.PostValues;
import global_values.Values;
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
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

public class NeighborhoodActivity extends Activity{

	//This array adapter will be the heart of the list.
	private AdapterPlacesListView mAdapter;
	private ListView listNeighborhood;
	//Threads.
	Thread thread,timerThread;
	//These TextViews will show titles.
	private TextView txttNeighborhood,txtChoose;
	private ImageView imgAD;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_places_neighborhood);
						
		if (Build.VERSION.SDK_INT>=14){
			getActionBar().setDisplayHomeAsUpEnabled(true);
			setTitle(Values.TEMP_NAME_CITY);
			//Load title font.
			int titleId = getResources().getIdentifier("action_bar_title", "id","android");
		    TextView txt = (TextView) findViewById(titleId);
		    Typeface tf = Typeface.createFromAsset(getAssets(), Values.FONT_PATH);
		    txt.setTypeface(tf);
			
		}
		//Get the object from UI . 
		listNeighborhood = (ListView) findViewById(R.id.activity_places_neighborhood_listView);
		txtChoose = (TextView)findViewById(R.id.txtNeighborhood);
		txttNeighborhood = (TextView) findViewById(R.id.txtChoose);						
		imgAD = (ImageView)findViewById(R.id.imgAd);
		
		
		imgAD.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				openBrowser(Values.URL_IMAGE_AD);
			};
		});
							
								
							
		final PostValues[] parameters = new PostValues[2];						
									
		parameters[0] = new PostValues();
		parameters[0].field = Values.TEMP_FIELD_STATE;
		parameters[0].value = Values.TEMP_COD_STATE;
		
		parameters[1] = new PostValues();
		parameters[1].field = Values.TEMP_FIELD_CITY;
		parameters[1].value = Values.TEMP_COD_CITY;
		
	
						
								
			 //SHOW WARMMING BOX PLEASE WAIT
			Functions.showWammingDialog(this, Values.WARNING_PLEASE_WAIT_PT);
			thread = new Thread()
			{
				@Override
				public void run() {
												
					//GETTING THE LIST
					final String result = MyDatabase.getFromPHP(PHP.php_GET_NEIGHBORHOOD,parameters);
												
					runOnUiThread(new Runnable() {
						public void run() {
													
								try{
									loadingArrayList(Functions.convertJsonToList_Places(result));						
								}
								catch(Exception e){
									Log.i("FABIO ERRO",e.toString());
								}
								finally{
									Functions.closeWammingDialog();
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
		txttNeighborhood.setTypeface(tf);
	
	}
	
	
	public void openBrowser(String url) {

		Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse(url));
		startActivity(intent);
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
	
	public void onClick(View view)
	{
		//Get the state object selected.
		Places state = mAdapter.getItem(listNeighborhood.getPositionForView(view));		
		
		Values.TEMP_COD_NEIGHBORHOOD = state.getCod();
		Values.TEMP_NAME_NEIGHBORHOOD = state.getName();
										
		Intent i = new Intent("android.intent.action.PLACES");
		startActivity(i);
	}
	
	private void cleanTempDataNeighborhood()
	{
		Values.TEMP_COD_CITY = "";
		Values.TEMP_NAME_CITY = "";
		
		finish();
	}
								
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		
		cleanTempDataNeighborhood();		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home)
		{			
			cleanTempDataNeighborhood();
		}
			return super.onOptionsItemSelected(item);
	}
								
								
	public void loadingArrayList(ArrayList<Places> itens)
	{
		mAdapter = new AdapterPlacesListView(this,itens,getAssets());
									
		listNeighborhood.setAdapter(mAdapter);
									
	}

							
							
}
