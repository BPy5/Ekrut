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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import logic.Item;
import logic.Subscriber;


public class CartController implements Initializable{	
	@FXML
	private Button removebtn;
	@FXML
	private Button paymentbtn;
	@FXML
	private Button removeallbtn;
	@FXML
	private Button backbtn;
	@FXML
	private TextField removeidtxt;
	@FXML
	private TextField totalpricetxt;
	@FXML
	private ImageView image;
	@FXML
	private TableView<ItemTable> table;
	@FXML
	private TableColumn<ItemTable,String> productidcol;
	@FXML
	private TableColumn<ItemTable,String> amountcol;
	@FXML
	private TableColumn<ItemTable,String> pricecol;

	private ArrayList<Item> cart;
	private ArrayList<ItemTable> tableCart;
	private ObservableList<ItemTable> obs;
	private int Threshold = 0;
	private int MachineNumber = -1;
	
	/**
	 * Fetches local carts value from static cart in ChatClient
	 * Initializes table columns
	 * Loads values into table
	 */
	public void LoadAndSetTable() {
		
		cart = ChatClient.cart;
		productidcol.setCellValueFactory(new PropertyValueFactory<>("Label"));
		amountcol.setCellValueFactory(new PropertyValueFactory<>("Amount"));
		pricecol.setCellValueFactory(new PropertyValueFactory<>("PriceAll"));	
		createItemTableCart();
		obs = FXCollections.observableArrayList(tableCart);  // insert database details to obs
		table.setItems(obs);  // load database colummns into table and display them
	}
	
	/**
	 * Checks if user is a subscriber and works accordingly
	 * If user is a subscriber then calls InsertOrder()
	 * Otherwise moves to Purchase window
	 * 
	 * @param event - Type of action that occurred in the window by the user (when pressing a button in this scenario)
	 * @throws Exception
	 */
	public void ProceedPayment(ActionEvent event) throws Exception {
		
		if(!cart.isEmpty())
		{
			Parent root = null;
			if(ChatClient.isSubscriber == true)
			{
				Message msg = new Message(null,null);
				msg.setContent(ChatClient.ID);
				msg.setCommand(Command.ReadUserVisa);
				ClientUI.chat.accept(msg);
				Optional<ButtonType> result = InsertOrder(event);
				if(result.get() == ButtonType.YES) {
					((Node)event.getSource()).getScene().getWindow().hide();
					root = FXMLLoader.load(getClass().getResource("/gui_client_windows/Receipt.fxml"));
					Stage primaryStage = new Stage();
					Scene scene = new Scene(root);
					scene.getStylesheets().add(getClass().getResource("/css/everything.css").toExternalForm());
					primaryStage.setTitle("Receipt");
					primaryStage.setScene(scene);		
					primaryStage.show();
				}
			}
			else
			{
				((Node)event.getSource()).getScene().getWindow().hide();
				root = FXMLLoader.load(getClass().getResource("/gui_client_windows/Purchase.fxml"));
				Stage primaryStage = new Stage();
				Scene scene = new Scene(root);
				scene.getStylesheets().add(getClass().getResource("/css/everything.css").toExternalForm());
				primaryStage.setTitle("Purchase");
				primaryStage.setScene(scene);		
				primaryStage.show();
			}
			
		}
		else
		{
			Alert alert = new Alert(AlertType.ERROR,"Must have items in cart to purchase",ButtonType.OK);
			alert.showAndWait();
		}
	}
	/**
	 * Saves local carts value in static cart in ChatClient
	 * Goes back to order window
	 * 
	 * @param event - Type of action that occurred in the window by the user (when pressing a button in this scenario)
	 * @throws Exception
	 */
	public void Back(ActionEvent event) throws Exception {
		ChatClient.cart = this.cart;
		((Node)event.getSource()).getScene().getWindow().hide();
		Parent root = FXMLLoader.load(getClass().getResource("/gui_client_windows/ekrutOrder.fxml"));
		Stage primaryStage = new Stage();
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/css/everything.css").toExternalForm());

		primaryStage.setTitle("Ekrut Order");
		primaryStage.setScene(scene);		
		primaryStage.show();
	}
	
