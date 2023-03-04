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
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;


/**
 * Class responsible for ceo reports window
 */
public class CEOReportsController implements Initializable {
	
	// Buttons in window
	@FXML
	private Label titleLbl;
	@FXML
	private Button viewMachinesBtn;
	@FXML
	private Button monthlyReportBtn;
	@FXML
	private Button inventoryReportBtn;
	@FXML
	private Button backBtn;
	@FXML
	private Button viewdiscountBtn;
	@FXML
	private ImageView image;
	
	/**
	 * Method that runs before displaying the window.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		String actual = "/images/ekrut.png" ;
		String path = this.getClass().getResource(actual).toExternalForm();
		Image img = new Image(path,true);
		
		image.setImage(img);
		// set title to welcome and ceo name
		this.titleLbl.setText("Welcome " + ChatClient.Fname);
	}
	
	
	/**
	 * Load Stock table window when pressing View Machines button
	 * @param event Type of action that occurred in the window by the user (when pressing a button in this scenario)
	 * @throws Exception
	 */
	public void ViewMachines(ActionEvent event) throws Exception{
		nextWindow(event,"/gui_client_windows/StockTable.fxml","Stock Table");
	}

	/*
	 * Load discount for location window when pressing viewDiscount button
	 * @param event Type of action that occurred in the window by the user (when pressing a button in this scenario)
	 * @throws Exception
	 */
	public void viewDiscount(ActionEvent event) throws Exception {
		nextWindow(event,"/gui_client_windows/DiscountLocation.fxml","Discount Location");
	}
	
	/*
	 * Load reports window window when pressing viewReports button
	 * @param event Type of action that occurred in the window by the user (when pressing a button in this scenario)
	 * @throws Exception
	 */
	public void ViewReports(ActionEvent event) throws Exception {
		nextWindow(event,"/gui_client_windows/ChooseReportType.fxml","Choose Report Type");
	}

	/*
	 * back to main window
	 * @param event Type of action that occurred in the window by the user (when pressing a button in this scenario)
	 * @throws Exception
	 */
	public void BackBtn(ActionEvent event) throws Exception {
		nextWindow(event,"/gui_client_windows/WorkerUI.fxml","WORKER UI");
	}
	
	/*
	 * Loads desired window with the desired title
	 * @param event Type of action that occurred in the window by the user (when pressing a button in this scenario)
	 * @param window_location Window location to load
	 * @param title Title of window
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
