package client;




import java.net.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Menu;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.TilePane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import sun.misc.OSEnvironment;
import javafx.animation.TranslateTransition;
import java.io.*;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;

public class ChatClient extends Application {
	
	private Socket socket;
	private ClientThread thread;
	private PrintStream clientOut;
	private PrintStream streamOut;
	
	String servername = "localhost";
	int serverport = 9001;
	
	private double WINDOW_WIDTH;
	private double WINDOW_HEIGHT;
	private Rectangle2D primaryScreenDimensions;
	
	private GridPane grid;
	private GridPane left;
	private GridPane right;
	
	private ScrollPane userScroll;
	private ScrollPane roomScroll;
	private TilePane users;
	private TilePane room;
	
	private TextArea console;
	private TextArea chat;
	private ScrollPane chatArea;
	
	private ContextMenu messageOpt;
	private ContextMenu requestOpt;
	
	private MenuItem message;
	private MenuItem add;

	
	private Button send;
	private Button accept;
	private Button decline;


	
	@Override
	public void start(Stage primaryStage) {

		try {			
			primaryStage.setTitle("JavaChat");
			
			createElements();
			
			primaryScreenDimensions = Screen.getPrimary().getVisualBounds();
			WINDOW_WIDTH = primaryScreenDimensions.getWidth();
			WINDOW_HEIGHT = primaryScreenDimensions.getHeight();
			
			configGrid();

			Scene scene = new Scene(grid, WINDOW_WIDTH, WINDOW_HEIGHT, Color.WHITE);

			primaryStage.setScene(scene);

			primaryStage.isResizable();

			configElements();

			primaryStage.setScene(scene);

			//configButtons();
			
			configEventHandlers();
			
			clientOut.println("hi");
			left.add(new Text( "Current Room"), 0, 0);
			left.add(roomScroll, 0, 1);
			left.add(new Text("User List"), 0, 2);
			left.add(userScroll, 0, 3);
			
			right.add(chat, 0, 0, 2, 1);
			right.add(console, 0, 1, 1, 1);
			right.add(send, 1, 1, 1, 1);
			
			
			grid.add(left, 0, 0);
			grid.add(right, 1, 0);
			

			connect();
			
			primaryStage.show();
			


		} catch(Exception e) {

			e.printStackTrace();		

		}

	}

	

	private void createElements(){

		grid = new GridPane();
		left = new GridPane();
		right = new GridPane();
		
		
		userScroll = new ScrollPane();
		roomScroll = new ScrollPane();
		users = new TilePane(Orientation.HORIZONTAL);
		users.setTileAlignment(Pos.CENTER_LEFT);
		users.prefColumnsProperty().set(1);
		room = new TilePane(Orientation.VERTICAL);
		room.setTileAlignment(Pos.CENTER_LEFT);
		room.prefColumnsProperty().set(1);
		
		chat = new TextArea();
		clientOut = new PrintStream(new Console(chat), true);
		
		console = new TextArea();
		send = new Button("Send");
		
		chatArea = new ScrollPane();
		
		message = new MenuItem("Message user");
		add = new MenuItem("Add user to room");
		
		messageOpt = new ContextMenu(new MenuItem[] {message, add});

	}

	

	private void configElements() {

		left.prefHeightProperty().bind(grid.prefHeightProperty());
		left.prefWidthProperty().set(200);
		right.prefHeightProperty().bind(grid.prefHeightProperty());
		right.prefWidthProperty().bind(grid.prefWidthProperty());
		
		chat.prefHeightProperty().bind(right.prefHeightProperty().multiply(.90));
		chat.prefWidthProperty().bind(right.prefWidthProperty());
		
		send.prefHeightProperty().bind(right.prefHeightProperty().multiply(.1));
		send.minWidthProperty().set(100);
		
		console.prefHeightProperty().bind(send.prefHeightProperty());
		console.prefWidthProperty().bind(right.prefWidthProperty());
		
		userScroll.prefHeightProperty().bind(grid.prefHeightProperty().multiply(.8));
		userScroll.prefWidthProperty().set(200);
		userScroll.minWidthProperty().set(200);
		users.setMaxWidth(200);
		userScroll.setContent(users);
		userScroll.setHbarPolicy(ScrollBarPolicy.NEVER);
		userScroll.setVbarPolicy(ScrollBarPolicy.ALWAYS);
		
		roomScroll.prefHeightProperty().bind(grid.prefHeightProperty().multiply(.2));
		roomScroll.minHeightProperty().bind(grid.prefHeightProperty().multiply(.2));
		roomScroll.prefWidthProperty().set(200);
		roomScroll.minWidthProperty().set(200);
		room.setMaxWidth(200);
		roomScroll.setContent(room);
		roomScroll.setHbarPolicy(ScrollBarPolicy.NEVER);
		roomScroll.setVbarPolicy(ScrollBarPolicy.ALWAYS);
		
		
		chat.editableProperty().set(false);
	}

	

	private void configGrid() {

		grid.setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT);

		grid.setMaxSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);

	}
	

	private void configEventHandlers() {
		send.setOnAction(new EventHandler<ActionEvent>(){

			public void handle(ActionEvent e){

				try {
					streamOut.println(console.getText());
					console.setText("");
				} catch (Exception e1) {
					e1.printStackTrace();
				}

			}

		});
	}


	public static void main(String[] args) {
		launch(args);
	}
	
	private void connect() {
		try {
			socket = new Socket(servername, serverport);
			streamOut = new PrintStream(socket.getOutputStream());
			thread = new ClientThread(this, new DataInputStream(socket.getInputStream()));
			thread.start();
			
			if (clientOut != null) clientOut.println("Connected to server " + servername + " on port " + serverport);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public void printMessage(String process) {
		clientOut.println(process);
	}



	public void request(String split) {
		// TODO Auto-generated method stub
		
	}



	public void addUser(String string) {
		// TODO Auto-generated method stub
		
	}



	public void removeUser(String string) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
}