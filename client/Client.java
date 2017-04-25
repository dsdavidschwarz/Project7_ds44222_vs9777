package client;

import java.net.*;
import java.util.ArrayList;
import java.io.*;

public class Client implements Runnable{
	private Socket socket;
	private BufferedReader is;
	private PrintStream os;
	private BufferedReader clientInput;
	private PrintStream clientOutput;
	private ClientThread clientThread;
	private boolean stop = false;
	
	//command flags, set by listener thread
	
	public Client(String host, int port) {
		try {
			socket = new Socket(host, port);
			os = new PrintStream(socket.getOutputStream());
			is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
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
		new Thread(new Client("localhost", 2222)).start();
	}
}