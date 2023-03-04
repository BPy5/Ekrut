package gui_client;

import java.net.URL;
import java.util.ResourceBundle;

import client.ChatClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * This method lets the user choose what shipment method he likes
 */
public class ShipmentMethodController implements Initializable {
	
	@FXML
	private Button backBtn;
	@FXML
	private Button immediatePickupBtn;
	@FXML
	private Button deliveryBtn;
	@FXML
	private ImageView image;
	
	
	/**
	 * This method sets supply method as "self pickup" and shows RemoteLocation.fxml
	 * @param event
	 * @throws Exception
	 */
	public void ImmediatePickupBtn(ActionEvent event) throws Exception {
		ChatClient.supplyMethod = "Self pickup";
		((Node)event.getSource()).getScene().getWindow().hide();
		Parent root = FXMLLoader.load(getClass().getResource("/gui_client_windows/RemoteLocation.fxml"));
		Stage primaryStage = new Stage();
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/css/everything.css").toExternalForm());
		primaryStage.setTitle("Receipt");
		primaryStage.setScene(scene);		
		primaryStage.show();		
	}
	
	/**
	 * This method sets supply method as "delivery" and shows ShipmentAddress.fxml
	 * @param event
	 * @throws Exception
	 */
	public void DeliveryBtn(ActionEvent event) throws Exception {
		ChatClient.supplyMethod = "Delivery";
		((Node)event.getSource()).getScene().getWindow().hide();
		Parent root = FXMLLoader.load(getClass().getResource("/gui_client_windows/ShipmentAddress.fxml"));
		Stage primaryStage = new Stage();
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/css/everything.css").toExternalForm());
		primaryStage.setTitle("Insert your address for shipment request");
		primaryStage.setScene(scene);		
		primaryStage.show();		
	}
	
	/**
	 * This method shows the window of the user that is currently using the system
	 * @param event
	 * @throws Exception
	 */
	public void BackBtn(ActionEvent event) throws Exception {
		((Node)event.getSource()).getScene().getWindow().hide();
		Parent root = null;
		System.out.println(ChatClient.role);
		if(ChatClient.role.equals("customer")) 
			root =FXMLLoader.load(getClass().getResource("/gui_client_windows/UserUI.fxml"));
		else
			root =FXMLLoader.load(getClass().getResource("/gui_client_windows/WorkerUI.fxml"));
		Stage primaryStage = new Stage();
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/css/everything.css").toExternalForm());
		primaryStage.setTitle("Cart");
		primaryStage.setScene(scene);		
		primaryStage.show();		
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		String actual = "/images/ekrut.png" ;
		String path = this.getClass().getResource(actual).toExternalForm();
		Image img = new Image(path,true);
		
		image.setImage(img);
	}

}
