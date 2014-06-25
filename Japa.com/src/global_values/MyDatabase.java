package global_values;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

public class MyDatabase extends Activity{
  	

	//GET STATE OF DATABASE
	public static String getFromPHP(String php,PostValues[] parameters)
	{	
		PHP.STATE_OF_CONNECTION = true;
		return getListFromDatabase_with_POST(php,parameters);
	}
	
	
	private static String getListFromDatabase_with_GET(String php, String parameters) {
		
		HttpClient httpClient = new DefaultHttpClient();
		HttpContext localContext = new BasicHttpContext();
		HttpResponse response = null;
		String result = null;
		
		HttpGet httpget = new HttpGet(
				PHP.IP+"/"+php+ parameters);
		
		//Log.i("FILHO FABIO", PHP.IP+"/"+php+ parameters);		
		
		try {
			response = httpClient.execute(httpget, localContext);			
			HttpEntity entity = response.getEntity();
			result = EntityUtils.toString(entity,"UTF-8");
			Log.i("FILHO FABIO", "OK");	
		} catch (Exception  e) {
			
			Log.i("FILHO ERROOOOOOOO", e.toString());
		}
		return result;
		}
	
	
	private static String getListFromDatabase_with_POST(String php,PostValues[] parameters) {

		String result = null;
		HttpClient httpClient = new DefaultHttpClient();
		HttpContext localContext = new BasicHttpContext();
		HttpPost httpPost = new HttpPost(
				PHP.IP+"/"+php);
		Log.i("FILHO FABIO", PHP.IP+"/"+php);
		HttpResponse response = null;
		
		try {
			if (parameters!=null){
				
				List<NameValuePair> params = new ArrayList<NameValuePair>(0);
				
				
				params.add(new BasicNameValuePair("imei",Values.TEMP_IMEI));	
			
				for(int i=0; i < parameters.length; i++)
				{				
					params.add(new BasicNameValuePair(parameters[i].field, parameters[i].value));				
				}
				
				httpPost.setEntity(new UrlEncodedFormEntity(params));
				Log.i("FILHO FABIO", "OK POST");	
			}
			response = httpClient.execute(httpPost, localContext);
			HttpEntity entity = response.getEntity();
			result = EntityUtils.toString(entity,"UTF-8");
			
		} catch (Exception e) {
			Log.i("FILHO ERRO POST", e.toString());	
		}		
		return result;

	}
	
	
	
	
}
