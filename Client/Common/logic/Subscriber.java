package logic;

import java.io.Serializable;

public class Subscriber implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * user first name 
	 */
	private String Fname;
	
	/**
	 * user last name 
	 */
	private String LName;
	
	/**
	 * the user phone number
	 */
	private String PhoneNum;
	
	/**
	 * the user email
	 */
	private String Email;
	
	/**
	 * the user credit card number 
	 */
	private String Visa;
	
	/**
	 *  the username of the user
	 */
	private String UserName;
	
	/**
	 * the user password 
	 */
	private String Password;
	
	/**
	 * the user's role
	 */
	private String Role;
	
	/**
	 * the identification of the user
	 */
	private int id;
	
	/**
	 * the subscriber number
	 */
	private int SubNum;
	
	/**
	 *  represents if the user is new subscriber 
	 */
	private int Is_new_subscriber;
	
	/**
	 * constructor that sets all attributes to default value
	 */
	public Subscriber() { }

	/**
	 * constructor that gets the information of users and sets all attribute to new values
	 * @param fname - user first name
	 * @param lName - user last name
	 * @param Id - user ID
	 * @param phoneNum - user phone number
	 * @param email - user email
	 * @param visa - user credit card
	 * @param subNum - user subscriber number
	 * @param username - username
	 * @param password - user password
	 * @param role - user role
	 * @param is_new_subscriber - represents if the user is new subscriber 
	 */
	public Subscriber(String fname, String lName, int Id, String phoneNum, String email, String visa, int subNum,String username,String password, String role,int is_new_subscriber) {
		super();
		Fname = fname;
		LName = lName;
		id = Id;
		PhoneNum = phoneNum;
		Email = email;
		Visa = visa;
		SubNum = subNum;
		UserName = username;
		Password = password;
		Role = role;
		Is_new_subscriber = is_new_subscriber;
	}

	/**
	 * @return user id
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Sets user id
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * @return user first name
	 */
	public String getFname() {
		return Fname;
	}
	
	/**
	 * Sets user first name
	 * @param fname
	 * 
	 */
	public void setFname(String fname) {
		Fname = fname;
	}
	
	/**
	 * @return user last name
	 */
	public String getLName() {
		return LName;
	}
	
	/**
	 * Sets user last name
	 * @param lName
	 */
	public void setLName(String lName) {
		LName = lName;
	}
	
	/**
	 * @return user phone number
	 */
	public String getPhoneNum() {
		return PhoneNum;
	}
	
	/**
	 * Sets user phone number
	 * @param phoneNum
	 */
	public void setPhoneNum(String phoneNum) {
		PhoneNum = phoneNum;
	}
	
	/**
	 * @return user email
	 */
	public String getEmail() {
		return Email;
	}
	
	/**
	 * Sets user email
	 * @param email
	 */
	public void setEmail(String email) {
		Email = email;
	}
	
	/**
	 * @return  user credit card
	 */
	public String getVisa() {
		return Visa;
	}
	
	/**
	 * Sets user credit card
	 * @param visa
	 */
	public void setVisa(String visa) {
		Visa = visa;
	}
	
	/**
	 * @return subscriber number
	 */
	public int getSubNum() {
		return SubNum;
	}
	
	/**
	 * Sets subscriber number
	 * @param subNum
	 */
	public void setSubNum(int subNum) {
		SubNum = subNum;
	}
	
	/**
	 * @return user name
	 */
	public String getUserName() {
		return UserName;
	}

	/**
	 * Sets user name
	 * @param userName
	 */
	public void setUserName(String userName) {
		UserName = userName;
	}

	/**
	 * @return user password
	 */
	public String getPassword() {
		return Password;
	}

	/**
	 * Sets user password
	 * @param password
	 */
	public void setPassword(String password) {
		Password = password;
	}
	
	/**
	 * @return user role
	 */
	public String getRole() {
		return Role;
	}

	/**
	 * Sets user role
	 * @param role
	 */
	public void setRole(String role) {
		Role = role;
	}
	
	
	/**
	 * @return is new subscriber
	 */
	public int getIs_new_subscriber() {
		return Is_new_subscriber;
	}

	/**
	 * Sets is new subscriber
	 * @param is_new_subscriber - represents if the user is new subscriber 
	 */
	public void setIs_new_subscriber(int is_new_subscriber) {
		Is_new_subscriber = is_new_subscriber;
	}

	/**
	 * returns the objects in a string
	 */
	@Override
	public String toString() {
		return this.Fname + " " + this.LName + " " + this.id + " " + this.Email + " " + this.PhoneNum + " " + this.Visa + " " + this.SubNum +" "+ this.UserName+" "+ this.Password;
	}
	
	
	/**
	 * returns true if the id of two users are equal 
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		
		if (obj.getClass() == this.getClass()) {
			Subscriber other = (Subscriber)obj;
			if (other.getId() == this.id)
				return true;
		}
		
		return false;
	}
}
