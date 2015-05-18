package edu.rosehulman.extension;

import java.util.HashMap;

import plugin.Plugin;
import servlet.Servlet;

public class DataPlugin implements Plugin {
	public static final String NAME = "My Plugin";
	protected HashMap<String, Class<? extends Servlet>> servletMap;

	public DataPlugin(){
		initMap();
	}
	
	private void initMap(){
		this.servletMap = new HashMap<String, Class<? extends Servlet>>();
		
		String key = generateKey(GetDataHandler.METHOD, GetDataHandler.RELATIVE_URL);
		this.servletMap.put(key, GetDataHandler.class);
		
		key = generateKey(PostDataHandler.METHOD, PostDataHandler.RELATIVE_URL);
		this.servletMap.put(key, PostDataHandler.class);
		
		key = generateKey(PutDataHandler.METHOD, PutDataHandler.RELATIVE_URL);
		this.servletMap.put(key, PutDataHandler.class);
		
		key = generateKey(DeleteDataHandler.METHOD, DeleteDataHandler.RELATIVE_URL);
		this.servletMap.put(key, DeleteDataHandler.class);
	}
	
	@Override
	public Servlet getServlet(String method, String relativeUrl) throws ServletNotFoundException{
		String key = generateKey(method, relativeUrl);
		
		if(this.servletMap.containsKey(key)){
			try {
				return this.servletMap.get(key).newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
				return null;
			}
		} else {
			throw new ServletNotFoundException("Key " + key + " not valid in plugin " + NAME);
		}
	}
	
	private String generateKey(String method, String relativeUrl){
		return method + ":" + relativeUrl;
	}
}
