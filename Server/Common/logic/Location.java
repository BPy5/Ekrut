package logic;

import java.io.Serializable;

public class Location implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * location name 
	 */
	private String location;
	
	/**
	 * the value of the sale 
	 */
	private int sale_value;
	
	/**
	 * constructor that sets all attributes to default value
	 */
	public Location() {
		
	}
	
	/**
	 * constructor that gets the information of location and sale value sets all attribute to new values
	 * @param location - location name
	 * @param sale_value - the value of the sale
	 */
	public Location(String location, int sale_value) {
		this.location = location;
		this.sale_value = sale_value;
	}

	/**
	 * @return location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * Sets location
	 * @param location
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * @return sale value
	 */
	public int getSale_value() {
		return sale_value;
	}

	/**
	 * Sets sale value
	 * @param sale_value - the value of the sale
	 */
	public void setSale_value(int sale_value) {
		this.sale_value = sale_value;
	}
	
}
