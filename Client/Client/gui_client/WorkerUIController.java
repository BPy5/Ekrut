package gui_client;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

import client.ChatClient;
import client.ClientUI;
import common.Command;
import common.Message;
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
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import logic.Request;

/**
 * This class opens the user page based on role
 */
public class WorkerUIController implements Initializable{
	
	@FXML
	private Label titletxt;
	@FXML
	private ImageView image;

	/**
	 * This window initializes the text of a label
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		String actual = "/images/ekrut.png" ;
		String path = this.getClass().getResource(actual).toExternalForm();
		Image img = new Image(path,true);
		
		image.setImage(img);
		this.titletxt.setText("Welcome " + ChatClient.Fname);
	}
	
	/**
	 * This method goes to user page based on role
	 * @param event - representing some type of action
	 * @throws Exception - a form of Throwable that indicates conditions that a reasonable application might want to catch
	 */
	public void WorkWindowBtn(ActionEvent event) throws Exception
	{
		((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
		Stage primaryStage = new Stage();
		Parent root = null;
		String title = "";
		switch(ChatClient.role) {
		
		case "ceo":
			root = FXMLLoader.load(getClass().getResource("/gui_client_windows/CEOReports.fxml"));
			title = "CEO Reports";
			break;
		
		case "rgn":
			ChatClient.locationName = "North";
			root = FXMLLoader.load(getClass().getResource("/gui_client_windows/RegionalManager.fxml"));
			title = "Regional Manager";
			break;
			
		case "rgs":
			ChatClient.locationName = "South";
			root = FXMLLoader.load(getClass().getResource("/gui_client_windows/RegionalManager.fxml"));
			title = "Regional Manager";
			break;
			
		case "rgu":
			ChatClient.locationName = "UAE";
			root = FXMLLoader.load(getClass().getResource("/gui_client_windows/RegionalManager.fxml"));
			title = "Regional Manager";
			break;
		
		case "stm":
			root = FXMLLoader.load(getClass().getResource("/gui_client_windows/UpdateStock.fxml"));
			title = "Update Stock";
			break;
			
		case "mkm":
			root = FXMLLoader.load(getClass().getResource("/gui_client_windows/DiscountLocation.fxml"));
			title = "Discount Location";
			break;
			
		case "dlw":
			root = FXMLLoader.load(getClass().getResource("/gui_client_windows/ViewOrdersDeliveryOperator.fxml"));
			title = "View Orders Delivery Operator";
			break;
			
		case "dlo":
			root = FXMLLoader.load(getClass().getResource("/gui_client_windows/ViewOrdersDeliveryOperator.fxml"));
			title = "View Orders Delivery Operator";
			break;
			
		case "customer":
			root = FXMLLoader.load(getClass().getResource("/gui_client_windows/UserUI.fxml"));
			title = "UserUI";
			break;
			
		case "csw": //costumer service worker
			root = FXMLLoader.load(getClass().getResource("/gui_client_windows/CustomerServiceWorker.fxml"));
			title = "Customer Service Worker";
			break;
			
		case "svw": //costumer service worker
			root = FXMLLoader.load(getClass().getResource("/gui_client_windows/SubscriberRequest.fxml"));
			title = "Subscriber Request";
			break;
		
		default:
			break;
		}
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/css/everything.css").toExternalForm());
		primaryStage.setTitle(title);
		primaryStage.setScene(scene);
		primaryStage.show();	
	}
	
	/**
	 * This method calls function nextWindow to show ShipmentMethod.fxml
	 * @param event
	 * @throws Exception
	 */
	public void CreateRemoteOrderBtn(ActionEvent event) throws Exception {
		nextWindow(event, "/gui_client_windows/ShipmentMethod.fxml", "Shipment Method");
	}
	
	/**
	 * This method calls function nextWindow to show LoginEkrut.fxml
	 * @param event
	 * @throws Exception
	 */
	public void LogOutBtn(ActionEvent event) throws Exception
	{
		ClientUI.chat.accept(new Message(ChatClient.ID, Command.Disconnect));
		nextWindow(event, "/gui_client_windows/LoginEkrut.fxml", "Login Ekrut"); 
	}
	
	/**
	 * This method hides the currently open window and shows the desired window
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
