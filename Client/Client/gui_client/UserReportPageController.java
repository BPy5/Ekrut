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
import logic.UsersReports;

/**
 * This class lets the CEO choose which user report he would like to view
 */
public class UserReportPageController implements Initializable{

	@FXML
	private Button showReportBtn;
	@FXML
	private Button backBtn;
	
	@FXML
	public ComboBox<String> cmbYear;
	@FXML
	private ComboBox<String> cmbMonth;
	@FXML
	private ImageView image;
	
	public String year;
	public String month;
	
	public static String counters;
	
	ObservableList<String> yearList;
	ObservableList<String> MonthList;
	
	Message messageToServer = new Message(null,null);
	
	/**
	 * This method saves the selected value from the year combo box in static string
	 * @param event
	 */
	@FXML
	public void SelectYear(ActionEvent event) {
		year = cmbYear.getSelectionModel().getSelectedItem().toString();
	}
	
	/**
	 * This method saves the selected value from the month combo box in static string
	 * @param event
	 */
	@FXML
	public void SelectMonth(ActionEvent event) {
		month = cmbMonth.getSelectionModel().getSelectedItem().toString();
	}
	
	
	/**
	 * This method initializes all the combo boxes in the window
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		String actual = "/images/ekrut.png" ;
		String path = this.getClass().getResource(actual).toExternalForm();
		Image img = new Image(path,true);
		
		image.setImage(img);
		ArrayList<String> year = new ArrayList<>(Arrays.asList("2022","2023"));
		
		yearList = FXCollections.observableArrayList(year);
		cmbYear.getItems().clear();
		cmbYear.setItems(yearList);
		
		ArrayList<String> month = new ArrayList<String>(Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"));
		
		MonthList = FXCollections.observableArrayList(month);
		cmbMonth.getItems().clear();
		cmbMonth.setItems(MonthList);
	}
	
	/**
	 * This method shows the report if it exists 
	 * @param event
	 * @throws Exception
	 */
	public void ShowReportBtn(ActionEvent event) throws Exception{
		// Checking if one or more fields are empty
		boolean foundReport = false;
		if ((cmbYear.getValue() == null) || (cmbMonth.getValue() == null))
		{
			Alert alert = new Alert(AlertType.ERROR,"One or more feilds is Empty!",ButtonType.OK);
			alert.showAndWait();
		}
		else
			{
			messageToServer.setCommand(Command.ReadUserReports);
			messageToServer.setContent(0);
			ClientUI.chat.accept(messageToServer);
			
			for (UsersReports userReport : ChatClient.usersReport) {
				if ((userReport.getMonth().equals(month)) && (userReport.getYear().equals(year)))
				{
					counters = userReport.getData();
					foundReport = true;	// if the report was found
					break;
				}
			}
			if (foundReport)
			nextWindow(event, "/gui_client_windows/BarChart.fxml", "Bar Chart (Users Report)");
			else {
				Alert alert = new Alert(AlertType.ERROR,"Couldn't find report",ButtonType.OK);
				alert.showAndWait();
			}
		}
	}
	
	/**
	 * This method calls nextWindow function to show new windnow
	 * @param event
	 * @throws Exception
	 */
	public void BackBtn(ActionEvent event) throws Exception {
		nextWindow(event, "/gui_client_windows/ChooseReportType.fxml", "Choose Report Type");
	}
	
	
	/**
	 * This method hides the currently open window and shows the requested one (based on path)
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
