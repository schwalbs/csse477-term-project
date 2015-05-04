/*
 * Router.java
 * May 3, 2015
 *
 * Simple Web Server (SWS) for EE407/507 and CS455/555
 * 
 * Copyright (C) 2011 Chandan Raj Rupakheti, Clarkson University
 * 
 * This program is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License 
 * as published by the Free Software Foundation, either 
 * version 3 of the License, or any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/lgpl.html>.
 * 
 * Contact Us:
 * Chandan Raj Rupakheti (rupakhcr@clarkson.edu)
 * Department of Electrical and Computer Engineering
 * Clarkson University
 * Potsdam
 * NY 13699-5722
 * http://clarkson.edu/~rupakhcr
 */
 
package server;

import plugin.PluginManager;
import protocol.HttpRequest;
import protocol.Protocol;
import protocol.response.HttpResponseDecorator;
import protocol.response.HttpResponseFactory;
import servlet.Servlet;

/**
 * 
 * @author Chandan R. Rupakheti (rupakhcr@clarkson.edu)
 */
public class Router {
	private PluginManager pm;
	public Router(PluginManager pm){
		this.pm = pm;
	}
	
	public void processRequest(HttpRequest request, HttpResponseDecorator decorator){
		try {
			Servlet servlet = this.pm.getServlet(this.constructKey(request));
			decorator.setResponse(HttpResponseFactory.createResponse(Protocol.OK_CODE, null, Protocol.CLOSE));
			
			servlet.process(request, decorator);
		} catch (InstantiationException | IllegalAccessException e) {
			decorator.setResponse(HttpResponseFactory.createResponse(Protocol.INTERNAL_SERVER_ERROR, null, Protocol.CLOSE));
		} catch (NullPointerException e){
			decorator.setResponse(HttpResponseFactory.createResponse(Protocol.NOT_FOUND_CODE, null, Protocol.CLOSE));
		}
	}
	
	public String constructKey(HttpRequest request){
		return request.getMethod() + ":" + request.getUri();
	}
}
