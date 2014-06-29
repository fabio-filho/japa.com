package objectsOfLists;

import global_values.Functions;

public class RestaurantItensListView {

    private String name,cod,address;
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	private int image_id;
	
	
    public String getCod() {
		return cod;
	}

	public void setCod(String cod) {
		this.cod = cod;
	}


	public RestaurantItensListView() {

    }
	public RestaurantItensListView(String name) {
    	
    	this.name = name;

    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getImage_id() {
		return image_id;
	}

	public void setImage_id(int image_id) {
		this.image_id = image_id;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.name;
	}

}