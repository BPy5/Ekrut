package client;
import gui_client.LoginController;
import gui_client.LoginEkrutController;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.stage.Stage;


/**
 * Client UI class, responsible for running the client
 *
 */
public class ClientUI extends Application {
	public static ClientController chat; //only one instance

	// run client
	public static void main( String args[] ) throws Exception { 
		launch(args);  
    }
	
	
	/**
	 * Loads the first window for the client to display
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		LoginController loginFrame = new LoginController(); 
		loginFrame.start(primaryStage);
	}   
}
