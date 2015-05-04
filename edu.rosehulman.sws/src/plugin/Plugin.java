package plugin;

import servlet.Servlet;

public interface Plugin {
	
	public Servlet getServlet(String method, String relativeUrl) throws ServletNotFoundException; 

	public class ServletNotFoundException extends Exception{
		private static final long serialVersionUID = 8008135L;

		public ServletNotFoundException(String message){
			super(message);
		}
	}
}
