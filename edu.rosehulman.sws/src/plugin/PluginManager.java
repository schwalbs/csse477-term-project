package plugin;

import java.util.HashMap;
import java.util.Map;

public class PluginManager {
	private Map<String, Class<? extends Plugin>> pluginMap;
	
	public PluginManager(){
		this.pluginMap  = new HashMap<String, Class<? extends Plugin>>();
	}
	
	public void installPlugin(String pluginRoot, Class<? extends Plugin> toInstall){
		this.pluginMap.put(pluginRoot, toInstall);
	}
	
	public boolean isPluginInstalled(String pluginRoot){
		return this.pluginMap.containsKey(pluginRoot);
	}
	
	public Plugin getPlugin(String key) throws InstantiationException, IllegalAccessException, NullPointerException{
		Plugin plugin = null;
		for(String k : pluginMap.keySet()){
			if(key.matches(k)){
				plugin = pluginMap.get(k).newInstance();
			}
		}
		return plugin;
	}
	
}
