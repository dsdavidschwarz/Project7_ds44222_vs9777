package assignment7;

import java.net.*;
import java.io.*;
import java.util.HashSet;
import java.util.ArrayList;

class ServerThread extends Thread {
	private String name = null;
	private BufferedReader is;
	private PrintStream os = null;
	private Socket clientSocket = null;
	private HashSet<ServerThread> threads;
	private HashSet<ServerThread> currentroom;
	private boolean connected;

	public ServerThread(Socket clientSocket, HashSet<ServerThread> threads) {
		this.clientSocket = clientSocket;
		this.threads = threads;
		connected = true;
		currentroom = new HashSet<ServerThread>();
		currentroom.add(this);
		
	}

	public void run() {
		try {
			os = new PrintStream(clientSocket.getOutputStream());
			os.flush();
			
			is = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			

			
			//Configure user name
			synchronized(this) {
				boolean validname = false;
				boolean uniquename = true;
				String newname = "anon" + Integer.toString(threads.size());
				os.println("Enter your name, your name should not contain an @ or /");
				while (!validname || !uniquename) {
					newname = is.readLine();
					if (!uniquename) uniquename = true;
					if (newname.equals("null")) continue;
					if (newname.indexOf('@') == -1 && newname.indexOf('/') == -1) {
						validname = true;
						for (ServerThread thread: threads) {
							if (thread.name != null && thread != this) {
								if (thread.name.equals("@" + newname)) {
									uniquename = false;
									break;
								}
							}
						}
					} else os.println("name is invalid, try again");
					if (!uniquename) os.println("name is already in use, try again");
				}
				//adds handle symbol to name, for use with parser
				name = '@' + newname;
				//Send online status to server
				os.println("Welcome " + name);
				os.println("/users " + onlineUsers());
			        for(ServerThread thread : threads) {
			        	if (thread != this) thread.os.println("/online " + name);
			        	
			        }
			}	
     
			/*
			 * Main thread loop, terminated when client wants to disconnect
			 */
			while (connected) {
				String line = is.readLine();
				process(line);
			}
			
			// Send offline status to server and removes itself from the list of connected clients
			synchronized (this) {
				for(ServerThread thread : threads) {
					if (thread != null && thread != this && thread.name != null) {
						if (thread != this) thread.os.println("/offline " + name);
					}
				}
				threads.remove(this);
			}
			//closes IO streams and the socket, thread will finish running and end.
			is.close();
			os.close();
			clientSocket.close();
      
		}  catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String onlineUsers() {
		String list = "";
		synchronized(this) {
			for (ServerThread thread: threads) {
				if (thread != this) list += thread.name + " ";
			}
		}
		return list;
	}

	/**
	 * Processes input from client and sends appropriate outputs. 
	 * Commands are initiated by the client sending a line that begins with /<command>. Each is described here:
	 * /help returns the list of valid commands to the client, and a brief description of each
	 * /quit disconnects the client, and signifies that the client-side will close the client.
	 * /message <name> creates a new room with <name>
	 * /room <list of names seperated by space> sets the current room to contain only the <list of names>
	 * /add <name> adds <name> to the current room
	 * /accept <name> accepts a room invite by a user;
	 * /decline <name> declines a room invite by a user;
	 * MORE TO COME
	 * 	 
	 * *
	 * @param line
	 */
	private void process(String line) {
		if (line.startsWith("/")) {
			int status = handle(line.split(" "));
			if (status <= 0) return;
		} else if (currentroom != null) {
			synchronized (this) {
				for(ServerThread thread : currentroom) {
					if (thread != null && thread.name != null) {
						thread.os.println(name + ": " + line);
					}
				}
			}
		}
	}


	/**
	 * Handles server commands, denoted by /
	 */
	private int handle(String[] line) {
		if (line[0].toLowerCase().equals("/help")) {
			os.println("/quit /message /add ");
			return 1;
		}
		if (line[0].toLowerCase().equals("/quit")) {
			connected = false;
			return -1;
		}
		if (line[0].toLowerCase().equals("/message")) {
			if (line[1].startsWith("@")) {
				synchronized(this) {
					currentroom = new HashSet<ServerThread>();
					currentroom.add(this);
					for(ServerThread thread : threads) {
						if (thread.name.equals(line[1])) {
							if (thread != this) thread.os.println("/request " + name);
							else thread.os.println("You cannot message yourself!");
							return 1;
						}
					}
					os.println("user not online, sorry!");
					return 1;
				}
			}
		}
		
		if (line[0].toLowerCase().equals("/add")) {
			if(currentroom != null) {
				if (line[1].startsWith("@")) {
					synchronized(this) {
						for(ServerThread thread : threads) {
							if (thread.name.equals(line[1])) {
								if (thread != this) thread.os.println("/request " + name);
								else thread.os.println("You cannot add yourself!");
								return 1;
							}
						}
						os.println("user not online, sorry!");
						return 1;
					}
				}
			}
		}
		
		if (line[0].toLowerCase().equals("/accept")) {
			if (line[1].startsWith("@")) {
				synchronized(this) {
					for(ServerThread thread : threads) {
						if (thread.name.equals(line[1])) {
							currentroom = thread.currentroom;
							currentroom.add(this);
							os.println(line[1] +" entered the room");
							return 1;
						}
					}
					os.println("user no longer online, sorry!");
					return 1;
				}
			}
		}
		if (line[0].toLowerCase().equals("/decline")) {
			if (line[1].startsWith("@")) {
				synchronized(this) {
					for(ServerThread thread : threads) {
						if (thread.name.equals(line[1])) {
							thread.os.println(name + " declined your room invite");
							return 1;
						}
					}
					os.println("user no longer online, sorry!");
					return 1;
				}
			}
		}
		os.println("Invalid command, type /help for a list of valid commands");
		return 0;
	}
}