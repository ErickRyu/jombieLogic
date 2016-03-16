package jombie.Server;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import jombie.Model.Location;
import jombie.Model.User;

public class Client {
	
	JTextArea incoming;
	JTextField outgoing;
	JPanel incomingDot;
	BufferedImage img;
	// ChattingPanel chatPanel;

	BufferedReader reader;
	PrintWriter writer;
	Socket sock;
	
	
	private static String name;					
	private static User loginUser;		
	private static String ip;
	private static String port;
	
	private static Map<String, List<Integer>> map = new HashMap<String, List<Integer>>();
	private static Map<User, Location> _userMap = new HashMap<User, Location>();
	
	private static List<User> userList = new ArrayList<>();
	
	
	public static void main(String[] args) {
//		Scanner sc = new Scanner(System.in);
//		System.out.print("Input name : ");
//		name = sc.next();
		name = "E";
		// System.out.print("IP : ");
		// ip = sc.next();
		// System.out.print("Port : ");
		// port = sc.next();
		Client client = new Client();
		client.go();
	}
	public void setPanel(){
		JFrame frame = new JFrame("Ludicrously Simple Chat Client");
		JPanel panel = new JPanel();
		
		incoming = new JTextArea(15, 50);
		incoming.setLineWrap(true);
		incoming.setWrapStyleWord(true);
		incoming.setEditable(false);
		incoming.setTabSize(2);
		int width = 20;
		int height = 20;
		img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		JScrollPane qScroller = new JScrollPane(incoming);
		qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		outgoing = new JTextField(20);
		JButton sendButton = new JButton("Send");
		sendButton.addActionListener(new SendButtonListener());
		panel.add(qScroller);
		panel.add(outgoing);
		panel.add(sendButton);
		frame.getContentPane().add(BorderLayout.CENTER, panel);
		frame.setSize(650, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	public void go() {
		// chatPanel = new ChattingPanel();
		// chatPanel.settingPanel();
		
		String ip = "127.0.0.1";
		String port = "5000";
		
		setUpNetworking(ip, port);
		Thread readerThread = new Thread(new IncomingReader());
		readerThread.start();
		
		logIn();
		// after success to setUpNetwork and login, show Panel
		setPanel();
		
	} // end go()
	
	public void logIn(){
		int y = (int)(Math.random() * 20);
		int x = (int)(Math.random() * 20);
		Location initialLocation = new Location(y, x);	// random Location
		loginUser = new User(initialLocation,name);
	}
	
	public void setUpNetworking(String ip, String port) {
		try {
			sock = new Socket(ip, Integer.parseInt(port));
			System.out.println("[Info] socket success");
		} catch (Exception e) {
			System.out.println("[Error] ip or port is not correct");
		}
		try {
			InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
			reader = new BufferedReader(streamReader);
			writer = new PrintWriter(sock.getOutputStream());

			// chatPanel.getReaderAndWriter(reader, writer);
			System.out.println("networking established");
			
			// network connection ���� ���� user ����
		} catch (IOException ex) {
			ex.printStackTrace();
		} // end try
	} // end setUpNetworking()

	public class SendButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				writer.print(name + " : ");
				writer.println(outgoing.getText());
				writer.flush();

			} catch (Exception ex) {
				ex.printStackTrace();
			}

			outgoing.setText("");
			outgoing.requestFocus();
		}
	} // end class sendButtonListener
	
	
	public void processCommand(String[] commandAndArguments){
		String command = commandAndArguments[0];
		if (command.equals("MOVE")) {
			int y = Integer.parseInt(commandAndArguments[1]);
			int x = Integer.parseInt(commandAndArguments[2]);
			moveUser(y, x);
		} else if(command.equals("STATUS")){
			//print user status
			
		} else if (command.equals("EXIT")) {
			System.exit(0);
		}
	}
	public void moveUser(int y, int x){
		map.put(name, new ArrayList<Integer>(Arrays.asList(y, x)));
		_userMap.put(new User(name), new Location(y, x));
		String name = "";
		Map<Integer, Map<Integer, User>> toPrintUsers = new HashMap<Integer, Map<Integer, User>>();
		
		
		for (Entry<User, Location> userAndLocation : _userMap.entrySet()){
			User user = userAndLocation.getKey();
			Location location = userAndLocation.getValue();
			int locY = location.getLocation_y();
			int locX = location.getLocation_x();
			Map<Integer, User> rows = toPrintUsers.get(y);
			if(rows == null){
				rows = new HashMap<Integer, User>();
				toPrintUsers.put(locY, rows);
			}
			rows.put(locX, user);
			System.out.println("[Info] made user : " + toPrintUsers.get(y).get(x).getUserName());
		}
		
		
		// map�� name�� key�� �������� ������ѳ�����? ���� User��ü�� ���� �ʿ䰡 ���� �� ������.
		// find git hub above, change that
		Map<Integer, Map<Integer, String>> locations = new HashMap<Integer, Map<Integer, String>>();
		for (Entry<String, List<Integer>> keyAndValue : map.entrySet()) {
			name = keyAndValue.getKey();
			List<Integer> yAndX = keyAndValue.getValue();
			y = yAndX.get(0);
			x = yAndX.get(1);
			Map<Integer, String> rows = locations.get(y);
			if (rows == null) {
				rows = new HashMap<Integer, String>();
				locations.put(y, rows);
			}
			rows.put(x, name);
		}
		// place to determine isNearEnemy
		
		
//		printU sers(locations);
		printUsers2(toPrintUsers);
	}
	public void printUsers(Map<Integer, Map<Integer, String>> locations){
		
		incoming.setText("");
		for (int i = 0; i < 20; i++) {
			StringBuffer stringBuffer = new StringBuffer();
			for (int j = 0; j < 20; j++) {
				Map<Integer, String> rows = locations.get(i);
				if (rows != null) {
					name = rows.get(j);
					if (name != null) {
						stringBuffer.append("\t" + name);
						continue;
					}
				}
				stringBuffer.append("\t.");
			}
			incoming.append(stringBuffer.toString());
			incoming.append("\n");
		}
	}
	
	public void printUsers2(Map<Integer, Map<Integer, User>> locations){
		incoming.setTabSize(2);
		incoming.setText("");
		String name = "";
		for (int i = 0; i < 20; i++) {
			StringBuffer stringBuffer = new StringBuffer();
			for (int j = 0; j < 20; j++) {
				// nullPointerException occured....
				Map<Integer, User> rows = locations.get(i);
				if (rows != null) {
					name = rows.get(j).getUserName();
					if (name != null) {
						stringBuffer.append("\t" + name);
						continue;
					}
				}
				stringBuffer.append("\t.");
			}
			incoming.append(stringBuffer.toString());
			incoming.append("\n");
		}
	}
	public class IncomingReader implements Runnable {

		@Override
		public void run() {
			String message;
			try {
				while ((message = reader.readLine()) != null) {

					// Move order
					String[] nameAndCommand = message.split(" : ");
					String name = nameAndCommand[0];
					String command = nameAndCommand[1];
					String[] commandAndArguments = command.split(" ");
					
					processCommand(commandAndArguments);
					
					// map ���� ����   
					// name -> user ����
					// map<int, map<int, string>>
					// 1. map���� �����ų��?
					// 2. list�� ��������
					// �ϴ� list�� ���� (static���� ����)
					
					
					//���� Game���� ���ư��� ��?���� �������� ��
					// ���� ������ MOVE �����? �ɵ����� ��
					// ���߿� �����ϱⰡ ���� �� ����
					
				}
			} catch (Exception ex) {
				ex.printStackTrace();

			} // end try

		} // end run()

	} // end class Incoming

} // end class