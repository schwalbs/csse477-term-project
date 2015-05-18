package edu.rosehulman.txteditorapi;

import java.io.File;

import protocol.HttpRequest;
import protocol.response.HttpResponseDecorator;
import servlet.Servlet;

public class GetAllServlet implements Servlet {

	private HttpRequest req;
	private HttpResponseDecorator dec;
	@Override
	public void set(HttpRequest req, HttpResponseDecorator dec) {
		this.req = req;
		this.dec = dec;
		
	}
	@Override
	public void run() {
		File folder = new File("./files");
		File[] list = folder.listFiles();
		dec.write( "{\"code\": 200, \"message\": \"OK\", \"payload\": [");
		System.out.println(list.length);
		for(int i = 0; i < list.length; i++){
			System.out.println(list[i].getName());
			dec.write("\"");
			dec.write(list[i].getName());
			dec.write("\"");
			if(i != list.length -1){
				dec.write(",");
			}
		}
		dec.write("]}");
	}
}
