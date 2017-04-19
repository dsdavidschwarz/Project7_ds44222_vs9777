package assignment7;

import java.net.*;
import java.util.ArrayList;
import java.io.*;

public class ServerThread extends Thread {
	private Server server;
	private Socket socket;
	private int ID = -1;
	private DataInputStream streamIn;
	private DataOutputStream streamOut;
	private boolean stop = false;

	public ServerThread(Server server, Socket socket) {  
	   super();
	   this.server = server;
	   this.socket = socket;
	   ID = socket.getPort();
	}
	
	public void send(String msg) {
	   try {
		   streamOut.writeUTF(msg);
		   streamOut.flush();
	   }
	   catch(IOException ioe) {
		   System.out.println(ID + " ERROR sending: " + ioe.getMessage());
		   server.remove(ID);
		   end();
	   }
	}
	
	public int getID() {
		return ID;
	}
	
	public void run() {
		System.out.println("Server Thread " + ID + " running.");
		while (!stop) {
			try {  
				server.handle(new ArrayList<Integer>(ID), streamIn.readUTF());
			}
			catch(IOException ioe) {
				System.out.println(ID + " ERROR reading: " + ioe.getMessage());
				server.remove(ID);
				end();
			}
		}
	}
	
	public void open() throws IOException {
		streamIn = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
		streamOut = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
	}
	
	public void close() throws IOException {
		if (socket != null) socket.close();
		if (streamIn != null) streamIn.close();
		if (streamOut != null) streamOut.close();
	}

	public void end() {
		stop = true;
	}
}