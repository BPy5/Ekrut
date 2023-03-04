package gui_client;



import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
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
import javafx.scene.control.TextField;
import logic.Item;
import logic.Order;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class RemoteCodeController implements Initializable{

	@FXML
	private Button backbtn;
	@FXML
	private Button OrderSummaryBtn;
	@FXML
	private TextField Codetxt;
	@FXML
	private ImageView image;
	
	private boolean flag = true;
	
	/**
	 * Retrieves order by order_number in database and check if said order matches the criteria of remote order reception
	 * If order code exists in database then conducts further checks
	 * If the retrieved order matches the customer ID,supply method,machine ID and status then the order given to the user
	 * then updates the order status and moves to Receipt.fxml window
	 * Otherwise throws alert
	 * 
	 * @param event - Type of action that occurred in the window by the user (when pressing a button in this scenario)
	 */
	public void ProceedSummary(ActionEvent event) {
		flag = true;
		if(Codetxt.getText().isEmpty())
		{
			Alert alert = new Alert(AlertType.ERROR,"Code field can't be empty",ButtonType.OK);
			alert.showAndWait();
		}
		else
		{
			try {
				Integer.parseInt(Codetxt.getText());
				Message msg = new Message(null,null);
				msg.setCommand(Command.ReadOrders);
				msg.setContent(Integer.parseInt(Codetxt.getText()));
				ClientUI.chat.accept(msg);
			}catch(Exception e) {
				Alert alert = new Alert(AlertType.ERROR,"Code must be a number",ButtonType.OK);
				alert.showAndWait();
				flag = false;
			}
			
			if(flag == true)
			{
				try {
					Order order = ChatClient.orders.get(0);
					if(ChatClient.ID == order.getCustomer_id() && (order.getSupply_method().equals("Self pickup") && !(order.getOrder_status().equals("Picked up") && (order.getMachine_id() == ChatClient.machineToLoad))))
					{
						AddItemsToCart(order.getItems_in_order());
						Message supplyMethod = new Message(null,null);
						ArrayList<String> arr = new ArrayList<String>();
						arr.add("ordersupplymethod");
						arr.add(Codetxt.getText());
						arr.add("Picked up");
						supplyMethod.setCommand(Command.UpdateOrderSupplyMethod);
						supplyMethod.setContent(arr);
						ClientUI.chat.accept(supplyMethod);
						((Node) event.getSource()).getScene().getWindow().hide();
						Parent root = FXMLLoader.load(getClass().getResource("/gui_client_windows/Receipt.fxml"));
						Stage primaryStage = new Stage();
						Scene scene = new Scene(root);
						primaryStage.setTitle("Receipt");
						primaryStage.setScene(scene);
						primaryStage.show();
						
					}
					else
					{
						Alert alert = new Alert(AlertType.ERROR,"Can't retrieve order",ButtonType.OK);
						alert.showAndWait();
					}
					//cart = order.items_in_order
					//Add tests to check if the ID in the order is the same ID of the current user, and if the Supply Method is not "Picked up"
					//Update Supply Method to "Picked Up"
					//and go to receipt
				}catch(Exception e) {
					Alert alert = new Alert(AlertType.ERROR,"Order doesn't exist",ButtonType.OK);
					alert.showAndWait();
				}
			}
		}
	}
	
	/**
	 * Adds string of items to the static cart in ChatClient
	 * 
	 * @param items - a string of items to add 
	 */
	public void AddItemsToCart(String items)
	{
		ChatClient.cart = new ArrayList<Item>();
		String[] itemsArr = items.split("[.]");
		String[] nameAmount;
		for(String item : itemsArr)
		{
			nameAmount = item.split(",");
			ChatClient.cart.add(new Item(nameAmount[0],nameAmount[1],GetPrice(nameAmount[0])));
		}
	}
	
	/**
	 * Hides current window
	 * Goes back to UserUI.fxml window
	 * 
	 * @param event - Type of action that occurred in the window by the user (when pressing a button in this scenario)
	 * @throws IOException
	 */
	public void BackBtn(ActionEvent event) throws IOException {
		((Node) event.getSource()).getScene().getWindow().hide();
		Parent root = FXMLLoader.load(getClass().getResource("/gui_client_windows/UserUI.fxml"));
		Stage primaryStage = new Stage();
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/css/everything.css").toExternalForm());
		primaryStage.setTitle("EKRUT");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * Reads items 
	 * Reads machines
	 *
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		String actual = "/images/ekrut.png" ;
		String path = this.getClass().getResource(actual).toExternalForm();
		Image img = new Image(path,true);
		
		image.setImage(img);
		Message msg = new Message(null,null);
		msg.setCommand(Command.ReadItems);
		msg.setContent(0);
		ClientUI.chat.accept(msg);
		msg = new Message(null,null);
		msg.setCommand(Command.ReadMachines);
		msg.setContent(0);
		ClientUI.chat.accept(msg);
	}
	
	/**
     * Gets the price of an item by name, searches the items ArrayList in ChatClient (Which holds all items)
     * 
     * @param name - item name
     * @return Price of the item if found, otherwise returns -1
     */
	public int GetPrice(String name)
	{
		for(Item item : ChatClient.items)
		{
			if(item.getProductID().equals(name))
				return item.getPrice();
		}
		return -1;
	}
}
