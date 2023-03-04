package server;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import common.Command;
import common.Message;
import databaselogic.DatabaseController;
import javafx.scene.control.Alert;
import logic.InventoryReports;
import logic.Location;
import logic.Machine;
import logic.Order;
import logic.OrdersReports;
import logic.Subscriber;

/**
 * A class that defines how the report that creates and saves the reports each month function
 *
 */
public class ReportsThread implements Runnable {
	
	ArrayList<Location> locations = new ArrayList<Location>();
	DatabaseController db;
	String password;
	Message ReportmessageToServer = new Message(null, null);
	private int month,year;
	private ArrayList<String> newItems = new ArrayList<String>(); 
	
	/**
	 * Constructor that gets an instance of dbController and saves it
	 * 
	 * @param db - Instance of dbController to use its functions
	 */
	public ReportsThread(DatabaseController db)
	{
		this.db = db;
	}
	
	/**
	 * Method that defines how the thread that uses this class runs
	 * Checks todays date, if its the first day of the month then calls 3 report creation methods
	 */
	@Override
	public void run() {
			try {
				while(true) {
					if(LocalDate.now().getDayOfMonth() == 1) 
					{
						if(LocalDate.now().getMonthValue() == 1)
						{
							month = 12; 
							year = LocalDate.now().getYear()-1; 
						}
						else
						{
							month = LocalDate.now().getMonthValue()-1;
							year = LocalDate.now().getYear();
						}
						CreateOrderReports(month,year);
						CreateInventoryReports(month,year);
						CreateUserReports(month,year);
					}
					Thread.sleep(86400000); 
				}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			
		
	}
	
	/**
	 * Reads all locations from Database
	 * For each location in database this method creates an order report and saves it in database
	 * 
	 * @param month - the month of the newly created order
	 * @param year - the year of the newly created order
	 */
	public void CreateOrderReports(int month,int year)
	{
		try {
			String monthStr = String.valueOf(month);
			String yearStr = String.valueOf(year);
			Message msg = new Message(0,Command.ReadLocations);
			ArrayList<Object> objects = null;
			objects = db.ReadFromDB(msg);
			for(Object obj : objects)
			{
				locations.add((Location)obj);
			}
			for(Location location : locations)
			{
			//we need a report for ALL locations
				OrdersReports currentReport;
				createNewOrderReport(location.getLocation(),monthStr,yearStr);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Creates an order report based on month, year and location
	 * Reads all orders
	 * Checks each order from database
	 * If order were currently checking matches the order criteria then it is added to the report using scanOrder()
	 * At the end of the order checks insert order if there's at least 1 order that fits the criteria
	 * 
	 * @param location - location of which the report is based on
	 * @param month - month of which the report is based on
	 * @param year - year of which the report is based on
	 */
	public void createNewOrderReport(String location,String month,String year) {
			
			try {
				ArrayList<Order> orders = new ArrayList<Order>();
				ArrayList<Object> objects = new ArrayList<Object>();
				Message orderToServer = new Message(null,null);
				orderToServer.setCommand(Command.ReadOrders);
				orderToServer.setContent(0);
				objects = db.ReadFromDB(orderToServer);
				for(Object obj : objects)
				{
					orders.add((Order)obj);
				}
				String[] dateSplit= null;
				String things = "";
				boolean alertFlag = false;
				for (Order order : orders)
				{
					dateSplit = order.getOrder_created().toString().split("-");
					// if combo box selected values exist in the orders report
					if ( order.getLocation().equals(location)
							&& dateSplit[0].equals(year)
							&& Integer.parseInt(dateSplit[1]) == Integer.parseInt(month))
					{
						things += order.getItems_in_order();
						
						alertFlag = true;
					}
					
				}
				if (alertFlag)
				{
					scanOrder(things);
					ArrayList<String> NewReport = new ArrayList<String>(Arrays.asList("",location,fromArrayToString(newItems),month,year));
					orderToServer.setCommand(Command.InsertOrderReport);
					orderToServer.setContent(NewReport);
					db.SaveToDB(orderToServer);
				}
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}
	
	/**
	 * Scans each item in report data to check if it already exists in order report using checkInReport()
	 * If it does exist then this method will ignore it
	 * Otherwise add it to report to be inserted to the database
	 * 
	 * @param data - report data
	 */
	public void scanOrder(String data) {
		String[] temp = data.split("\\.");
		
		for (String s : temp) {
			checkInReport(s);
		}
		
	}
	
	/**
	 * Checks if a certain item already exists in report data using checkContains()
	 * If it is then add the amount to the correct cell in the report data
	 * Otherwise inserts item data into a new cell in the report data
	 * 
	 * @param data - item data
	 */
	public void checkInReport(String data) {
		String[] nameAmount = data.split(",");
		String[] old = null;
		int newAmount;
		int index = checkContains(newItems,nameAmount[0]);
		if(index == -1) //checkContains
		{
			newItems.add(data);
		}
		else
		{
			old = newItems.get(index).split(",");
			newAmount = Integer.parseInt(nameAmount[1]) + Integer.parseInt(old[1]); //
			newItems.set(index, nameAmount[0] + "," + String.valueOf(newAmount));
		}
	}
	
	/**
	 * Scans the array given in the parameters to check if it contains a certain item name
	 * If the array contains the item name then we return the item's index in the array
	 * Otherwise it returns -1
	 * 
	 * @param arr - the array which we are scanning
	 * @param name - the name of the item were looking for in the array
	 * @return index of the item in the array, -1 otherwise
	 */
	public int checkContains(ArrayList<String> arr, String name)
	{
		int size = arr.size();
		String[] split = null;
		for(int i = 0;i<size;i++)
		{
			split = arr.get(i).split(",");
			if(split[0].equals(name))
				return i;
		}
		return -1;
	}
	
	/**
	 * Turns an array into a string
	 * 
	 * @param arr - the array which we are turning into a string
	 * @return the array in the form a string
	 */
	public String fromArrayToString(ArrayList<String> arr)
	{
		int size = arr.size();
		String str = "";
		for(int i = 0;i<size;i++)
		{
			str += arr.get(i);
			str += ".";
		}
		
		return str;
	}
	
	/**
	 * Creates an inventory report based on month, year and location
	 * Reads all machines
	 * Creates an inventory check for each machine using getOldItemsString() and getNewItemsString
	 * Creates an inventory report based on results of 2 methods getOldItemsString() and getNewItemsString
	 * 
	 * @param month - month of which the report is based on
	 * @param year - year of which the report is based on
	 */
	public void CreateInventoryReports(int month,int year)
	{
		try {
			int monthBefore,yearBefore;
			if(month == 1)
			{
				monthBefore = 12; //should be 12
				yearBefore = year-1; //should be -1
			}
			else
			{
				monthBefore = month-1;
				yearBefore = year-1;
			}
			String monthStr = String.valueOf(month);
			String yearStr = String.valueOf(year);
			String monthBeforeStr = String.valueOf(monthBefore);
			String yearBeforeStr = String.valueOf(yearBefore);
			String newData = "";
			String oldData = "";
			Message msg = new Message(null,null);
			Message orderToServer = new Message(null,null);
			ArrayList<Machine> machines = new ArrayList<Machine>();
			ArrayList<Object> objects = null;
			msg = new Message(null,null);
			msg.setCommand(Command.ReadMachines);
			msg.setContent(0);
			objects = db.ReadFromDB(msg);
			for(Object obj : objects)
			{
				machines.add((Machine)obj);
			}
			for(Machine machine : machines)
			{
				oldData = getOldItemsString(machine.getMachine_id(),monthBeforeStr,yearBeforeStr);
				newData = getNewItemsString(machine);
				orderToServer.setCommand(Command.InsertInventoryReport);
				ArrayList<String> newReport = new ArrayList<String>(Arrays.asList(String.valueOf(machine.getMachine_id()),machine.getLocation(),oldData,newData,monthStr,yearStr));
				orderToServer.setContent(newReport);
				db.SaveToDB(orderToServer);			
			}
			
			
			
			
			
		}catch(Exception e) { e.printStackTrace();}
		
	}
	
	/**
	 * Generates a string representing the current stock of the machine
	 * 
	 * @param machine - machine which were making the report on
	 * @return string representing the current stock of the machine
	 */
	public String getNewItemsString(Machine machine)
	{
		ArrayList<String> items = machine.getItems();
		ArrayList<Integer> amount = machine.getAmountItems();
		int size = items.size();
		String newItems = "";
		for(int i=0;i<size;i++)
		{
			newItems += items.get(i);
			newItems += ",";
			newItems += String.valueOf(amount.get(i));
			newItems += ".";
		}
		return newItems;
	}
	
	/**
	 * Generates a string representing the stock of the machine at the start of the report's month
	 * 
	 * @param machineId - the ID of the machine which were making the report on
	 * @param month - month of the current report were making
	 * @param year - year of the current report were making
	 * @return string representing the stock of the machine at the start of the report's month
	 */
	public String getOldItemsString(int machineId,String month,String year)
	{
		try {
			ArrayList<Object> objects = new ArrayList<Object>();
			Message m = new Message(null,null);
			m.setCommand(Command.ReadInventoryReports);
			m.setContent(0);
			objects = db.ReadFromDB(m);
			for(Object obj : objects)
			{
				if(((InventoryReports)obj).getMonth().equals(month) && ((InventoryReports)obj).getYear().equals(year) && ((InventoryReports)obj).getMachine_id() == machineId)
					return ((InventoryReports)obj).getStock();
			}
			return null;
			
			
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	
	/*
	 * Creates an order report based on month, year and location Reads all orders
	 * Checks each order from database If order were currently checking matches the
	 * order criteria then it is added to the report using scanOrder() At the end of
	 * the order checks insert order if there's at least 1 order that fits the
	 * criteria
	 */
	
	/**
	 * Creates a user report based on month, year
	 * Reads all users
	 * Reads all orders
	 * Checks each order's date
	 * If the date matches the criteria the order is added to a local array
	 * Otherwise it is ignore'
	 * Calls findUserOrdersCount with parameters userID and the local array of order 
	 * Counts each user's amount of orders in the month and year specified and adds it to the report data
	 * Inserts the report into the Database
	 * 
	  @param month - month of which the report is based on
	 * @param year - year of which the report is based on
	 */
	public void CreateUserReports(int month,int year)
	{
		try {
			String monthStr = String.valueOf(month);
			String yearStr = String.valueOf(year);
			int cnt0 = 0;
			int cnt1 = 0;
			int cnt2 = 0;
			int cnt3 = 0;
			int cnt4 = 0;
			int cnt5 = 0;
			ArrayList<Object> objects = new ArrayList<Object>();
			ArrayList<Order> orders = new ArrayList<Order>();
			ArrayList<Subscriber> users = new ArrayList<Subscriber>();
			Message messageToServer = new Message(null,null);
			Message orderToServer = new Message(null,null);
			messageToServer.setCommand(Command.ReadUsers);
			messageToServer.setContent(0);	
			objects = db.ReadFromDB(messageToServer);
			for(Object obj : objects)
			{
				users.add((Subscriber)obj);
			}
			
			messageToServer.setCommand(Command.ReadOrders);
			messageToServer.setContent(0);	
			objects = db.ReadFromDB(messageToServer); 
			for(Object obj : objects)
			{
				String[] monthYear = getMonthYear(((Order)obj).getOrder_created());
				if(monthYear[0].equals(monthStr) && monthYear[1].equals(yearStr))
					orders.add((Order)obj);
				//test if order is in the month thats specified
			}
			
			int ordersCount = 0;
			for (Subscriber user : users) {
				
				if(user.getRole().equals("customer"))
				{
					ordersCount = findUserOrdersCount(user.getId(),orders);
					
					if (ordersCount == 0)
						cnt0++;
					if ((ordersCount > 0) && (ordersCount <= 5))
						cnt1++;
					if ((ordersCount > 5) && (ordersCount <= 10))
						cnt2++;
					if ((ordersCount > 10) && (ordersCount <= 15))
						cnt3++;
					if ((ordersCount > 15) && (ordersCount <= 20))
						cnt4++;
					if ((ordersCount > 20) && (ordersCount <= 25))
						cnt5++;
				}
			}
			String data = cnt0 + "," + cnt1 + "," + cnt2 + "," + cnt3 + "," + cnt4 + "," + cnt5;
			orderToServer.setCommand(Command.InsertUsersReport);
			ArrayList<String> newReport = new ArrayList<String>(Arrays.asList(monthStr,yearStr,data));
			orderToServer.setContent(newReport);
			db.SaveToDB(orderToServer);
		}catch(SQLException e) { e.printStackTrace();}
	}
	
	/**
	 * Counts all orders that a customer has had in the month and year specified in the report
	 * 
	 * @param id - ID of a customer
	 * @param orders - an ArrayList of orders 
	 * @return the amount of orders under the ID of the customer 
	 */
	public int findUserOrdersCount(int id,ArrayList<Order> orders) {
		int cnt = 0;
		
		for (Order i : orders) {
			if (i.getCustomer_id() == id)
				cnt++;
		}
		return cnt;
	}
	
	/**
	 * Turns a date into an array of string which holds the month and year value in string form
	 * 
	 * @param date - the date to split
	 * @return a string array holding the month value in the first cell and the year value in the second cell
	 */
	public String[] getMonthYear(Date date)
	{
		String[] monthYear = new String[2];
		String[] temp = date.toString().split("-");
		monthYear[1] = temp[0];
		monthYear[0] = temp[1];
		return monthYear;
	}

	
	

}
