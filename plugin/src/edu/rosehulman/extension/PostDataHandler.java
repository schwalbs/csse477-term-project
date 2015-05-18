package edu.rosehulman.extension;

import protocol.HttpRequest;
import protocol.response.HttpResponseDecorator;
import servlet.Servlet;

public class PostDataHandler implements Servlet {
	public static final String METHOD = "POST";
	public static final String RELATIVE_URL = "/data";

	private HttpResponseDecorator dec;
	private HttpRequest req;

	@Override
	public void set(HttpRequest req, HttpResponseDecorator dec) {
		this.dec = dec;
		this.req = req;
	}

	@Override
	public void run() {
		dec.write("<!DOCTYPE html>");
		dec.write("<html>");
		dec.write("<head>");
		dec.write("<title>Servlet HelloWorldServlet</title>");
		dec.write("</head>");
		dec.write("<body>");
		dec.write("Got post request :<br/>");
		dec.write("Request content:" + String.valueOf(req.getBody()));
		dec.write("</body>");
		dec.write("</html>");

	}
}
