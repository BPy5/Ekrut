package gui_client;

import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import client.ChatClient;
import client.ClientUI;
import common.Command;
import common.Message;
import gui_client.CartController.ItemTable;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import logic.*;

public class ReceiptController implements Initializable {

	/**
	 * This class is used to represent items in table
	 *
	 */
	public class ItemTable {
		/**
		 * item name
		 */
		private String label;
		/**
		 * item amount
		 */
		private Integer amount;
		/**
		 * item total price (price * amount)
		 */
		private Integer priceAll;

		
		/**
		 * Constructor that calculates total price (amount times price)
		 * 
		 * @param label - name of item
		 * @param amount - amount of item in stock
		 * @param price - price of item
		 */
		public ItemTable(String label, Integer amount, Integer price) {
			super();
			this.label = label;
			this.amount = amount;
			int NewPrice = amount * price;
			this.priceAll = NewPrice;
		}

		
		/**
		 * Returns item name
		 * 
		 * @return this.label
		 */
		public String getLabel() {
			return label;
		}
		
		
		/**
		 * Sets the item name to a new value
		 * 
		 * @param label - new item name
		 */
		public void setLabel(String label) {
			this.label = label;
		}

		/**
		 * returns item amount
		 * 
		 * @return this.amount
		 */
		public Integer getAmount() {
			return amount;
		}

		
		/**
		 * Sets the item amount to a new value
		 * 
		 * @param amount - new item amount
		 */
		public void setAmount(Integer amount) {
			this.amount = amount;
		}

		/**
		 * Returns the overall price of a singular item
		 * 
		 * @return this.priceAll
		 */
		public Integer getPriceAll() {
			return priceAll;
		}

		/**
		 * Sets the overall price of item to a new value
		 * 
		 * @param priceAll - new item overall price
		 */
		public void setPriceAll(Integer priceAll) {
			this.priceAll = priceAll;
		}

	}
	
	@FXML
	private ImageView image;
	@FXML
	private TableView<ItemTable> receiptTable;
	@FXML
	private TableColumn<ItemTable, String> productIdCol;
	@FXML
	private TableColumn<ItemTable, String> amountCol;
	@FXML
	private TableColumn<ItemTable, String> priceCol;
	@FXML
	private Label ordercodelbl;

	private ArrayList<ItemTable> tableCart = new ArrayList<ItemTable>();

	private ObservableList<ItemTable> obs;

	private int MachineNumber = -1;

	private int Threshold = 0;

	@FXML
	private Button okBtn;
	@FXML
	private Button backBtn;

	/**
	 * Initializes table columns
	 * Calls FindMachineNumber() to find out which machine to load
	 * Calls createItemTableCart()
	 * Fetches threshold from loaded machines
	 * Loads order number onto order code label 
	 * Hides order number label if there's no order to load
	 * Loads cart items onto table
 	 */
	public void LoadAndSetTable() {
		productIdCol.setCellValueFactory(new PropertyValueFactory<>("Label"));
		amountCol.setCellValueFactory(new PropertyValueFactory<>("Amount"));
		priceCol.setCellValueFactory(new PropertyValueFactory<>("PriceAll"));
		FindMachineNumber(ChatClient.machineToLoad);
		createItemTableCart();
		if(ChatClient.orderId != -1)
		{
			ordercodelbl.setText(ordercodelbl.getText() + " " + ChatClient.orderId);
		}
		else
		{
			ordercodelbl.setVisible(false);
		}
		Threshold = ChatClient.machines.get(MachineNumber).getThreshold();
		obs = FXCollections.observableArrayList(tableCart); // insert database details to obs
		receiptTable.setItems(obs); // load database colummns into table and display them
	}

