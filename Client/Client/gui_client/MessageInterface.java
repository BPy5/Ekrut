package gui_client;

import common.Message;

public interface MessageInterface {
	
	//sends message to server and updates values in ChatClient static variables
	public Details sendMessage(Message obj);

}
