package jombie.View;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.PrintWriter;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

public class ChattingPanel {
	public JTextArea incoming;
	public JTextField outgoing;
	public JFrame frame = new JFrame("Ludicrously Simple Chat Client");
	public JPanel panel = new JPanel();
	
	BufferedReader reader;
	PrintWriter writer;
	public void settingPanel() {
//		JFrame frame = new JFrame("Ludicrously Simple Chat Client");
//		JPanel panel = new JPanel();
		incoming = new JTextArea(15, 50);
		incoming.setLineWrap(true);
		incoming.setWrapStyleWord(true);
		incoming.setEditable(false);
		JScrollPane qScroller = new JScrollPane(incoming);
		qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		outgoing = new JTextField(20);
		JButton sendButton = new JButton("Send");
		sendButton.addActionListener(new SendButtonListener());
		panel.add(qScroller);
		panel.add(outgoing);
		panel.add(sendButton);
	}
	public void setVisiblePanel(){
		frame.getContentPane().add(BorderLayout.CENTER, panel);
		frame.setSize(400,500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	public void getReaderAndWriter(BufferedReader reader, PrintWriter writer){
		this.reader = reader;
		this.writer = writer;
	}
	public void wirteIncoming(){
		
	}
	public class SendButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			try{
//				writer.print(name + " : ");
				writer.println(outgoing.getText());
				writer.flush();
				
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			outgoing.setText("");
			outgoing.requestFocus();
		}
	} // end class sendButtonListener
}
