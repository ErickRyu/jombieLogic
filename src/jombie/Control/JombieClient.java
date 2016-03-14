package jombie.Control;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
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
import java.util.Random;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

public class JombieClient {

	JTextArea incoming;
	JTextField outgoing;
	JPanel incomingDot;
	BufferedImage img;
	// ChattingPanel chatPanel;

	BufferedReader reader;
	PrintWriter writer;
	Socket sock;
	static String name;
	static String ip;
	static String port;
	static Map<String, List<Integer>> map = new HashMap<String, List<Integer>>();

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.print("Input name : ");
		name = sc.next();

		// System.out.print("IP : ");
		// ip = sc.next();
		// System.out.print("Port : ");
		// port = sc.next();
		JombieClient client = new JombieClient();
		client.go();
	}

	public void go() {
		// chatPanel = new ChattingPanel();
		// chatPanel.settingPanel();
		JFrame frame = new JFrame("Ludicrously Simple Chat Client");
		JPanel panel = new JPanel();
		incoming = new JTextArea(15, 50);
		incoming.setLineWrap(true);
		incoming.setWrapStyleWord(true);
		incoming.setEditable(false);

		// test for print dot
		incomingDot = new JPanel();
		incomingDot.setSize(20, 20);
		incomingDot.setVisible(true);
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

		panel.add(incomingDot);

		panel.add(outgoing);
		panel.add(sendButton);

		String ip = "127.0.0.1";
		String port = "5000";

		setUpNetworking(ip, port);

		Thread readerThread = new Thread(new IncomingReader());
		readerThread.start();

		frame.getContentPane().add(BorderLayout.CENTER, panel);
		frame.setSize(400, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		// chatPanel.setVisiblePanel();
	} // end go()

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

	public void paint(int radius) {
		Graphics2D g = img.createGraphics();
		g.setColor(Color.orange);
		g.fillRect(0, 0, 150, 150);
		g.setColor(Color.black);

		g.drawOval((150 / 2 - radius), (150 / 2 - radius), radius * 2, radius * 2);
	}

	private void doDrawing(Graphics g) {

		Graphics2D g2d = (Graphics2D) g;

		g2d.setPaint(Color.blue);

//		int w = getWidth();
		int w = 20;
		int h = 20;
//		int h = getHeight();

		Random r = new Random();

		for (int i = 0; i < 2000; i++) {

			int x = Math.abs(r.nextInt()) % w;
			int y = Math.abs(r.nextInt()) % h;
			g2d.drawLine(x, y, x, y);
		}
	}

	public class IncomingReader implements Runnable {

		@Override
		public void run() {
			String message;
			try {
				while ((message = reader.readLine()) != null) {

					paint(2);
//					incomingDot.

					// Graphics2D g2d = (Graphics2D)incomingDot;
					// g2d.drawOval(5, 5, 100, 100);

					// Move order
					String[] nameAndCommand = message.split(" : ");
					String name = nameAndCommand[0];
					String command = nameAndCommand[1];
					String[] commandAndArguments = command.split(" ");

					if (commandAndArguments[0].equals("MOVE")) {
						int y = Integer.parseInt(commandAndArguments[1]);
						int x = Integer.parseInt(commandAndArguments[2]);
						map.put(name, new ArrayList<Integer>(Arrays.asList(y, x)));

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

						incoming.setTabSize(2);
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
					} else if (commandAndArguments[0].equals("EXIT")) {
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();

			} // end try

		} // end run()

	} // end class Incoming

} // end class