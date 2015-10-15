package Cluedo.Networking;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Implements a registration server.
 * 
 * @author Tiago de Lima
 */
public class RegServer {
	
	// Registration period timeout in milliseconds.
	private int regTimeout = 30000;
	// Maximum number of connections open at the same time.
	private int maxConnOpen = 5;
	// Server socket.
	private ServerSocket serverSocket = null;
	// List of communication servers, one for each client.
	private List<ComServer> clients = null;
	
	/**
	 * RegServer constructor 1.
	 * 
	 * @param port An integer specifying the communication port of the server.
	 * @throws IOException if an I/O exception occurs.
	 */
	RegServer(int port) throws IOException {
		this.serverSocket = new ServerSocket(port);
		this.clients = new ArrayList<ComServer>();
	}
	
	/**
	 * RegServer constructor 2.
	 * 
	 * @param port An integer specifying the communication port of the server.
	 * @param maxConnOpen An integer specifying the maximum number of clients.
	 * @param regTimeout An integer specifying the timeout of the registration
	 *     period in milliseconds.
	 * @throws IOException if an I/O error occurs.
	 */
    public RegServer(int port, int maxConnOpen, int regTimeout) throws IOException {
    	this(port);
    	this.maxConnOpen = maxConnOpen;
    	this.regTimeout = regTimeout;
    }
    
    /**
     * Starts the server and waits for client connections.
     * 
     * @throws SocketException if a TCP error occurs.
     * @throws IOException if an I/O error occurs.
     */
    public void open() throws SocketException, IOException {
    	// Each call to accept() will block the execution for 250 milliseconds.
    	this.serverSocket.setSoTimeout(250);
    	
    	// Calculate the instant the registrations will stop.
    	Instant endInstant = Instant.now().plusMillis(regTimeout);
    	    	
    	Socket client = null;

    	while (true) {
    		try {
    			// Wait for a client connection.
    			client = this.serverSocket.accept();
				// Adds to the list of clients.
				this.clients.add(new ComServer(client));
                                System.out.println("A new Client has connected.");
                                this.sendAll("A new Client has connected.");
    		} catch (SocketTimeoutException e) {
                        Instant nowInstant = Instant.now().plusMillis(0);
                        Float millisLeft = Float.parseFloat(""+(endInstant.toEpochMilli() - nowInstant.toEpochMilli())) / 1000;
                        if(millisLeft > 0) System.out.println(millisLeft +" seconds left for Clients to connect.");
                        else System.out.println("Maximum connexion time reached.");
    			// If the number of connections reached its limit.
    			if (this.getNumClients() >= this.maxConnOpen) {
    				break;
    			}
    			// If the elapsed time exceeds regTimeout.
    			if (Instant.now().isAfter(endInstant)) {
    				break;
    			}
    			// if (Thread.interrupted()) {
    			// 	break;
    			// }
    		}
    	}
    }
    
    /**
     * Gets the number of clients currently connected.
     * 
     * @return An integer corresponding to the number of clients currently
     *     connected.
     */
    public int getNumClients() {
    	return this.clients.size();
    }
    
    /**
     * Waits for a message from a client.
     * 
     * @param clientNumber An int designating wich client to wait for.
     * @return A string containing the message
     * @throws IOException if an I/O error occurs.
     */
    public String receive(int clientNumber) throws IOException {
    	return this.clients.get(clientNumber).recieve();
    }
    
    /**
     * Sends a message to a client.
     * 
     * @param clientNumber An int designating the client to which the message
     *     will be sent.
     * @param msg A String containing the message.
     * @throws IOException if an I/O error occurs.
     */
    public void send(int clientNumber, String msg) throws IOException {
    	this.clients.get(clientNumber).send(msg);
    }

    public void sendAll(String msg) throws IOException {
    	for (int i = 0; i < this.getNumClients(); ++i) this.send(i, msg);
    }
    
    /**
     * Closes the connection with a client.
     * 
     * @param clientNumber An integer designating the client.
     * @throws IOException if an I/O error occurs.
     */
    public void close(int clientNumber) throws IOException {
    	clients.get(clientNumber).close();
    	clients.remove(clientNumber);
    }
    
    /**
     * Closes the connections with all clients.
     * 
     * @throws IOException if an I/O error occurs.
     */
    public void close() throws IOException {
    	for (int i = 0; i < this.clients.size(); ++i) {
    		this.close(i);
    	}
    	this.serverSocket.close();
    }    
}
