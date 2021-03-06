/*
 * HttpResponse400.java
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
 
package protocol.response;

import java.io.File;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.Date;

import protocol.Protocol;

/**
 * 
 * @author Chandan R. Rupakheti (rupakhcr@clarkson.edu)
 */
public class HttpResponse200 extends HttpResponse {

	/**
	 * @param file
	 * @param connection
	 */
	public HttpResponse200(File file, String connection) {
		super(file, connection);
		this.status = Protocol.OK_CODE;
		this.phrase = Protocol.OK_TEXT;
		init();
	}
	
	private void init(){
		// Lets add last modified date for the file
		if(this.getFile() != null){
			long timeSinceEpoch = this.getFile().lastModified();
			Date modifiedTime = new Date(timeSinceEpoch);
			this.put(Protocol.LAST_MODIFIED, modifiedTime.toString());
			
			// Lets get content length in bytes
			long length =  this.getFile().length();
			this.put(Protocol.CONTENT_LENGTH, length + "");
			
			// Lets get MIME type for the file
			FileNameMap fileNameMap = URLConnection.getFileNameMap();
			String mime = fileNameMap.getContentTypeFor( this.getFile().getName());
			// The fileNameMap cannot find mime type for all of the documents, e.g. doc, odt, etc.
			// So we will not add this field if we cannot figure out what a mime type is for the file.
			// Let browser do this job by itself.
			if(mime != null) { 
				this.put(Protocol.CONTENT_TYPE, mime);
			}
		}
	}
}
