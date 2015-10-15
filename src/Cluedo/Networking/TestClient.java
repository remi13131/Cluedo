package Cluedo.Networking;

import java.io.Console;
import java.io.IOException;

/**
 * Tests the client.
 * 
 * @author delima
 */
public class TestClient {
	public static void main(String[] args) {
		if (args.length != 2) {
	           System.err.println(
	               "Usage: java TestClient <host name> <port number>");
	           System.exit(1);
	       }
		
		String hostName = args[0];
		int portNumber = Integer.parseInt(args[1]);
		Client client = new Client();
		
		try {
			client.open(hostName, portNumber);
			
			Console console = System.console();
	        if (console == null) {
	            System.err.println("No console.");
	            System.exit(1);
	        }

	        String userCommand;
	        String serverMessage;
	        while (true) {
		        serverMessage = client.receive();
		        System.out.println("Server: " + serverMessage);
		        if (serverMessage.equals(ComServer.END_MSG)) {
		        	break;
		        }
	        	userCommand = console.readLine("Client: ");	            
		        client.send(userCommand); // move suggest plum library candlestick
		        if (userCommand.equals("exit")) {
		        	break;
		        }
			}
        	client.send(ComServer.END_MSG);
	        client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
