package edu.rosehulman.txteditorapi;

import protocol.HttpRequest;
import protocol.response.HttpResponseDecorator;
import servlet.Servlet;

public class IndexServlet implements Servlet {

	private HttpRequest req;
	private HttpResponseDecorator dec;
	
	@Override
	public void set(HttpRequest req, HttpResponseDecorator dec) {
		this.req = req;
		this.dec = dec;
	}

	@Override
	public void run() {
		dec.write("<!DOCTYPE html>");
        dec.write("<html>");
        dec.write("<head>");
        dec.write("<script src=\"//code.jquery.com/jquery-1.11.3.min.js\"></script>");        
        dec.write("<title>Servlet HelloWorldServlet</title>");            
        dec.write("</head>");
        dec.write("<body>");
        dec.write("<button onclick=\"sendRequest()\" >Send</button>");
        dec.write("<script>function sendRequest(){"
        		+ "$.ajax({"
        		+ "url:'/txteditor/hello',"
        		+ "type:'GET',"
        		+ "success: function(data){"
        		+ "$('#hello').html(data);"
        		+ "}"
        		+ "});}"
        		+ "</script>");
        dec.write("<h2 id='hello'></h2>");
        dec.write("</body>");
        dec.write("</html>");
	}

}
