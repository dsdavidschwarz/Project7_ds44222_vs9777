package assignment7;

import java.net.*;
import java.util.ArrayList;
import java.io.*;

public class Client implements Runnable{
	private Socket socket;
	private int ID;
	
	public Client(Socket socket){
		this.socket = socket;
		ID = 1;
	}
	public Client() {
		ID = 1;
	}
	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	
	public void send(String msg) {
	}
	
	public int getID() {
		return ID;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	public void open(){
		
	}
	
	public void start(){
		
	}
}
