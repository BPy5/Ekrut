package gui_client;

public class Details {
	
	private String password,role,Fname;
	private int ID;
	private boolean isSubscriber,FirstSubscriberOrder;
	
	public Details() {}
	
	public Details(String password, String role, String fname, int iD, boolean isSubscriber,boolean firstSubscriberOrder) {
		super();
		this.password = password;
		this.role = role;
		Fname = fname;
		ID = iD;
		this.isSubscriber = isSubscriber;
		FirstSubscriberOrder = firstSubscriberOrder;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getFname() {
		return Fname;
	}

	public void setFname(String fname) {
		Fname = fname;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public boolean isSubscriber() {
		return isSubscriber;
	}

	public void setSubscriber(boolean isSubscriber) {
		this.isSubscriber = isSubscriber;
	}

	public boolean isFirstSubscriberOrder() {
		return FirstSubscriberOrder;
	}

	public void setFirstSubscriberOrder(boolean firstSubscriberOrder) {
		FirstSubscriberOrder = firstSubscriberOrder;
	}
	
	
	
	

}
