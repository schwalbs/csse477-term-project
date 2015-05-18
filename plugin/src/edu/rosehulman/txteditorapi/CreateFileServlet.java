package edu.rosehulman.txteditorapi;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import org.json.JSONException;
import org.json.JSONObject;

import protocol.HttpRequest;
import protocol.response.HttpResponseDecorator;
import servlet.Servlet;

public class CreateFileServlet implements Servlet {

	private HttpRequest req;
	private HttpResponseDecorator dec;
	@Override
	public void set(HttpRequest req, HttpResponseDecorator dec) {
		this.req = req;
		this.dec = dec;
		
	}
	
	@Override
	public void run() {
		String body = new String(this.req.getBody());
		JSONObject obj = new JSONObject(body);
		PrintWriter writer;
		System.out.println(obj.getString("filename"));
		try {
			writer = new PrintWriter("files/"+obj.getString("filename"), "UTF-8");
			writer.close();
		} catch (FileNotFoundException | UnsupportedEncodingException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dec.write("{\"code\": 200, \"message\":\"OK\"}");
	
	}
}
