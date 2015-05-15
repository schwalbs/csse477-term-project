package edu.rosehulman.staticfiles;

import java.io.File;
import java.nio.file.Files;

import protocol.HttpRequest;
import protocol.Protocol;
import protocol.response.HttpResponseDecorator;
import protocol.response.HttpResponseFactory;
import servlet.Servlet;

public class DeleteStaticFileServlet implements Servlet {

	private HttpRequest req;
	private HttpResponseDecorator dec;
	

	@Override
	public void set(HttpRequest req, HttpResponseDecorator dec) {
		this.req = req;
		this.dec = dec;
	}
	
	@Override
	public void run() {
		File file = new File(StaticFilePlugin.fileDir + req.getRootUrl());
		
		if(!file.exists()){
			dec.setResponse(HttpResponseFactory.createResponse(Protocol.NOT_FOUND_CODE, null, Protocol.CLOSE));
			dec.write("");
			return;
		}
		
		if(file.isDirectory()){
			dec.setResponse(HttpResponseFactory.createResponse(Protocol.BAD_REQUEST_CODE, null, Protocol.CLOSE));
			dec.write("");
			return;			
		} else {
			try{
				Files.delete(file.toPath());
				dec.write("");
			} catch (Exception e){
				dec.setResponse(HttpResponseFactory.createResponse(Protocol.INTERNAL_SERVER_ERROR, null, Protocol.CLOSE));
				dec.write("");
			}
		}
	}

}
