package audio;

import java.util.prefs.*;

/**
 * Constants used by AudioController
 * 
 * 
 * @author chrisnordqvist
 * 
 */

public class AudioConstants {

	public static final float zROLLOFF = 100;

	private static float BGM_Volume = 0.7f;
	private static float FX_VOLUME = 0.8f;

	public static void setBGMVolume(float f) {

	}

	public static void setFXVolume(float f) {

	}
	
	public static float getBGMVolume() {
		return BGM_Volume;
	}

	public static float getFXVolume() {
		return FX_VOLUME;
	}


}
