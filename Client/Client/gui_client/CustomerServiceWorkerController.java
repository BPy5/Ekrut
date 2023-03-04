package gui_client;


import java.net.URL;
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
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import logic.Subscriber;


/**
 * Controller class for customer service window
 */
public class CustomerServiceWorkerController implements Initializable{
	
	//Window components
	@FXML
	private TextField FirstNametxt;
	@FXML
	private TextField LastNametxt;
	@FXML
	private TextField Idtxt;
	@FXML
	private TextField PhoneNumbertxt;
	@FXML
	private TextField Emailtxt;
	@FXML
	private TextField CreditCardtxt;
	@FXML
	private TextField Usernametxt;
	@FXML
	private TextField Passwordtxt;
	@FXML
	private TextField Roletxt;
	@FXML
	private Button addBtn;
	@FXML
	private Button backBtn;
	@FXML
	private Label titleLbl;
	@FXML
	private ImageView image;
	
	//message objects to send to server
	Message messageToServer = new Message(null, null);
	Message ReadUsersMessage = new Message(null, null);

	
	/*
	 * Registers user and adds (saves) their details to database.
	 * @param event Type of action that occurred in the window by the user (when pressing a button in this scenario)
	 * @throws Exception
	 */
	public void Add(ActionEvent event) throws Exception {
		boolean flag = true;
		
		// Get details from textfields and insert them to a string
		String newUser = FirstNametxt.getText() + " " + LastNametxt.getText() + " " + Idtxt.getText() + " " 
				+ PhoneNumbertxt.getText() + " " + Emailtxt.getText() + " " + CreditCardtxt.getText() + " -1 " + 
				Usernametxt.getText() + " " + Passwordtxt.getText() + " " + Roletxt.getText();
		
		// Read all users to check if user details are already in database 9if user is already registered)
		ReadUsersMessage.setCommand(Command.ReadUsers);  
		ReadUsersMessage.setContent(0);
		ClientUI.chat.accept(ReadUsersMessage);  // send the request to server to read all users
		
		// loop through all users and check if the user already exists
		for(Subscriber s : ChatClient.subscribers)
		{
			// if user already exists, show an alert and set flag to false so that the user won't be registered again
			if ((s.getId()== Integer.parseInt(Idtxt.getText()) || s.getUserName().equals(Usernametxt.getText()) ||
					s.getPhoneNum().equals(PhoneNumbertxt.getText())))
			{
				Alert alert = new Alert(AlertType.ERROR,"User already exists!",ButtonType.OK);
				alert.showAndWait();
				flag = false;
			}
		}
		
		//if user doesn't exist, register them
		if (flag)
		{
			//insert user to database
			messageToServer.setCommand(Command.InsertUser);
			messageToServer.setContent(newUser);
			ClientUI.chat.accept(messageToServer);
			
			//Prompt that user has been addded
			Alert alert = new Alert(AlertType.CONFIRMATION,"User successfully added",ButtonType.OK);
			alert.showAndWait();}
	}

	/**
	 * back to main page of customer service worker (worker ui)
	 * @param event Type of action that occurred in the window by the user (when pressing a button in this scenario)
	 * @throws Exception
	 */
	public void BackBtn(ActionEvent event) throws Exception {
		((Node) event.getSource()).getScene().getWindow().hide();
		Parent root = FXMLLoader.load(getClass().getResource("/gui_client_windows/WorkerUI.fxml"));
		Stage primaryStage = new Stage();
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/css/everything.css").toExternalForm());
		primaryStage.setTitle("worker UI");
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
