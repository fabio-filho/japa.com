package global_values;

import java.util.ArrayList;
import java.util.List;

import objectsOfLists.Places;
import objectsOfLists.RestaurantItensListView;
import objectsOfLists.Telephone;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Build;
import android.util.Log;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.filhossi.japa.com.R;

public class Functions extends Activity{
	
	//Var for Warning Dialog .
	private static ProgressDialog dialog;	
	
	
	///Open warning DIALOG	
	public static void showWammingDialog(Context context,String msg)
	{
			
		if (Build.VERSION.SDK_INT>=14){
			dialog = new ProgressDialog(context,AlertDialog.THEME_DEVICE_DEFAULT_DARK);
		}
		else{
			dialog = new ProgressDialog(context);
		}
        dialog.setMessage(msg);
        dialog.setCancelable(false);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();
		
	}
	
	//Close warning Dialog
	public static void closeWammingDialog()
	{
		dialog.dismiss();		
	}
	
	
	
	
	
	///CONVERT JASON TO LIST MAIN LOCATION
	public static ArrayList<Places> convertJsonToList_Places(String resultConnection)
	{
		ArrayList<Places> list = new ArrayList<Places>();
	
		try
		{
			JSONArray json= new JSONArray(resultConnection);
		
			for(int i =0; i<json.length();i++)
			{	
				Places p = new Places();				
				p.setCod(json.getJSONObject(i).getString("cod"));
				p.setName(Functions.tranformToReadable(json.getJSONObject(i).getString("name")));
				list.add(p);			
				
			}
			
			Log.i("FILHO FABIO", "OK JSON");	
		}
		catch(JSONException o)
		{
			Log.i("FILHO ERRO JSON", o.toString());
		}
	
			return list;
		}
		
	
	///CONVERT JASON TO ARRAY TO PLACES
	public static ArrayList<RestaurantItensListView> convertJsonToArray(String resultConnection)
		{
			ArrayList<RestaurantItensListView> list = new ArrayList<RestaurantItensListView>();
			
			try
			{			
				JSONArray json= new JSONArray(resultConnection);
							
				
				for(int i =0; i<json.length();i++)
				{				
					RestaurantItensListView item = new RestaurantItensListView();				
					item.setCod((json.getJSONObject(i).getString("cod")));
					item.setName(Functions.tranformToReadable(json.getJSONObject(i).getString("name")));
					item.setAddress(Functions.tranformToReadable(json.getJSONObject(i).getString("address")));
					list.add(item);
				}
				
				Log.i("FILHO FABIO", "OK JSON");	
			}
			catch(Exception o)
			{Log.i("ERROR MYSQL GETTING 1", o.toString());}
		
				return list;
		
		
			}
				
		
	///CONVERT JASON TO LIST DETAILS
	public static Places convertJsonToList_To_Details(String resultConnection)
				{
					Places result = new Places();
					
					try
					{			
						JSONArray json= new JSONArray(resultConnection);
					
						for(int i =0; i<json.length();i++)
						{					
							result.setAddress(Functions.tranformToReadable(json.getJSONObject(i).getString("address")));
							result.setDescription(Functions.tranformToReadable(json.getJSONObject(i).getString("description")));
							result.setName(Functions.tranformToReadable(json.getJSONObject(i).getString("name")));
											
						}
						
						Log.i("FILHO FABIO", "OK JSON");	
					}
					catch(JSONException o)
					{
						Log.i("FILHO ERRO JSON", o.toString());
					}
				
					return result;
					}
						
								
	///CONVERT JASON TO LIST TELEPHONES
	public static List<Telephone> convertJsonToList_To_Telephone(String resultConnection)
				{
					List<Telephone> list = new ArrayList<Telephone>();
					
					try
					{			
						JSONArray json= new JSONArray(resultConnection);
					
						for(int i =0; i<json.length();i++)
						{	
							Telephone tel = new Telephone();
							tel.setCod(json.getJSONObject(i).getString("cod"));							
							tel.setNumber(json.getJSONObject(i).getString("number"));	
							list.add(tel);
						}
						
						Log.i("FILHO FABIO", "OK JSON");	
					}
					catch(JSONException o)
					{
						Log.i("FILHO ERRO JSON", o.toString());
					}
				
					return list;
					}
		
		
	
	///CONVERT JASON TO ONE TELEPHONE
	public static Telephone convertJsonTo_One_Telephone(String resultConnection)
				{
		
					List<Telephone> list = new ArrayList<Telephone>();
					
					try
					{			
						JSONArray json= new JSONArray(resultConnection);					
						
						for(int i =0; i<json.length();i++)
						{	
							Telephone tel = new Telephone();
							tel.setCod(json.getJSONObject(i).getString("cod"));							
							tel.setNumber(json.getJSONObject(i).getString("number"));	
							list.add(tel);
						}
						Log.i("FILHO OK JSON TELEPHONE","OKOKOOKOKOKOK");
					}
					catch(JSONException o)
					{
						Log.i("FILHO ERRO JSON TELEPHONE", o.toString());
					}
				
					return list.get(0);
					}
		
	
	
	
	//Ckeck connection with internet.
	public static boolean checkConnectionIntenet(ConnectivityManager conectivtyManager) {  
		
	    boolean conectado;  

	    if (conectivtyManager.getActiveNetworkInfo() != null  
	            && conectivtyManager.getActiveNetworkInfo().isAvailable()  
	            && conectivtyManager.getActiveNetworkInfo().isConnected())
	    {  
	        conectado = true;  
	    } else {  
	        conectado = false;  
	    }  
	    return conectado;  
	} 
	
	
	//Transform the text received readable.
	private static String tranformToReadable(String word)
		{
			//Check the length of params.
			if (word.length()>=4){
				//String var buffer.
				String newWord = "";
				//Loop for read the word.
				for (int index=0; index < word.length();index++)
				{	
					//Getting the word.
					newWord+= word.charAt(index);
					//Finding the char code: #!# .
					try{
						if ((word.charAt(index+1)=='#')&&(word.charAt(index+2)=='!')&&(word.charAt(index+3)=='#'))
						{
							//Getting the char correct and adding to new word.
							newWord+= "'";
							//Incrementing the index to two positions after the code char found.
							index+=3;
						}		
					}	
					catch(Exception o){}
				}			
			
				word = newWord;
			}
			
			
			return word;
		}
		
		
	//Transform the text received readable.
	public static String tranformToGoogleMapsURL(String url)
		{
			//String var buffer.
			String newUrl = "";
			//Loop for read the word.
			for (int index=0; index < url.length();index++)
			{	
				//Getting the word.
				newUrl+= url.charAt(index);
				
				if (index < url.length()-1){		
					
						if(url.charAt(index+1)==' ')
						{
							newUrl+='+';
							index++;
						}				
				}
						
			}				
				
			return newUrl;
		}
		
			//Loading listView.
			public static ListView loadingArrayList(Context context,ListView list,
					ArrayList<Places> itens, int layout)
			{
				//Setting the itens and layout to adapter.
				ArrayAdapter<Places> mAdapter;
				mAdapter = new ArrayAdapter<Places>(context,R.layout.activity_spinner_itens,itens);
				//Setting the adapter to listView.
				list.setAdapter(mAdapter);
				                                                                   
				return list;
				
			}


	
		
}