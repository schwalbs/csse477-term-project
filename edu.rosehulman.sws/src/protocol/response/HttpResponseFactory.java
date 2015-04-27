/*
 * HttpResponseFactory.java
 * Oct 7, 2012
 *
 * Simple Web Server (SWS) for CSSE 477
 * 
 * Copyright (C) 2012 Chandan Raj Rupakheti
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
 */
 
package protocol.response;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * This is a factory to produce various kind of HTTP responses.
 * 
 * @author Chandan R. Rupakheti (rupakhet@rose-hulman.edu)
 */
public class HttpResponseFactory {
	private static Map<Integer, Class<? extends HttpResponse>> ResponseTypes = new HashMap<Integer,  Class<? extends HttpResponse>>();
	
	public static void addResponseType(int type,  Class<? extends HttpResponse> classType){
		ResponseTypes.put(type, classType);
	}
	
	public static HttpResponse createResponse(int code, File file, String connection){
		try {
			return ResponseTypes.get(code).getDeclaredConstructor(File.class, String.class).newInstance(file, connection);
		} catch (Exception e) {
			e.printStackTrace();
			return new HttpResponse400(connection);
		}
	}	
}
