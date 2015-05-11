package server;

import gui.WebServer;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import plugin.PluginLoader;
import plugin.PluginManager;
import protocol.Protocol;
import protocol.response.HttpResponse200;
import protocol.response.HttpResponse301;
import protocol.response.HttpResponse400;
import protocol.response.HttpResponse404;
import protocol.response.HttpResponse500;
import protocol.response.HttpResponse505;
import protocol.response.HttpResponseFactory;

/**
 * This represents a welcoming server for the incoming
 * TCP request from a HTTP client such as a web browser. 
 * 
 * @author Chandan R. Rupakheti (rupakhet@rose-hulman.edu)
 */
public class Server implements Runnable {
	private String rootDirectory;
	private int port;
	private boolean stop;
	private ServerSocket welcomeSocket;
	
	private long connections;
	private long serviceTime;

	private ArrayList<InetAddress> blacklist;
	private RequestRateChecker blc;
	private PluginManager pluginManager;
	private PluginLoader pluginLoader;
	public Router router;
	private WebServer window;
	/**
	 * @param rootDirectory
	 * @param port
	 */
	public Server(String rootDirectory, int port, WebServer window) {
		this.rootDirectory = rootDirectory;
		this.port = port;
		this.stop = false;
		this.connections = 0;
		this.serviceTime = 0;
		this.window = window;
		
		blacklist = new ArrayList<>();
		blc = new RequestRateChecker(this);
		pluginManager = new PluginManager();
		pluginLoader = new PluginLoader(pluginManager);
		router = new Router(pluginManager);
		
		setMappings();
	}

	/**
	 * Gets the root directory for this web server.
	 * 
	 * @return the rootDirectory
	 */
	public String getRootDirectory() {
		return rootDirectory;
	}


	/**
	 * Gets the port number for this web server.
	 * 
	 * @return the port
	 */
	public int getPort() {
		return port;
	}
	
	/**
	 * Returns connections serviced per second. 
	 * Synchronized to be used in threaded environment.
	 * 
	 * @return
	 */
	public synchronized double getServiceRate() {
		if(this.serviceTime == 0)
			return Long.MIN_VALUE;
		double rate = this.connections/(double)this.serviceTime;
		rate = rate * 1000;
		return rate;
	}
	
	/**
	 * Increments number of connection by the supplied value.
	 * Synchronized to be used in threaded environment.
	 * 
	 * @param value
	 */
	public synchronized void incrementConnections(long value) {
		this.connections += value;
	}
	
	public void addToBlacklist(InetAddress toBlacklist){
		this.blacklist.add(toBlacklist);
	}
	
	/**
	 * Increments the service time by the supplied value.
	 * Synchronized to be used in threaded environment.
	 * 
	 * @param value
	 */
	public synchronized void incrementServiceTime(long value) {
		this.serviceTime += value;
	}

	/**
	 * The entry method for the main server thread that accepts incoming
	 * TCP connection request and creates a {@link ConnectionHandler} for
	 * the request.
	 */
	public void run() {
		try {
			new Thread(pluginLoader).start();
			new Thread(blc).start();
			
			this.welcomeSocket = new ServerSocket(port);
			this.welcomeSocket.getInetAddress();
			// Now keep welcoming new connections until stop flag is set to true
			while(true) {
				// Listen for incoming socket connection
				// This method block until somebody makes a request
				Socket connectionSocket = this.welcomeSocket.accept();
				this.blc.addToMap(connectionSocket.getInetAddress());
				
				
				if(this.blacklist.contains(connectionSocket.getInetAddress())){
					connectionSocket.close();
					continue;
				}

				// Come out of the loop if the stop flag is set
				if(this.stop)
					break;
				
				// Create a handler for this incoming connection and start the handler in a new thread
				ConnectionHandler handler = new ConnectionHandler(this, connectionSocket);
				new Thread(handler).start();
				//RequestHandler requestHandler = new RequestHandler(this, connectionSocket, handler.getQueue());
				//new Thread(requestHandler).start();
			}
			this.welcomeSocket.close();
		}
		catch(Exception e) {
			window.showSocketException(e);
		}
	}
	
	public void setMappings(){
		HttpResponseFactory.addResponseType(Protocol.OK_CODE, HttpResponse200.class);
		HttpResponseFactory.addResponseType(Protocol.MOVED_PERMANENTLY_CODE, HttpResponse301.class);
		HttpResponseFactory.addResponseType(Protocol.BAD_REQUEST_CODE, HttpResponse400.class);
		HttpResponseFactory.addResponseType(Protocol.NOT_FOUND_CODE, HttpResponse404.class);
		HttpResponseFactory.addResponseType(Protocol.INTERNAL_SERVER_ERROR, HttpResponse500.class);
		HttpResponseFactory.addResponseType(Protocol.NOT_SUPPORTED_CODE, HttpResponse505.class);
	}
	
	/**
	 * Stops the server from listening further.
	 */
	public synchronized void stop() {
		if(this.stop)
			return;
		

		// Set the stop flag to be true
		this.stop = true;
		try {
			// This will force welcomeSocket to come out of the blocked accept() method 
			// in the main loop of the start() method
			Socket socket = new Socket(InetAddress.getLocalHost(), port);
			
			// We do not have any other job for this socket so just close it
			socket.close();
			
			this.pluginLoader.stop();
		}
		catch(Exception e){}
	}
	
	/**
	 * Checks if the server is stopeed or not.
	 * @return
	 */
	public boolean isStoped() {
		if(this.welcomeSocket != null)
			return this.welcomeSocket.isClosed();
		return true;
	}
}
