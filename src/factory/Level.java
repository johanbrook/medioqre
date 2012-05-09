package factory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static tools.Logger.*;
import core.JSONSerializable;
import datamanagement.ResourceLoader;

/**
 * A class used to package all resources used in one level.
 * 
 * @author John Barbero Unenge and Johan Brook
 *
 */
public class Level implements JSONSerializable {
	
	// Default paths
	
	private String	defaultConfigPath;
	
	// View part
	// .act is an abbreviation for actor data
	private String[]	actorsData		= { "spritesheets/json/frank.act",
											"spritesheets/json/walker1.act", "spritesheets/json/gameitems.act",
											"spritesheets/json/machinegun_projectile.act",
											"spritesheets/json/portal.act" };
	
	private String tileMapData = "spritesheets/levels/level1.lvl";
	
	private JSONObject config;
	private Map<String, String> configFiles;
	
	/**
	 * Create a new level with default config file ("gamedata/config.json")
	 */
	public Level() {
		this("gamedata/config.json");
	}

	/**
	 * Create a new level from a path to a given JSON config file
	 * 
	 * @param configPath The path to the JSON resource
	 */
	public Level(String configPath) {
		this.defaultConfigPath = configPath;
		this.config = new JSONObject();
		this.configFiles = new HashMap<String, String>();
		
		JSONObject configFile = ResourceLoader.parseJSONFromPath(this.defaultConfigPath);
		parseNestedConfigFiles(configFile);
	}
	
	
	private void parseNestedConfigFiles(JSONObject configFile) {
		if(configFile == null) {
			throw new IllegalArgumentException("Input config file can't be null");
		}
		
		Iterator<String> it = configFile.keys();
		while(it.hasNext()) {
			String configKey = it.next();
				
				try {
					Object configValue;
					
					if(configKey.endsWith("Config") || configKey.endsWith("config")) {
					
						this.configFiles.put(configKey, configFile.getString(configKey));
						configValue = ResourceLoader.parseJSONFromPath(configFile.getString(configKey));
						log("Loading "+configKey+ " file from "+configFile.get(configKey)+" ...");
					}
					else {
						configValue = configFile.get(configKey);
					}
					
					this.config.put(configKey, configValue);
					
				} catch (JSONException e) {
					err("Couldn't parse nested JSON from path "+configKey);
					e.printStackTrace();
				}
		}
		
	}
	
	
	// Getters
	
	/**
	 * Get the file path to the actors data.
	 * 
	 * @return An array of file paths
	 */
	public String[] getActorsData() {
		return this.actorsData;
	}
	
	/**
	 * Get the file path to the tilemap data.
	 * 
	 * @return The file path
	 */
	public String getTileMapData() {
		return this.tileMapData;
	}
	
	/**
	 * Return a map with all the separate config files.
	 * <p>Map:</p>
	 * <pre>{configName => configFilePath}</pre>
	 * 
	 * @return The config files referenced in the main config file
	 */
	public Map<String, String> getConfigMap() {
		return this.configFiles;
	}
	
	
	/**
	 * Return a collection with all separate config files.
	 * 
	 * @return The config file collection
	 */
	public Collection<String> getConfigFiles() {
		return this.configFiles.values();
	}
	
	/**
	 * Return the config.
	 * 
	 * @return A JSONObject with all config keys and values
	 */
	public JSONObject getConfig() {
		return this.config;
	}
	
	/**
	 * Get the path from where the config data is read.
	 * 
	 * @return The path to the main config file on disk
	 */
	public String getConfigPath() {
		return this.defaultConfigPath;
	}


	@Override
	public JSONObject serialize() {
		return this.config;
	}

	@Override
	public void deserialize(JSONObject o) {
		try {
			JSONArray array = o.getJSONArray("actorsData");
			this.actorsData = new String[array.length()];
			for (int i = 0; i < this.actorsData.length; i++) {
				this.actorsData[i] = array.getString(i);
			}

			this.tileMapData = o.getString("tileMapData");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
