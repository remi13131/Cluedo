package Cluedo.Networking;

import java.io.IOException;

/**
 * Tests the server.
 * 
 * @author delima
 */
public class TestServer {
	public static void main(String[] args) {
		if (args.length != 3) {
			System.err.println(
					"Usage: java TestServer <port number> <num clients> <timeout>");
			System.exit(1);
		}

		int portNumber = Integer.parseInt(args[0]);
		int numClients = Integer.parseInt(args[1]);
		int timeout = Integer.parseInt(args[2]);

		System.out.println("Starting server.");
		try {
			RegServer server = new RegServer(portNumber, numClients, timeout);
			System.out.println("Waiting for connections...");
			server.open();
			for (int i = 0; i < server.getNumClients(); ++i) {
				server.send(i, "begin");
			}
			String msg;
			while (server.getNumClients() > 0) {
				for (int i = 0; i < server.getNumClients(); ++i) {
					server.send(i, "next message");
				}
				for (int i=0; i < server.getNumClients(); ++i) {
					msg  = server.receive(i);
					System.out.format("Client %1$d: %2$s\n", i, msg);
					if (msg.equals(ComServer.END_MSG)) {
						server.close(i);
					}
				}
			}
			server.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
