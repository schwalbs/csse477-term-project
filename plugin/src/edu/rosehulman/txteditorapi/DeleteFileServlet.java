package edu.rosehulman.txteditorapi;

import java.io.File;

import protocol.HttpRequest;
import protocol.response.HttpResponseDecorator;
import servlet.Servlet;

public class DeleteFileServlet implements Servlet {

	private HttpRequest req;
	private HttpResponseDecorator dec;
	private String filename;
	public DeleteFileServlet (String filename) {
		this.filename = filename;
	}
	@Override
	public void set(HttpRequest req, HttpResponseDecorator dec) {
		this.req = req;
		this.dec = dec;
		
	}
	@Override
	public void run() {
		File f = new File("files/" + this.filename);
		f.delete();
		dec.write("{\"code\": 200, \"message\":\"OK\"}");
	}
}
