package logic;

import java.io.Serializable;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter.DEFAULT;

public class Connected implements Serializable {
	
	private static final long serialVersionUID = 1L;
	/**
	 * user IP
	 */
	private String ip;
	
	/**
	 * The server's host
	 */
	private String Host;
	
	/**
	 * user status
	 */
	private String Status;
	
	/**
	 * the connected users's ID
	 */
	private int connectedUserID;
	
	/**
	 * constructor that sets all attributes to default value
	 */
	public Connected() { }

	/**
	 * constructor that gets the information of connected users and sets all attribute to new values
	 * @param ip - user IP
	 * @param Host - the server's host
	 * @param Status - user status
	 * @param connectedUserID - the connected user ID
	 */
	public Connected(String ip, String Host, String Status, int connectedUserID) {
		this.ip = ip;
		this.Host = Host;
		this.Status = Status;
		this.connectedUserID = connectedUserID;
	}
	
	/**
	 * @return connectedUserID
	 */
	public int getConnectedUserID() {
		return connectedUserID;
	}

	/**
	 * Sets connectedUserID
	 * @param connectedUserID - the connected user ID
	 */
	public void setConnectedUserID(int connectedUserID) {
		this.connectedUserID = connectedUserID;
	}
	
	/**
	 * @return ip
	 */
	public String getIp() {
		return this.ip;
	}

	/**
	 * Sets IP
	 * @param ip - user IP
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * @return Host
	 */
	public String getHost() {
		return Host;
	}

	/**
	 * Sets host
	 * @param host - the server's host
	 */
	public void setHost(String host) {
		Host = host;
	}

	/**
	 * @return Status
	 */
	public String getStatus() {
		return Status;
	}

	/**
	 * Sets status - user status
	 * @param status
	 */
	public void setStatus(String status) {
		Status = status;
	}
}