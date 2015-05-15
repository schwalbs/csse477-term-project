package edu.rosehulman.staticfiles;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import protocol.HttpRequest;
import protocol.Protocol;
import protocol.response.HttpResponseDecorator;
import protocol.response.HttpResponseFactory;
import servlet.Servlet;

public class PutStaticFileServlet implements Servlet {
	private HttpRequest req;
	private HttpResponseDecorator dec;
	

	@Override
	public void set(HttpRequest req, HttpResponseDecorator dec) {
		this.req = req;
		this.dec = dec;
	}
	

	@Override
	public void run(){
		File file = new File(StaticFilePlugin.fileDir + req.getRootUrl());
		
		
		try {
			FileWriter writer = new FileWriter(file, true);
			writer.write(req.getBody());
			writer.close();
			dec.write("");			
		} catch (IOException e) {
			dec.setResponse(HttpResponseFactory.createResponse(Protocol.INTERNAL_SERVER_ERROR, null, Protocol.CLOSE));
			dec.writeError();
		}		
	}

}
