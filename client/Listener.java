package client;

import java.io.*;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextAreaBuilder;
import javafx.stage.Stage;

public class Listener extends Application {
	
	PrintStream ps;
	TextArea ta;
	
	public Listener() {}
	
    @Override
    public void start(Stage primaryStage) throws IOException {

        ta = TextAreaBuilder.create().prefWidth(800).prefHeight(600).wrapText(true).build();
        Console console = new Console(ta);
        PrintStream ps = new PrintStream(console, true);
        Scene app = new Scene(ta);

        primaryStage.setScene(app);
        primaryStage.show();
        
        ps.println("working");
        this.ps = ps;
    }

    public void init(String[] args) {
        launch(args);
    }
    
    public PrintStream getStream() {
    	return ps;
    }

    public static class Console extends OutputStream {

        private TextArea output;

        public Console(TextArea ta) {
            this.output = ta;
        }

        @Override
        public void write(int i) throws IOException {
        	Platform.runLater(new Runnable() {
                public void run() {
                    output.appendText(String.valueOf((char) i));
                }
            });

        }
    }
    
	private Client createClient() {

		return new Client(this.ip, this.port, data -> {

			Platform.runLater(() -> {

				chatWindow.appendText(data.toString() + "\n");

			});

		});

	}
}