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
import logic.Location;
import logic.Machine;
import javafx.stage.Stage;

public class RemoteLocationController implements Initializable {
	private String selected;
	private Message messageToServer = new Message(null, null);
	private ObservableList<String> MachineIdList;
	private ObservableList<String> LocationList;
	
	@FXML
	private ImageView image;
	@FXML
	private ComboBox<String> cmbLocation;
	@FXML
	private ComboBox<String> cmbMachine;
	@FXML
	private Button backbtn;
	@FXML
	private Button startorderbtn;
	
	/**
	 *	Initializes window components
	 *	Calls SetLocationComboBox()
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		String actual = "/images/ekrut.png" ;
		String path = this.getClass().getResource(actual).toExternalForm();
		Image img = new Image(path,true);
		
		image.setImage(img);
//		GetLocations();
		SetLocationComboBox();
		
	}
	
//	public void GetLocations() {
//		ClientUI.chat.accept(new Message(0, Command.ReadLocations));
//		ArrayList<String> locationsAsStrings = new ArrayList<>();
//		
//		for (Location location : ChatClient.locations) 
//			locationsAsStrings.add(location.getLocation());
//		
//		LocationList = FXCollections.observableArrayList(locationsAsStrings);
//	}
	
	/**
	 * Initializes the location combo box 
	 * 
	 */
	public void SetLocationComboBox() { 
//		cmbLocation.getItems().clear();
//		cmbLocation.setItems(LocationList);
		ArrayList<String> type = new ArrayList<String>();
		
		type.add("North");
		type.add("South");
		type.add("UAE");
		
		LocationList = FXCollections.observableArrayList(type);
		cmbLocation.getItems().clear();
		cmbLocation.setItems(LocationList);
	}
	
	/**
	 * This method dictates where the selected value of the combo box is saved
	 * Calls SetMachineIdComboBox()
	 * 
	 * @param event - Type of action that occurred in the window by the user (when pressing a button in this scenario)
	 */
	@FXML
	public void Select(ActionEvent event) {
		selected = cmbLocation.getSelectionModel().getSelectedItem().toString();
		SetMachineIdComboBox();
	}
	
	
	/**
	 * Initializes the machine combo box by location combo box selected value
	 * 
	 */
	public void SetMachineIdComboBox() {
		ArrayList<String> typeMachine = new ArrayList<String>();
		messageToServer.setCommand(Command.ReadMachines);
		messageToServer.setContent(0);	
		ClientUI.chat.accept(messageToServer); 
		
		for(Machine machine : ChatClient.machines) {
			if(selected.equals(machine.getLocation())) {
				typeMachine.add(String.valueOf(machine.getMachine_id()));
			}
		}
		
		MachineIdList = FXCollections.observableArrayList(typeMachine);
		cmbMachine.getItems().clear();
		cmbMachine.setItems(MachineIdList);
	}

	/**
	 * Hides current window
	 * Goes back to UserUI.fxml window
	 * 
	 * @param event - Type of action that occurred in the window by the user (when pressing a button in this scenario)
	 * @throws Exception
	 */
	public void BackBtn(ActionEvent event) throws Exception  { //fix this apparently its null
		((Node)event.getSource()).getScene().getWindow().hide();
		Parent root = FXMLLoader.load(getClass().getResource("/gui_client_windows/ShipmentMethod.fxml"));
		Stage primaryStage = new Stage();
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/css/everything.css").toExternalForm());
		//scene.getStylesheets().add(getClass().getResource("/gui/loginsubscriber.css").toExternalForm());
		primaryStage.setTitle("Shipment Method");
		primaryStage.setScene(scene);		
		primaryStage.show();
	}
	
    /**
     * Saved location combo box selected value in static location in ChatClient 
     * Saved machine combo box selected value in static machineToLoad in ChatClient 
     * Hides current window
     * Goes to ekrutOrder.fxml window
     * Throws alert if there was a combo box with no selected value
     * 
     * @param event - Type of action that occurred in the window by the user (when pressing a button in this scenario)
     * @throws Exception
     */
    public void StartOrderBtn(ActionEvent event) throws Exception {
    	try {
    	ChatClient.locationName = cmbLocation.getSelectionModel().getSelectedItem().toString();
    	ChatClient.machineToLoad = Integer.parseInt(cmbMachine.getSelectionModel().getSelectedItem().toString());
    	((Node)event.getSource()).getScene().getWindow().hide();
		Parent root = FXMLLoader.load(getClass().getResource("/gui_client_windows/ekrutOrder.fxml"));
		Stage primaryStage = new Stage();
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/css/everything.css").toExternalForm());
		//scene.getStylesheets().add(getClass().getResource("/gui/loginsubscriber.css").toExternalForm());
		primaryStage.setTitle("EKRUT");
		primaryStage.setScene(scene);		
		primaryStage.show();
    	}catch (Exception e)
    	{
    		Alert alert = new Alert(AlertType.ERROR,"Please select a location and a machine",ButtonType.OK);
			alert.showAndWait();
    	}
    	
		
		//Integer.parseInt(cmbMachine.getSelectionModel().getSelectedItem().toString()
	}
}
