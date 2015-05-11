package server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 
 * @author Chandan R. Rupakheti (rupakhcr@clarkson.edu)
 */
public class HeartBeat {
	static String path = "-cp ../../bin server.StartServerMain";
	
	public static void main(String[] args){
		//new WebServer().startServer();
		while(true){
			sendRequest();
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	public static void sendRequest(){
		  HttpURLConnection connection = null;  
		  try {
			System.out.println("Checking if server is alive");
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
			System.out.println("Server is down!\nRestarting Server");
			try {
				Runtime.getRuntime().exec("java -jar StartServer.jar");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		  } finally {
		    if(connection != null) {
		      connection.disconnect(); 
		    }
		  }
	}
}