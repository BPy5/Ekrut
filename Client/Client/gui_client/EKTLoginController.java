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

/**
 * This class lets users sign in quickly
 */
public class EKTLoginController implements Initializable {
	
	public static String u = "";
	
	public OLOKPageController p;
	
	@FXML
	private ComboBox<String> cmbUser;
	@FXML
	private Button backBtn;
	@FXML
	private Button loginBtn;
	@FXML
	private ImageView image;
	
	ObservableList<String> users;
	
	Message messageToServer = new Message(null, null);
	
	/**
	 * This method saves the selected value from the combo box to the string u
	 * @param event
	 */
	@FXML
	public void Selectuser(ActionEvent event) {
		u = cmbUser.getSelectionModel().getSelectedItem().toString();
	}
	
	/**
	 * This method initializes string u, and calls function setUserComboBox
	 * @param location
	 * @param resources
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		String actual = "/images/ekrut.png" ;
		String path = this.getClass().getResource(actual).toExternalForm();
		Image img = new Image(path,true);
		
		image.setImage(img);
		u = "";
		setUserComboBox();
	}
	
	/**
	 * This method initializes the combo box values
	 */
	public void setUserComboBox() {	
    	ArrayList<String> u = new ArrayList<String>(Arrays.asList("128763524","123456789","314887546"));
		users = FXCollections.observableArrayList(u);
		cmbUser.getItems().clear();
		cmbUser.setItems(users);
	}
	
	/**
	 * This method goes to the user page
	 * @param event
	 * @throws Exception
	 */
	public void login(ActionEvent event) throws Exception
	{
		if(u.isEmpty())	// check if combo box was empty
		{
			Alert alert = new Alert(AlertType.ERROR,"You must enter an ID",ButtonType.OK);
			alert.showAndWait();
		}
		else
		{
			Message msg = new Message(u,Command.EKTConnect);
			ClientUI.chat.accept(msg);
		
			if(p.type.equals("OK"))	// if configuration type is "OK"
			{
				if(ChatClient.ID != -1 && ChatClient.isSubscriber == true) {			// if the user exists
					nextWindow(event, "/gui_client_windows/UserUI.fxml", "USER UI");	// goes to UserUI window
				}
				else	// the id doesn't exist in data base
				{
					Alert alert = new Alert(AlertType.ERROR,"The ID you entered can't use EKT",ButtonType.OK);
					alert.showAndWait();
				}
			}
			else	// configuration type is "OK"
			{
				if(ChatClient.ID == -1) {
					Alert alert = new Alert(AlertType.ERROR,"The ID you entered can't use EKT",ButtonType.OK);
					alert.showAndWait();
					return;
				}
				
				if(ChatClient.role.equals("customer"))
				{
					if(ChatClient.isSubscriber == true) {
						nextWindow(event,"/gui_client_windows/UserUI.fxml","USER UI");
					    return;
					}
					else {
						Alert alert = new Alert(AlertType.ERROR,"The ID you entered can't use EKT",ButtonType.OK);
						alert.showAndWait();
					}
				}
				else
					nextWindow(event, "/gui_client_windows/WorkerUI.fxml", "WORKER UI");
			}
		}
	}
	
	/**
	 * This method calls nextWindow function
	 * @param event
	 * @throws Exception
	 */
	public void BackBtn(ActionEvent event) throws Exception {
		nextWindow(event,"/gui_client_windows/LoginEkrut.fxml","Login EKRUT");
	}
	
	/**
	 * This method hides the currently open window and shows the desired window
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