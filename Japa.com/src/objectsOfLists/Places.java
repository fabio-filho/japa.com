package objectsOfLists;

import java.io.Serializable;

public class Places implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private String cod,name,address,description,cod_owner;
	

	public String getCod_owner() {
		return cod_owner;
	}
	public void setCod_owner(String cod_owner) {
		this.cod_owner = cod_owner;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCod() {
		return cod;
	}
	public void setCod(String string) {
		this.cod = string;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return getName();
	}

}
