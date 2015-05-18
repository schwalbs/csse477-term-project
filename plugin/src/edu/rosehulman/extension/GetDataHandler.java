package edu.rosehulman.extension;

import protocol.HttpRequest;
import protocol.response.HttpResponseDecorator;
import servlet.Servlet;

public class GetDataHandler implements Servlet {
	public static final String METHOD = "GET";
	public static final String RELATIVE_URL = "/data";
	
	private HttpResponseDecorator dec;
	
	@Override
	public void set(HttpRequest req, HttpResponseDecorator dec) {
		this.dec = dec;
	}

	@Override
	public void run() {
		dec.write("<!DOCTYPE html>");
        dec.write("<html>");
        dec.write("<head>");
        dec.write("<title>Servlet HelloWorldServlet</title>");            
        dec.write("</head>");
        dec.write("<body>");
        dec.write("<form name='input' action='/test/data' method='post'>");
        dec.write("Username: <input type='text' name='user'/> <br/>");
        dec.write("Password: <input type='password' name='password'/> <br/>");
        dec.write("<input type='submit'/> <br/>");
        dec.write("</form>");
        dec.write("</body>");
        dec.write("</html>");
		
	}
}
