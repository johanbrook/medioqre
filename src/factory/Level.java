package factory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import static tools.Logger.*;
import core.JSONSerializable;
import datamanagement.ResourceLoader;

public class Level implements JSONSerializable {
	
	// Default paths
	
	private String	defaultConfigPath;
	private String	enemyData		= "gamedata/enemy.json";
	private String	itemData		= "gamedata/items.json";
	private String	playerData		= "gamedata/player.json";
	private String	weaponData		= "gamedata/weapons.json";
	
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
					}
					else {
						configValue = configFile.get(configKey);
					}
					
					this.config.put(configKey, configValue);
					log("Loading "+configKey+ " file from "+configFile.getString(configKey)+" ...");
					
				} catch (JSONException e) {
					err("Couldn't parse nested JSON from path "+configKey);
					e.printStackTrace();
				}
		}
		
	}
	
	
	// Getters
	
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
			this.enemyData = o.getString("enemyConfig");
			this.itemData = o.getString("itemConfig");
			this.playerData = o.getString("playerConfig");
			this.weaponData = o.getString("weaponConfig");

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
