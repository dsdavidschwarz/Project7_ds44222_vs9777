package client;

import java.io.*;


public class ClientThread extends Thread {
	private ChatClient client = null;
	private DataInputStream is;
	private boolean connected = true;
	
	public ClientThread(ChatClient c, DataInputStream is) {
		this.client = c;
		this.is = is;
	}
	
	
	public void run() {
		try {
			while (connected) {
				process(is.readLine());				
			}
		} catch (IOException e) {
			System.err.println("IOException:  " + e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Listens to the server and outputs messages to the client
	 * @param readLine
	 * @return String to output
	 */
	private void process(String readLine) {
		if (readLine.startsWith("/")) {
			handle(readLine.split(" "));
		} else client.printMessage(readLine);
	}

	/**
	 * Handles server commands. This will be modified to support the UI
	 * @param split
	 * @return Command String to output
	 */
	private void handle(String[] split) {
		if (split[0].equals("/request")) {
			client.request(split[1]);
		}
		if (split[0].equals("/online")) {
			client.addUser(split[1]);
		}
		if (split[0].equals("/offline")) {
			client.removeUser(split[1]);
		}
		if (split[0].equals("/users")) {
			for (int i = 1; i < split.length; i++) {
			client.addUser(split[i]);
			}
		}
		if (split[0].equals("/entered")) {
			client.addRoom(split[1]);
		}
		if (split[0].equals("/left")) {
			client.removeRoom(split[1]);
		};
		
		if (split[0].equals("/roomList")) {
			for (int i = 1; i < split.length; i++) {
				client.addRoom(split[i]);
			}
		}
	}
	
	public void end () {
		try {
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		connected = false;
	}
	
}