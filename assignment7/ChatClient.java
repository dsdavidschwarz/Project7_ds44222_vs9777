package assignment7;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;
import java.net.*;
import java.time.LocalDateTime;

import javax.swing.*;
import javax.swing.text.AttributeSet;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.awt.*;
import java.awt.event.*;

public class ChatClient {

	private JTextPane incoming;
	private JTextPane incoming2;
	private JTextField outgoing;
	private JTextField outgoing2;
	private JTextField outgoing3;
	private JTextArea idZone;
	private JTextArea idZone2;
	private BufferedReader reader;
	private PrintWriter writer;
	private Socket socket;
	private int ID;
	private Color color;
	JFrame frame2 = new JFrame("Private Message");
	JFrame frame1 = new JFrame("Group Chat");

	public ChatClient() {
		// http://stackoverflow.com/questions/4246351/creating-random-colour-in-java
		Random random = new Random();
		final float hue = random.nextFloat();
		final float sat = 0.9f;
		final float lum = 1.0f;
		this.color = Color.getHSBColor(hue, sat, lum);
	}

	public void run() throws Exception {
		//Server.clientList.add();
		initView();
		initView2();
		setUpNetworking();
	}

	private void initView() {
		frame1.setSize(540, 460);
		JPanel mainPanel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		//mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
		incoming = new JTextPane();
		//incoming.setLineWrap(true);
		//incoming.setWrapStyleWord(true);
		incoming.setEditable(false);
		JScrollPane qScroller = new JScrollPane(incoming);
		qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		outgoing = new JTextField();
		outgoing.setForeground(color);
		outgoing.setText("Begin typing...");
		idZone = new JTextArea();
		idZone.setEditable(false);

	    JButton pm = new JButton("Private Message");
	    pm.addActionListener(new privateMessage());
		
		JButton sendButton = new JButton("Send");
		sendButton.addActionListener(new sendGroup());

		JButton sendButton3 = new JButton("Quit");
		sendButton3.addActionListener(new closeGroupChat());

		//c.fill = GridBagConstraints.HORIZONTAL;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 0.0;
		c.ipady = 300;
		c.gridwidth = 5;
		c.gridx = 0;
		c.gridy = 0;
		mainPanel.add(qScroller, c);
		c.ipady = 0;
		c.ipadx = 300;
		c.gridwidth = 3;
		c.gridx = 0;
		c.gridy = 1;
		mainPanel.add(outgoing, c);
		c.gridwidth = 1;
		c.ipadx = 0;
		c.gridx = 3;
		c.gridy = 1;
		mainPanel.add(sendButton, c);
		c.gridwidth = 1;
		c.gridx = 4;
		c.gridy = 1;
		mainPanel.add(sendButton3, c);
		c.gridwidth = 3;
		c.gridx = 0;
		c.gridy = 2;
		mainPanel.add(idZone, c);
		c.gridwidth = 2;
		c.gridx = 3;
		c.gridy = 2;
		mainPanel.add(pm, c);
		frame1.add(mainPanel, BorderLayout.CENTER);
		frame1.setLocationRelativeTo(null);
		mainPanel.setBackground(color);
		frame1.setVisible(true);
	}

	private void initView2() {
		frame2.setSize(540, 460);
		JPanel mainPanel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		incoming2 = new JTextPane(); //(15, 37);
		//incoming2.setLineWrap(true);
		//incoming2.setWrapStyleWord(true);
		incoming2.setEditable(false);
		idZone2 = new JTextArea();
		idZone2.setEditable(false);
		JScrollPane qScroller2 = new JScrollPane(incoming2);
		qScroller2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		qScroller2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		outgoing2 = new JTextField();
		outgoing3 = new JTextField();
		outgoing2.setForeground(color);

		JButton sendButton2 = new JButton("Send");
		sendButton2.addActionListener(new sendPM());

		JButton sendButton3 = new JButton("Close");
		sendButton3.addActionListener(new closeChat());

		c.fill = GridBagConstraints.BOTH;
		c.weightx = 0.0;
		c.ipady = 330;
		c.gridwidth = 7;
		c.gridx = 0;
		c.gridy = 0;
		mainPanel.add(qScroller2, c);
		c.ipady = 0;
		c.ipadx = 305;
		c.gridwidth = 3;
		c.gridx = 0;
		c.gridy = 1;
		mainPanel.add(outgoing2, c);
		c.gridwidth = 1;
		c.ipadx = 130;
		c.gridx = 4;
		c.gridy = 1;
		mainPanel.add(outgoing3, c);
		c.gridwidth = 1;
		c.ipadx = 0;
		c.gridx = 5;
		c.gridy = 1;
		mainPanel.add(sendButton2, c);
		c.gridwidth = 1;
		c.ipadx = 0;
		c.gridx = 5;
		c.gridy = 2;
		mainPanel.add(sendButton3, c);
		c.gridwidth = 5;
		c.gridx = 0;
		c.gridy = 2;
		mainPanel.add(idZone2, c);
		frame2.add(mainPanel, BorderLayout.CENTER);
		frame2.setVisible(false);
		mainPanel.setBackground(color);
		outgoing2.setText("Begin typing...");
		outgoing3.setText("Reciever");

	}

	private void setUpNetworking() throws Exception {
		this.socket = new Socket("127.0.0.1", 4242);
		InputStreamReader streamReader = new InputStreamReader(socket.getInputStream());
		reader = new BufferedReader(streamReader);
		writer = new PrintWriter(socket.getOutputStream());
		System.out.println("networking established");
		Thread readerThread = new Thread(new IncomingReader());
		readerThread.start();

		System.out.println(socket.getLocalPort());
		writer.println(socket.getLocalPort() + "+");
		writer.flush();
	}

	class sendGroup implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			writer.println(outgoing.getText());
			writer.flush();
			outgoing.setText("");
			outgoing.requestFocus();
		}
	}
	
	class privateMessage implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			frame2.setVisible(true);
		}
	}

	class sendPM implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			try {
				Integer.parseInt(outgoing3.getText());
				writer.println(outgoing2.getText() + "|" + outgoing3.getText());
				writer.flush();
				outgoing2.setText("");
				outgoing2.requestFocus();
				// outgoing3.setText("");
			} catch (NumberFormatException e) {
				// outgoing3.setText("Invalid");
			}

		}
	}

	class closeGroupChat implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			writer.println("has disconnected.");
			writer.flush();
			if (frame2.isActive()) {
				frame1.setVisible(false);
			} else {
				frame1.dispose();
			}
			
		}

	}

	class closeChat implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			//writer.println("has disconnected.");
			writer.flush();
			if (frame1.isActive()) {
				frame2.setVisible(false);
			} else {
				frame2.dispose();
			}
		}

	}
	
	
	public static void main(String[] args) {
		try {
			new ChatClient().run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	class IncomingReader implements Runnable {
		public void run() {
			String message;
			try {
				while ((message = reader.readLine()) != null) {
					String newSomething = Integer.toString(socket.getLocalPort());
					if (message.contains("|" + ID)) {
						int placeholder = message.lastIndexOf("|");
						message = message.substring(0, placeholder);
						incoming2.getDocument().insertString(0, message + "\n", null);
					} else if (message.contains(newSomething)) {
						int placeholder = message.lastIndexOf("+");
						message = message.substring(placeholder + 1, message.length());
						idZone.append("Your ID is " + message + ".");
						idZone2.append("Your ID is " + message + ".");
						ID = Integer.parseInt(message);
					} else if (message.contains("+") || (message.contains("|"))) {
					} else {
						incoming.getDocument().insertString(0, message + "\n", null);
					}

				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	

}