package Cluedo.Networking;

import java.net.Socket;
import java.net.UnknownHostException;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStreamReader;
import java.io.IOException;

/**
 * Implements a network client.
 * 
 * @author Tiago de Lima
 */
public class Client {
	
	private Socket socket = null;
	private PrintWriter out;
	private BufferedReader in;

	/**
	 * Opens a connection with the server.
	 * 
	 * @param addr The server address.
	 * @param port The server port.
	 * @throws UnknownHostException if IP address of the server could not be
	 *     determined.
	 * @throws IOException if an I/O error occurs.
	 */
	public void open(String addr, int port)
			throws UnknownHostException, IOException {
		
		this.socket = new Socket(addr, port);
		this.out = new PrintWriter(socket.getOutputStream(), true);
		this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}
	
	/**
	 * Waits for a message from the server.
	 * 
	 * @return A String containing the message.
	 * @throws IOException if an I/O error occurs.
	 */
	public String receive() throws IOException {
		return this.in.readLine();
	}
	
	/**
	 * Sends a message to the server.
	 * 
	 * @param msg A String containing the message.
	 */
	public void send(String msg) {
            this.out.println(msg);
	}
	
        public void flushOut(){
             this.out.flush();
        }
        
	/**
	 * Closes the connection with the server.
	 * 
	 * @throws IOException if an I/O error occurs.
	 */
	public void close() throws IOException {
                this.out.flush();
		this.out.close();
		this.in.close();
		this.socket.close();
	}
}
