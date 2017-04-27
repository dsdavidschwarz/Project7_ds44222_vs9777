package UI;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

public class SidebarController {
	@FXML private BorderPane border;
    @FXML private Accordion accordian;
    @FXML private TitledPane chatroomPane;
    @FXML private AnchorPane chatroomAnchor;
    @FXML private Button addChatButton;
    @FXML private AnchorPane chatListAnchor;
    @FXML private ListView<?> chatList;
    @FXML private TitledPane activeUserPane;
    @FXML private AnchorPane activeUserAnchor;
    @FXML private Label onlineUserCount;
    @FXML private Text onlineUserText;
    @FXML private AnchorPane userListAnchor;
    @FXML private ListView<?> userList;
    @FXML private TitledPane chatRequestPane;
    @FXML private AnchorPane requestAnchor;
    @FXML private AnchorPane requestListAnchor;
    @FXML private ListView<?> requestList;
    @FXML private ContextMenu popUp;
    @FXML private MenuItem acceptRequest;
    @FXML private MenuItem declineRequest;
    @FXML private Label username;
    @FXML private Text hiText;
    
    
	

}


