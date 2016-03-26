package jombie.Server;

import java.awt.BorderLayout;
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

import jombie.Control.Game;
import jombie.Model.Location;
import jombie.Model.User;

public class Client {

	JTextArea incoming;
	JTextField outgoing;
	JPanel incomingDot;
	BufferedImage img;
	/** ChattingPanel chatPanel;. */

	BufferedReader reader;
	PrintWriter writer;
	Socket sock;

	private static String name;
	private static User loginUser;
	private static String ip;
	private static String port;
	private static Game game;
	 
	private static Map<String, List<Integer>> map = new HashMap<String, List<Integer>>();
	private static HashMap<String, User> _userMap = new HashMap<String, User>();

	/** Map에 name을 key로 가지고 user클래스쪽에서 location정보와 나머지를 모두 읽어들여야겠음. */

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.print("Input name : ");
		name = sc.next();
		// System.out.print("IP : ");
		// ip = sc.next();
		// System.out.print("Port : ");
		// port = sc.next();
		Client client = new Client();
		client.go();
		sc.close();
	}

	public void setPanel() {
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
		String ip = "172.200.110.143";
		String port = "5000";

		setUpNetworking(ip, port);
		Thread readerThread = new Thread(new IncomingReader());
		readerThread.start();

		logIn();
		setPanel();

	}

	/** End go(). */

	public void logIn() {
		int y = (int) (Math.random() * 20);
		int x = (int) (Math.random() * 20);
		Location initialLocation = new Location(y, x); // random Location
		loginUser = new User(name, initialLocation);
		_userMap.put(name, loginUser);
	}

	public void setUpNetworking(String ip, String port) {
		try {
			sock = new Socket(ip, Integer.parseInt(port));
			System.out.println("[Info] socket success");
		} catch (Exception e) {
			System.out.println("[Error] socket failed");
		}
		try {
			InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
			reader = new BufferedReader(streamReader);
			writer = new PrintWriter(sock.getOutputStream());
			game = new Game();
			
			System.out.println("[Info] networking established");
		} catch (IOException ex) {
			System.out.println("[Error] networking failed");
			ex.printStackTrace();
		} // end try
	}

	/** End setUpNetworking(). */

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
	}

	/** End class sendButtonListener. */

	public void processCommand(String name, String[] commandAndArguments) {
		String command = commandAndArguments[0];
		if ("MOVE".equals(command)) {
			int y = Integer.parseInt(commandAndArguments[1]);
			int x = Integer.parseInt(commandAndArguments[2]);
			moveUser(name, y, x);
		} else if ("STATUS".equals(command)) {
			printStatus();
		} else if ("EXIT".equals(command)) {
			System.exit(0);
		}
	}

	public void moveUser(String name, int y, int x) {
//		map.put(name, new ArrayList<Integer>(Arrays.asList(y, x)));

		User user = _userMap.get(name);
		if (user != null) {
			user.setUserLocation(y, x);
		} else {
			user = new User(name, new Location(y, x));
		}
		_userMap.put(name, user);

		Map<Integer, Map<Integer, User>> locations = new HashMap<Integer, Map<Integer, User>>();

		for (Entry<String, User> nameAndUser : _userMap.entrySet()) {
			name = nameAndUser.getKey();
			user = nameAndUser.getValue();
			Location location = user.getUserLocation();
			int locY = location.getLocationY();
			int locX = location.getLocationX();
			Map<Integer, User> rows = locations.get(locY);
			if (rows == null) {
				rows = new HashMap<Integer, User>();
				locations.put(locY, rows);
			}
			rows.put(locX, user);
		}
		game.isNearEnemy(_userMap);
		// call Attack and Change user Status

		// map�� name�� key�� �������� ������ѳ�����? ���� User��ü�� ���� �ʿ䰡
		// ���� �� ������.
		
		//printUser는 실행시키지 않은채로 Location정보 Double로 변경시킨 것 실행.
		
		printUsers(locations);

	}

	public void printUsers(Map<Integer, Map<Integer, User>> locations) {
		incoming.setTabSize(2);
		incoming.setText("");
		String name = "";

		for (int i = 0; i < 20; i++) {
			StringBuffer stringBuffer = new StringBuffer();
			for (int j = 0; j < 20; j++) {
				Map<Integer, User> rows = locations.get(i);
				if (rows != null) {
					User user = rows.get(j);
					if (user != null) {
						name = user.getUserName();
						stringBuffer.append("\t").append(name);
						continue;
					}
				}
				stringBuffer.append("\t.");
			}
			incoming.append(stringBuffer.toString());
			incoming.append("\n");
		}
	}

	public void printStatus() {
		incoming.setText("");
		for (Entry<String, User> users : _userMap.entrySet()) {
			users.getValue().printUserStatus(incoming);
		}
	}

	public class IncomingReader implements Runnable {

		@Override
		public void run() {
			String message;
			try {
				while ((message = reader.readLine()) != null) {
					try {
						String[] nameAndCommand = message.split(" : ");
						String name = nameAndCommand[0];
						String command = nameAndCommand[1];
						String[] commandAndArguments = command.split(" ");

						processCommand(name, commandAndArguments);
					} catch (ArrayIndexOutOfBoundsException idxE) {

					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();

			} // end try

		} // end run()

	} // end class Incoming

} // end class