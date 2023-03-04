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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;


/*
 * Controller for discount location window
 */
public class DiscountLocationController implements Initializable {
	
	// Window compontnets
	@FXML
	private Button backbtn;
	@FXML
	private Button confirmbtn;	
	@FXML
	private ComboBox cmbLocation;
	@FXML
	private TextField discount;
	@FXML
	private ImageView image;
	
	private String location;
	private ObservableList<String> LocationList;
	Message messageToServer = new Message(null, null);
	
	/**
	 * initializes combobox with locations
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setLocationComboBox();
		String actual = "/images/ekrut.png" ;
		String path = this.getClass().getResource(actual).toExternalForm();
		Image img = new Image(path,true);
		
		image.setImage(img);
	}
	
	/**
	 * @param event Type of action that occurred in the window by the user (when choosing an option in combobox in this scenario)
	 */
	@FXML
	public void Select(ActionEvent event) {
		location = cmbLocation.getSelectionModel().getSelectedItem().toString();
	}
	
	/*
	 *  Inserts locations in combobox
	 */
	public void setLocationComboBox() {
		// Arraylist of locations
		ArrayList<String> type = new ArrayList<String>(Arrays.asList("North", "South", "UAE"));
		
		// Set locations arraylist in observable list 
		LocationList = FXCollections.observableArrayList(type);
		//clear combobox
		cmbLocation.getItems().clear(); 
		//display locations in combobox
		cmbLocation.setItems(LocationList);
	}
	
	
    /**
     * Get assigned discount value from textfield and assign it to the relevant location
     */
    public void ConfirmBtn() {
		String val = discount.getText();  // get assigned discount value
		String[] dis = new String[3];
		
		//if hasn't chosen a location, show an alert and don't do any changes
		if(cmbLocation.getValue() == null) {
			Alert alert = new Alert(AlertType.ERROR,"Must choose location first!",ButtonType.OK);
			alert.showAndWait();
			return;
		}
		
		//check if entered a numerical value in discount text field.
		// if not, show alert and don't do any changes
		if (!val.matches("[0-9]+")) {
			Alert alert = new Alert(AlertType.ERROR,"Discount should be a number!",ButtonType.OK);
			alert.showAndWait();
			return;
		}
		
		// if discount is in range [0, 100], update discount
		// to server according to chosen location
		if((Integer.parseInt(val) > 0 && Integer.parseInt(val) <= 100)) {
			switch (location) {
				case "North":
					dis[0]="location";
					dis[1]="North";
					dis[2]=val;
					messageToServer.setCommand(Command.UpdateDiscount);
					messageToServer.setContent(dis);
					ClientUI.chat.accept(messageToServer);
					break;
	
				case "South":
					dis[0]="location";
					dis[1]="South";
					dis[2]=val;
					messageToServer.setCommand(Command.UpdateDiscount);
					messageToServer.setContent(dis);
					ClientUI.chat.accept(messageToServer);
					break;
					
				case "UAE":
					dis[0]="location";
					dis[1]="UAE";
					dis[2]=val;
					messageToServer.setCommand(Command.UpdateDiscount);
					messageToServer.setContent(dis);
					ClientUI.chat.accept(messageToServer);
					break;
					
				default:
					break;
			}
			
			// Prompt user that update is done successfully
			Alert alert = new Alert(AlertType.CONFIRMATION,"Discount applied successfully.",ButtonType.OK);
			alert.showAndWait();
		 }
		
		//if discount in not in range [0, 100] show alert to user and don't do any changes
		else {
			Alert alert = new Alert(AlertType.ERROR,"Discount not in range\nRange must a number be between 0 and 100.",ButtonType.OK);
			alert.showAndWait();
		}
	}
	
	
    /**
	 * Back to main page of user. If user is a marketing manager (mkm), go back to worker ui, else, go back to CEO main page.
	 * Only CEO and Marketing manager can view this page
	 * @param event Type of action that occurred in the window by the user (when pressing a button in this scenario)
	 * @throws Exception
	 */
    public void BackBtn(ActionEvent event) throws Exception {
    	// new window title
    	String title = "";
    	
    	// hide current window
		((Node)event.getSource()).getScene().getWindow().hide();
		Parent root = null;
		
		// if user is marketing manager (mkm), go back to worker ui,  
		if (ChatClient.role.equals("mkm")) {
			root =  FXMLLoader.load(getClass().getResource("/gui_client_windows/WorkerUI.fxml"));
			title = "WORKER UI";
		}
		
		//else, go back to CEO main page.
		else {
			root = FXMLLoader.load(getClass().getResource("/gui_client_windows/CEOReports.fxml"));
			title = "CEO Reports";
		}
		
		Stage primaryStage = new Stage();
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/css/everything.css").toExternalForm());
		primaryStage.setTitle(title);
		primaryStage.setScene(scene);		
		primaryStage.show();
	}

}
