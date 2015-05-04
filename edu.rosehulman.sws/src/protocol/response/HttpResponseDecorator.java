/*
 * HttpResponseDecorator.java
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

package protocol.response;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import protocol.Protocol;

/**
 * 
 * @author Chandan R. Rupakheti (rupakhcr@clarkson.edu)
 */
public class HttpResponseDecorator extends HttpResponse {

	private HttpResponse response;
	private BufferedOutputStream out;
	private OutputStream outStream;
	private boolean hasHeader;

	/**
	 * @param file
	 * @param connection
	 */
	public HttpResponseDecorator(File file, String connection) {
		super(file, connection);
		this.hasHeader = false;
	}

	public void setResponse(HttpResponse response) {
		this.hasHeader = false;
		this.out = new BufferedOutputStream(this.outStream, Protocol.CHUNK_LENGTH);
		this.response = response;
	}

	public void setOutputStream(OutputStream outStream) {
		this.outStream = outStream;
		this.out = new BufferedOutputStream(outStream, Protocol.CHUNK_LENGTH);
	}

	public void write(String content) {
		try {
			if (!hasHeader) {
				this.writeHeader();
			}
			out.write(content.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void writeHeader() throws IOException{
		// First status line
		String line = response.getVersion() + Protocol.SPACE
				+ response.getStatus() + Protocol.SPACE
				+ response.getPhrase() + Protocol.CRLF;

		out.write(line.getBytes());

		// Write header fields if there is something to write in header
		// field
		if (response.getHeader() != null
				&& !response.getHeader().isEmpty()) {
			for (Map.Entry<String, String> entry : response.getHeader()
					.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();

				// Write each header field line
				line = key + Protocol.SEPERATOR + Protocol.SPACE
						+ value + Protocol.CRLF;
				out.write(line.getBytes());
			}
		}

		// Write a blank line
		out.write(Protocol.CRLF.getBytes());
		hasHeader = true;
	}
	
	public void writeError(){
		try {
			this.writeHeader();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.flush();
	}
	
	
	public void flush(){
		try {
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
