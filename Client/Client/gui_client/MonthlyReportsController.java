package gui_client;

import java.util.ArrayList;
import java.util.Arrays;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import logic.Order;
import logic.OrdersReports;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import client.ChatClient;
import client.ClientUI;
import common.Command;
import common.Message;

/**
 * This class lets the CEO decide which report he would like to view
 */
public class MonthlyReportsController implements Initializable {
	
	@FXML
	private Button showReportBtn;
	@FXML
	private Button backBtn;
	
	@FXML
	public ComboBox<String> cmbYear;
	@FXML
	private ComboBox<String> cmbMonth;
	@FXML
	private ComboBox<String> cmbLocation;
	@FXML
	private ImageView image;
	
	private int flag = 0;
	
	Message ReportmessageToServer = new Message(null, null);
	Message messageToServer = new Message(null, null);
	Message orderToServer = new Message(null, null);
	
	private static String location;
	public static String year;
	public static String month;
	public static String requestedReport;
	
	private ArrayList<String> newItems = new ArrayList<>();

	ObservableList<String> yearList;
	ObservableList<String> MonthList;
	ObservableList<String> LocationList;
	
	/**
	 * This method saves the selected value from location combo box to static string "location"
	 * @param event - representing some type of action
	 */
	@FXML
	public void Select(ActionEvent event) {
		location = cmbLocation.getSelectionModel().getSelectedItem().toString();
	}
	
	/**
	 * This method saves the selected value from year combo box to static string "year"
	 * @param event - representing some type of action
	 */
	@FXML
	public void SelectYear(ActionEvent event) {
		year = cmbYear.getSelectionModel().getSelectedItem().toString();
	}
	
	/**
	 * This method saves the selected value from month combo box to static string "month"
	 * @param event - representing some type of action
	 */
	@FXML
	public void SelectMonth(ActionEvent event) {
		month = cmbMonth.getSelectionModel().getSelectedItem().toString();
	}
	
	/**
	 * This method calls functions to initialize combo box values and then reads machines from data base.
	 * @param url -  Uniform ResourceLocator, a pointer to a "resource" on the WorldWide Web
	 * @param rb -  ResourceBundle
	 */
	public void initialize(URL url, ResourceBundle rb) {
		String actual = "/images/ekrut.png" ;
		String path = this.getClass().getResource(actual).toExternalForm();
		Image img = new Image(path,true);
		
		image.setImage(img);
		setYearComboBox();	//initializes combo box
		setMonthComboBox();
		setLocationComboBox();
		
		// read machines now
		messageToServer.setCommand(Command.ReadMachines);
		messageToServer.setContent(0);	
		ClientUI.chat.accept(messageToServer); 
	}
	
    /**
     * This method initializes year combo box values
     */
    public void setYearComboBox() {	
    	ArrayList<String> year = new ArrayList<String>(Arrays.asList("2022","2023"));
    	
		yearList = FXCollections.observableArrayList(year);
		cmbYear.getItems().clear();
		cmbYear.setItems(yearList);
	}
	
