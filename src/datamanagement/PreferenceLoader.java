package datamanagement;

import java.util.prefs.*;

/**
 * Loads User Preferences
 * 
 * @author chrisnordqvist
 * 
 */

public class PreferenceLoader {
//	private static PreferenceLoader instance;
	private static Preferences prefs = Preferences.userNodeForPackage(PreferenceLoader.class);;

//	private PreferenceLoader() {
//		prefs = Preferences.userNodeForPackage(PreferenceLoader.class);
//	};
//
//	public static PreferenceLoader getInstance() {
//		if (instance == null)
//			instance = new PreferenceLoader();
//
//		return instance;
//	}

	public static float getFloat(String key, float def) {
		return prefs.getFloat(key, def);
	}

	public static void putFloat(String key, float value) {
		prefs.putFloat(key, value);
	}
	
	public static boolean getBoolean(String key, boolean def) {
		return prefs.getBoolean(key, def);
	}

	public static void putBoolean(String key, boolean value) {
		prefs.putBoolean(key, value);
	}

}
