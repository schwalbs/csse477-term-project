package plugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import servlet.Servlet;

public class PluginLoader  implements Runnable{
		public static final int POLL_INTERVAL = 5000;
		public static final File PLUGIN_DIR = new File("plugins");
		public static final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yy HH:mm:SS ");
		public boolean stop = false;
		
		private PluginManager pluginManager;
		private long lastChecked = 0;
		
		public PluginLoader(PluginManager pluginManager){
			this.pluginManager = pluginManager;
		}
		
		public void stop(){
			stop = true;
		}
		
		@SuppressWarnings("unchecked")
		private void load(){	
			if(PLUGIN_DIR.exists() && PLUGIN_DIR.isDirectory()){
				//go to all the plugin directories
				for(File pluginDir :  PLUGIN_DIR.listFiles()){				
					//make sure the plugin directory is actually a directory
					if(!pluginDir.isDirectory()){
						System.out.println("ERROR " + pluginDir.getAbsolutePath() + " isn't a directory");
						continue;
					}
					
					//make sure config file exists
					File configFile = new File(pluginDir.getPath() + "/config.json");
					String pluginName = pluginDir.getName();
					if(!configFile.exists()){
						System.out.println("ERROR config.json doesn't exist for plugin " + pluginName);
						continue;
					}
					
					//load the input stream of the config file
					InputStream stream;
					try {
						stream = new FileInputStream(configFile);
					} catch (FileNotFoundException e) {
						System.out.println("ERROR reading config.json for plugin " + pluginName);
						continue;
					}
					
					//start going through and finding jars/ classes
					JSONArray classes = (JSONArray) (new JSONTokener(stream)).nextValue();

					//get the jar file and make sure it exists
					File jarFile = new File(pluginDir.getPath() + "/" + pluginName + ".jar");
					if(!jarFile.exists()){
						System.out.println("ERROR " + pluginName + " does not exist");
						continue;
					}
					
					//attempt to close stream
					try{
						stream.close();
					} catch (IOException e){
						System.out.println("ERROR closing config.json stream");
						continue;
					}
					
					//check if it's been updated
					if(pluginManager.isPluginInstalled(pluginName) && jarFile.lastModified() <= lastChecked){
						System.out.println(pluginName + " already installed");
						continue;
					}
					
					//create the class loader linked to the jar
					URL[] jarURL;
					try {
						jarURL = new URL[] {jarFile.toURI().toURL()};
					} catch (MalformedURLException e1) {
						//report error creating it
						System.out.println("ERROR reading jar file " + jarFile.getName() + " in plugin " + pluginName);
						continue;
					}
					URLClassLoader jarClassLoader = new URLClassLoader(jarURL);

					Map<String, Class<? extends Servlet>> map = new HashMap<String, Class<? extends Servlet>>();
					for(int i=0; i<classes.length(); i++){
						try{
							//get the full class name (package and class name)
							JSONObject clazz = classes.getJSONObject(i);
							String classname = clazz.getString("classname");
							try {
								//load class
								Class<?> loaded = jarClassLoader.loadClass(classname);
								
								//make sure it's a servlet implementation and it has the correct annotations
								if(loaded.newInstance() instanceof Servlet){
									if(!loaded.isAnnotationPresent(Servlet.ServletDetails.class)){
										System.out.println("ERROR " + loaded.getName() + " implements Servlet but doesn't have ServletDetails annotations");
									} else {
										Servlet.ServletDetails servletDetails = (Servlet.ServletDetails) loaded.getAnnotation(Servlet.ServletDetails.class);
										
										//add the patterns to the map with this class
										for(String urlPattern : servletDetails.urlPatterns()){
											map.put(servletDetails.method() + ":" + urlPattern, (Class<? extends Servlet>) loaded);											
										}
									}
								}
							} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
								//Came accross an error, just continue
								System.out.println("ERROR loading class " + classname + " for plugin " + pluginName);
								continue;
							}							
						} catch (JSONException e){
							//issue reading config file
							e.printStackTrace();
							System.out.println("ERROR in config.json formatting");
							continue;
						} 
					}
					try {
						//try and close class loader
						jarClassLoader.close();
						
						//install plugin on the pluginmanager
						pluginManager.installPlugin(pluginName, map);
						System.out.println("Successfully installed plugin " + pluginName);
					} catch (IOException e) {
						System.out.println("ERROR closing class loader on jar file " + jarFile.getName() + " in plugin " + pluginName);
					} catch (Exception e){
						System.out.println("ERROR installing plugin " + pluginName);
					}
					
				}
			}
		}
		
		@Override
		public void run() {
			stop = false;
			while(!stop){			
				try {
					System.out.println(formatter.format(new Date()));
					load();
					lastChecked = (new Date()).getTime();
					Thread.sleep(POLL_INTERVAL);
					System.out.println();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}			
	
}
