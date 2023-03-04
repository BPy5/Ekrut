package gui_client;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import logic.Delivery;

/**
 * @author bish_
 *
 */
public class ViewOrdersDeliveryOperatorController implements Initializable {
	private ObservableList<Delivery> allDeliveries;
	private ArrayList<Delivery> changedDeliveries = new ArrayList<>();
	@FXML
	private TableView<Delivery> DeliveriesTable;
	@FXML
	private TableColumn<Delivery,String> orderNumberCol;
	@FXML
	private TableColumn<Delivery,String> customerIDCol;
	@FXML
	private TableColumn<Delivery,String> totalPriceCol;	
	@FXML
	private TableColumn<Delivery,String> locationCol;
	@FXML
	private TableColumn<Delivery,String> statusCol;
	@FXML
	private Button backBtn;
	@FXML
	private Label titlelbl;
	@FXML
	private ImageView image;
	
	
	/**
	 * Initialize window components before wnidow starts
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		String actual = "/images/ekrut.png" ;
		String path = this.getClass().getResource(actual).toExternalForm();
		Image img = new Image(path,true);
		
		image.setImage(img);
		// set title
		this.titlelbl.setText("Welcome " + ChatClient.Fname);
		// import deliveries
		ImportDeliveries();
		
		//allow status column to be editable
		statusCol.setCellFactory(TextFieldTableCell.forTableColumn());
	}
	
	/**
	 * Import all entries of deliveries in database and display them
	 */
	public void ImportDeliveries() {
		changedDeliveries.clear();
		GetRemoteDeliveries();  // get entries
		DisplayDeliveries(); // display entries in window
		DeliveriesTable.refresh();
	}
	
	/**
	 * Reset changes done in table by user
	 */
	public void ResetChangesBtn() {
		// Ask user to confirm action
		boolean option = RaiseAlertConfirmation("Are you sure you want to reset changes?");
		if (option == false)
			return;
		
		// if confirmed, import deliveries again and display them
		ImportDeliveries();
	}
	
	/**
	 * Get all entries of remote deliveries in database
	 */
	private void GetRemoteDeliveries() {
		ClientUI.chat.accept(new Message(0, Command.ReadDeliveries));
//		System.out.println(ChatClient.deliveries);
		allDeliveries = FXCollections.observableArrayList(ChatClient.deliveries);
	}
	
	/**
	 * display entries of deliveries to table
	 */
	public void DisplayDeliveries() {
		orderNumberCol.setCellValueFactory(new PropertyValueFactory<>("order_id"));
		customerIDCol.setCellValueFactory(new PropertyValueFactory<>("customer_id"));
		totalPriceCol.setCellValueFactory(new PropertyValueFactory<>("total_price"));
		locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
		statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
		
		DeliveriesTable.setItems(allDeliveries);
	}
	
