package audio;

import java.util.prefs.*;

import datamanagement.PreferenceLoader;

import sun.tools.tree.ThisExpression;

/**
 * Constants used by AudioController
 * 
 * 
 * @author chrisnordqvist
 * 
 */

public class AudioConstants {

	private static PreferenceLoader prefs = PreferenceLoader.getInstance();
	public static final float zROLLOFF = 100;

	// private static Preferences prefs = Preferences.userRoot().node("");

	public static void setBGMVolume(float f) {
		prefs.putFloat("BGM_VOLUME", f);
	}

	public static void setFXVolume(float f) {
		prefs.putFloat("FX_VOLUME", f);
	}

	public static float getBGMVolume() {
		return prefs.getFloat("BGM_VOLUME", 0.8f);
	}

	public static float getFXVolume() {
		return prefs.getFloat("FX_VOLUME", 0.8f);
	}

}
