package Cluedo.Networking;

import java.net.Socket;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

/**
 * Implements a communication server.
 *  
 * @author delima
 */
public class ComServer {
	
	// Message used to end communication.
	public static String END_MSG = "exit";
			
	private Socket socket = null;
	private PrintWriter out;
	private BufferedReader in;

	/**
	 * ComServer constructor.
	 * 
	 * @param socket A Socket representing the connection with the client.
	 * @throws IOException if an I/O error occurs.
	 */
    public ComServer(Socket socket) throws IOException {
    	this.socket = socket;
        out = new PrintWriter(this.socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(
        		this.socket.getInputStream()));
    }
    
    /**
     * Sends a message to the client.
     * 
     * @param msg A String containing the message.
     * @throws IOException if an I/O occurs.
     */
    public void send(String msg) throws IOException {
    	this.out.println(msg);
    }
    
    /**
     * Waits for a message from the client.
     * 
     * @return A String containing the message.
     * @throws IOException if an I/O error occurs.
     */
    public String recieve() throws IOException {
    	return this.in.readLine();
    }
    
    /**
     * Closes the connection with the client.
     * @throws IOException if an I/O error occurs.
     */
    public void close() throws IOException {
    	this.out.close();
    	this.in.close();
    	this.socket.close();
    }
}
