package client;

import java.net.*;
import java.util.ArrayList;
import java.io.*;

public class Client extends Thread{
	private Socket socket;
	private DataInputStream is;
	private PrintStream os;
	private BufferedReader clientInput;
	private PrintStream clientOutput;
	private ClientThread clientThread;
	private boolean stop = false;
	
	//command flags, set by listener thread
	
	public Client(String host, int port) {
		try {
			socket = new Socket(host, port);
			if (socket.isConnected()) System.out.println("connected!");
			else return;
			os = new PrintStream(socket.getOutputStream());
			os.flush();
			is = new DataInputStream(socket.getInputStream());
			clientInput = new BufferedReader(new InputStreamReader(System.in));
			clientOutput = System.out;
		} catch (Exception e) {
			System.out.println("Failed to initialize client and connect to server");
			e.printStackTrace();
		}
		
		try {
			clientThread = new ClientThread(clientOutput, is);
			clientThread.start();
			
		} catch (Exception e) {
			System.out.println("Failed to initialize listener thread");
			e.printStackTrace();
		}
	}
	
	public void run() {
		while (!stop) {
			try {
				os.println(clientInput.readLine());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		Client client = new Client("localhost", 9001);
		client.start();
		while (true) {}
	}
}