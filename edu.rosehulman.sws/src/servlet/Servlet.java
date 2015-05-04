package servlet;

import protocol.HttpRequest;
import protocol.response.HttpResponseDecorator;

public interface Servlet {
	public void process(HttpRequest request, HttpResponseDecorator decorator);

}
