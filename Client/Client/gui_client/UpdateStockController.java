package gui_client;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.smartcardio.CommandAPDU;

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
import logic.Machine;
import logic.StockRequest;

/**
 * Controller class for window of stock update.
 */
public class UpdateStockController implements Initializable{
	private Message messageToServer = new Message(null, null);
	private ArrayList<ViewItem> newItems = new ArrayList<>();
	private ObservableList<String> cmbList;
	private ObservableList<ViewItem> obs;
	private int machineThreshold = 0;
	private Machine currentMachine = null;
	private boolean hasUpdatedValues = false;
	
	@FXML
	private ImageView image;
	@FXML
	private Button logOutBtn;
	@FXML
	private Button updateBtn;
	@FXML
	private Button resetBtn;
	@FXML
	private Label machineIDlbl;
	@FXML
	private Label machineItemslbl;
	@FXML
	private Label thresholdlbl;
	@FXML
	private Label machineLocationlbl;
	@FXML
	private Label requestIDlbl;
	@FXML
	private ComboBox<String> requestCmb;
	@FXML
	private TableView<ViewItem> machineTable;
	@FXML
	private TableColumn<ViewItem,String> itemsCol;
	@FXML
	private TableColumn<ViewItem,String> amountCol;

	
	/**
	 * Initialize method 
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		String actual = "/images/ekrut.png" ;
		String path = this.getClass().getResource(actual).toExternalForm();
		Image img = new Image(path,true);
		
		image.setImage(img);
		DisplayComponentsInfo(false);
		updateBtn.setDisable(true);
		ReadMachinesAndStockRequests();
		setRequestComboBox();
		itemsCol.setCellValueFactory(new PropertyValueFactory<>("Name"));
		amountCol.setCellValueFactory(new PropertyValueFactory<>("Amount"));
		amountCol.setCellFactory(TextFieldTableCell.forTableColumn());
		
	}
	
	public void ReadMachinesAndStockRequests() {
		ClientUI.chat.accept(new Message(0, Command.ReadStockRequests));
		ClientUI.chat.accept(new Message(0, Command.ReadMachines));
	}
	
	public void setRequestComboBox() {
		ArrayList<String> requests = new ArrayList<String>();
    	for(StockRequest r : ChatClient.stockRequests) {
    		requests.add(String.valueOf(r.getStock_request_id()));
    	}
		cmbList = FXCollections.observableArrayList(requests);
		requestCmb.getItems().clear();
		requestCmb.setItems(cmbList);
	}

	/**
	 * @param event Detected event
	 * @throws Exception
	 */
	public void LogoutBtn(ActionEvent event) throws Exception {
		((Node)event.getSource()).getScene().getWindow().hide();
		Parent root = FXMLLoader.load(getClass().getResource("/gui_client_windows/LoginEkrut.fxml"));
		Stage primaryStage = new Stage();
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/css/everything.css").toExternalForm());
		primaryStage.setTitle("Login Ekrut");
		primaryStage.setScene(scene);		
		primaryStage.show();
		ClientUI.chat.accept(new Message(ChatClient.ID, Command.Disconnect));
	}
	
	
	/**
	 * @param event
	 */
	@FXML
	public void UpdateTable(ActionEvent event) {
		ArrayList<ViewItem> listToDiplay = new ArrayList<>();
		int numberOfItems;
		int chosenRequestID = Integer.parseInt(requestCmb.getSelectionModel().getSelectedItem().toString());
		Machine machineToLoad = FindMachineNumber(chosenRequestID);
		
		if (machineToLoad == null) {
			RaiseAlertError("Cannot load chosen machine.");
			return;
		}
		
		currentMachine = machineToLoad;
		DisplayComponentsInfo(true);
		updateBtn.setDisable(false);
		hasUpdatedValues = false;
		
		machineThreshold = machineToLoad.getThreshold();
		thresholdlbl.setText("Threshold: " + machineToLoad.getThreshold());
		machineLocationlbl.setText("Machine location: " + machineToLoad.getLocation());
		machineIDlbl.setText("Machine ID: " + machineToLoad.getMachine_id());
		
		DisplayOriginalValuesOfMachineToTable();
	}
	
