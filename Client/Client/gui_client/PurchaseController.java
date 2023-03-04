package gui_client;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import logic.Item;

public class PurchaseController implements Initializable{

	@FXML
	private TextField creditCardtxt;
	@FXML
	private Button backBtn;
	@FXML
	private Button purchaseBtn;
	@FXML
	private TextField priceLbl;
	@FXML
	private ImageView image;
	
	private int Threshold = 0;
	
	private int MachineNumber = -1;
	
	
	
	/**
	 * Calls nextWindow() with parameters of Cart.fxml window
	 * 
	 * @param event - Type of action that occurred in the window by the user (when pressing a button in this scenario)
	 * @throws Exception
	 */
	public void BackBtn(ActionEvent event) throws Exception {
		nextWindow(event, "/gui_client_windows/Cart.fxml", "Cart");	
	}
	
	
	/**
	 * Checks if creditCard textfield holds a valid credit card value or is empty
	 * If its empty then read credit card info from database
	 * Otherwise save credit card details and call InsertOrder()
	 * After inserting order calls nextWindow() with parameters of Receipt.fxml window
	 * Throws alert if credit card info is invalid and isn't empty
	 * 
	 * @param event - Type of action that occurred in the window by the user (when pressing a button in this scenario)
	 * @throws Exception
	 */
	public void Purchase(ActionEvent event) throws Exception {
		if(creditCardtxt.getText().length() != 16)
		{
			if(creditCardtxt.getText().isEmpty())
			{
				Alert alert = new Alert(AlertType.WARNING,"Are you sure you want to continue?",ButtonType.NO,ButtonType.YES);
				Optional<ButtonType> result = alert.showAndWait();
				if(result.get() == ButtonType.YES) {
					Message msg = new Message(null,null);
					msg.setContent(ChatClient.ID);
					msg.setCommand(Command.ReadUserVisa);
					ClientUI.chat.accept(msg);
					InsertOrder(event);
					nextWindow(event, "/gui_client_windows/Receipt.fxml", "Receipt");
				}
			}
			else
			{
				Alert alert = new Alert(AlertType.ERROR,"Please enter valid credit card details, alternatively leave the field blank if you wish to use the credit card you signed up with",ButtonType.OK);
				alert.showAndWait();
			}
		}
		else
		{
			if (!creditCardtxt.getText().matches("[0-9]+")) {
				Alert alert = new Alert(AlertType.ERROR,"Please enter valid credit card details, alternatively leave the field blank if you wish to use the credit card you signed up with",ButtonType.OK);
				alert.showAndWait();
			}
			
			else {
				Alert alert = new Alert(AlertType.WARNING,"Are you sure you want to continue?",ButtonType.NO,ButtonType.YES);
				Optional<ButtonType> result = alert.showAndWait();
				if(result.get() == ButtonType.YES) {
					ChatClient.creditcard = creditCardtxt.getText();
					InsertOrder(event);
					nextWindow(event, "/gui_client_windows/Receipt.fxml", "Receipt");
				}
			}
			
		}
	}

	 /**
	 * Calculates the new total price value
	 * Sets price label value to new total price value 
	 */
	public void updateTotalPrice()
	 {
	    	int sum = 0;
	    	for(Item item : ChatClient.cart)
	    	{
	    		sum += item.getPrice() * Integer.parseInt(item.getAmount());
	    	}
	    	priceLbl.setText(String.valueOf(sum));
	    	return;
	 }
	 
	 /**
	  * Searches for item in items ArrayList in ChatClient 
	  * If item is found then return price
	  * Otherwise returns -1
	  * 
	 * @param name - item name
	 * @return item price if found, -1 otherwise
	 */
	public int getPrice(String name)
	 {
	    	for(Item item : ChatClient.items)
	    	{
	    		if(item.getProductID().equals(name))
	    			return item.getPrice();
	    	}
	    	return -1; 
	 }
	
