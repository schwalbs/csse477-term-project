/*
 * PluginManager.java
 * May 1, 2015
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
 
package plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import servlet.Servlet;

/**
 * 
 * @author Chandan R. Rupakheti (rupakhcr@clarkson.edu)
 */
public class PluginManager {
	//key is combination of 
	private Map<String, Class<? extends Servlet>> servletMap;
	private ArrayList<String> installedPlugins;
	
	public PluginManager(){
		servletMap  = new HashMap<String, Class<? extends Servlet>>();
		installedPlugins = new ArrayList<String>();
	}
	
	public void installPlugin(String pluginName, Map<String, Class<? extends Servlet>> map){
		if(!installedPlugins.contains(pluginName))
			installedPlugins.add(pluginName);
		servletMap.putAll(map);
	}
	
	public boolean isPluginInstalled(String pluginName){
		return installedPlugins.contains(pluginName);
	}
	
	public Servlet getServlet(String key) throws InstantiationException, IllegalAccessException, NullPointerException{
			return servletMap.get(key).newInstance();
	}	
}
