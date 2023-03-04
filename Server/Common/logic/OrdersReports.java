package logic;

import java.io.Serializable;

public class OrdersReports implements Serializable
{

	
	private static final long serialVersionUID = 1L;
	
	/**
	 * the identification of the report
	 */
	private String report_id;
	
	/**
	 *the identification of the machine 
	 */
	private String machine_id;
	
	/**
	 * the location of the report
	 */
	private String location;
	
	/**
	 * report's data 
	 */
	private String data;
	
	/**
	 * the month of the report
	 */
	private String month;
	
	/**
	 * the year of the report 
	 */
	private String year;
	
	/**
	 * constructor that sets all attributes to default value
	 */
	public OrdersReports()
	{
		
	}
	/**
	 *  constructor that gets the information of order report and sets all attribute to new values
	 * @param report_id - report id
	 * @param machine_id - machine id 
	 * @param location - the location of the report
	 * @param data - report data
	 * @param month - the month of the report
	 * @param year - the year of the report
	 */
	public OrdersReports(String report_id, String machine_id, String location, String data, String month, String year) {
		
		this.report_id = report_id;
		this.machine_id = machine_id;
		this.location = location;
		this.data = data;
		this.month = month;
		this.year = year;
	}
	
	/**
	 * @return report id
	 */
	public String getReport_id() {
		return report_id;
	}
	
	/**
	 * Sets report id
	 * @param report_id
	 */
	public void setReport_id(String report_id) {
		this.report_id = report_id;
	}
	
	
	/**
	 * @return machine id
	 */
	public String getMachine_id() {
		return machine_id;
	}
	
	/**
	 * Sets machine id
	 * @param machine_id
	 */
	public void setMachine_id(String machine_id) {
		this.machine_id = machine_id;
	}
	
	/**
	 * @return location
	 */
	public String getLocation() {
		return location;
	}
	
	/**
	 * Sets location
	 * @param location - the location of the report
	 */
	public void setLocation(String location) {
		this.location = location;
	}
	/**
	 * @return data
	 */
	public String getData() {
		return data;
	}
	/**
	 * Sets report data
	 * @param data - report data
	 */
	public void setData(String data) {
		this.data = data;
	}
	/**
	 * @return month
	 */
	public String getMonth() {
		return month;
	}
	/**
	 * Sets month
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
	 * Sets year
	 * @param year - the year of the report
	 */
	public void setYear(String year) {
		this.year = year;
	}
	/**
	 *  returns the objects in the form of a string
	 */
	@Override
	public String toString() {
		return "OrdersReports [report_id=" + report_id + ", machine_id=" + machine_id + ", location=" + location
				+ ", data=" + data + ", month=" + month + ", year=" + year + "]";
	}
	
	
	
}
