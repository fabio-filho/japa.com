package com.filho.japa.com;


import global_values.Values;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;


public class SplashScreenActivity extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);
		//Check phone's SDK version.
		if (Build.VERSION.SDK_INT>=14){
			//Load title font.
			int titleId = getResources().getIdentifier("action_bar_title", "id","android");
		    TextView txt = (TextView) findViewById(titleId);
			Typeface tf = Typeface.createFromAsset(getAssets(), Values.FONT_PATH);
		    txt.setTypeface(tf);					
		}
		
		//This thread is for take a time. 
		Thread tr = new Thread()
		{			
			public void run(){
				
				try{					
					sleep(4000);				
				}
				catch(Exception o){
				}
				finally
				{
					Intent intent = new Intent("android.intent.action.MENU");
					startActivity(intent);
					finish();
				}			
			}			
		};		
		
		tr.start();
		
	}
	
	@Override
	public void onBackPressed() {
		//Nothing to do.
	}
	
	

}
