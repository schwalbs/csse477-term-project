package servlet;

import protocol.HttpRequest;
import protocol.response.HttpResponseDecorator;

public interface Servlet extends Runnable {
	public void set(HttpRequest req, HttpResponseDecorator dec);
	public void run();

}
