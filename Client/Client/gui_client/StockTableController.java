package gui_client;

import java.net.URL;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import logic.Machine;

/**
 * This class shows the current stock of a chosen machine
 */
public class StockTableController implements Initializable{

	Message messageToServer = new Message(null, null);
	private static String machineCode;
	
	@FXML
	private Button showStockBtn;
	@FXML
	private Button updateBtn;
	@FXML
	private Button backBtn;
	@FXML
	private ImageView image;
	@FXML
	private TextField machineCodetxt;
	
	private ObservableList<ViewItem> obs;
	@FXML
	private TableView<ViewItem> machineTable;
	@FXML
	private TableColumn<ViewItem,String> itemsCol;
	@FXML
	private TableColumn<ViewItem,String> amountCol;
	
	/**
	 * This method makes the table invisible when we first open the window
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		String actual = "/images/ekrut.png" ;
		String path = this.getClass().getResource(actual).toExternalForm();
		Image img = new Image(path,true);
		
		image.setImage(img);
		machineTable.setVisible(false);
	}
	
	/**
	 * This method checks if machine id text field is empty,
	 * if it's not empty it will call function LoadMachine
	 * @param event
	 * @throws Exception
	 */
	public void ShowStockBtn(ActionEvent event) throws Exception{
		
		machineCode = machineCodetxt.getText();
		
		if(machineCode.equals(""))	// checking if no value was entered to machine id text field
		{
			Alert alert = new Alert(AlertType.ERROR,"Must enter machine id !",ButtonType.OK);
			alert.showAndWait();
		}
		else 
		{
			LoadMachine();
			machineTable.setVisible(true);	// make the table visible
		}
	}
	
	/**
	 * This method reads machines from data base. And fills the table.
	 */
	public void LoadMachine() {
				
		messageToServer.setCommand(Command.ReadMachines);	// read machines from data base
		messageToServer.setContent(Integer.parseInt(machineCode));	// find machine based on machine id
		ClientUI.chat.accept(messageToServer); 
		
		itemsCol.setCellValueFactory(new PropertyValueFactory<>("Name"));	// table columns
		amountCol.setCellValueFactory(new PropertyValueFactory<>("Amount"));
		ArrayList<ViewItem> items = new ArrayList<>();

		if (ChatClient.machines.contains(null))	// if the table doesn't have values
		{	
			Alert alert = new Alert(AlertType.ERROR,"Machine Id does not exist !",ButtonType.OK);
			alert.showAndWait();
		}
		else	// if the table has values
		{
			try 
			{
				Machine temp = ChatClient.machines.get(0);
				int size = temp.getItems().size();
				for(int i = 0;i<size;i++)	// loop to fill table
					items.add(new ViewItem(temp.getItems().get(i),temp.getAmountItems().get(i).toString()));
				
				obs = FXCollections.observableArrayList(items);
				machineTable.setItems(obs);
			}
			catch (Exception e) 
			{
				Alert alert = new Alert(AlertType.ERROR,"Machine Id does not exist !",ButtonType.OK);
				alert.showAndWait();
			}
		}
	}
	
	
	/**
	 * This method goes back to user page based on role
	 * @param event
	 * @throws Exception
	 */
	public void BackBtn(ActionEvent event) throws Exception {
		((Node)event.getSource()).getScene().getWindow().hide();
		Parent root = null;
		String title = "";
		switch(ChatClient.role) {
		
		case "ceo":
			root = FXMLLoader.load(getClass().getResource("/gui_client_windows/CEOReports.fxml"));
			title = "CEO Reports";
			break;
		
		case "stm":
			root = FXMLLoader.load(getClass().getResource("/gui_client_windows/WorkerUI.fxml"));
			title = "Worker UI";
			break;
			
		default:
			break;
		}
		
		Stage primaryStage = new Stage();
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/css/everything.css").toExternalForm());
		primaryStage.setTitle(title);
		primaryStage.setScene(scene);		
		primaryStage.show();		
	}

	/**
	 * This class has getters to fill the table
	 */
	public class ViewItem
	{
		private String Name;
		private String Amount;
		
		/**
		 * @return Name
		 */
		public String getName() {
			return Name;
		}
		
		/**
		 * @return Amount
		 */
		public String getAmount() {
			return Amount;
		}
		
		/**
		 * Constructor
		 * @param name
		 * @param amount
		 */
		public ViewItem(String name,String amount) {
			this.Name = name;
			this.Amount = amount;
		}
	}

}
