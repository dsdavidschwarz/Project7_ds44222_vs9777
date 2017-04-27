package UI;

import UI.ResizeHelper;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
//import com.gluonhq.charm.glisten.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;


public class LoginController implements Initializable {
	private String username;
	@FXML private AnchorPane anchor;
    @FXML private Text usernameText;
    @FXML private TextField usernameField;
    @FXML private Button loginButton;
    
    private void handleButtonAction(ActionEvent event) throws IOException {
    	//call ChatClient instead?
    	//Parent chatRoomParent = FXMLLoader.load(getClass().getResource("Chat.fxml")); 
    	Parent sideBarParent = FXMLLoader.load(getClass().getResource("SideBar.fxml"));
    	//Scene chatRoomScene = new Scene(chatRoomParent);
    	Scene sideBarScene = new Scene(sideBarParent);
    	Stage app_stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
    	app_stage.hide();
    	//app_stage.setScene(chatRoomScene);
    	app_stage.setScene(sideBarScene);
    	app_stage.setTitle("Chat Messenger");
    	app_stage.show();
    }
	
    public String getUsername() {
    	return username;
    }
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ActionEvent e;
		
		loginButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					handleButtonAction(event);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		
		usernameField.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				username = usernameField.getText();
				//System.out.println(username);
			}
		});
		
	}

}