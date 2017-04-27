package UI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainLauncher extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root;
        //use FXML to create GUI 
        try {
        	root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        	primaryStage.setTitle("Login");
        	primaryStage.setScene(new Scene(root));
        	primaryStage.show();
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
    
}