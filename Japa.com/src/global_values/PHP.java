package global_values;

public class PHP {
	
	public final static String
	IP = "http://104.236.46.185/projects/japacom/php",
	php_GET_CITY="get_city.php",
	php_GET_COUNTRY="get_country.php"	,
	php_GET_NEIGHBORHOOD="get_neighborhood.php",
	php_GET_STATE="get_state.php",
	php_GET_RESTAURANTS="get_restaurants.php",
	php_GET_RESTAURANT_DETAILS="get_restaurant_details.php",
	php_GET_TELEPHONE="get_telephone.php";

	
	public static Boolean 
	
	STATE_OF_CONNECTION = false;
	
	
	public final static int  
		
		TIME_MAX_FOR_CONNECTION = 6000;
	
	

	//Creating delay Max of connection
	public static Thread createDelayThread()
	{
		Thread thread = new Thread(){
			
			@Override
			public void run() {
				
				try {					
					sleep(TIME_MAX_FOR_CONNECTION);
				}
				catch(Exception o){}
				
			}
			
		};
		
		return thread;
	}

	
	
}
