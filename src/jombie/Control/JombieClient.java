package jombie.Control;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
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
	
	
	BufferedReader reader;
	PrintWriter writer;
	Socket sock;
	static String name;
	public static void main (String [] args) {
		Scanner sc = new Scanner(System.in);
		System.out.println("Input name : ");
		name = sc.next();
		JombieClient client = new JombieClient ();
		client.go();
	}
	
	public void go() {
		
		JFrame frame = new JFrame("Ludicrously Simple Chat Client");
		JPanel panel = new JPanel();
		
		incoming = new JTextArea(15, 50);
		incoming.setLineWrap(true);
		incoming.setWrapStyleWord(true);
		incoming.setEditable(false);
		JScrollPane qScroller = new JScrollPane(incoming);
		qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
//		outgoing = new JTextField(20);
		JButton[] sendButton = new JButton[9];
		sendButton[0] = new JButton("¢Ø");
		sendButton[1] = new JButton("¡è");
		sendButton[2] = new JButton("¢Ö");
		sendButton[3] = new JButton("¡ç");
		sendButton[4] = new JButton("  ");
		sendButton[5] = new JButton("¡æ");
		sendButton[6] = new JButton("¢×");
		sendButton[7] = new JButton("¡é");
		sendButton[8] = new JButton("¢Ù");
		
		panel.add(qScroller);
//		panel.add(outgoing);
		for(int i = 0 ;i < 9; i ++){
			sendButton[0].addActionListener(new SendButtonListener(i));
			panel.add(sendButton[i]);
		}
		setUpNetworking();
		
		Thread readerThread = new Thread(new IncomingReader());
		readerThread.start();
		
		frame.getContentPane().add(BorderLayout.CENTER, panel);
		frame.setSize(400,500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	
	} // end go()
	
	public void setUpNetworking() {
		
		try{
			sock = new Socket("127.0.0.1", 5000);
			InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
			reader = new BufferedReader(streamReader);
			writer = new PrintWriter(sock.getOutputStream());
			System.out.println("networking established");
		} catch (IOException ex) {
			ex.printStackTrace();
		} // end try
		
	} // end setUpNetworking()
	
	public class SendButtonListener implements ActionListener {
		int dicretion;
		SendButtonListener(int direction){
			this.dicretion = direction;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			try{
//				writer.print(name + " : ");
//				writer.println(outgoing.getText());
				
				
				writer.flush();
				
				
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			outgoing.setText("");
			outgoing.requestFocus();
		}
	} // end class sendButtonListener
	
	public class IncomingReader implements Runnable {

		@Override
		public void run() {
			String message;
			try {
				
				while((message = reader.readLine()) != null) {
					System.out.println("read " + message);
					incoming.append(message + "\n");
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			} // end try
			
		} // end run()
		
	} // end class Incoming
	
} // end class