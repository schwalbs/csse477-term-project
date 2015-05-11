package server;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import plugin.Plugin;
import plugin.Plugin.ServletNotFoundException;
import plugin.PluginManager;
import protocol.HttpRequest;
import protocol.Protocol;
import protocol.response.HttpResponseDecorator;
import protocol.response.HttpResponseFactory;
import servlet.Servlet;

public class Router {
	private PluginManager pm;
	private ExecutorService executor;

	public Router(PluginManager pm) {
		this.pm = pm;
		this.executor = Executors.newFixedThreadPool(40);
	}

	public void processRequest(HttpRequest request, HttpResponseDecorator decorator) {
		try {
			Plugin plugin = this.pm.getPlugin(request.getRootUrl());
			decorator.setResponse(HttpResponseFactory.createResponse(Protocol.OK_CODE, null, Protocol.CLOSE));

			Servlet serv = plugin.getServlet(request.getMethod(), request.getRelativeUrl());
			serv.set(request, decorator);
			
			serv.run();
//			this.executor.submit(serv);
//			try {
//				f.get();
//			} catch (Exception e) {
//			}
		} catch (InstantiationException | IllegalAccessException e) {
			decorator.setResponse(HttpResponseFactory.createResponse(Protocol.INTERNAL_SERVER_ERROR, null,
					Protocol.CLOSE));
		} catch (ServletNotFoundException | NullPointerException e) {
			decorator.setResponse(HttpResponseFactory.createResponse(Protocol.NOT_FOUND_CODE, null, Protocol.CLOSE));
		}
	}
}
