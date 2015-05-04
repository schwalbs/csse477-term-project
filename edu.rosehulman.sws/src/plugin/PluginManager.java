package plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import servlet.Servlet;

public class PluginManager {
	//key is combination of method:url
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
