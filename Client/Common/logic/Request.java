package logic;

import java.io.Serializable;

public class Request implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * the identification of the request
	 */
	private int request_id;
	
	/**
	 * the identification of the customer 
	 */
	private int customer_id;
	
	/**
	 *  request type
	 */
	private String type;
	
	/**
	 *  request status 
	 */
	private String status;
	
	/**
	 * constructor that sets all attributes to default value
	 */
	public Request() {
		
	}
	
	/**
	 * constructor that gets the information of request report and sets all attribute to new values
	 * @param request_id - request id
	 * @param customer_id - customer id
	 * @param type - request type
	 * @param status - request status
	 */
	public Request(int request_id, int customer_id, String type,String status) {
		this.request_id = request_id;
		this.customer_id = customer_id;
		this.type = type;
		this.status = status;
	}
	
	/**
	 * @return status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Sets request status
	 * @param status - request status
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return request id
	 */
	public int getRequest_id() {
		return request_id;
	}

	/**
	 * Sets request id
	 * @param request_id
	 */
	public void setRequest_id(int request_id) {
		this.request_id = request_id;
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
	 * @return type of request
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets request type
	 * @param type - request type
	 */
	public void setType(String type) {
		this.type = type;
	}
}

