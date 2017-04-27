package client;

import java.io.IOException;
import java.io.OutputStream;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

public class Console extends OutputStream {
	private TextArea output;
	public Console(TextArea t) {
		this.output = t;
	}

    @Override
    public void write(int i) throws IOException
    {
	    Platform.runLater(new Runnable() {
	        public void run() {
	            output.appendText(String.valueOf((char) i));
	        }
	    });
    }
}