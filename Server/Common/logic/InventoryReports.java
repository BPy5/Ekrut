package logic;

import java.io.Serializable;

public class InventoryReports implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * the location of the report
	 */
	private String location;
	
	/**
	 * the inventory in the report, the amount of each item at the beginning of the month
	 */
	private String inventory;
	
	/**
	 * the current stock in the machine
	 */
	private String stock;
	
	/**
	 * the month of the report
	 */
	private String month; 
	
	/**
	 * the year of the report
	 */
	private String year;
	
	/**
	 *  the identification of the report
	 */
	private int report_id;
	
	/**
	 * the identification of the machine
	 */
	private int machine_id;
	
	/**
	 *  constructor that sets all attributes to default value
	 */
	public InventoryReports() {
		
	}
	
	/**
	 * constructor that gets the information of inventory reports and sets all attribute to new values
	 * @param location - location of the report
	 * @param inventory - machine stock at the start of the month
	 * @param stock -  machine stock at the end of the month
	 * @param month - the month of the report
	 * @param year - the year of the report
	 * @param report_id - report id
	 * @param machine_id - machine id
	 */
	public InventoryReports(String location, String inventory, String stock, String month, String year, int report_id, int machine_id) {
		super();
		this.location = location;
		this.inventory = inventory;
		this.stock = stock;
		this.month = month;
		this.year = year;
		this.report_id = report_id;
		this.machine_id = machine_id;
	}
	
	/**
	 * @return inventory
	 */
	public String getInventory() {
		return inventory;
	}

	/**
	 * Sets inventory
	 * @param inventory - machine stock at the start of the month
	 */
	public void setInventory(String inventory) {
		this.inventory = inventory;
	}

	/**
	 * @return location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * Sets location
	 * @param location - location of the report
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * @return stock
	 */
	public String getStock() {
		return stock;
	}

	/**
	 * Sets stock
	 * @param stock - machine stock at the end of the month
	 */
	public void setStock(String stock) {
		this.stock = stock;
	}

	/**
	 * @return month
	 */
	public String getMonth() {
		return month;
	}

	/**
	 * Sets month of the report
	 * @param month - the month of the report
	 */
	public void setMonth(String month) {
		this.month = month;
	}

	/**
	 * @return year
	 */
	public String getYear() {
		return year;
	}

	/**
	 * Sets year of the report
	 * @param year - the year of the report
	 */
	public void setYear(String year) {
		this.year = year;
	}

	/**
	 * @return report id
	 */
	public int getReport_id() {
		return report_id;
	}

	/**
	 * Sets report id
	 * @param report_id
	 */
	public void setReport_id(int report_id) {
		this.report_id = report_id;
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
	 *  returns the objects in the form of a string
	 */
	@Override
	public String toString() {
		return "InventoryReports [location=" + location + ", stock=" + stock + ", month=" + month + ", year=" + year
				+ ", report_id=" + report_id + ", machine_id=" + machine_id + "]";
	}
	
}
