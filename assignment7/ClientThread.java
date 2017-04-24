package assignment7;

import java.net.*;
import java.io.*;

public class ClientThread extends Thread{
	private Socket socket;
	private Client	client;
	private DataInputStream streamIn;
	private boolean stop = false;

	public ClientThread(Client client,Socket socket) {
		this.client   = client;
		this.socket   = socket;
		open();  
		start();
		}
	
	public void open() {
		try {
			streamIn  = new DataInputStream(socket.getInputStream());
		}
		catch(IOException ioe) {
			System.out.println("Error getting input stream: " + ioe);
			client.stop();
		}
	}
	
	public void close() {
		try {
			if (streamIn != null) streamIn.close();
		}
		catch(IOException ioe) {
			System.out.println("Error closing input stream: " + ioe);
		}
	}
	
	public void run() {
		while (!stop) {
			try {
				client.handle(streamIn.readUTF());
			} 
			catch(IOException ioe) {
				System.out.println("Listening error: " + ioe.getMessage());
				client.stop();
			}
		}
	}
	
	public void end() {
		stop = true;
	}
}
