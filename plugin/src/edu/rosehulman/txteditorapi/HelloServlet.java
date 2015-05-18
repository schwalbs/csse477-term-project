package edu.rosehulman.txteditorapi;

import protocol.HttpRequest;
import protocol.response.HttpResponseDecorator;
import servlet.Servlet;

public class HelloServlet implements Servlet {

	private HttpRequest req;
	private HttpResponseDecorator dec;
	
	@Override
	public void set(HttpRequest req, HttpResponseDecorator dec) {
		this.req = req;
		this.dec = dec;
	}

	@Override
	public void run() {
		dec.write("Hello World");

	}

}
