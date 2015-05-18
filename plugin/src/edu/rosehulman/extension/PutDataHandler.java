package edu.rosehulman.extension;

import protocol.HttpRequest;
import protocol.response.HttpResponseDecorator;
import servlet.Servlet;

public class PutDataHandler implements Servlet{
	public static final String METHOD = "PUT";
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
        dec.write("<strong>PUTin</strong>");
        dec.write("</body>");
        dec.write("</html>");		
	}

}
