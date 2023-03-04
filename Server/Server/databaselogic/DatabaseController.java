package databaselogic;

import java.sql.Connection;


import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;

//import Client.ChatClient;
import common.Command;
import common.Message;
import gui_server.ServerInfoController;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import logic.*;
import server.EchoServer;

//This Class is built using Singleton design pattern
public class DatabaseController {
	  private Connection conn;
	  private boolean isConnectedToDB = false;
	  private static DatabaseController DBFunctionsInstance = null;  // only one instance (singleton)
	  
	  private DatabaseController() {
		  DBFunctionsInstance = this;
	  }
	
	  private DatabaseController(String dbpassword) {
		  if(isConnectedToDB == false) {
		  	  ConnectToDB(dbpassword);
			  DBFunctionsInstance = this;
		  }
	  }
	  
	  public boolean IsConnectedToDB() {
		  return isConnectedToDB;
	  }
	
	
	public void ConnectToDB(String databasePassword) {
		  boolean noErrors = true;
		  if (isConnectedToDB)
			  return;
		  try {
		      Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
//		      System.out.println("Driver definition succeed");
		      EchoServer.updateCommandText("Driver definition succeed");  
		  } catch (Exception ex) {
			  /* handle the error*/
//			  sic.screen.setText("Driver definition failed");
//			  System.out.println("Driver definition failed");
			  EchoServer.updateCommandText("Driver definition failed");
			  noErrors = false;
	      }
	      
	      try 
	      {
	          conn = DriverManager.getConnection("jdbc:mysql://localhost/ekrut?serverTimezone=IST", "root", databasePassword);
//	          System.out.println("SQL connection succeed");
	          EchoServer.updateCommandText("SQL connection succeed");
	   	  } catch (SQLException ex)  { /* handle any errors*/
//				System.out.println("SQLException: " + ex.getMessage());
//				System.out.println("SQLState: " + ex.getSQLState());
//				System.out.println("VendorError: " + ex.getErrorCode());
				EchoServer.updateCommandText("SQLException: " + ex.getMessage());
				EchoServer.updateCommandText("SQLState: " + ex.getSQLState());
				EchoServer.updateCommandText("VendorError: " + ex.getErrorCode());
				noErrors = false;
	   	  }
	      
	      if(noErrors)
	    	  isConnectedToDB = true;
	  }
	
