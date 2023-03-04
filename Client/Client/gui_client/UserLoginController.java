package gui_client;

import java.net.URL;
import java.security.DomainCombiner;
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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * This class launches the account login page
 */
public class UserLoginController implements Initializable {

	@FXML
	private AnchorPane anchorPane;
	@FXML
	private TextField Usernametxt;
	@FXML
	private PasswordField passwordtxt;
	@FXML
	private Label Usernamelbl;
	@FXML
	private Label Passwordlbl;
	@FXML
	private Button Loginbtn;
	@FXML
	private Button Backbtn;
	@FXML
	private ImageView image;
	
	/**
	 * This method lets the user login to his account
	 * @param event
	 * @throws Exception
	 */
	@FXML
	public void loginBtn(ActionEvent event) throws Exception {
		String username = Usernametxt.getText();	// gets the text inserted from the text fields
		String password = passwordtxt.getText();
		
		
		if(username.equals("") || password.equals(""))	//checks if one the the text fields is empty
		{
			Alert alert = new Alert(AlertType.ERROR,"Must enter username and password",ButtonType.OK);
			alert.showAndWait();
		}
		else 
		{
			Message msg = new Message(null, Command.Connect); // message to connect to server
			String[] userPass = {username,password};
			msg.setContent(userPass);
			String title = "";
			ClientUI.chat.accept(msg);
			//check if password is correct / if client exists and then proceed;
			if(ChatClient.password.equals(password))	
			{
				((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
				Stage primaryStage = new Stage();
				Parent root = null;
				if(OLOKPageController.type.equals("OK"))	// if configuration type is "OK"
				{
					root = FXMLLoader.load(getClass().getResource("/gui_client_windows/UserUI.fxml"));
				}
				else 
				{
					if(ChatClient.role.equals("customer"))	// if the user is customer
						{
						root = FXMLLoader.load(getClass().getResource("/gui_client_windows/UserUI.fxml"));
						title = "UserUI";
						}
					else	// if the user is a worker
						{
						root = FXMLLoader.load(getClass().getResource("/gui_client_windows/WorkerUI.fxml"));
						title = "WorkerUI";
						}
				}
				Scene scene = new Scene(root);
				scene.getStylesheets().add(getClass().getResource("/css/everything.css").toExternalForm());
				primaryStage.setTitle(title);
				primaryStage.setScene(scene);
				primaryStage.show();	
			}
			else	// checking user details
			{
				if(ChatClient.password.equals(""))
				{
					Alert alert = new Alert(AlertType.ERROR,"Incorrect login info",ButtonType.OK);
					alert.showAndWait();
				}
				
				else if(ChatClient.password.equals("User already connected"))
				{
					Alert alert = new Alert(AlertType.ERROR,"User already logged in",ButtonType.OK);
					alert.showAndWait();
				}
				
				else
				{
					Alert alert = new Alert(AlertType.ERROR,"Incorrect login info",ButtonType.OK);
					alert.showAndWait();
				}			
			}
		}
	}
	
	/**
	 * This method hides the currently open window and shows "LoginEkrut" window.
	 * @param event
	 * @throws Exception
	 */
	public void backBtn(ActionEvent event) throws Exception {
		((Node)event.getSource()).getScene().getWindow().hide();
		Parent root = FXMLLoader.load(getClass().getResource("/gui_client_windows/LoginEkrut.fxml"));
		Stage primaryStage = new Stage();
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/css/everything.css").toExternalForm());
		primaryStage.setTitle("Login Ekrut");
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