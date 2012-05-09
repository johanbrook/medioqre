package datamanagement;

import java.util.prefs.*;

/**
 * Loads User Preferences
 * 
 * @author chrisnordqvist
 *
 */

public class PreferenceLoader
{
	private static PreferenceLoader instance;
	private Preferences prefs;
	
	private PreferenceLoader(){
	        prefs = Preferences.userNodeForPackage(PreferenceLoader.class);
	};
	
	public static PreferenceLoader getInstance(){
		if(instance == null)
			instance = new PreferenceLoader();
		
		return instance;
	}
	
	public float getFloat(String key, float def){
		return prefs.getFloat(key, def);
	}
	
	public void putFloat(String key, float value){
		prefs.putFloat(key, value);
	}
	
	
}
