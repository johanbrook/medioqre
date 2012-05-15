package tools.datamanagement;

import java.util.prefs.*;

/**
 * Loads User Preferences
 * 
 * @author chrisnordqvist
 * 
 */

public class PreferenceLoader {

	private static Preferences prefs = Preferences.userNodeForPackage(PreferenceLoader.class);

	/**
	 * Gets float value
	 * 
	 * @param key options key
	 * @param def default value
	 * @return stored preference value for the key, if no value is found the default value provided is given.
	 */
	public static float getFloat(String key, float def) {
		return prefs.getFloat(key, def);
	}

	/**
	 * Stores float value in OS standard preference storage
	 * 
	 * @param key options key
	 * @param value the value to store
	 */
	public static void putFloat(String key, float value) {
		prefs.putFloat(key, value);
	}
	
	/**
	 * Gets boolean value
	 * 
	 * @param key options key
	 * @param def default value
	 * @return stored preference value for the key, if no value is found the default value provided is given.
	 */
	public static boolean getBoolean(String key, boolean def) {
		return prefs.getBoolean(key, def);
	}

	/**
	 * Stores boolean value in OS standard preference storage
	 * 
	 * @param key options key
	 * @param value the value to store
	 */
	public static void putBoolean(String key, boolean value) {
		prefs.putBoolean(key, value);
	}

}
