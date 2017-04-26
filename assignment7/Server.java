package assignment7;

import java.io.DataInputStream;
import java.io.PrintStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.net.ServerSocket;
import java.util.HashSet;

public class Server {
	private static ServerSocket serverSocket;
	private static HashSet<ServerThread> threads;
	
	public static void main(String args[]) {
		threads = new HashSet<ServerThread>();
		int portNumber = 9001;
		if (args.length < 1) {
			System.out.println("Usage: java MultiThreadChatServerSync <portNumber>\n" + "Now using port number=" + portNumber);
		} else {
			portNumber = Integer.valueOf(args[0]).intValue();
		}
		try {
			serverSocket = new ServerSocket(portNumber);
		} catch (IOException e) {
			System.out.println(e);
		}
		while (true) {
			try {
				ServerThread thread = new ServerThread(serverSocket.accept(), threads);
				threads.add(thread);
				thread.start();
			} catch (IOException e) {
				System.out.println(e);
			}
		}
  }
}