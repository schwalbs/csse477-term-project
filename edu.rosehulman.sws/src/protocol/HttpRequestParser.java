/*
 * HttpRequestParser.java
 * Apr 26, 2015
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
 
package protocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * 
 * @author Chandan R. Rupakheti (rupakhcr@clarkson.edu)
 */
public class HttpRequestParser {

	/**
	 * Reads raw data from the supplied input stream and constructs a 
	 * <tt>HttpRequest</tt> object out of the raw data.
	 * 
	 * @param inputStream The input stream to read from.
	 * @return A <tt>HttpRequest</tt> object.
	 * @throws Exception Throws either {@link ProtocolException} for bad request or 
	 * {@link IOException} for socket input stream read errors.
	 */
	public static HttpRequest read(InputStream inputStream) throws Exception {
		// We will fill this object with the data from input stream and return it
		HttpRequest request = new HttpRequest();
		
		InputStreamReader inStreamReader = new InputStreamReader(inputStream);
		BufferedReader reader = new BufferedReader(inStreamReader);
		
		//First Request Line: GET /somedir/page.html HTTP/1.1
		String line = reader.readLine(); // A line ends with either a \r, or a \n, or both
		
		if(line == null) {
			throw new ProtocolException(Protocol.BAD_REQUEST_CODE, Protocol.BAD_REQUEST_TEXT);
		}
		
		// We will break this line using space as delimeter into three parts
		StringTokenizer tokenizer = new StringTokenizer(line, " ");
		
		// Error checking the first line must have exactly three elements
		if(tokenizer.countTokens() != 3) {
			throw new ProtocolException(Protocol.BAD_REQUEST_CODE, Protocol.BAD_REQUEST_TEXT);
		}
		
		request.method = tokenizer.nextToken();		// GET
		request.uri = tokenizer.nextToken();		// /somedir/page.html
		request.version = tokenizer.nextToken();	// HTTP/1.1
		
		// Rest of the request is a header that maps keys to values
		// e.g. Host: www.rose-hulman.edu
		// We will convert both the strings to lower case to be able to search later
		line = reader.readLine().trim();
		
		while(!line.equals("")) {
			// THIS IS A PATCH 
			// Instead of a string tokenizer, we are using string split
			// Lets break the line into two part with first space as a separator 
			
			// First lets trim the line to remove escape characters
			line = line.trim();
			
			// Now, get index of the first occurrence of space
			int index = line.indexOf(' ');
			
			if(index > 0 && index < line.length()-1) {
				// Now lets break the string in two parts
				String key = line.substring(0, index); // Get first part, e.g. "Host:"
				String value = line.substring(index+1); // Get the rest, e.g. "www.rose-hulman.edu"
				
				// Lets strip off the white spaces from key if any and change it to lower case
				key = key.trim().toLowerCase();
				
				// Lets also remove ":" from the key
				key = key.substring(0, key.length() - 1);
				
				// Lets strip white spaces if any from value as well
				value = value.trim();
				
				// Now lets put the key=>value mapping to the header map
				request.header.put(key, value);
			}
			
			// Processed one more line, now lets read another header line and loop
			line = reader.readLine().trim();
		}
		
		int contentLength = 0;
		try {
			contentLength = Integer.parseInt(request.header.get(Protocol.CONTENT_LENGTH.toLowerCase()));
		}
		catch(Exception e){}
		
		if(contentLength > 0) {
			request.body = new char[contentLength];
			reader.read(request.body);
		}
		
		return request;
	}

}
