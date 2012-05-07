package audio;

import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/*
 * Sound library for Frank the Tank
 * 
 * @author Chris
 * 
 */

public class SoundLibrary {

	// public static void main(String[] mamma){
	// System.out.println(c.getResource("sounds/fx/walk.wav").toString().substring(5));
	// }
	//

	private final Map<String, URL> fxSoundLibrary = initializeFXSoundLibrary();
	private final Map<Class<?>, URL> weaponSoundLibrary = initializeWeaponSoundLibrary();
	private final Map<Integer, URL> bgmLibrary = initializeBGMLibrary();
	private static ClassLoader c = SoundLibrary.class.getClassLoader();

	// FX Sound Library
	private Map<String, URL> initializeFXSoundLibrary() {
		Map<String, URL> fx = new HashMap<String, URL>();

		fx.put("walk", c.getResource("sounds/fx/walk.wav"));
		fx.put("startUpSound", c.getResource("sounds/fx/walk.wav"));

		return Collections.unmodifiableMap(fx);
	}

	// Weapon Sound Library
	private Map<Class<?>, URL> initializeWeaponSoundLibrary() {
		Map<Class<?>, URL> fx = new HashMap<Class<?>, URL>();

		fx.put(model.weapon.MachineGun.class,
				c.getResource("sounds/weapon/machineGun.wav"));

		return Collections.unmodifiableMap(fx);
	}

	// BGM Library
	private Map<Integer, URL> initializeBGMLibrary() {
		Map<Integer, URL> bgml = new HashMap<Integer, URL>();

		bgml.put(1, c.getResource("sounds/bgm/frankTheTank.wav"));
		bgml.put(2, c.getResource("sounds/fx/walk.wav"));

		return Collections.unmodifiableMap(bgml);
	}

	public URL getFXSound(String code) {
		return fxSoundLibrary.get(code);
	}

	public URL getWeaponSound(Class<?> type) {
		return weaponSoundLibrary.get(type);
	}

	public String getWeaponId(Class<?> type) {
		return weaponSoundLibrary.get(type).toString().substring(13);
	}

	public URL getBGMURL(Integer code) {
		return bgmLibrary.get(code);
	}

	public String getBGMIdentifyer(Integer code) {
		return bgmLibrary.get(code).toString().substring(14);
	}

	public URL getStartUpSound() {
		return fxSoundLibrary.get("startUpSound");
	}

}
