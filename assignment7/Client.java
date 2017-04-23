package assignment7;

import java.net.*;
import java.util.ArrayList;
import java.io.*;

public class Client implements Runnable{
	private Server server;
	private Socket socket;
	private BufferedReader console;
	private DataOutputStream out;
	private ClientThread clientThread;
	private boolean stop = false;
	
	//Self initialized constructor
	public Client(InetAddress serverName, int serverPort) {
		try {
			System.out.println("Establishing connection. Please wait ...");
			socket = new Socket(serverName, serverPort);
	        System.out.println("Connected: " + socket);
	        start();
		}
        catch(UnknownHostException uhe) {
        	System.out.println("Host unknown: " + uhe.getMessage());
        }
        catch(IOException ioe) {
        	System.out.println("Unexpected exception: " + ioe.getMessage());
        }
	}
	
	//Server side constructor with known socket
	public Client(Socket socket){
		this.socket = socket;
	}
	
	//Server side constructor with unknown socket
	public Client() {
	}
	
	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	
	public int getID() {
		return (int) clientThread.getId();
	}
	@Override
	public void run(){
	while (!stop) {
		try	{
			out.writeUTF(console.readLine());
			out.flush();
		}
		catch(IOException ioe) {
        	 System.out.println("Sending error: " + ioe.getMessage());
        	 stop();
            }
		}
	}
	
	public void handle(String msg) {
		System.out.println(msg);
	}
	
	public void send(String msg) {
		try {
			out.writeUTF(msg);
			out.flush();
		}
		catch(IOException e) {
			server.remove((int) clientThread.getId());
			stop();
		}
	}
	public void open(){
		
	}
	
	public void start(){
		try {
			console = new BufferedReader(new InputStreamReader(System.in));
			out = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			System.out.println("Error Creating Listener");
			e.printStackTrace();
		}
		clientThread = new ClientThread(this, socket);
	}
	
	public void stop() {
		stop = true;
		try {
			console.close();
			out.close();
			socket.close();
			clientThread.close();
			clientThread.end();
		} catch (IOException e) {
			System.out.println("Error Closing");
			e.printStackTrace();
		}
		
	}
	
	public static void main(String args[]) {
		//new Client("0.0.0.0/0.0.0.0", 80);
	}
}
