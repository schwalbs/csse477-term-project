package edu.rosehulman.txteditorapi;

import plugin.Plugin;
import servlet.Servlet;

public class TxtEditorAPIPlugin implements Plugin {
	
	@Override
	public Servlet getServlet(String method, String relativeUrl)
			throws ServletNotFoundException {
		
		if(method.equals("GET") && relativeUrl.equals("")){
			return new IndexServlet();
		}else {
			return new HelloServlet();
		}
	}

}
