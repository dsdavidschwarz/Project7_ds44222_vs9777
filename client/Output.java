package client;

import java.io.IOException;
import java.io.OutputStream;

import javafx.application.Platform;
import javafx.scene.text.Text;

public class Output extends OutputStream {
	private Text output;
	
	public Output(Text t) {
		this.output = t;
	}

    @Override
    public void write(int i) throws IOException
    {
	    Platform.runLater(new Runnable() {
	        public void run() {
	            output.setText(output.getText() + String.valueOf((char) i));
	        }
	    });
    }
}