	/**
	 * 
	 */
	public void UpdateBtn() {
		String[] dataForUpdate = new String[5];  //{"machinesAmount", amount_per_item, items, total_inventory, machine_id}
		int new_total_inventory = 0;
		StringBuilder newItemsStr = new StringBuilder(), newAmountsStr = new StringBuilder();
		if (!hasUpdatedValues) {
			RaiseAlertError("No Changes detected.");
			return;
		}
		
		for(ViewItem item : obs) {
			newItemsStr.append(item.getName() + ",");
			newAmountsStr.append(item.getAmount() + ",");
			new_total_inventory += Integer.parseInt(item.getAmount());
		}
		
		newItemsStr.deleteCharAt(newItemsStr.length() - 1);
		newAmountsStr.deleteCharAt(newAmountsStr.length() - 1);
		
		dataForUpdate[0] = "machinesAmount";
		dataForUpdate[1] = newAmountsStr.toString();
		dataForUpdate[2] = newItemsStr.toString();
		dataForUpdate[3] = String.valueOf(new_total_inventory);
		dataForUpdate[4] = String.valueOf(currentMachine.getMachine_id());
		
		ClientUI.chat.accept(new Message(dataForUpdate, Command.UpdateMachineStock));
		Alert alert = new Alert(AlertType.INFORMATION, "Updated machine stock and stock request successfully.", ButtonType.OK);
		alert.showAndWait();
		
		UpdateStockRequest();
		ReadMachinesAndStockRequests();
	}
	
	private void UpdateStockRequest() {
		String[] data = new String[2];
		int chosenRequestID = Integer.parseInt(requestCmb.getSelectionModel().getSelectedItem().toString());
		
		data[0] = "updatestockrequest";
		data[1] = String.valueOf(chosenRequestID);
		
		ClientUI.chat.accept(new Message(data, Command.UpdateStockRequest));
	}
	
	/**
	 * 
	 * @param productStringCellEditEvent
	 */
	public void checkModifications(TableColumn.CellEditEvent<ViewItem, String> productStringCellEditEvent) {
		String newVal = productStringCellEditEvent.getNewValue();
		int index = machineTable.getSelectionModel().getSelectedIndex();
		ViewItem selectedItem = machineTable.getSelectionModel().getSelectedItem();
		if (!productStringCellEditEvent.getNewValue().matches("[0-9]+")) {
			RaiseAlertError("Invalid input! Input should be numerical only.");
			machineTable.refresh();
			return;
		}
		
		if (Integer.parseInt(newVal) <= machineThreshold) {
			RaiseAlertError("New amount should be above threshold of machine.");
			machineTable.refresh();
			return;
		}
		
		selectedItem.setAmount(newVal);
		obs.set(index, selectedItem);
		if (!newItems.contains(selectedItem))
			newItems.add(selectedItem);
		machineTable.refresh();
		hasUpdatedValues = true;
	} 
	
	/**
	 * 
	 */
	public void DisplayOriginalValuesOfMachineToTable() {
		ArrayList<String> items = currentMachine.getItems();
		ArrayList<Integer> amount = currentMachine.getAmountItems();
		ArrayList<ViewItem> listToDiplay = new ArrayList<>();
		int numberOfItems = items.size();
		
		for (int i = 0; i < numberOfItems; i++) {
			listToDiplay.add(new ViewItem(items.get(i), amount.get(i).toString()));
		}
		
		obs = FXCollections.observableArrayList(listToDiplay);
		machineTable.setItems(obs);
	}
	
	/**
	 * @param condition
	 */
	private void DisplayComponentsInfo(boolean condition) {
		thresholdlbl.setVisible(condition);
		machineLocationlbl.setVisible(condition);
		machineIDlbl.setVisible(condition);
		machineItemslbl.setVisible(condition);
		machineTable.setVisible(condition);
		resetBtn.setVisible(condition);
	}
	
	/**
	 * @param requestID
	 * @return
	 */
	private Machine FindMachineNumber(int requestID) {
		int sizeMachines = ChatClient.machines.size();
    	int machineNumber = -1;
    	for (StockRequest sr : ChatClient.stockRequests) {
    		if (sr.getStock_request_id() == requestID) {
    			machineNumber = sr.getMachine_id();
    			break;
    		}
    	}
    	
    	if (machineNumber == -1)
    		return null;
    	
    	for (int i = 0; i < sizeMachines; i++) {
    		if (ChatClient.machines.get(i).getMachine_id() == machineNumber)
    			return ChatClient.machines.get(i);
    	}
    	
    	return null;
	}
	
	/**
	 * @param message
	 */
	private void RaiseAlertError(String message) {
		Alert alert = new Alert(AlertType.ERROR, message, ButtonType.OK);
		alert.showAndWait();
	}	
		
	
	/**
	 * 
	 *
	 */
	public class ViewItem {
		private String Name;
		private String Amount;
		
		public ViewItem(String name,String amount) {
			this.Name = name;
			this.Amount = amount;
		}
		
		public String getName() {
			return Name;
		}
		
		public void setName(String Name) {
			this.Name = Name;
		}
		
		public String getAmount() {
			return Amount;
		}
		
		public void setAmount(String Amount) {
			this.Amount = Amount;
		}
		
		/**
		 *
		 */
		@Override
		public boolean equals(Object obj) {
			ViewItem temp;
			if (obj == null)
				return false;
			
			if (obj.getClass() == this.getClass()) {
				temp = (ViewItem)obj;
				if (temp.getName() == this.getAmount())
					return true;
			}
			
			return false;
		}
	}
}
