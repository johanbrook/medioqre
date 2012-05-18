package tools.factory;

import graphics.json.JSONSerializable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tools.datamanagement.ResourceLoader;

import static tools.Logger.*;

/**
 * A class used to package all resources used in one level.
 * 
 * @author John Barbero Unenge and Johan Brook
 * 
 */
public class Level {
	
	// Default paths
	
	private String	defaultConfigPath;	
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
	 * @param configPath
	 *            The path to the JSON resource
	 */
	public Level(String configPath) {
		this.defaultConfigPath = configPath;
		this.config = new JSONObject();
		this.configFiles = new HashMap<String, String>();

		JSONObject configFile = ResourceLoader.parseJSONFromPath(this.defaultConfigPath);
		this.config = ResourceLoader.parseNestedConfigFiles(configFile);
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
}