	/**
	 * Calls FindMachineNumber()
	 * Calls updateTotalPrice()
	 * Initializes all window components
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		String actual = "/images/ekrut.png" ;
		String path = this.getClass().getResource(actual).toExternalForm();
		Image img = new Image(path,true);
		
		image.setImage(img);
		FindMachineNumber(ChatClient.machineToLoad);
		updateTotalPrice();
		
	}
	
	/**
	 * Hides current window
	 * Moves to window based on location
	 * 
	 * @param event - Type of action that occurred in the window by the user (when pressing a button in this scenario)
	 * @param window_location - location of next window
	 * @param title - title of next window
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
	
	/**
	  * Does preparations to move to purchase window
	  * If cart is empty then throw an alert and stay in cart otherwise saves cart in static cart in ChatClient
	  * Inserts order into database
	  * Inserts Delivery into database if the user demanded a delivery
	  * Updates the user status if this was his first order as a subscriber
	  * 
	 * @param event - Type of action that occurred in the window by the user (when pressing a button in this scenario)
	 * @return result of alert, ButtonType.YES if user pressed yes, ButtonType.NO otherwise 
	 * @throws IOException
	 */
	public void InsertOrder(ActionEvent event) throws IOException
	{
		
			int size = ChatClient.availableItems.size();
			ArrayList<Integer> itemAmount = ChatClient.available;
			String itemsToFill = "";
			for(int i =0;i<size;i++)
			{
				if(itemAmount.get(i) <= Threshold)
				{
					if(i!= 0)
						itemsToFill += ",";
					itemsToFill += ChatClient.availableItems.get(i);
				}
			}
			if(!itemsToFill.isEmpty()) //send request to fill machine by threshold
			{
				ClientUI.chat.accept(new Message(ChatClient.machineToLoad,Command.ReadStockRequests));
				if(ChatClient.stockRequests.isEmpty())
				{
					ArrayList<String> data = new ArrayList<String>();
					data.add(String.valueOf(ChatClient.machineToLoad));
					ClientUI.chat.accept(new Message(data,Command.InsertStockRequest));
				}
				
			}
			((Node)event.getSource()).getScene().getWindow().hide();
			Alert alert1 = new Alert(AlertType.CONFIRMATION,"Order added!",ButtonType.OK);
			alert1.showAndWait();
			ArrayList<String> data = new ArrayList<String>();
			data.add(String.valueOf(ChatClient.ID));
			data.add(ChatClient.locationName);
			String items = "";
			int price = 0;
			for(int i =0;i<ChatClient.cart.size();i++)
			{
				items += ChatClient.cart.get(i).getProductID();
				items += ",";
				items += ChatClient.cart.get(i).getAmount();
				items += ".";
				price += ChatClient.cart.get(i).getPrice()* Integer.parseInt(ChatClient.cart.get(i).getAmount());
			}
			data.add(items);
			data.add(ChatClient.creditcard);
			data.add(String.valueOf(price));
			data.add(ChatClient.supplyMethod);
			data.add(String.valueOf(ChatClient.machineToLoad));
			String newStock = CreateNewStock(ChatClient.available,ChatClient.availableItems);
			data.add(newStock);
			int totalInventory = CreateNewInventory(newStock);
			data.add(String.valueOf(totalInventory));
			Message msg = new Message(data,Command.InsertOrder);
			ClientUI.chat.accept(msg);
			if(ChatClient.supplyMethod == "Delivery")
			{
				data = new ArrayList<String>();
				ClientUI.chat.accept(new Message(0,Command.ReadOrders));
				size = ChatClient.orders.size();
				data.add(String.valueOf(ChatClient.orders.get(size-1).getOrder_num()));
				data.add(String.valueOf(ChatClient.ID));
				data.add(String.valueOf(calculateTotalPrice()));
				data.add(ChatClient.deliveryLocation);
				data.add(ChatClient.address);
				ClientUI.chat.accept(new Message(data,Command.InsertDelivery));
			}
			if(ChatClient.FirstSubscriberOrder == true)
			{
				msg = new Message(null,null);
				ArrayList<String> param = new ArrayList<String>();
				param.add("usersFirstCart");
				param.add(String.valueOf(ChatClient.ID));
				param.add("0");
				msg.setCommand(Command.UpdateUserFirstCart);
				msg.setContent(param);
				ClientUI.chat.accept(msg);
				ChatClient.FirstSubscriberOrder = false;
			}
			//Disconnect
	}
	
	/**
	 * Calculates total price based on items in static cart in ChatClient
	 * 
	 * @return total price
	 */
	public int calculateTotalPrice()
	 {
	    	int sum = 0;
	    	for(Item item : ChatClient.cart)
	    	{
	    		sum += item.getPrice() * Integer.parseInt(item.getAmount());
	    	}
	    	return sum;
	 }
	
	/**
	 * Calculates new stock based on availability of items and amount of items picked in cart
	 * 
	 * @param available - ArrayList of amount available per item
	 * @param availableItems - ArrayList of names of available items
	 * @return string representing new stock
	 */
	public String CreateNewStock(ArrayList<Integer> available,ArrayList<String> availableItems)
	{
		
		int i,j =0;
		int size = ChatClient.machines.get(MachineNumber).getItems().size();
		String newStock = "";
		for(i=0;i<size;i++)
		{
			if(ChatClient.machines.get(MachineNumber).getItems().get(i).equals(availableItems.get(j)))
			{
				newStock += String.valueOf(available.get(j));
				if(i != size-1)
					newStock += ",";
				j++;
			}
			else
			{
				newStock += "0";
				if(i != size-1)
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
	public int CreateNewInventory(String newStock)
	{
		String[] items = newStock.split(",");
		int inv = 0;
		for(String item : items)
		{
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
	public void FindMachineNumber(int id)
	  {
	    	int size = ChatClient.machines.size();
			for(int i = 0;i<size;i++)
			{
				if(ChatClient.machines.get(i).getMachine_id() == id)
					MachineNumber = i;
			}
	  }
	
}
