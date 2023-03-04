package gui_client;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

import client.ChatClient;
import client.ClientUI;
import common.Command;
import common.Message;
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
import logic.InventoryReports;
import logic.Machine;

/**
 * This class lets the CEO choose which report he would like to open.
 */
public class InventoryReportController implements Initializable{
	
	Message messageToServer = new Message(null,null);
	
	private String location;
	private String machineId;
	private String month;
	private String year;
	
	public static InventoryReports requestedInventoryReport = new InventoryReports();
	public static boolean existFlag = false;
	
	@FXML
	private Button showReportBtn;
	@FXML
	private Button backBtn;
	@FXML
	private ComboBox<String> cmbLocation;
	@FXML
	private ComboBox<String> cmbMachineId;
	@FXML
	private ComboBox<String> cmbYear;
	@FXML
	private ComboBox<String> cmbMonth;
	@FXML
	private ImageView image;
	
	ObservableList<String> MachineIdList;
	ObservableList<String> LocationList;
	ObservableList<String> YearList;
	ObservableList<String> MonthList;
	
	/**
	 * This method saves the location value selected from the location combo box.
	 * Then calls setMachineIdComboBox function.
	 * @param event
	 */
	@FXML
	public void Select(ActionEvent event) {
		location = cmbLocation.getSelectionModel().getSelectedItem().toString();
		setMachineIdComboBox();
	}
	
	/**
	 * This method saves the machine Id value selected from the machine Id combo box.
	 * @param event
	 */
	@FXML
	public void SelectMachineId(ActionEvent event) {
	    machineId = cmbMachineId.getSelectionModel().getSelectedItem().toString();
	}
	
	/**
	 * This method saves the month value selected from the month combo box.
	 * @param event
	 */
	@FXML
	public void SelectMonth(ActionEvent event) {
		month = cmbMonth.getSelectionModel().getSelectedItem().toString();
	}
	
	/**
	 * This method saves the year value selected from the year combo box.
	 * @param event
	 */
	@FXML
	public void SelectYear(ActionEvent event) {
		year = cmbYear.getSelectionModel().getSelectedItem().toString();
	}
	
	/**
	 * This method initializes all the combo boxes in the window.
	 * @param location
	 * @param resources
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		String actual = "/images/ekrut.png" ;
		String path = this.getClass().getResource(actual).toExternalForm();
		Image img = new Image(path,true);
		
		image.setImage(img);
		ArrayList<String> locationStr = new ArrayList<String>(Arrays.asList("North", "South", "UAE"));
		LocationList = FXCollections.observableArrayList(locationStr);
		cmbLocation.getItems().clear();
		cmbLocation.setItems(LocationList);

		ArrayList<String> yearStr = new ArrayList<String>(Arrays.asList("2022","2023"));
		YearList = FXCollections.observableArrayList(yearStr);
		cmbYear.getItems().clear();
		cmbYear.setItems(YearList);
		
		ArrayList<String> monthStr = new ArrayList<String>(Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"));
		MonthList = FXCollections.observableArrayList(monthStr);
		cmbMonth.getItems().clear();
		cmbMonth.setItems(MonthList);
	}
	
	
	/**
	 * This method find all the machines id based on the selected location from the location combo box.
	 */
	public void setMachineIdComboBox() {
		
		ArrayList<String> typeMachine = new ArrayList<String>();
		
		messageToServer.setCommand(Command.ReadMachines);	// command to read machines
		messageToServer.setContent(0);	
		ClientUI.chat.accept(messageToServer); 
	
		for(Machine i : ChatClient.machines)
			if(location.equals(i.getLocation()))
				typeMachine.add(String.valueOf(i.getMachine_id()));
		
		MachineIdList = FXCollections.observableArrayList(typeMachine);
		cmbMachineId.getItems().clear();
		cmbMachineId.setItems(MachineIdList);
	}
	

	/**
	 * This method shows the requested report.
	 * @param event
	 * @throws Exception
	 */
	public  void ShowReport(ActionEvent event) throws Exception{
		// checking if one of the combo boxes was not selected.
		if ((cmbLocation.getValue() == null )||(cmbMachineId.getValue() == null ) || (cmbYear.getValue() == null ) || (cmbYear.getValue() == null )) 
		{
			Alert alert = new Alert(AlertType.ERROR,"One or more fields is empty!",ButtonType.OK);
			alert.showAndWait();
		}
		
		else	// if all combo boxes values were selected.
		{
			//read inventory report from data base
			messageToServer.setCommand(Command.ReadInventoryReports);
			messageToServer.setContent(0);	
			ClientUI.chat.accept(messageToServer); 
			
			// loop to find the report
			for (InventoryReports ir : ChatClient.InventoryReport)
				if ((ir.getLocation().equals(location))&&(ir.getMonth().equals(month))&&ir.getYear().equals(year))
				{
					requestedInventoryReport = ir;
					existFlag = true;
				}
			
			if(existFlag == true)	// if flag is true show the data
				nextWindow(event,"/gui_client_windows/InventoryData.fxml","Inventory Data");
			else {
				Alert alert = new Alert(AlertType.ERROR,"Couldn't find report",ButtonType.OK);
				alert.showAndWait();
			}
		}
	}

	/**
	 * This method calls nextWindow function.
	 * @param event
	 * @throws Exception
	 */
	public void BackBtn(ActionEvent event) throws Exception {
		nextWindow(event,"/gui_client_windows/ChooseReportType.fxml","Choose Report Type");
	}
	
	/**
	 * This method hides the currently open window and shows the requested window.
	 * @param event
	 * @param path
	 * @param title
	 * @throws Exception
	 */
	private void nextWindow(ActionEvent event, String path, String title) throws Exception {
		((Node)event.getSource()).getScene().getWindow().hide();
		Parent root = FXMLLoader.load(getClass().getResource(path));
		Stage primaryStage = new Stage();
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/css/everything.css").toExternalForm());
		primaryStage.setTitle(title);
		primaryStage.setScene(scene);		
		primaryStage.show();	
	}
	
}
