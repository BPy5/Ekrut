package client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import common.Message;
import common.Command;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * Class that defines the timer thread's behavior
 *
 */
public class Timespent extends Application implements Runnable{

	/**
	 * Constructor that initializes Thread
	 */
	public Timespent() {
		
	}

	/**
	 * Method that defines what the thread that runs this instance works
	 * Sleeps for 10 seconds 
	 * If the thread wakes up it Disconnects user, empties values in ChatClient and goes back to LoginEkrut window 
	 */
	@Override
	public void run() {
		try {
			
			Thread.sleep(10000);
			
		Platform.runLater(new Runnable() {
			@Override
		    public void run() {
		    	//hide all windows
		    	//disconnect current user
		    	ObservableList<Window> windows12;
//		    	windows12.get(0).hide();
		    	Iterator<Window> it = Stage.impl_getWindows();
		    	ArrayList<Window> windows = new ArrayList<Window>();
		    	while(it.hasNext())
		    	{
		    		windows.add(it.next());
		    	}
		    	windows12 = FXCollections.observableArrayList(windows);
		    	for(Window w : windows12)
		    	{
		    		w.hide();
		    	}
				Parent root = null;
				try {
					root = FXMLLoader.load(getClass().getResource("/gui_client_windows/LoginEkrut.fxml"));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Message msg = new Message(null,null);
		    	msg.setCommand(Command.Disconnect);
		    	msg.setContent(ChatClient.ID);
		    	ClientUI.chat.accept(msg);
				ChatClient.cart = new ArrayList<>();
				Stage primaryStage = new Stage();
				Scene scene = new Scene(root);
				//scene.getStylesheets().add(getClass().getResource("/gui/loginsubscriber.css").toExternalForm());
				primaryStage.setTitle("EKRUT");
				primaryStage.setScene(scene);		
				primaryStage.show();
		    }
		});
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/**
	 * Must have method to give the thread JavaFX permissions (Class extends Applications)
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		return;
	}

}