	/**
	 * Removes selected item
	 * Throws an alert if there's no selected item
	 * Updates total price value and labels value
	 * Reloads table
	 * Throws an alert after successful procedure
	 */
	public void RemoveItem() {
		
		try {
			ItemTable remove = table.getSelectionModel().getSelectedItem();
			int index = getItemIndex(remove.getLabel());
			Alert alert = new Alert(AlertType.WARNING,"Are you sure you want to remove this item?",ButtonType.NO,ButtonType.YES);
			Optional<ButtonType> result = alert.showAndWait();
			if(result.get() == ButtonType.YES) {
				if(index != -1) {
					cart.remove(index);
				}
				updateTotalPrice();
				LoadAndSetTable();
				alert = new Alert(AlertType.CONFIRMATION,"Item removed successfully!",ButtonType.OK);
				alert.showAndWait();
			}
				
		}catch(NullPointerException e) {
			
			Alert alert = new Alert(AlertType.ERROR,"Select an item to remove!",ButtonType.OK);
			alert.showAndWait();
		}
	}

    /**
     * Throws an alert that asks if user is sure he wants to remove
     * Removes all items from cart
     */
    public void RemoveAll() {
    	Alert alert = new Alert(AlertType.WARNING,"Are you sure you want to remove all item?",ButtonType.NO,ButtonType.YES);
		Optional<ButtonType> result = alert.showAndWait();
		if(result.get() == ButtonType.YES) {
	    	cart.removeAll(cart);
	    	updateTotalPrice();
	    	LoadAndSetTable();
	    	alert = new Alert(AlertType.CONFIRMATION,"Remove all items!",ButtonType.OK);
			alert.showAndWait(); //**************************************************************** 
		//add an alert that asks the customer if hes sure he wants to remove all items
		}
    	
    }
    
	/**
	 *	Calls FindMachineNumber()
	 *	Calls LoadAndSetTable()
	 *	updateTotalPrice()
	 *	Initializes all window components
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		String actual = "/images/ekrut.png" ;
		String path = this.getClass().getResource(actual).toExternalForm();
		Image img = new Image(path,true);
		
		image.setImage(img);
		FindMachineNumber(ChatClient.machineToLoad);
		LoadAndSetTable();
		updateTotalPrice();
	}
	
	 /**
	 * Calculates total price 
	 * Sets the total price label to new total price value
	 */
	public void updateTotalPrice()
	 {
	    	int sum = 0;
	    	for(Item item : cart)
	    	{
	    		sum += item.getPrice() * Integer.parseInt(item.getAmount());
	    	}
	    	totalpricetxt.setText(String.valueOf(sum));
	    	return;
	 }
	 
	 /**
	  * Searches for an item in items ArrayList in ChatClient
	  * returns the price of item if found
	  * returns -1 if the item wasn't found
	  * 
	 * @param name - item name
	 * @return price of an item if found, -1 otherwise
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
	  * Searches for the item in local cart
	  * Returns the index of the item in local cart if found
	  * Returns -1 otherwise
	  *
	  * @param name - item name
	  * @return index of an item in cart if found, -1 otherwise
	 */
	public int getItemIndex(String name)
	 {
		 int size = cart.size();
		 for(int i =0;i<size;i++)
		 {
			 if(cart.get(i).getProductID().equals(name))
				 return i;
		 }
		 return -1;
	 }
	 
	 /**
	 * Generates cart using ItemTable instances
	 */
	public void createItemTableCart()
	 {
		 tableCart = new ArrayList<ItemTable>();
		 for(Item item : cart)
		 {
			 tableCart.add(new ItemTable(item.getProductID(),Integer.parseInt(item.getAmount()),item.getPrice()));
		 }
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
	public Optional<ButtonType> InsertOrder(ActionEvent event) throws IOException
		{
			Alert alert = new Alert(AlertType.WARNING,"Are you sure you want to continue?",ButtonType.NO,ButtonType.YES);
			Optional<ButtonType> result = alert.showAndWait();
			if(result.get() == ButtonType.YES)
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
			return result;
		}
		
		/**
		 * Calculates total price based on items in cart
		 * 
		 * @return total price of items in cart
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
		
		
		/**
		 * This class is used to represent items in table
		 *
		 */
		public class ItemTable{
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
}

