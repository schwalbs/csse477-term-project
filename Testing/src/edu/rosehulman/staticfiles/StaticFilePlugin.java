package edu.rosehulman.staticfiles;

import plugin.Plugin;
import servlet.Servlet;

public class StaticFilePlugin implements Plugin {
	public static final String fileDir = "web";

	@Override
	public Servlet getServlet(String method, String relativeUrl)
			throws ServletNotFoundException {
		switch (method){
		case "GET":
			return new GetStaticFileServlet();
		case "POST":
			return new PostStaticFileServlet();
		case "PUT":
			return new PutStaticFileServlet();
		case "DELETE":
			return new DeleteStaticFileServlet();
		default:
			throw new ServletNotFoundException("Method " + method + " not valid for static file.");
		}
	}
}
