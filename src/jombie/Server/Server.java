package jombie.Server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
//import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
/** Import java.awt.event.*; */

public class Server {

	ArrayList clientOutputStreams;

	public static void main(String[] args) {
		Server server = new Server();
		server.go();
	} /** End main(). */

	public void go() {
		clientOutputStreams = new ArrayList();

		try {
			ServerSocket serverSock = new ServerSocket(5000);

			while (true) {
				System.out.println("[Info] trying to connect");
				Socket clientSock = serverSock.accept();
				System.out.println("[Info] trying to socketAccept");
				PrintWriter writer = new PrintWriter(clientSock.getOutputStream());
				clientOutputStreams.add(writer);

				Thread t = new Thread(new ClientHandler(clientSock));
				t.start();
				System.out.println("got a connection");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} // end try

	} /** End go(). */

	public class ClientHandler implements Runnable {

		BufferedReader reader;
		Socket sock;

		public ClientHandler(Socket clientSocket) {
			try {
				sock = clientSocket;
				InputStreamReader isReader = new InputStreamReader(sock.getInputStream());
				reader = new BufferedReader(isReader);
			} catch (Exception ex) {
				ex.printStackTrace();
			} // end try
		} /** End ClientHandler(). */

		@Override
		public void run() {
			String messages;

			try {
				while ((messages = reader.readLine()) != null) {
					System.out.println("read " + messages);
					tellEveryone(messages);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			} // end try

		} // end run()

	} /** End class ClientHandler. */

	public void tellEveryone(String message) {

		Iterator it = clientOutputStreams.iterator();
		while (it.hasNext()) {
			try {
				PrintWriter writer = (PrintWriter) it.next();
				writer.println(message);
				writer.flush();
			} catch (Exception ex) {
				ex.printStackTrace();
			} // end try
		} // end while
	} // end tellEveryone()
}