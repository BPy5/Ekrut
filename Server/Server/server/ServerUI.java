package server;

import gui_server.ServerInfoController;
import javafx.application.Application;
import javafx.stage.Stage;

public class ServerUI extends Application {
	final public static int DEFAULT_PORT = 5555;

	public static void main( String args[] ) throws Exception {   
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub				  		
		ServerInfoController serverInfoFrame = new ServerInfoController(); // create StudentFrame
		 
		serverInfoFrame.start(primaryStage);
	}
}
