package gui_server;

import java.net.URL;
import java.util.ResourceBundle;

import databaselogic.DatabaseController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import logic.Connected;
import server.EchoServer;

public class ServerInfoController implements Initializable {
	private DatabaseController dbController;
	private boolean ranServerAlready = false;
	
	@FXML
	private TextField serverIptxt;
	@FXML
	private TextField serverPortxt;
	@FXML
	private PasswordField databasePasswordtxt;
	@FXML
	private Button startServerbtn;
	@FXML
	private Button importUsersBtn;
	@FXML
	private ImageView image;
	
	@FXML
	public TextArea screen;
	public static String commandLineText = "";
	private String currentStr = "";
	
	@FXML
	private TableView<Connected> table;
	@FXML
	public TableColumn<Connected, String> colIp;
	@FXML
	public TableColumn<Connected, String> colHost;
	@FXML
	public TableColumn<Connected, String> colStatus;
	@FXML
	public TableColumn<Connected, String> colUserID;
	
	private ObservableList<Connected> data;
	
   
	/**
	 * Starts window
	 * @param primaryStage
	 * @throws Exception
	 */
	public void start(Stage primaryStage) throws Exception {
		// get port and initialize port text field
		String port = Integer.toString(EchoServer.DEFAULT_PORT);
		//FXMLLoader loader = new FXMLLoader();
		Parent root = FXMLLoader.load(getClass().getResource("/gui_server/ServerInfo.fxml"));
		Scene scene = new Scene(root);
		
		scene.getStylesheets().add(getClass().getResource("/css/everything.css").toExternalForm());
		primaryStage.setTitle("Server Info");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	/**
	 * Initialize window components before window starts
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		String actual = "/images/ekrut.png" ;
		String path = this.getClass().getResource(actual).toExternalForm();
		Image img = new Image(path,true);
		
		image.setImage(img);
		
		new Thread() {
	        public void run() {
	        	while (true) {
		        	if (!currentStr.equals(commandLineText)) {
		        		screen.replaceText(0, screen.getLength(), commandLineText + "\n");
		        		currentStr = commandLineText;
		        	} else {
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
		        	}
	        	}
            }

	    }.start();
		
		serverIptxt.setText(EchoServer.getLocalIp());  // Set current ip
		serverPortxt.setText("5555");
		databasePasswordtxt.setText("Bv654gF11!");
		LoadTable();
	}
	
	
	/**
	 * Run server when pressing run server button
	 */
	public void RunServerBtn() {
		// on click, change button color to #373057
		if (serverPortxt.getText().equals("")) {
			System.out.println("Please Enter Port");
			return;
		}
		
		String[] args = {serverPortxt.getText(), databasePasswordtxt.getText()};
		EchoServer.runServer(args);
//		LockUnlock(true);
		ranServerAlready = true;	
	}
	
	/**
	 * Refresh cpnnected clients 
	 */
	public void RefreshClientsBtn() {
		table.getItems().clear();
		LoadTable();
		data = FXCollections.observableArrayList(EchoServer.users);
		table.setItems(data);
	}
	
//	private void LockUnlock(boolean condition) {
//		serverIptxt.setDisable(condition);
//		serverPortxt.setDisable(condition);
//		databasePasswordtxt.setDisable(condition);
//		startServerbtn.setDisable(condition);
//	}
	
	
	/**
	 * Initialize and load table
	 */
	public void LoadTable() {
		colIp.setCellValueFactory(new PropertyValueFactory<>("Ip"));
		colHost.setCellValueFactory(new PropertyValueFactory<>("Host"));
		colStatus.setCellValueFactory(new PropertyValueFactory<>("Status"));
		colUserID.setCellValueFactory(new PropertyValueFactory<>("connectedUserID"));
	}
	
	/**
	 * Import external users to database to ekrut database
	 */
	public void ImportUsersBtn() {
		if (!ranServerAlready) {
//			System.out.println("Haven't ran server yet!");
			EchoServer.updateCommandText("Haven't ran server yet!");
			return;
		}
		
		dbController = DatabaseController.GetFunctionsInstance();
		dbController.ImportExternalUsers();
//		System.out.println("Imported Users successfully.");
		EchoServer.updateCommandText("Imported Users successfully.");
		importUsersBtn.setDisable(true);
	}
	
//	public void PrintConsole(String msg1) {
//		screen.appendText(msg1 + "\n");
//			
//	}
	
	/**
	 * Quit server
	 */
	public void QuitBtn() {
		System.exit(0);
	}
}
