package assignment7;

import java.net.*;
import java.util.ArrayList;
import java.io.*;

public class Server {
	private ServerSocket server;
	private ArrayList<Client> clientList;
	
	public Server(int port) {
		try {
			System.out.println("initializing server to port: " + port + "");
			server = new ServerSocket(port);
			System.out.println("Server created!");
			}
		catch (Exception e) {
			System.out.println("Failed to start server");
			e.printStackTrace();
		}
	}
	  
	public void stop() {
	}
	
	public void addThread() {
		Client c;
		try {
			c = new Client(server.accept());
			clientList.add(c);
			c.open();
		} catch (IOException e) {
			System.out.println("Server failed to accept client");
			e.printStackTrace();
		}
	}
	
	public void addThread(Client client) {
		try {
			client.setSocket(server.accept());
			clientList.add(client);
			client.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public synchronized void send(ArrayList<Integer> ID, String input) {
		for (Integer i : ID) {
			Client c = getClient(i);
			if (c != null) c.send(input);
		}
	}
	
	public synchronized void remove(int ID) {
		clientList.remove(getClient(ID));
	}
	
	private Client getClient(int i) {
		for (Client c : clientList) {
			if (c.getID() == i) return c;
		}
		return null;
	}
	
	
}

