package objectsOfLists;

import android.util.Log;
import android.widget.Toast;

public class Telephone {
	
	private String number, cod, cod_cliente;

	public String getNumber() {
		return number;
	}
	

	public String getNumberCall() {
		return tranformTelephoneToCall(number);
	}
	
	public void setNumber(String number) {
		this.number = number;
	}

	public String getCod() {
		return cod;
	}

	public void setCod(String cod) {
		this.cod = cod;
	}

	public String getCod_cliente() {
		return cod_cliente;
	}

	public void setCod_cliente(String cod_cliente) {
		this.cod_cliente = cod_cliente;
	}
	
	//Transform the number telephone to readable.			
	public  static String tranformTelephoneToShow(String number)
	{
		
		String newNumber="";
		
		
		for (int index=4 ;index < number.length();index++)
		{
			newNumber+=number.charAt(index);
			
			if(index==7)
			{
				newNumber+='-';
			}
						
		}
		
		
		return newNumber;
	}
	
	//Transform the number telephone to call.			
	public static String tranformTelephoneToCall(String number)
	{
		String newNumber="";
		
		for (int index=4 ;index < number.length();index++)
		{
			newNumber+=number.charAt(index);			
						
		}
		
		return newNumber;
	}
		
	
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return tranformTelephoneToShow(number);
	}

}
