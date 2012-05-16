package audio;

import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Sound library for Frank the Tank
 * 
 * @author chrisnordqvist
 * 
 */

public class SoundLibrary {

	private final Map<String, URL> fxSoundLibrary = initializeFXSoundLibrary();
	private final Map<Class<?>, URL> weaponSoundLibrary = initializeWeaponSoundLibrary();
	private final Map<Integer, URL> bgmLibrary = initializeBGMLibrary();
	private static ClassLoader c = SoundLibrary.class.getClassLoader();

	/**
	 * Initializes FX Sound Library
	 * 
	 * @return fxSoundLibrary
	 */
	private Map<String, URL> initializeFXSoundLibrary() {
		Map<String, URL> fx = new HashMap<String, URL>();

		fx.put("walk", c.getResource("sounds/fx/walk.wav"));
		fx.put("startUpSound", c.getResource("sounds/fx/walk.wav"));

		return Collections.unmodifiableMap(fx);
	}

	/**
	 * Initializes Weapon Sound Library
	 * 
	 * @return weaponSoundLibrary
	 */
	private Map<Class<?>, URL> initializeWeaponSoundLibrary() {
		Map<Class<?>, URL> weapon = new HashMap<Class<?>, URL>();

		weapon.put(model.weapon.MachineGun.class,
				c.getResource("sounds/weapon/machineGun.wav"));
		weapon.put(model.weapon.Grenade.class,
				c.getResource("sounds/weapon/grenade.wav"));
		weapon.put(model.weapon.Melee.class,
				c.getResource("sounds/weapon/sword.wav"));
		weapon.put(model.weapon.PortalGun.class,
				c.getResource("sounds/weapon/portalGun.wav"));

		return Collections.unmodifiableMap(weapon);
	}

	/**
	 * Initializes Background Music Library
	 * 
	 * @return bgmLibrary
	 */
	private Map<Integer, URL> initializeBGMLibrary() {
		Map<Integer, URL> bgml = new HashMap<Integer, URL>();

		bgml.put(1, c.getResource("sounds/bgm/launcher.ogg"));
		bgml.put(2, c.getResource("sounds/bgm/main.ogg"));

		return Collections.unmodifiableMap(bgml);
	}

	/**
	 * Gets URL for a given FX Sound
	 * 
	 * @param code
	 *            library identifier for sound
	 * @return URL with path to sound
	 */
	public URL getFXSound(String code) {
		return fxSoundLibrary.get(code);
	}

	/**
	 * Gets URL for a given Weapon Sound
	 * 
	 * @param type
	 *            weapon class
	 * @return URL with path to sound
	 */
	public URL getWeaponSound(Class<?> type) {
		return weaponSoundLibrary.get(type);
	}

	/**
	 * Gets filename identifier for Weapon Sound
	 * 
	 * @param type
	 *            weapon class
	 * @return String with filename
	 */
	public String getWeaponId(Class<?> type) {
		return weaponSoundLibrary.get(type).toString().substring(13);
	}

	/**
	 * Gets URL for Background Music
	 * 
	 * @param code
	 *            BGM id
	 * @return URL with path to sound
	 */
	public URL getBGMURL(Integer code) {
		return bgmLibrary.get(code);
	}

	/**
	 * Gets filename identifier for Background music
	 * 
	 * @param code
	 *            BGM id
	 * @return String with filename
	 */
	public String getBGMId(Integer code) {
		return bgmLibrary.get(code).toString().substring(14);
	}

	/**
	 * Gets startup sound for launcher
	 * 
	 * @return URL to Startup Sound
	 */
	public URL getStartUpSound() {
		return bgmLibrary.get(1);
	}

}
