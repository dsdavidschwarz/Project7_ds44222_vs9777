package client;

import java.io.*;

public class ClientThread extends Thread {
	private PrintStream clientOutput = null;
	private DataInputStream is;
	private boolean connected = true;
	
	public ClientThread(PrintStream outputLine, DataInputStream is) {
		this.clientOutput = outputLine;
		this.is = is;
	}
	
	
	public void run() {
		try {
			while (connected) {
				String line = is.readLine();
				if (line != null) clientOutput.println(process(line));
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
	private String process(String readLine) {
		if (readLine.startsWith("/")) {
			return handle(readLine.split(" "));
		} else return readLine;
	}

	/**
	 * Handles server commands. This will be modified to support the UI
	 * @param split
	 * @return Command String to output
	 */
	private String handle(String[] split) {
		if (split[0].equals("/request")) {
			return split[1] + " wants to chat, type /accept " + split[1] + " or /decline " + split[1];
		}
		if (split[0].equals("/online")) {
			return split[1] + " is online";
		}
		if (split[0].equals("/offline")) {
			return split[1] + " is offline";
		}
		if (split[0].equals("/users")) {
			String out = "Users online: ";
			for (int i = 1; i < split.length - 1; i++) {
				out += split[i] + ", ";
			}
			out += split[split.length - 1];
			return out;
		} else return "Just received an invalid server command " + split[0] + " sorry!";
	}
}