	/**
	 * This method initializes month combo box values
	 */
	public void setMonthComboBox() {
		ArrayList<String> month = new ArrayList<String>(Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"));
	
		MonthList = FXCollections.observableArrayList(month);
		cmbMonth.getItems().clear();
		cmbMonth.setItems(MonthList);
	}
	
	/**
	 * This method initializes location combo box values
	 */
	public void setLocationComboBox() {
		ArrayList<String> type = new ArrayList<String>(Arrays.asList("North", "South", "UAE"));
				
		LocationList = FXCollections.observableArrayList(type);
		cmbLocation.getItems().clear();
		cmbLocation.setItems(LocationList);
	}
	
	/**
	 * This method checks if all combo boxes were selected and calls function OrderReportSearch
	 * @param event
	 * @throws Exception -  indicates conditions that a reasonable application might want to catch
	 */
	public void ShowReportBtn(ActionEvent event) throws Exception{
		// Checking if one or more fields are empty
		if ((cmbYear.getValue() == null) || (cmbMonth.getValue() == null) || (cmbLocation.getValue() == null))
		{
			Alert alert = new Alert(AlertType.ERROR,"One or more feilds is Empty!",ButtonType.OK);
			alert.showAndWait();
		}
		else
		{
			OrderReportSearch(event);
		}
	}
	
	/**
	 * This method finds the report and shows it
	 * @param event
	 * @throws Exception -  indicates conditions that a reasonable application might want to catch 
	 */
	public void OrderReportSearch(ActionEvent event) throws Exception
	{
		boolean currentDate = checkCurrentDate(); 
		
		OrdersReports currentReport; 
	
		Message msgToUpdate = new Message(null, null);
		
		if (currentDate) // if currentDate = true recreate this month's report
		{
			currentReport = findCurrentReport();
			createNewOrderReport();
			
			if (flag==0) {	// if there are no reports in the selected location
				Alert alert = new Alert(AlertType.ERROR,"There are no reports !",ButtonType.OK);
				alert.showAndWait();
				}
			// there is no such report in data base (meaning requested year, month and machine id)
			else if(currentReport == null)
			{	// create new report
				ArrayList<String> report = new ArrayList<String>(Arrays.asList("",location,fromArrayToString(newItems),month,year));
				ReportmessageToServer.setCommand(Command.InsertOrderReport);	// insert order report
				ReportmessageToServer.setContent(report);
				ClientUI.chat.accept(ReportmessageToServer);
			}
			else // else find the report in data base, if you find it show it, else make it
			{
				//update the current report, insert newItems instead of the old ones , update data
				ArrayList<String> WhatToUpdate = new ArrayList<>(Arrays.asList("ordersreport",currentReport.getReport_id(),fromArrayToString(newItems)));
				msgToUpdate.setCommand(Command.DatabaseUpdate);
				msgToUpdate.setContent(WhatToUpdate);
				ClientUI.chat.accept(msgToUpdate);
			}
			FindRequestedOrderReport(event);
		}
		else {	// if currentDate is false , 
				// meaning the order report is already in the data base
				FindRequestedOrderReport(event);
			 }
	}
	
		/**
		 * This method checks if the combo boxes values are today's date
		 * @return true - if the selected date is now
		 * @return false - if the selected date is not now
		 */
		public boolean checkCurrentDate() {
			if ((year.equals(String.valueOf(LocalDate.now().getYear()))))
				if ((month.equals(String.valueOf(LocalDate.now().getMonthValue()))))
					return true;
			return false;
		}
		
	/**
	 * This method searches for a report in data base based on location month and year
	 * @return orderReport - if the report exists in data base
	 * @return null - if there is no such report in data base
	 */
	public OrdersReports findCurrentReport() {
		// reads data base
		ReportmessageToServer.setCommand(Command.ReadOrdersReports);
		ReportmessageToServer.setContent(0);	
		ClientUI.chat.accept(ReportmessageToServer);
		try {
			if(ChatClient.orderReport.get(0)==(null)) return null;	// if the table is empty
			
			for (OrdersReports orderReport : ChatClient.orderReport)	// loop to find the report
			{
				// if combo box selected values exist in the orders report
				if (orderReport.getLocation().equals(location.toString()) &&
						orderReport.getYear().equals(year.toString()) &&
							orderReport.getMonth().equals(month.toString())) 
				{
				return orderReport;
				}
			}
		}
		catch(Exception e) {
			Alert alert = new Alert(AlertType.ERROR,"Couldn't find report",ButtonType.OK);
			alert.showAndWait();
		}
		return null;
	}
		
	/**
	 * This method creates new order report to insert to data base
	 */
	public void createNewOrderReport() {
		
		orderToServer.setCommand(Command.ReadOrders);
		orderToServer.setContent(0);
		ClientUI.chat.accept(orderToServer);
		String[] dateSplit = null;
		for(Order o : ChatClient.orders)	// this loop to add items to report
		{
			dateSplit = o.getOrder_created().toString().split("-");
			if(dateSplit[0].equals(year) && Integer.parseInt(dateSplit[1])==(Integer.parseInt(month)) && location.equals(o.getLocation()))
			{
				// call another function 
				// to add data
				scanOrder(o.getItems_in_order());
				flag++;
			}
		}
	}
	
	/**
	 * This method scans whether items from data are already in the report or not
	 * @param data - Items_in_order from data base
	 */
	public void scanOrder(String data) {
		String[] temp = data.split("\\.");
		
		for (String s : temp) {
			checkInReport(s);
		}
		
	}
	
	/**
	 * This method adds items to new report
	 * @param data - string[] than has item names and their amount
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
	 * This method adds to array list the amount of item
	 * @param arr - ArrayList that has item names and their amount
	 * @param s - item name
	 * @return i - index of the item name location in array list
	 * @return (-1) - if the item name does not exist in the array list
	 */
	public int checkContains(ArrayList<String> arr, String s)
	{
		int size = arr.size();
		String[] split = null;
		for(int i = 0;i<size;i++)
		{
			split = arr.get(i).split(",");
			if(split[0].equals(s))
				return i;
		}
		return -1;
	}
	
	/**
	 * This method changes from array list to string
	 * @param arr - array list that contains items that were ordered
	 * @return str - string that has the order report data
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
	 * This method searches for the requested report
	 * @param event
	 * @throws Exception
	 */
	public void FindRequestedOrderReport(ActionEvent event) throws Exception{
		
		ReportmessageToServer.setCommand(Command.ReadOrdersReports);
		ReportmessageToServer.setContent(0);	
		ClientUI.chat.accept(ReportmessageToServer);
		boolean flag = false;
		// find the requested order
		try {
			if (ChatClient.orderReport.get(0)!=null)
			{
				for (int j = 0; j < ChatClient.orderReport.size(); j++)
			{	if (ChatClient.orderReport.get(j).getLocation().equals(location.toString()) &&
					ChatClient.orderReport.get(j).getYear().equals(year.toString()) &&
						ChatClient.orderReport.get(j).getMonth().equals(month.toString())) 
					{
							requestedReport = ChatClient.orderReport.get(j).getData();
							flag = true;
							break;
					}
			}
		if (flag) {	// if the report was found
			nextWindow(event,"/gui_client_windows/ReportsCEO.fxml","Pie Chart (ReportsCEO)");
		}

		else
			{
				orderToServer.setCommand(Command.ReadOrders);
				orderToServer.setContent(0);
				ClientUI.chat.accept(orderToServer);
				String[] dateSplit= null;
				String things = "";
				boolean alertFlag = false;
				for (Order order : ChatClient.orders)
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
					ClientUI.chat.accept(orderToServer);
					FindRequestedOrderReport(event);
				}
				if (!alertFlag)
				{
					Alert alert = new Alert(AlertType.ERROR,"No order reports in the requested timeline",ButtonType.OK);
					alert.showAndWait();
					}
				}
			}
			else
			{
				Alert alert = new Alert(AlertType.ERROR,"No reports available!",ButtonType.OK);
				alert.showAndWait();
			}
		} // end of try
		catch(Exception e) 
		{
		Alert alert = new Alert(AlertType.ERROR,"Couldn't find report",ButtonType.OK);
		alert.showAndWait();
		}
	}
	
	/**
	 * This method calls funtion nextWindow to show ChooseReportType.fxml window
	 * @param event
	 * @throws Exception
	 */
	public void BackBtn(ActionEvent event) throws Exception {
		nextWindow(event,"/gui_client_windows/ChooseReportType.fxml","Choose Report Type");
	}
	
	/**
	 * This method hides the currently open window and shows the requested window based on window_location, which is the given path
	 * @param event
	 * @param window_location
	 * @param title
	 * @throws Exception
	 */
	private void nextWindow(ActionEvent event, String window_location, String title) throws Exception {
		((Node)event.getSource()).getScene().getWindow().hide();
		Parent root = FXMLLoader.load(getClass().getResource(window_location));
		Stage primaryStage = new Stage();
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/css/everything.css").toExternalForm());
		primaryStage.setTitle(title);
		primaryStage.setScene(scene);		
		primaryStage.show();	
	}
}