	/**
	 * Hides current window
	 * Disconnects user
	 * Goes back to LoginEkrut window
	 * 
	 * @param event - Type of action that occurred in the window by the user (when pressing a button in this scenario)
	 * @throws Exception
	 */
	public void OKBtn(ActionEvent event) throws Exception {
		Message msg = new Message(null,null);
    	msg.setCommand(Command.Disconnect);
    	msg.setContent(ChatClient.ID);
    	ClientUI.chat.accept(msg);
		((Node) event.getSource()).getScene().getWindow().hide();
		Parent root = FXMLLoader.load(getClass().getResource("/gui_client_windows/LoginEkrut.fxml")); Stage
		primaryStage = new Stage(); 
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/css/everything.css").toExternalForm());
		primaryStage.setTitle("Login ekrut"); primaryStage.setScene(scene);
		primaryStage.show(); 
	}

	/**
	 * Calls LoadAndSetTable()
	 * Initializes window components
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		String actual = "/images/ekrut.png" ;
		String path = this.getClass().getResource(actual).toExternalForm();
		Image img = new Image(path,true);
		
		image.setImage(img);
		LoadAndSetTable();

	}

	/**
	 * Creates new cart based on instances of ItemTable
	 */
	public void createItemTableCart() {
		for (Item item : ChatClient.cart) {
			tableCart.add(new ItemTable(item.getProductID(), Integer.parseInt(item.getAmount()), item.getPrice()));
		}
	}

	/**
	 * Calculates new stock based on availability of items and amount of items picked in cart
	 * 
	 * @param available - ArrayList of amount available per item
	 * @param availableItems - ArrayList of names of available items
	 * @return string representing new stock
	 */
	public String CreateNewStock(ArrayList<Integer> available, ArrayList<String> availableItems) {

		int i, j = 0;
		int size = ChatClient.machines.get(MachineNumber).getItems().size();
		String newStock = "";
		for (i = 0; i < size; i++) {
			if (ChatClient.machines.get(MachineNumber).getItems().get(i).equals(availableItems.get(j))) {
				newStock += String.valueOf(available.get(j));
				if (i != size - 1)
					newStock += ",";
				j++;
			} else {
				newStock += "0";
				if (i != size - 1)
					newStock += ",";
			}
		}
		return newStock;
	}

	/**
	 * Calculates new inventory based on items in cart
	 * 
	 * @param newStock - string representing new stock
	 * @return amount of items in stock
	 */
	public int CreateNewInventory(String newStock) {
		String[] items = newStock.split(",");
		int inv = 0;
		for (String item : items) {
			inv += Integer.parseInt(item);
		}
		return inv;
	}

	/**
	 * Finds the index of the current machine in machines ArrayList in ChatClient
	 * Sets the value of index found in local variable "MachineNumber"
	 * 
	 * @param id - machine ID
	 */
	public void FindMachineNumber(int id) {
		int size = ChatClient.machines.size();
		for (int i = 0; i < size; i++) {
			if (ChatClient.machines.get(i).getMachine_id() == id)
				MachineNumber = i;
		}
	}

	/**
	 * Searches for item in items ArrayList in ChatClient by name
	 * If item is found returns its index
	 * Otherwise returns -1;
	 * 
	 * @param name - item name
	 * @return item index if item is found, -1 otherwise
	 */
	public int findItemIndex(String name) {
		int size = ChatClient.machines.get(MachineNumber).getItems().size();
		for (int i = 0; i < size; i++) {
			if (ChatClient.machines.get(MachineNumber).getItems().get(i).equals(name))
				return i;
		}
		return -1;
	}

	/**
	 * Searches for item in items ArrayList in ChatClient by name
	 * If item is found then returns its price
	 * Otherwise returns -1
	 * 
	 * @param name - item name
	 * @return item price if found, -1 otherwise
	 */
	public int getPrice(String name) {
		for (Item item : ChatClient.items) {
			if (item.getProductID().equals(name))
				return item.getPrice();
		}
		return -1;
	}

	/**
	 * Calculates total price of order
	 * 
	 * @return total price
	 */
	public int calculateTotalPrice() {
		int sum = 0;
		for (Item item : ChatClient.cart) {
			sum += item.getPrice() * Integer.parseInt(item.getAmount());
		}
		return sum;
	}

}
