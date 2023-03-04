package gui_client;

import java.net.URL;

import java.util.ArrayList;
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
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import logic.Location;

/**
 * This class lets the user choose the shipment address
 */
public class ShipmentAddressController implements Initializable{
	
	@FXML
	private ComboBox RegionBox;
	@FXML
	private TextField citytxt;
	@FXML
	private TextField streettxt;
	@FXML
	private Button backBtn;
	@FXML
	private Button sendreqBtn;
	@FXML
	private ImageView image;
	
	private ObservableList<String> obs;
	
	private String region = "";
	
	/**
	 * This method reads locations and starts location combo box values
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		String actual = "/images/ekrut.png" ;
		String path = this.getClass().getResource(actual).toExternalForm();
		Image img = new Image(path,true);
		
		image.setImage(img);
		
		ClientUI.chat.accept(new Message(0,Command.ReadLocations));
		
	 	ArrayList<String> Locations = new ArrayList<String>();
	 	
    	for(Location loc : ChatClient.locations)
    		Locations.add(loc.getLocation());
    	
    	obs = FXCollections.observableArrayList(Locations);
		RegionBox.getItems().clear();
		RegionBox.setItems(obs);
	}
	
	/**
	 * This method saves the shipment address details
	 * @param event
	 * @throws Exception
	 */
	public void SendYourRequest(ActionEvent event) throws Exception {
		if(region.isEmpty() || citytxt.getText().isEmpty() || streettxt.getText().isEmpty())
		{
			Alert alert = new Alert(AlertType.ERROR,"One or more fields is Empty!",ButtonType.OK);
			alert.showAndWait();
		}
		else
		{
			ChatClient.address += citytxt.getText();
			ChatClient.address += ",";
			ChatClient.address += streettxt.getText();
			System.out.println(ChatClient.address);
			ChatClient.deliveryLocation = region;
			((Node)event.getSource()).getScene().getWindow().hide();
			Parent root = FXMLLoader.load(getClass().getResource("/gui_client_windows/RemoteLocation.fxml"));
			Stage primaryStage = new Stage();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/css/everything.css").toExternalForm());
			primaryStage.setTitle("Remote Location");
			primaryStage.setScene(scene);		
			primaryStage.show();	
		}
	}

	/**
	 * This method shows the previous page
	 * @param event
	 * @throws Exception
	 */
	public void BackBtn(ActionEvent event) throws Exception {
		((Node)event.getSource()).getScene().getWindow().hide();
		Parent root = FXMLLoader.load(getClass().getResource("/gui_client_windows/ShipmentMethod.fxml"));
		Stage primaryStage = new Stage();
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/css/everything.css").toExternalForm());
		primaryStage.setTitle("Shipment Method");
		primaryStage.setScene(scene);		
		primaryStage.show();	
	}
	
	 
	 /**
	  * This method saves the value of the region from the combo box
	 * @param event
	 */
	@FXML
		public void SelectLocation(ActionEvent event) {
		 try {
			region = RegionBox.getSelectionModel().getSelectedItem().toString();
		 	}
		 catch(Exception e) {e.printStackTrace();}
		}

}
