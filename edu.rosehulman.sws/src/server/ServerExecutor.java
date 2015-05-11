package server;

import gui.WebServer;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 
 * @author Chandan R. Rupakheti (rupakhcr@clarkson.edu)
 */
public class ServerExecutor {
	public static void main(String[] args){
		//new WebServer().startServer();
		sendRequest();
	}
	
	public static void sendRequest(){
		  HttpURLConnection connection = null;  
		  try {
		    //Create connection
		    URL url = new URL("http://localhost:8080/test/data");
		    connection = (HttpURLConnection)url.openConnection();
		    connection.setRequestMethod("GET");

		    connection.setUseCaches(false);
		    connection.setDoOutput(true);

		    //Send request
		    DataOutputStream wr = new DataOutputStream (
		        connection.getOutputStream());
		    wr.close();

		  } catch (Exception e) {
		    new WebServer().startServer();
		  } finally {
		    if(connection != null) {
		      connection.disconnect(); 
		    }
		  }
	}
}