package gui_client;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
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
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import logic.InventoryReports;
import logic.Item;

/**
 * This class loads the data for inventory report.
 */
public class InventoryDataController implements Initializable{
	
	private ObservableList<ViewItem> obs;
	Message messageToServer = new Message(null, null);
	
	@FXML
	private PieChart pieChart;
	
	@FXML
	private Label dateLbl;
	@FXML
	private Label locationLbl;
	@FXML
	private Label monthYearLbl;
	@FXML
	private Label stockLbl;
	@FXML
	private Label machineIdLbl;
	@FXML
	private ImageView image;
	
	@FXML
	private TableView<ViewItem> tableSub;
	@FXML
	private TableColumn<ViewItem,String> nameCol;
	@FXML
	private TableColumn<ViewItem,String> unitPriceCol;
	@FXML
	private TableColumn<ViewItem,String> inventoryCol;
	@FXML
	private TableColumn<ViewItem,String> stockCol;
	@FXML
	private TableColumn<ViewItem,String> valueCol;
	
	ArrayList<ViewItem> items = new ArrayList<>();
	ArrayList<PieChart.Data> data = new ArrayList<>();
	
	ObservableList<PieChart.Data> pieChartData;
	
	InventoryReports report = InventoryReportController.requestedInventoryReport;
	
	/**
	 * This method initializes the labels in the inventory reports xml file. And calls loadAndSetData function.
	 * @param location
	 * @param resources
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		String actual = "/images/ekrut.png" ;
		String path = this.getClass().getResource(actual).toExternalForm();
		Image img = new Image(path,true);
		
		image.setImage(img);
		this.dateLbl.setText(String.valueOf(LocalDate.now()));
		this.locationLbl.setText(report.getLocation());
		this.monthYearLbl.setText(report.getMonth()+"/"+report.getYear());
		this.machineIdLbl.setText("machine id : "+report.getMachine_id());
		loadAndSetData();
	}
	
	/**
	 * This method fills the table
	 */
	public void loadAndSetData() {
		
		messageToServer.setCommand(Command.ReadItems);
		messageToServer.setContent(0);	
		ClientUI.chat.accept(messageToServer); 
		
		splitItems(report.getStock(), report.getInventory());
		
		nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
		unitPriceCol.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
		inventoryCol.setCellValueFactory(new PropertyValueFactory<>("inventory"));
		stockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
		valueCol.setCellValueFactory(new PropertyValueFactory<>("value"));
		
		tableSub.setItems(obs);
	}
	
	
	/**
	 * This method calculates the current stock and inventory then initializes the pie chart and table.
	 * @param stock
	 * @param inventory
	 */
	public void splitItems(String stock, String inventory)
	 {
		 String[] stockSplit = stock.split("\\.");
		 String[] inventorySplit = inventory.split("\\.");
		 String[] stockFinalSplit = null;
		 String[] inventoryFinalSplit = null;
		 int price;
		 int val;
		 int i = 0;
		 for (String s : stockSplit) {
			stockFinalSplit = s.split(",");
			inventoryFinalSplit = inventorySplit[i++].split(",");
			price = getUnitPriceColumn(stockFinalSplit[0]);
			val = price*Integer.parseInt(inventoryFinalSplit[1]);
			items.add(new ViewItem(stockFinalSplit[0], String.valueOf(price)+" NIS", inventoryFinalSplit[1], stockFinalSplit[1] , String.valueOf(val)+" NIS"));
			data.add(new PieChart.Data(stockFinalSplit[0], Integer.parseInt(stockFinalSplit[1])));
			
		}
		 
		pieChartData = FXCollections.observableArrayList(data);
		pieChart.getData().addAll(pieChartData);
		obs = FXCollections.observableArrayList(items);
		
	 }
	
	/**
	 * This method reads items from data base and returns the price of the item
	 * @param name
	 * @return item.getPrice()
	 */
	public int getUnitPriceColumn(String name) {
		
		for (Item item : ChatClient.items)
			if (name.equals(item.getProductID()))
				return item.getPrice();
		return 0;
	}
	
	
	/**
	 * This class shows relevant information about an item.
	 */
	public class ViewItem
	{
		/**
		 * name of item
		 */
		private String name;
		
		/**
		 * the unit price of the item
		 */
		private String unitPrice;
		
		/**
		 * how much of the item is in the inventory
		 */
		private String inventory;
		
		/**
		 * the current stock of the item
		 */
		private String stock;
		
		/**
		 * the inventory value
		 */
		private String value;
		
		public String getName() {
			return name;
		}
		
		public String getUnitPrice() {
			return unitPrice;
		}
		
		public String getInventory() {
			return inventory;
		}
		
		public String getStock() {
			return stock;
		}
		
		public String getValue() {
			return value;
		}
		
		/**
		 * @param name - item name
		 * @param unitPrice - item price
		 * @param inventory - item inventory amount
		 * @param stock - item current stock
		 * @param value - inventory value
		 */
		public ViewItem(String name,String unitPrice,String inventory,String stock,String value) {
			this.name = name;
			this.unitPrice = unitPrice;
			this.inventory = inventory;
			this.stock = stock;
			this.value = value;
		}
	}

	/**
	 * This method hides the currently open window and shows InventoryReport window.
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void BackBtn(ActionEvent event) throws Exception
	{
		((Node)event.getSource()).getScene().getWindow().hide();
		Parent root = FXMLLoader.load(getClass().getResource("/gui_client_windows/InventoryReport.fxml"));
		Stage primaryStage = new Stage();
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/css/everything.css").toExternalForm());
		primaryStage.setTitle("Inventory Report");
		primaryStage.setScene(scene);		
		primaryStage.show();	
	}
	
	

}
