package logic;

import java.io.Serializable;

public class StockRequest implements Serializable{

	
	private static final long serialVersionUID = 1L;
	/**
	 * the identification of the stock request
	 */
	private int stock_request_id;
	
	/**
	 * the identification of the machine
	 */
	private int machine_id;
	
	/**
	 * the date when the request was resolved 
	 */
	private String resolved_date;
	
	/**
	 * request status
	 */
	private String status;
	
	/**
	 * constructor that gets the information of stock request and sets all attribute to new values
	 * @param stock_request_id - the id of the request
	 * @param machine_id - machine id
	 * @param resolved_date - the date when the request was resolved
	 * @param status - request status
	 */
	public StockRequest(int stock_request_id,int machine_id,String resolved_date,String status)
	{
		this.stock_request_id = stock_request_id;
		this.machine_id = machine_id;
		this.resolved_date = resolved_date;
		this.status = status;
	}

	/**
	 * constructor that sets all attributes to default value
	 */
	public StockRequest() {
	}

	/**
	 * @return stock request id
	 */
	public int getStock_request_id() {
		return stock_request_id;
	}

	/**
	 * Sets stock request id
	 * @param stock_request_id
	 */
	public void setStock_request_id(int stock_request_id) {
		this.stock_request_id = stock_request_id;
	}

	/**
	 * @return machine id
	 */
	public int getMachine_id() {
		return machine_id;
	}

	/**
	 * Sets machine id
	 * @param machine_id
	 */
	public void setMachine_id(int machine_id) {
		this.machine_id = machine_id;
	}

	
	/**
	 * @return resolved date
	 */
	public String getResolved_date() {
		return resolved_date;
	}

	/**
	 * Sets the date when the request was resolved
	 * @param resolved_date - the date when the request was resolved
	 */
	public void setResolved_date(String resolved_date) {
		this.resolved_date = resolved_date;
	}

	/**
	 * @return status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Sets status
	 * @param status - request status
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	
	/**
	 * returns the objects in a string
	 */
	@Override 
	public String toString()
	{
		return "Stock Request : " + this.machine_id + " -> " + this.resolved_date;
	}

}
