package gui_client;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;

import client.ChatClient;
import client.ClientUI;
import common.Command;
import common.Message;
import gui_client.StockTableController.ViewItem;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import logic.Request;

public class SubscriberRequestController  implements Initializable
{
	@FXML
	private Button updateBtn;
	@FXML
	private Button backBtn;
	
	@FXML
	private TableView<ViewRequest> requestTable;
	@FXML
	private TableColumn<ViewRequest,String> idcol;
	@FXML
	private TableColumn<ViewRequest,String> typecol;
	@FXML
	private ImageView image;
	
	private ObservableList<ViewRequest> obs;
	
	Message messageToServer = new Message(null, null);

	/**
	 * Reads requests 
	 * Calls loadTable()
	 * 
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		String actual = "/images/ekrut.png" ;
		String path = this.getClass().getResource(actual).toExternalForm();
		Image img = new Image(path,true);
		
		image.setImage(img);
		messageToServer.setCommand(Command.ReadRequests);
		messageToServer.setContent(0);
		ClientUI.chat.accept(messageToServer);
		
		loadTable();
		
	}

	/**
	 * Updates the users is_subsriber field which dictates if he's a subscriber or not and gives said user a subscriber number
	 * Updates the status of the request in the database
	 * Reloads table
	 */
	public void Update()
	{
		    String[] user = new String[2] ;
		    String[] request= new String[2];
		    ArrayList<ViewRequest> view = new ArrayList<>();
		    view.addAll(obs);
			ViewRequest req = requestTable.getSelectionModel().getSelectedItem();
			user[0] = "users";
			user[1] =String.valueOf(req.getID());
			messageToServer.setCommand(Command.UpdateUsers);
			messageToServer.setContent(user);
			ClientUI.chat.accept(messageToServer);
			for (ViewRequest v : obs ) 
			{
				
			   if(req.getID()==v.getID())
			   {
				   view.remove(v);
			   }
			}
			obs = FXCollections.observableArrayList(view);
			requestTable.setItems(obs);
			request[0]="requests";
			request[1]=String.valueOf(req.getID());
			messageToServer.setCommand(Command.UpdateRequest);
			messageToServer.setContent(request);
			ClientUI.chat.accept(messageToServer);
			Alert alert = new Alert(AlertType.INFORMATION,"Updated status into subsriber",ButtonType.OK);
			alert.showAndWait();
			
			
	}
	/**
	 * Hides current window
	 * Goes to WorkerUI.fxml window
	 * 
	 * @param event - Type of action that occurred in the window by the user (when pressing a button in this scenario)
	 * @throws Exception
	 */
	public void Back(ActionEvent event) throws Exception {
		((Node)event.getSource()).getScene().getWindow().hide();
		Parent root = FXMLLoader.load(getClass().getResource("/gui_client_windows/WorkerUI.fxml"));
		Stage primaryStage = new Stage();
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/css/everything.css").toExternalForm());
		primaryStage.setTitle("WORKER UI");
		primaryStage.setScene(scene);		
		primaryStage.show();	
	}
	
	/**
	 * Initializes table columns
	 * Loads values into table from database
	 */
	public void loadTable()
	{
		ArrayList<ViewRequest> request= new ArrayList<>();
		idcol.setCellValueFactory(new PropertyValueFactory<>("ID"));
		typecol.setCellValueFactory(new PropertyValueFactory<>("Type"));
		
		
		for (Request r : ChatClient.userRequest)
		{
			if(r.getStatus().equals("Pending")) {
				request.add(new ViewRequest(r.getCustomer_id(), r.getType()));

			}
			
			
		}
		obs = FXCollections.observableArrayList(request);
		requestTable.setItems(obs);
		
	}
	
	/**
	 * This class is used to view requests on a table
	 *
	 */
	public class ViewRequest
	{
		/**
		 * request ID
		 */
		private int id; 
		/**
		 * the request type
		 */
		private String type;
		
		public int getID() {
			return id;
		}
		
		public String getType() {
			return type;
		}

		public ViewRequest(int id, String type) {
			super();
			this.id = id;
			this.type = type;
		}
		
		
	}
	
}
