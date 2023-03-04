package logic;

import java.io.Serializable;

public class UsersReports implements Serializable{


	private static final long serialVersionUID = 1L;

	/**the identification of the report
	 * 
	 */
	private int report_id;
	
	/**
	 * the month of the report
	 */
	private String month;
	
	/**
	 * the year of the report
	 */
	private String year;
	
	/**
	 * report data 
	 */
	private String data;
	
	
	/**
	 * constructor that sets all attributes to default value
	 */
	public UsersReports() {
	
	}
	
	/**
	 * constructor that gets the information of users reports and sets all attribute to new values
	 * @param report_id - report id 
	 * @param month - the month of the report
	 * @param year- the year of the report
	 * @param data - report data
	 */
	public UsersReports(int report_id, String month, String year, String data) {
		super();
		this.report_id = report_id;
		this.month = month;
		this.year = year;
		this.data = data;
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
	 * @return data
	 */
	public String getData() {
		return data;
	}
	
	/**
	 * Sets data
	 * @param data - report data
	 */
	public void setData(String data) {
		this.data = data;
	}

	/** 
	 *  returns the objects in the form of a string
	 */
	@Override
	public String toString() {
		return "UsersReports [report_id=" + report_id + ", month=" + month + ", year=" + year + ", data=" + data + "]";
	}
}
