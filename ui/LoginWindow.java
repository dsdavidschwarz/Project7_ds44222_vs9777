package ui;
import java.io.PrintStream;
import java.net.Socket;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LoginWindow extends Application {
	
    public void init (String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("JavaChat");
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        BackgroundImage myBI= new BackgroundImage(new Image("file:resources/background.jpg", 1680, 1080, false,true),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
                  BackgroundSize.DEFAULT);
        grid.setBackground(new Background(myBI));

        Text scenetitle = new Text("Connect to JavaChat");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        scenetitle.setFill(Color.WHITESMOKE);
        grid.add(scenetitle, 0, 0, 2, 1);

        Label userName = new Label("User Name:");
        userName.setTextFill(Color.WHITESMOKE);
        grid.add(userName, 0, 1);

        TextField userTextField = new TextField();
        userTextField.setPromptText("no '@' or '/' please!");
        grid.add(userTextField, 1, 1);

        Label sv = new Label("Server: ");
        sv.setTextFill(Color.WHITESMOKE);
        grid.add(sv, 0, 2);

        TextField svBox = new TextField();
        svBox.setPromptText("localhost");
        grid.add(svBox, 1, 2);
        
        Label pt = new Label("Port: ");
        pt.setTextFill(Color.WHITESMOKE);
        grid.add(pt, 0, 3);
        
        TextField ptBox = new TextField();
        ptBox.setPromptText("9001");
        grid.add(ptBox, 1, 3);

        Button btn = new Button("Sign in");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 4);

        final Text actiontarget = new Text();
        grid.add(actiontarget, 1, 6);

        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
            	if (userTextField.getText() != "") {
            		name =  userTextField.getText();
            		userTextField.clear();
            	}
            	if (svBox.getText() != "") {
            		servername = svBox.getText();
            		svBox.clear();
            	}
            	if (ptBox.getText() != "") {
            		port = ptBox.getText();
            		ptBox.clear();
            	}
            	try {
            		connect()
            		streamOut.println(name);
            		
            } catch(Exception e) {
            	e.printStackTrace();
            }
            }
        });

        Scene scene = new Scene(grid, 300, 275);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}