	/**
	 * Goes back to previous window (worker ui)
	 * @param event event done in window 
	 * @throws Exception
	 */
	public void BackBtn(ActionEvent event) throws Exception {
		((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
		Stage primaryStage = new Stage();
		
		Parent root = FXMLLoader.load(getClass().getResource("/gui_client_windows/WorkerUI.fxml"));
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/css/everything.css").toExternalForm());
//		String title = (ChatClient.role.equals("dlw")) ? "Deliverer" : "Delivery Operator";
		primaryStage.setTitle("WORKER UI");
		primaryStage.setScene(scene);
		
		primaryStage.show();
	}
	
	/**
	 * Change status of delivery in table
	 * @param productStringCellEditEvent  event done in window ( change event in table)
	 */
	@FXML
	public void ChangeStatus(TableColumn.CellEditEvent<Delivery, String> productStringCellEditEvent) {
		Delivery selectedDelivery = DeliveriesTable.getSelectionModel().getSelectedItem();
		int index = DeliveriesTable.getSelectionModel().getSelectedIndex();
		String oldVal = productStringCellEditEvent.getOldValue();
		String newVal= productStringCellEditEvent.getNewValue();
		
		if (ChatClient.role.equals("dlo")) {
			if (!oldVal.equals("Pending") && !oldVal.equals("Delivered"))
				RaiseAlertConfirmation("Can only modify delivered or pending deliveries!");
			
			else if (newVal.equals("Delivering") || newVal.equals("delivering")) {
				if (oldVal.equals("Pending"))
					addToChangedList(selectedDelivery, index, "Delivering");
			}
			
			else if (newVal.equals("Completed") || newVal.equals("completed")) {
				if (oldVal.equals("Delivered")) 
					addToChangedList(selectedDelivery, index, "Completed");
			}
			
			else 
				RaiseAlertConfirmation("Invalid Status! Modified status should be \"Delivering\" or \"Completed\".");
		}
		
		else if (ChatClient.role.equals("dlw")) {			
			if (!oldVal.equals("Delivering"))
				RaiseAlertConfirmation("Can only modify deliveries in transit!");
			
			else if (newVal.equals("Delivered") || newVal.equals("delivered"))
				addToChangedList(selectedDelivery, index, "Delivered");
				
			else 
				RaiseAlertConfirmation("Invalid Status! Modified status should be \"Delivered\".");
		}
		
		DeliveriesTable.refresh();
	}
	
	/**
	 * add changed deliveries to an arraylist
	 * @param selectedDelivery changed deliveries in table
	 * @param index index of changed deliveries
	 * @param status  status of deliveries
	 */
	private void addToChangedList(Delivery selectedDelivery, int index, String status) {
		selectedDelivery.setStatus(status);
		allDeliveries.set(index, selectedDelivery);
		
		if (!changedDeliveries.contains(selectedDelivery))
			changedDeliveries.add(selectedDelivery);
	}
	
	/**
	 * update all deliveries to database
	 */
	public void UpdateDeliveries() {
		boolean isConfirmed = RaiseAlertConfirmation("Are you sure you want to update deliveries.");
		if (!isConfirmed)
			return;
		
		String  estimatedDelivery;
		int size = changedDeliveries.size();
		String[] dataForUpdate = new String[size+1];
		
		if (size == 0) {
			RaiseAlertConfirmation("No Changes have been made.");
			return;
		}
			
		dataForUpdate[0] = "delivery";
		for (int i = 1; i < dataForUpdate.length; i++) {
			if (ChatClient.role.equals("dlo") && changedDeliveries.get(i-1).getStatus().equals("Delivering")) {
				estimatedDelivery = addDaysToDate(changedDeliveries.get(i-1).getShipping_date().toString());
			}
			
			else {
				estimatedDelivery = changedDeliveries.get(i-1).getEstimated_Delivery().toString();
			}
			
			dataForUpdate[i] = changedDeliveries.get(i-1).getOrder_id() + " " + changedDeliveries.get(i-1).getStatus() + " " + estimatedDelivery;
//			System.out.println(dataForUpdate[i]);
		}
		ClientUI.chat.accept(new Message(dataForUpdate, Command.UpdateDeliveries));
		
		dataForUpdate[0] = "orders";
		for (int i = 1; i < dataForUpdate.length; i++) {
			dataForUpdate[i] = changedDeliveries.get(i-1).getOrder_id() + " " + changedDeliveries.get(i-1).getStatus();
//			System.out.println(dataForUpdate[i]);
		}
		ClientUI.chat.accept(new Message(dataForUpdate, Command.UpdateOrders));
	}
	
	/**
	 * accepts date and increments 14 days to it, returns new date after incrementation
	 * @param date date to update
	 * @return return the updated date
	 */
	private String addDaysToDate(String date) {
		String[] dateComponents = date.split("-");
		int year = Integer.parseInt(dateComponents[0]);
		int month = Integer.parseInt(dateComponents[1]);
		int day = Integer.parseInt(dateComponents[2]);
		
		// Add 14 days to date
		LocalDate newDate = LocalDate.of(year, month, day).plusDays(15);
		return newDate.toString();
	}

	/**
	 * raise alert to client
	 * @param message  message to display
	 * @return whether pressed ok or cancel
	 */
	private boolean RaiseAlertConfirmation(String message) {
		Alert alert = new Alert(AlertType.CONFIRMATION, message, ButtonType.OK, ButtonType.CANCEL);
		alert.showAndWait();
		if (alert.getResult() == ButtonType.OK)
			return true;
		
		return false;
	}	
}
