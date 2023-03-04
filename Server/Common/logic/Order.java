package logic;

import java.io.Serializable;
import java.sql.Date;

/**
 * @author user
 *
 */
public class Order implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 *  the order number
	 */
	private int order_num;
	
	/**
	 *  the identification of the customer
	 */
	private int customer_id;
	
	/**
	 *the price of the order 
	 */
	private int price;
	
	/**
	 *the identification of machine 
	 */
	private int machine_id;
	
	/**
	 * order status 
	 */
	private String order_status;
	
	/**
	 * the location in which the order was created
	 */
	private String location;
	
	/**
	 * user's credit card number
	 */
	private String credit_card_number;
	
	/**
	 * items in the order
	 */
	private String items_in_order;
	
	/**
	 * the order supply method
	 */
	private String supply_method;
	
	/**
	 * order created date
	 */
	private Date order_created;
	
	/**
	 * constructor that sets all attributes to default value
	 */
	public Order() { }

	/**
	 *  constructor that gets the information of order and sets all attribute to new values
	 * @param order_num - order number
	 * @param customer_id - customer id
	 * @param price - order price
	 * @param order_status - order status
	 * @param location - the location in which the order was created
	 * @param credit_card_number - user credit card number
	 * @param items_in_order - items in order
	 * @param supply_method - the order supply method
	 * @param order_created - order created date
	 * @param machine_id - the id of the machine in which the order was created 
	 */
	public Order(int order_num, int customer_id, int price, String order_status, String location,String credit_card_number, 
			String items_in_order, String supply_method, Date order_created, int machine_id) {
		super();
		this.order_num = order_num;
		this.customer_id = customer_id;
		this.price = price;
		this.order_status = order_status;
		this.location = location;
		this.credit_card_number = credit_card_number;
		this.items_in_order = items_in_order;
		this.supply_method = supply_method;
		this.order_created = order_created;
		this.machine_id = machine_id;
	}
	
	

	/**
	 * @return credit card number
	 */
	public String getCredit_card_number() {
		return credit_card_number;
	}

	/**
	 * Sets credit card number
	 * @param credit_card_number - user credit card number
	 */
	public void setCredit_card_number(String credit_card_number) {
		this.credit_card_number = credit_card_number;
	}

	/**
	 * @return order number
	 */
	public int getOrder_num() {
		return order_num;
	}

	/**
	 * Sets order number
	 * @param order_num - order number
	 */
	public void setOrder_num(int order_num) {
		this.order_num = order_num;
	}

	/**
	 * @return customer id
	 */
	public int getCustomer_id() {
		return customer_id;
	}

	/**
	 * Sets customer id
	 * @param customer_id
	 */
	public void setCustomer_id(int customer_id) {
		this.customer_id = customer_id;
	}

	/**
	 * @return price
	 */
	public int getPrice() {
		return price;
	}

	/**
	 * Sets price
	 * @param price - order price
	 */
	public void setPrice(int price) {
		this.price = price;
	}

	/**
	 * @return order status
	 */
	public String getOrder_status() {
		return order_status;
	}

	/**
	 * Sets order status
	 * @param order_status
	 */
	public void setOrder_status(String order_status) {
		this.order_status = order_status;
	}

	/**
	 * @return location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * Sets location
	 * @param location - the location in which the order was created
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * @return items in order
	 */
	public String getItems_in_order() {
		return items_in_order;
	}

	/**
	 * Sets items in order
	 * @param items_in_order
	 */
	public void setItems_in_order(String items_in_order) {
		this.items_in_order = items_in_order;
	}

	/**
	 * @return supply method
	 */
	public String getSupply_method() {
		return supply_method;
	}

	/**
	 * Sets supply method
	 * @param supply_method - the order supply method
	 */
	public void setSupply_method(String supply_method) {
		this.supply_method = supply_method;
	}

	/**
	 * @return order created date
	 */
	public Date getOrder_created() {
		return order_created;
	}

	/**
	 * Sets order created date
	 * @param order_created - order created date
	 */
	public void setOrder_created(Date order_created) {
		this.order_created = order_created;
	}

	/**
	 * Sets machine id
	 * @param machine_id - the id of the machine in which the order was created 
	 */
	public void setMachine_id(int machine_id) {
		this.machine_id = machine_id;
	}
	
	/**
	 * @return machine id
	 */
	public int getMachine_id() {
		return this.machine_id;
	}
	
	/**
	 * returns true if order number , items in order and customer id are equal in two objects
	 */
	@Override
	public boolean equals(Object obj) {
		Order temp;
		if (obj == null)
			return false;
		
		if (obj.getClass() == this.getClass()) {
			temp = (Order)obj;
			if (temp.getOrder_num() == this.getOrder_num() && temp.getItems_in_order().equals(this.getItems_in_order())
					&& temp.getCustomer_id() == this.getCustomer_id())
				return true;
		}
		
		return false;
	}	
}
