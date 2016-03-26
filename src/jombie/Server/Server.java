package jombie.Server;

import java.io.BufferedReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
//import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

/** Import java.awt.event.*; */

public class Server {
	List<ObjectInputStream> instrList;

	ObjectInputStream instream;
	ObjectOutputStream outstream;
	
	public static int count = 0;
	public static void main(String[] args) {
		Server server = new Server();
		server.go();
	}

	/** End main(). */

	public void go() {
		try {
			ServerSocket serverSock = new ServerSocket(5000);
			instrList = new ArrayList<>();
			while (true) {
				System.out.println("[Info] trying to connect");
				Socket clientSock = serverSock.accept();
				System.out.println("[Info] trying to socketAccept");
//				PrintWriter writer = new PrintWriter(clientSock.getOutputStream());
//				clientOutputStreams.add(writer);
				
				
				Thread t = new Thread(new ClientHandler(clientSock));
				t.start();
				System.out.println("got a connection");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} // end try

	}

	/** End go(). */
	// 172.200.110.143
	public class ClientHandler implements Runnable {

		BufferedReader reader;
		Socket sock;

		public ClientHandler(Socket clientSocket) {
			try {
				
				sock = clientSocket;
				// InputStreamReader isReader = new
				// InputStreamReader(sock.getInputStream());
				// reader = new BufferedReader(isReader);

				instream = new ObjectInputStream(sock.getInputStream());
				outstream = new ObjectOutputStream(sock.getOutputStream());
				
				instrList.add(instream);
			} catch (Exception ex) {
				ex.printStackTrace();
			} // end try
		}

		/** End ClientHandler(). */

		@Override
		public void run() {
//			String messages;

			try {
				// while ((messages = reader.readLine()) != null) {
				// System.out.println("read " + messages);
				//
				// tellEveryone(messages);
				// }
				
				Object obj = null;
				int count = 0;
				while((obj = instream.readObject()) != null){
					HashMap<Integer, String> tmp = (HashMap<Integer, String>)obj;
					System.out.println(++count + "-----------------------");
					for(Entry<Integer, String> tmpMap : tmp.entrySet()){
						System.out.println(tmpMap.getKey() + "  " + tmpMap.getValue());
					}
					
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			} // end try

		} // end run()

	}

	/** End class ClientHandler. */

//	public void tellEveryone(String message) {
////		Iterator it = clientOutputStreams.iterator();
//		while (it.hasNext()) {
//			try {
//				PrintWriter writer = (PrintWriter) it.next();
//				writer.println(message);
//				writer.flush();
//			} catch (Exception ex) {
//				ex.printStackTrace();
//			} // end try
//		} // end while
//	} // end tellEveryone()
}