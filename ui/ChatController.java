package ui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import messages.Message;

public class ChatController implements Initializable {
	@FXML private TextArea messageBox;
	@FXML private Label usernameLabel;
	@FXML private ListView chatPane;
	@FXML private BorderPane borderPane;
	
	private double xOffset;
	private double yOffset;

	public void sentButtonAction() throws IOException {
        String msg = messageBox.getText();
        if (!messageBox.getText().isEmpty()) {
            //Listener.send(msg);
            messageBox.clear();
        }
	}
	
	//format how message will send on chat
	public synchronized void addToChat(Message msg) {
	}
	
	public void setUsernameLabel(String username) {
		this.usernameLabel.setText(username);
		
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}

}
