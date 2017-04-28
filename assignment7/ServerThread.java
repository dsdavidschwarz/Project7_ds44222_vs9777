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
					} else {
						os.println("name is invalid, try again");
					}
					if (!uniquename) {
						os.println("name is already in use, try again");
					}
				}
				//adds handle symbol to name, for use with parser
				name = '@' + newname;
				//Send online status to server
				os.println("Welcome " + name);
				os.println("/users " + onlineUsers());
			        for(ServerThread thread : threads) {
			        	if (thread != this && thread.name != null) thread.os.println("/online " + name);
			        	
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
				for(ServerThread thread : currentroom) {
					if (thread != null && thread != this && thread.name != null) {
						if (thread != this) thread.os.println("/left " + name);
					}
				}
				currentroom.remove(this);
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
				if (thread != this && thread.name != null) list += thread.name + " ";
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
	 * /left indicates the user has left their current room
	 *
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
					for(ServerThread user : currentroom) {
						if (user != this && user != null) {
							this.os.println("/left " + user.name);
							user.os.println("/left " + this.name);
						}
					}
					currentroom.remove(this);
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
							for(ServerThread user : currentroom) {
								if (user != this && user != null) {
									this.os.println("/left " + user.name);
									user.os.println("/left " + line[1]);
								}
							}
							currentroom = thread.currentroom;
							for (ServerThread user : currentroom) {
								user.os.println("/entered " + this.name);
							}
							String roomList = "/roomList ";
							for(ServerThread user : currentroom) {
								roomList += user.name + " ";
							}
							this.os.println(roomList);
							currentroom.add(this);
							return 1;
						}
					}	
					return 1;
				}
			}
		}
		
		if (line[0].toLowerCase().equals("/decline")) {
			if (line[1].startsWith("@")) {
				synchronized(this) {
					for(ServerThread thread : threads) {
						if (thread.name.equals(line[1])) {
							thread.os.println("/decline " + line[1]);
							return 1;
						}
					}
					os.println("user no longer online, sorry!");
					return 1;
				}
			}
		}
		
		if (line[0].toLowerCase().equals("/left")) {
				synchronized(this) {
					for(ServerThread user : currentroom) {
						if (user != this && user != null) {
							this.os.println("/left " + user.name);
							user.os.println("/left " + this.name);
						}
					}
					currentroom.remove(this);
					currentroom = new HashSet<ServerThread>();
					currentroom.add(this);
				}
				return 1;
			}
			
		if (line[0].toLowerCase().equals("/w")) {
			if(currentroom != null) {
				if (line[1].startsWith("@")) {
					String message = this.name + " whispered to you:";
					for (int i = 2; i<line.length; i++) {
						message += " " + line[i];
					}
					synchronized(this) {
						for(ServerThread thread : threads) {
							if (thread.name.equals(line[1])) {
								if (thread != this) thread.os.println(message);
								else thread.os.println("You cannot whisper at yourself!");
								return 1;
							}
						}
						os.println("user not online, sorry!");
						return 1;
					}
				}
			}
		}
		os.println("Invalid command, type /help for a list of valid commands");
		return 0;
	}
}