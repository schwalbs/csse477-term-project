package edu.rosehulman.txteditorapi;

import plugin.Plugin;
import servlet.Servlet;

public class TxtEditorAPIPlugin implements Plugin {
	
	@Override
	public Servlet getServlet(String method, String relativeUrl)
			throws ServletNotFoundException {
		if(method.equals("GET") && relativeUrl.equals("")){
			return new IndexServlet();
		}else if(method.equals("GET") && relativeUrl.equals("/getall")){
			return new GetAllServlet();
		}else if(method.equals("GET")){
			String file = relativeUrl.split("/")[2];
			return new GetFileServlet(file);
		}else if(method.equals("POST")){
			return new PostEditServlet();
		}else if(method.equals("DELETE")){
			String file = relativeUrl.split("/")[2];
			return new DeleteFileServlet(file);
		}else if(method.equals("PUT")){
			return new CreateFileServlet();
		}else{
			return new HelloServlet();
		
		}
	}

}
