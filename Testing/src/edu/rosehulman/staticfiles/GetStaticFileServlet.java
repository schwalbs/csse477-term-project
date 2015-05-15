package edu.rosehulman.staticfiles;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import protocol.HttpRequest;
import protocol.Protocol;
import protocol.response.HttpResponseDecorator;
import protocol.response.HttpResponseFactory;
import servlet.Servlet;

public class GetStaticFileServlet implements Servlet {
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
		if(!file.exists() || file.isDirectory()){
			dec.setResponse(HttpResponseFactory.createResponse(Protocol.NOT_FOUND_CODE, null, Protocol.CLOSE));
			dec.write("");
			return;
		}
		
		BufferedReader br = null;
		try{
			String line;
			br = new BufferedReader(new FileReader(file));
			
			while((line = br.readLine()) != null){
				dec.write(line);
			}
			
		} catch (Exception e){
			dec.setResponse(HttpResponseFactory.createResponse(Protocol.INTERNAL_SERVER_ERROR, null, Protocol.CLOSE));
			dec.writeError();
		}
	}
}
