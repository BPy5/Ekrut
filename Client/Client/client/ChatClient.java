// "Object Oriented Software Engineering" and is issued under the open-source
// This file contains material supporting section 3.7 of the textbook:
// license found at www.lloseng.com 

package client;

import ocsf.client.*;
import common.*;
import gui_client.UserLoginController;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import logic.Item;
import logic.Location;
import logic.Machine;
import logic.OrdersReports;
import logic.Subscriber;
import logic.*;
import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;


//Boom badapam mr.WorldWide
/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
/**
 * @author bish_
 *
 */
/**
 * @author bish_
 *
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
  
  /**
   * Static variables which are objects that were imported from database for controllers to use
   */
  ChatIF clientUI; 
  public static ArrayList<Subscriber> subscribers;//+users
  public static ArrayList<Machine> machines;
  public static ArrayList<Item> items;
  public static ArrayList<Order> orders;
  public static ArrayList<Delivery> deliveries;
  public static ArrayList<OrdersReports> orderReport;
  public static ArrayList<InventoryReports> InventoryReport;
  public static ArrayList<Location> locations;
  public static ArrayList<Item> cart;
  public static ArrayList<String> availableItems; 
  public static ArrayList<Integer> available; 
  public static ArrayList<Request> userRequest;
  public static ArrayList<StockRequest> stockRequests;
  public static ArrayList<UsersReports> usersReport;
  public static String password;
  public static String role;
  public static String Fname;
  public static String locationName = "North"; //?????
  public static String supplyMethod = "Immediate pickup"; //?????
  public static String deliveryLocation = "";
  public static String address = "";
  public static String creditcard = "";
  public static boolean awaitResponse = false;
  public static boolean isSubscriber = false;
  public static boolean FirstSubscriberOrder = false;
  public static int orderId = -1;
  public static int ID;
  public static int machineToLoad = 1;
  public static Thread timer;
  private boolean FirstCart = false;
  //public static Stage primaryStage; //-> fixes the issue of windows popping up AND solves the timing thread
  

  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String host, int port, ChatIF clientUI) throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    openConnection();
    
  }

  
  //Instance methods ************************************************
    
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message object from the server.
   */
  @SuppressWarnings("unchecked")
  public void handleMessageFromServer(Object msg) {	  
	  Message responseFromServer = (Message) msg; // message object
	  
	  // check message type (command)
	  switch(responseFromServer.getCommand()) 
	  {
	  	
	  	  case ReadUsers:
	  		  subscribers = (ArrayList<Subscriber>) responseFromServer.getContent();
	  		  break;
	  		  
	  	 case ReadMachines:
	  		  machines = (ArrayList<Machine>) responseFromServer.getContent();
	  		  break;
	  	
	  	 case ReadItems:
	  		  if(FirstCart == false) {
	  			  cart = new ArrayList<Item>();
	  			  FirstCart = true;
	  		  }
	  		  items = (ArrayList<Item>) responseFromServer.getContent();
	  		  break;
	  		  
	  	  case Disconnect:
	  		  Reset();
	  		  ClientUI.chat.display("Disconnected");
	  		  break;
	  	  
	  	  case Connect:
	  		  String[] passRoleFnameSubNumFirstCart = (String[])(((Message)msg).getContent());
	  		  password = passRoleFnameSubNumFirstCart[0];
	  		  role = passRoleFnameSubNumFirstCart[1];
	  		  Fname = passRoleFnameSubNumFirstCart[2];
	  		  ID =Integer.parseInt(passRoleFnameSubNumFirstCart[5]);
	  		  if(Integer.parseInt(passRoleFnameSubNumFirstCart[3]) != -1)
	  			  isSubscriber = true;
	  		  else
	  			  isSubscriber = false;
	  		  if(Integer.parseInt(passRoleFnameSubNumFirstCart[4]) == 1)
	  			  FirstSubscriberOrder = true;
	  		  else
	  			  FirstSubscriberOrder = false;
	  		  break;
	  		  
	  	case ReadLocations:
	  		locations = (ArrayList<Location>) (responseFromServer.getContent());
	  		break; 
	  		
	  	case ReadOrders:
	  		orders = (ArrayList<Order>)(responseFromServer.getContent());
	  		break;
	  		
	  	case ReadOrdersReports:
	  		orderReport = (ArrayList<OrdersReports>) (responseFromServer.getContent());
	  		break;
	  		
	  	case ReadInventoryReports:
	  		InventoryReport = (ArrayList<InventoryReports>) (responseFromServer.getContent());
	  		break;
	  		
	  	case ReadStockRequests:
	  		stockRequests = (ArrayList<StockRequest>) (responseFromServer.getContent());
	  		break;
	  		
	  	case ReadRequests:
	  		userRequest= (ArrayList<Request>) (responseFromServer.getContent());
	  		break;
	  		
	  	case ReadUserVisa:
	  		creditcard = (String)responseFromServer.getContent();
	  		break;
	  		
	  	case InsertOrder:
	  		orderId = (int)responseFromServer.getContent();
	  		break;
	  		
	  	case ReadUserReports:
	  		usersReport = (ArrayList<UsersReports>) (responseFromServer.getContent());
	  		break;
	  		
	  	case ReadDeliveries:
	  		deliveries = (ArrayList<Delivery>)responseFromServer.getContent();
	  		break;
	  		
	  default:
		  System.out.println("ChatClient got response but didn't deal with it");
		  break;
	  	
	  }
	  awaitResponse = false;  // allow thread to continue
  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message object from the UI.    
   */
  public void handleMessageFromClientUI(Object message)  
  {
	  try
	  {
		  openConnection(); //in order to send more than one message
    	  awaitResponse = true;  
    	  sendToServer(message); // send message to server
    	  //while hasn't got any response from server, wait.
    	  while (awaitResponse) {
			  try {
				  Thread.sleep(100);
			  }   catch (InterruptedException e) {
				  e.printStackTrace();
			  }
		  }
      }
      catch(IOException e)
      {
          clientUI.display("Could not send message to server.  Terminating client.");
          quit();
      }
    }
  
  public void Logout() throws Exception
  { 
	  
  }
  
  
  /*
   * Reset all the declared static variables to null when disconnecting
   */
  public void Reset() { 
	  subscribers = null;
	  machines = null;
	  items = null;
	  orders = null;
	  deliveries = null;
	  orderReport = null;
	  InventoryReport = null;
	  locations = null;
	  cart = null;
	  availableItems = null;
	  available = null;
	  userRequest = null;
	  stockRequests = null;
	  usersReport = null;
	  password = null;
	  role = null;
	  Fname = null;
	  locationName = "North";
	  supplyMethod = "Immediate pickup";
	  deliveryLocation = "";
	  address = "";
	  creditcard = "";
	  awaitResponse = false;
	  isSubscriber = false;
	  FirstSubscriberOrder = false;
	  FirstCart = false;
	  orderId = -1;
	  ID = (Integer) null;
	  machineToLoad = -1;
	  timer = null;
  }
  
  
  /**
   * This method terminates the client.
   */
  public void quit() {
    try {
    	closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }
  
  
  
}
//End of ChatClient class
