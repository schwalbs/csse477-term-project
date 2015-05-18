package edu.rosehulman.txteditorapi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import protocol.HttpRequest;
import protocol.response.HttpResponseDecorator;
import servlet.Servlet;

public class GetFileServlet implements Servlet {

	private HttpRequest req;
	private HttpResponseDecorator dec;
	private String filename;
	public GetFileServlet (String filename) {
		this.filename = filename;
	}
	@Override
	public void set(HttpRequest req, HttpResponseDecorator dec) {
		this.req = req;
		this.dec = dec;
		
	}
	@Override
	public void run() {
		String content;
		try {
			content = readFile("./files/"+this.filename);
			dec.write("{\"code\": 200, \"message\": \"OK\", \"payload\": {\"filename\":\""+this.filename+"\", \"content\": \""+content+"\"}}");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}

	public String readFile(String fileName) throws IOException {
	    BufferedReader br = new BufferedReader(new FileReader(fileName));
	    try {
	        StringBuilder sb = new StringBuilder();
	        String line = br.readLine();

	        while (line != null) {
	            sb.append(line);
	            line = br.readLine();
	        }
	        return sb.toString();
	    } finally {
	        br.close();
	    }
	}
}