	public void ImportExternalUsers() {
		ArrayList<Subscriber> externalUsers = new ArrayList<>();
		ArrayList<Object> externalUsersAsObj = new ArrayList<>();
		int size;
		String query = "INSERT INTO users (first_name, last_name, ID, phone_number, email_address, "
				+ "credit_card_number, subscriber_number, user_name, password, role, is_new_subscriber) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement ps;
		
		try {
			//get external users
			externalUsersAsObj = ReadFromDB(new Message(0, Command.ReadExternalTable));
			size = externalUsersAsObj.size();
			
			//convert the user objects to subscriber and add them to arraylist
			for (Object user : externalUsersAsObj)
				externalUsers.add((Subscriber) user);
			
			//Update database
			ps = conn.prepareStatement(query);
			for (Subscriber user : externalUsers) {
				ps.setString(1, user.getFname());
				ps.setString(2, user.getLName());
				ps.setInt(3, user.getId());
				ps.setString(4, user.getPhoneNum());
				ps.setString(5, user.getEmail());
				ps.setString(6, user.getVisa());
				ps.setInt(7, user.getSubNum());
				ps.setString(8, user.getUserName());
				ps.setString(9, user.getPassword());
				ps.setString(10, user.getRole());
				ps.setInt(11, user.getIs_new_subscriber());
				ps.executeUpdate();
			}
		} catch(SQLException e) {e.printStackTrace();}
	}
	  
	  
	  public Object SaveToDB(Object message) throws SQLException {
		  
		  	PreparedStatement ps;
		  	Message msg = (Message)message;
		  	ArrayList<String> data = (ArrayList<String>) msg.getContent();
		  	
		  	switch(msg.getCommand()) {
			  	case InsertUser:
				  	ps = conn.prepareStatement("INSERT INTO users "
								+ "(first_name, last_name, id, phone_number, email_address,"
								+ " credit_card_number, subscriber_number,user_name,password,role,is_new_subscriber) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?,?,?)");
						try 
						{
							/*
							 * for (int i = 1; i < 11; i++) { if ((i==3) || (i==7)) ps.setInt(i,
							 * Integer.parseInt(data.get(i-1))); else ps.setString(i, data.get(i-1)); }
							 */
	//						ps.executeQuery();
							ps.setString(1, data.get(0));
							ps.setString(2, data.get(1));
							ps.setInt(3,Integer.parseInt(data.get(2)));
							ps.setString(4, data.get(3));
							ps.setString(5, data.get(4));
							ps.setString(6, data.get(5));
							ps.setInt(7,-1);
							ps.setString(8, data.get(7));
							ps.setString(9, data.get(8));
							ps.setString(10, data.get(9));
							ps.setInt(11, 0);
							ps.executeUpdate();
						} catch (SQLException e) { e.printStackTrace(); }
						break;
		  		
		  	case InsertOrder:
		  		ps = conn.prepareStatement("INSERT INTO orders "
						+ "(order_number, customer_id, order_status, order_created,"
						+ " location, items_in_order,credit_card_number,price,supply_method,machine_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
		  		int order_id = -1;
		  		try 
				{
		  			order_id = getLastId("orders", Command.ReadOrders);
					ps.setInt(1, order_id);
					ps.setInt(2, Integer.parseInt(data.get(0))); 
					if(data.get(5).equals("Immediate pickup"))
						ps.setString(3, "Completed");
					else
						ps.setString(3, "Pending");
					ps.setDate(4, Date.valueOf(LocalDate.now()));
					ps.setString(5, data.get(1));
					ps.setString(6, data.get(2));
					ps.setString(7, data.get(3));
					ps.setInt(8, Integer.parseInt(data.get(4)));
					ps.setString(9, data.get(5));
					ps.setInt(10, Integer.parseInt(data.get(6)));
					ps.executeUpdate();
					
				} catch (SQLException e) { e.printStackTrace(); }
		  		
				ps = conn.prepareStatement("UPDATE machines " 
						+ "SET amount_per_item = ?"
						+", total_inventory = ?"
						+ " WHERE machine_id = ?"); 
				try {
					ps.setString(1, data.get(7));
					ps.setInt(2, Integer.parseInt(data.get(8)));
					ps.setInt(3, Integer.parseInt(data.get(6)));
					ps.executeUpdate();
				} catch (SQLException e) { e.printStackTrace(); }
				return order_id;
				
			case InsertOrderReport:
		  		ps = conn.prepareStatement("INSERT INTO ordersreport "
						+ "(report_id, location, data, month, year)"
						+ " VALUES (?, ?, ?, ?, ?)");
		  		try 
		  		{
		  			ps.setString(1, String.valueOf(getLastId("ordersreport", Command.InsertOrderReport)));
		  			ps.setString(2, data.get(1));
		  			ps.setString(3, data.get(2));
		  			ps.setString(4, data.get(3));
		  			ps.setString(5, data.get(4));
		  			ps.executeUpdate();
		  			
		  		} catch (Exception e) {	e.printStackTrace();}
		  		break;
		  		
		  	case InsertInventoryReport:
		  		ps=conn.prepareStatement("INSERT INTO inventoryreport "
						+ "(report_id, machine_id, location, inventory, stock, month, year)"
						+ " VALUES ( ?, ?, ?, ?, ?, ?, ?)");
		  		try 
		  		{
		  			ps.setString(1, String.valueOf(getLastId("inventoryreport", Command.InsertInventoryReport)));
		  			ps.setString(2, data.get(0));
		  			ps.setString(3, data.get(1));
		  			ps.setString(4, data.get(2));
		  			ps.setString(5, data.get(3));
		  			ps.setString(6, data.get(4));
		  			ps.setString(7, data.get(5));
		  			
		  			ps.executeUpdate();
		  			
		  		} catch (Exception e) {	e.printStackTrace();}
		  		break;
		  		
		  	case InsertRequest:
		  		ps=conn.prepareStatement("INSERT INTO requests "
						+ "(request_id, customer_id, type , status)"
						+ " VALUES (?, ?, ? , ?)");
		  		try 
		  		{
		  			ps.setString(1, String.valueOf(getLastId("requests", Command.InsertRequest)));
		  			//ps.setString(1, data.get(0));
		  			ps.setString(2, data.get(1));
		  			ps.setString(3, data.get(2));
		  			ps.setString(4, data.get(3));
		  			
		  			ps.executeUpdate();
		  			
		  		} catch (Exception e) {	e.printStackTrace();}
		  		break;
		  		
		  	case InsertUsersReport:
		  		ps=conn.prepareStatement("INSERT INTO usersreports "
						+ "(report_id, month, year, data)"
						+ " VALUES ( ?, ?, ?, ?)");
		  		try 
		  		{
		  			ps.setString(1,String.valueOf(getLastId("usersreports",Command.InsertUsersReport)));
		  			ps.setString(2, data.get(0));
		  			ps.setString(3, data.get(1));
		  			ps.setString(4, data.get(2));
		  			ps.executeUpdate();
		  			
		  		} catch (Exception e) {	e.printStackTrace();}
		  		break;
		  		
		  	case InsertStockRequest:
		  		ps=conn.prepareStatement("INSERT INTO stockrequests "
						+ "(stock_request_id, machine_id,status)"
						+ " VALUES ( ?, ?, ?)");
		  		try 
		  		{
		  			ps.setString(1, String.valueOf(getLastId("stockrequests", Command.InsertStockRequest)));
		  			ps.setString(2, data.get(0)); //machine_id
		  			ps.setString(3, "Pending"); //items to fill
		  			ps.executeUpdate();
		  			
		  		} catch (Exception e) {	e.printStackTrace();}
		  		break;
		  		
		  	case InsertDelivery:
		  		ps = conn.prepareStatement("INSERT INTO delivery "
						+ "(delivery_id, order_id, customer_id, shipping_date, estimated_delivery, status, total_price, location, address)"
						+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
		  		try 
		  		{
		  			ps.setString(1, String.valueOf(getLastId("delivery", Command.InsertDelivery)));
		  			ps.setString(2, data.get(0)); 
		  			ps.setString(3, data.get(1));
		  			ps.setDate(4, Date.valueOf(LocalDate.now())); 
		  			ps.setDate(5, null); 
		  			ps.setString(6, "Pending"); 
		  			ps.setString(7, data.get(2));
		  			ps.setString(8, data.get(3));
		  			ps.setString(9, data.get(4));
		  			ps.executeUpdate();
		  			
		  		} catch (Exception e) {	e.printStackTrace();}
		  		break;
		  		
		  	default:
		  		break;
		  	}
			return null;
		  	
			

		}
	 
	  public void UpdateToDB(Message details) throws SQLException {
		    // data format = { id credit_card subscriber_num}
		 String[] s = (String[]) details.getContent(); //problematic
		 String TableName = s[0];
		 PreparedStatement ps;
		  switch (TableName)
		  {
		case "ordersupplymethod":
			ps = conn.prepareStatement("UPDATE orders "
					+ "Set order_status = ?"
					+ "Where order_number = ?");
			try {
				ps.setString(1, s[2]);
				ps.setString(2, s[1]);
			
				ps.executeUpdate();
			} catch (SQLException e) { e.printStackTrace(); }
			break;
		  
		case "ordersreport":
			ps = conn.prepareStatement("UPDATE ordersreport "
					+ "Set data = ?"
					+ "Where report_id = ?");
			try {
				ps.setString(1, s[2]);
				ps.setString(2, s[1]);
			
				ps.executeUpdate();
			} catch (SQLException e) { e.printStackTrace(); }
			break;
			
			
		case "updatestockrequest":
			ps = conn.prepareStatement("UPDATE stockrequests "
					+ " SET resolved_date = ? , status = ? "
					+ " WHERE stock_request_id = ? ");
			
			try {
				System.out.println(Date.valueOf(LocalDate.now()));
				ps.setDate(1, Date.valueOf(LocalDate.now()));
				ps.setString(2, "Resolved");
				ps.setInt(3, Integer.parseInt(s[1]));
				ps.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace(); 
			}
			break;
			
			
		case "orders":
			String[] numberAndStatusForOrder;
			ps = conn.prepareStatement("UPDATE orders "
					+ "SET order_status = ? "
					+ "WHERE order_number = ?");
			
			try {
				for (int i = 1; i < s.length; i++) {
					numberAndStatusForOrder = s[i].split(" ");
					ps.setString(1, numberAndStatusForOrder[1]);
					ps.setInt(2, Integer.parseInt(numberAndStatusForOrder[0]));
					ps.executeUpdate();
				}
			} catch (SQLException e) { e.printStackTrace(); }
			break;
			
		case "machinesAmount":
			ps = conn.prepareStatement("UPDATE machines "
					+ "SET amount_per_item = ?, items = ?, total_inventory = ? "
					+ "WHERE machine_id = ?"); 
			try {
				ps.setString(1, s[1]);
				ps.setString(2, s[2]);
				ps.setInt(3, Integer.parseInt(s[3]));
				ps.setInt(4, Integer.parseInt(s[4]));
				ps.executeUpdate();
		} catch (SQLException e) { e.printStackTrace(); }
		//add new Total Inventory
		break;
			
		case "location":
			ps = conn.prepareStatement("UPDATE location " 
					+ "SET sale_value = ?"
					+ " WHERE name = ?"); 
			try {
				ps.setInt(1, Integer.parseInt(s[2]));
				ps.setString(2, s[1]);
				ps.executeUpdate();
			} catch (SQLException e) { e.printStackTrace(); }
			//add new Total Inventory
			break;
			
		case "machinesThreshold":
			ps = conn.prepareStatement("UPDATE machines " 
					+ "SET threshold = ?"
					+ "WHERE machine_id = ?"); 
			try {
				ps.setString(1, s[2]);
				ps.setString(2, s[1]);
				ps.executeUpdate();
			} catch (SQLException e) { e.printStackTrace(); }
			//add new Total Inventory
			break;
			
		case "usersFirstCart":
			ps = conn.prepareStatement("UPDATE users " 
					+ "SET is_new_subscriber = ?"
					+ "WHERE ID = ?"); 
			try {
				ps.setString(1,"0");
				ps.setString(2,s[1]);
				ps.executeUpdate();
			} catch (SQLException e) { e.printStackTrace(); }
			//add new Total Inventory
			break;

		case "users":
			ps = conn.prepareStatement("UPDATE users " 
					+ "SET subscriber_number = ? , is_new_subscriber = ?" 
					+ " WHERE ID = ?"); 
			try {
				ps.setInt(1,getLastId("users", Command.InsertUser));
				ps.setInt(2, 1);
				ps.setInt(3, Integer.parseInt(s[1]));
				
				ps.executeUpdate();
			} catch (SQLException e) { e.printStackTrace(); }
			break;
			
		case "delivery":
			String[] numberAndStatus;
			ps = conn.prepareStatement("UPDATE delivery "
					+ "SET status = ?, estimated_delivery = ?"
					+ "WHERE order_id = ?");
			
			try {
				for (int i = 1; i < s.length; i++) {
					numberAndStatus = s[i].split(" ");
					ps.setString(1, numberAndStatus[1]);
					ps.setString(2, numberAndStatus[2]);
					ps.setInt(3, Integer.parseInt(numberAndStatus[0]));
//					System.out.println("Updated client about current delivery. Estimated time: " + numberAndStatus[2]);
					EchoServer.updateCommandText("Updated client about current delivery. Estimated time: " + numberAndStatus[2]);
					
					ps.executeUpdate();
				}
			} catch (SQLException e) { e.printStackTrace(); }
			break;
			
		case "requests":
			ps = conn.prepareStatement("UPDATE requests " 
					+ "SET status = ?" 
					+ " WHERE customer_id = ?"); 
			try {
				ps.setString(1, "Resolved");
				ps.setInt(2, Integer.parseInt(s[1]));
				
				ps.executeUpdate();
			} catch (SQLException e) { e.printStackTrace(); }
			break;

		default:
//			System.out.println("default entered in dbcontroller");
			EchoServer.updateCommandText("default entered in database controller");
			break;
		}
//			PreparedStatement ps = conn.prepareStatement("UPDATE users "
//					+ "Set credit_card_number = ?, subscriber_number = ? "
//					+ "Where id = ?");
			
			
		}
	  


	  public ArrayList<Object> ReadFromDB(Message m) throws SQLException {
		    Statement stmt;
			ArrayList<Object> alldata = new ArrayList<>();
			String query = m.getCommand().GetQuery();
			if ((int)m.getContent() != 0) 
				query += " WHERE " + m.getCommand().GetID() + " = " + (int)m.getContent();
			
			try {
				stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				switch (m.getCommand()) {
					case ReadMachines:
						Machine tempM;
						while(rs.next()) { 
							tempM = new Machine();
							tempM.setMachine_id(rs.getInt(1));
							tempM.setLocation(rs.getString(2));
							tempM.setThreshold(rs.getInt(3));
							tempM.setTotal_inventory(rs.getInt(4));
							tempM.setAllItems(rs.getString(5));
							tempM.setAmount_per_item(rs.getString(6));
							alldata.add(tempM);
						}
						break;
						
					case ReadDeliveries:
						Delivery tempD;
						while(rs.next()) {
							tempD = new Delivery();
							tempD.setDelivery_id(rs.getInt(1));
							tempD.setOrder_id(rs.getInt(2));
							tempD.setCustomer_id(rs.getInt(3));
							tempD.setShipping_date(rs.getDate(4));
							tempD.setEstimated_Delivery(rs.getDate(5));
							tempD.setStatus(rs.getString(6));
							tempD.setTotal_price(rs.getInt(7));
							tempD.setLocation(rs.getString(8));
							tempD.setAddress(rs.getString(9));
							alldata.add(tempD);
						}
						break;
						
					case ReadRequests:
						Request tempR;
						while(rs.next()) {
							tempR = new Request();
							tempR.setRequest_id(rs.getInt(1));
							tempR.setCustomer_id(rs.getInt(2));
							tempR.setType(rs.getString(3));
							tempR.setStatus(rs.getString(4));
							alldata.add(tempR);
						}
						break;
						
					case ReadUserReports:
						UsersReports userReport;
						while(rs.next())
						{
							userReport = new UsersReports();
							userReport.setReport_id(rs.getInt(1));
							userReport.setMonth(rs.getString(2));
							userReport.setYear(rs.getString(3));
							userReport.setData(rs.getString(4));
							alldata.add(userReport);
						}
						break;
						
					case ReadUsers:
						Subscriber tempSub;
						while(rs.next()) {
							tempSub = new Subscriber();
							tempSub.setFname(rs.getString(1));
							tempSub.setLName(rs.getString(2));
							tempSub.setId(rs.getInt(3));
							tempSub.setPhoneNum(rs.getString(4));
							tempSub.setEmail(rs.getString(5));
							tempSub.setVisa(rs.getString(6));
							tempSub.setSubNum(rs.getInt(7));
							tempSub.setUserName(rs.getString(8));
							tempSub.setPassword(rs.getString(9));
							tempSub.setRole(rs.getString(10));
							tempSub.setIs_new_subscriber(rs.getInt(11));
							alldata.add(tempSub);
						}
						break;
						
					case ReadUserVisa:
						String visa = "";
						while(rs.next())
						{
							visa = rs.getString(1);
						}
						ArrayList<Object> v = new ArrayList<Object>();
						v.add(visa);
						return v;
						
					case ReadOrders:
						Order tempO;
						while(rs.next()) {
							tempO = new Order();
							tempO.setOrder_num(rs.getInt(1));
							tempO.setCustomer_id(rs.getInt(2));
							tempO.setOrder_status(rs.getString(3));
							tempO.setOrder_created(rs.getDate(4));
							tempO.setLocation(rs.getString(5));
							tempO.setItems_in_order(rs.getString(6));
							tempO.setCredit_card_number(rs.getString(7));
							tempO.setPrice(rs.getInt(8));
							tempO.setSupply_method(rs.getString(9));
							tempO.setMachine_id(rs.getInt(10));
							alldata.add(tempO);
						}
						break;
						
					case ReadExternalTable:
						Subscriber tempExternalSub;
						while(rs.next()) {
							tempExternalSub = new Subscriber();
							tempExternalSub.setFname(rs.getString(1));
							tempExternalSub.setLName(rs.getString(2));
							tempExternalSub.setId(rs.getInt(3));
							tempExternalSub.setPhoneNum(rs.getString(4));
							tempExternalSub.setEmail(rs.getString(5));
							tempExternalSub.setVisa(rs.getString(6));
							tempExternalSub.setSubNum(rs.getInt(7));
							tempExternalSub.setUserName(rs.getString(8));
							tempExternalSub.setPassword(rs.getString(9));
							tempExternalSub.setRole(rs.getString(10));
							tempExternalSub.setIs_new_subscriber(rs.getInt(11));
							alldata.add(tempExternalSub);
						}
						
						break;
						
					case ReadItems:
						Item tempI;
						while(rs.next()) {
							tempI = new Item();
							tempI.setProductID(rs.getString(1));
							tempI.setPrice(rs.getInt(2));
							alldata.add(tempI);
						}
						break;
						
					case ReadLocations:
						Location tempL;
						while(rs.next()) {
							tempL = new Location();
							tempL.setLocation(rs.getString(1));
							tempL.setSale_value(rs.getInt(2));
							alldata.add(tempL);
						}
						break;
						
					
					case ReadOrdersReports:
						OrdersReports tempRorders;
						while(rs.next())
						{
							tempRorders = new OrdersReports();
							tempRorders.setReport_id(rs.getString(1));
							tempRorders.setLocation(rs.getString(2));
							tempRorders.setData(rs.getString(3));
							tempRorders.setMonth(rs.getString(4));
							tempRorders.setYear(rs.getString(5));
							alldata.add(tempRorders);
						}
						break;
						
					case ReadStockRequests:
						StockRequest tempRstock;
						while(rs.next())
						{
							tempRstock = new StockRequest();
							tempRstock.setStock_request_id(rs.getInt(1));
							tempRstock.setMachine_id(rs.getInt(2));
							tempRstock.setStatus(rs.getString(3));
							alldata.add(tempRstock);
						}
						break;
						
					case ReadInventoryReports:
						InventoryReports tempInventoryReport;
						while(rs.next())
						{
							tempInventoryReport = new InventoryReports();
							tempInventoryReport.setReport_id(rs.getInt(1));
							tempInventoryReport.setMachine_id(rs.getInt(2));
							tempInventoryReport.setLocation(rs.getString(3));
							tempInventoryReport.setInventory(rs.getString(4));
							tempInventoryReport.setStock(rs.getString(5));
							tempInventoryReport.setMonth(rs.getString(6));
							tempInventoryReport.setYear(rs.getString(7));
							alldata.add(tempInventoryReport);
						}
						break;

						
					default:
//						System.out.println("Reached default in database controller");
						EchoServer.updateCommandText("Reached default in database controller");
						return null;
							
				}
				rs.close();
			}
			catch (SQLException e) {e.printStackTrace(); }
			
			return alldata;
	   }
	  
	  public String[] ConnectToServer(String str , int flag) throws SQLException {
		    Statement stmt;
			String password = null;
			String value;
			String[] passRoleFnameSubNumFirstCart = new String[6];
			if(flag==1)
			{
				value = "user_name";
			}
			else 
				value = "ID";
			try 
			{
			
				stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT password,role,first_name,subscriber_number,is_new_subscriber,ID FROM users Where " +value +" = \""+ str +"\"");
		 		if(!rs.next())
		 			return new String[] {"-1","-1","-1","-1","-1","-1"};
				passRoleFnameSubNumFirstCart[0] = rs.getString(1);
		 		passRoleFnameSubNumFirstCart[1] = rs.getString(2);
		 		passRoleFnameSubNumFirstCart[2] = rs.getString(3);
		 		passRoleFnameSubNumFirstCart[3] = rs.getString(4);
		 		passRoleFnameSubNumFirstCart[4] = rs.getString(5);
		 		passRoleFnameSubNumFirstCart[5] = rs.getString(6);

				rs.close();
			} catch (SQLException e) { e.printStackTrace(); }
			
			return passRoleFnameSubNumFirstCart;
	   }
	  
	  public static synchronized DatabaseController GetFunctionsInstance(String databasePassword) {
		   return ( DBFunctionsInstance == null ) ? new DatabaseController(databasePassword) : DBFunctionsInstance;
	   }
	   
	   public static synchronized DatabaseController GetFunctionsInstance() {
		   return ( DBFunctionsInstance == null ) ? new DatabaseController() : DBFunctionsInstance;
	   }
	   
	   public int getLastId(String tableName, Command cmd) throws SQLException
	   {
		   String query = "SELECT " + cmd.GetID() + " FROM " + tableName; 
		   Statement stmt;
		   stmt = conn.createStatement();
		   ResultSet rs = stmt.executeQuery(query);
		   ArrayList<Integer> ID = new ArrayList<Integer>();
		   while(rs.next())
			   ID.add(rs.getInt(1));
		   int lastID = 0;
		   for(Integer id : ID)
		   {
			   if(id > lastID)
				   lastID = id;
		   }
		   lastID++;
		   return lastID;
		   
	   }
	   
	   
}
