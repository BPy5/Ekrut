package logic;

import java.io.Serializable;
import java.sql.Date;

public class Delivery implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * delivery id
	 */
	int delivery_id;
	
	/**
	 * order id
	 */
	int order_id;
	
	/**
	 * the customer's id
	 */
	int customer_id;
	
	/**
	 * the total price of the delivery
	 */
	int total_price;
	
	/**
	 * the shipping date of the delivery
	 */
	Date shipping_date;
	
	/**
	 * the estimated delivery date of the delivery
	 */
	Date estimated_delivery;
	
	/**
	 * the status of the delivery
	 */
	String status;
	
	/**
	 * the location of the delivery
	 */
	String location;
	
	/**
	 * the address for the delivery
	 */
	String address;
	
	/**
	 * constructor that sets all attributes to default value
	 */
	public Delivery() { 
	}
	
	/**
	 * constructor that gets the information of delivery and sets all attribute to new values
	 * @param delivery_id 
	 * @param order_id
	 * @param customer_id
	 * @param shipping_date
	 * @param estimated_delivery
	 * @param status
	 * @param total_price
	 * @param location
	 * @param address
	 */
	public Delivery(int delivery_id, int order_id, int customer_id, Date shipping_date, Date estimated_delivery, String status, int total_price, String location, String address) {
		this.delivery_id = delivery_id;
		this.order_id = order_id;
		this.customer_id = customer_id;
		this.shipping_date = shipping_date;
		this.estimated_delivery = estimated_delivery;
		this.status = status;
		this.total_price = total_price;
		this.location = location;
		this.address = address;
	}

	/**
	 * @return delivery id
	 */
	public int getDelivery_id() {
		return delivery_id;
	}

	/**
	 * @return address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * Sets address
	 * @param address
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * Sets delivery id 
	 * @param delivery_id
	 */
	public void setDelivery_id(int delivery_id) {
		this.delivery_id = delivery_id;
	}

	/**
	 * @return order id
	 */
	public int getOrder_id() {
		return order_id;
	}

	/**
	 * @param order_id
	 */
	public void setOrder_id(int order_id) {
		this.order_id = order_id;
	}

	/**
	 * @return customer id
	 */
	public int getCustomer_id() {
		return customer_id;
	}

	/**Sets customer id
	 * @param customer_id
	 */
	public void setCustomer_id(int customer_id) {
		this.customer_id = customer_id;
	}

	/**
	 * @return total price
	 */
	public int getTotal_price() {
		return total_price;
	}

	/**Sets total price
	 * @param total_price
	 */
	public void setTotal_price(int total_price) {
		this.total_price = total_price;
	}

	/**
	 * @return shipping date 
	 */
	public Date getShipping_date() {
		return shipping_date;
	}

	/**Sets shipping date
	 * @param shipping_date
	 */
	public void setShipping_date(Date shipping_date) {
		this.shipping_date = shipping_date;
	}

	/**
	 * @return estimated delivery date
	 */
	public Date getEstimated_Delivery() {
		return estimated_delivery;
	}

	
	/**
	 * Sets estimated delivery date
	 * @param estimated_dleivery
	 */
	public void setEstimated_Delivery(Date estimated_dleivery) {
		this.estimated_delivery = estimated_dleivery;
	}

	
	/**
	 * @return status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Sets status
	 * @param status
	 */
	public void setStatus(String status) {
		this.status = status;
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
	 * returns true if two objects are equal 
	 */
	@Override
	public boolean equals(Object obj) {
		Delivery temp;
		if (obj == null)
			return false;
		
		if (obj.getClass() == this.getClass()) {
			temp = (Delivery)obj;
			if (temp.getDelivery_id() == this.getDelivery_id())
				return true;
		}
		
		return false;
	}
}
