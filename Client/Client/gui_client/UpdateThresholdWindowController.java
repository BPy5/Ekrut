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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import logic.Machine;

public class UpdateThresholdWindowController implements Initializable {
	
	@FXML
	private Button backBtn;
	@FXML
	private Button updateBtn;
	@FXML
	private Label locationlbl;
	
	@FXML
	private ComboBox<String> locationName;
	@FXML
	private ComboBox<String> machineCode;
	@FXML
	private TextField thresholdValue;
	@FXML
	private ImageView image;
	
	ObservableList<String> MachineIdList; //ArrayList of machine IDs
	ObservableList<String> LocationList; //ArrayList of location names
	
	String selectedLocation = ""; //variable that saves selected location name
	String machineId = ""; //variable that saves machine ID
	
	/**
	 * Updates the value of threshold in a specific location and machine ID according to the selected value of combo boxes
	 * If either combo box doesn't have a selected value or the text field is empty then the method throws an exception
	 * Otherwise checks if new threshold value is between 0 and 100 (including 0 and 100)
	 * If it isn't then the method throws an alert
	 * Otherwise updates new theshold value
	 * 
	 * @param event - Type of action that occurred in the window by the user (when pressing a button in this scenario)
	 * @throws Exception
	 */
	@FXML
	public void UpdateBtn(ActionEvent event) throws Exception{
		String newValue = "";
		if(thresholdValue.getText().isEmpty() || selectedLocation.isEmpty() || machineId.isEmpty())
		{
			Alert alert = new Alert(AlertType.ERROR,"One or more fields is missing",ButtonType.OK);
			alert.showAndWait();
		}
		else {
			
			newValue = thresholdValue.getText();
			Message msg = new Message(null,null);
			ArrayList<String> data = new ArrayList<String>();
			data.add("machinesThreshold");
			data.add(machineId);	
			try {
					Integer.parseInt(newValue);
					if(Integer.parseInt(thresholdValue.getText()) < 0)
					{
						Alert alert = new Alert(AlertType.ERROR,"Threshold value must be at least 0",ButtonType.OK);
						alert.showAndWait();
					}
					else {
						data.add(newValue);
						msg.setContent(data);
						msg.setCommand(Command.UpdateMachineThreshold);
						ClientUI.chat.accept(msg);
						Alert alert = new Alert(AlertType.INFORMATION,"Value updated successfully!",ButtonType.OK);
						alert.showAndWait();
					}
				}catch(NumberFormatException e) {
					Alert alert = new Alert(AlertType.ERROR,"Must enter a NUMERIC value",ButtonType.OK);
					alert.showAndWait();
					data.add("0");
				}
			
		}
	}
	
	/**
	 * Hides current window
	 * Goes to RegionalManager.fxml window
	 * 
	 * @param event - Type of action that occurred in the window by the user (when pressing a button in this scenario)
	 * @throws Exception
	 */
	public void BackBtn(ActionEvent event) throws Exception {
		((Node)event.getSource()).getScene().getWindow().hide();
		Parent root = FXMLLoader.load(getClass().getResource("/gui_client_windows/RegionalManager.fxml"));
		Stage primaryStage = new Stage();
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/css/everything.css").toExternalForm());
		primaryStage.setTitle("Regional Manager");
		primaryStage.setScene(scene);		
		primaryStage.show();		
	}

	/**
	 *	Initializes window components
	 *	Sets the value of field "selectedLocation" to locationName in ChatClient
	 *	Sets the value of the label locationlbl to a new value
	 *	Calls setMachineBox()
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		String actual = "/images/ekrut.png" ;
		String path = this.getClass().getResource(actual).toExternalForm();
		Image img = new Image(path,true);
		
		image.setImage(img);
		selectedLocation = ChatClient.locationName;
		locationlbl.setText(locationlbl.getText() + selectedLocation);
		setMachineBox();
	
	}
	
	/**
	 * Initializes the machine combo box according to the value of selected location combo box value
	 * 
	 */
	public void setMachineBox()
	{
		ArrayList<String> typeMachine = new ArrayList<String>();
		ClientUI.chat.accept(new Message(0,Command.ReadMachines)); 
		
		for(Machine i : ChatClient.machines)
		{
			if(selectedLocation.equals(i.getLocation()))
			{
				typeMachine.add(String.valueOf(i.getMachine_id()));
			}
		}
		
		MachineIdList= FXCollections.observableArrayList(typeMachine);
		machineCode.getItems().clear();
		machineCode.setItems(MachineIdList);
	}
	
	/**
	 * This method dictates where the selected value of the combo box is saved
	 * If there was no selected combo box value the method throws an alert
	 * 
	 * @param event - Type of action that occurred in the window by the user (when pressing a button in this scenario)
	 */
	@FXML
	public void SelectMachineId(ActionEvent event) {
		try {
		machineId = machineCode.getSelectionModel().getSelectedItem().toString();
		}catch(Exception e) { 
			Alert alert = new Alert(AlertType.ERROR,"Must choose a machine ID",ButtonType.OK);
			alert.showAndWait();
		}
	}

}
