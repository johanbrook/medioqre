package audio;

import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import model.weapon.AbstractWeapon;

import event.Event;
import event.Event.Property;

/*
 * Sound library for Frank the Tank
 * 
 * @author Chris
 * 
 */

public class SoundLibrary {

	public static void main(String[] mamma){		
		System.out.println(c.getResource("sounds/fx/walk.wav").toString().substring(5));
	}
	
	
	private final Map<String, String> fxSoundLibrary = initializeFXSoundLibrary();
	private static final Map<Integer, String> backgroundMusicLibrary = initializeBackgroundMusicLibrary();
	private static final Map<Class<?>, String> weaponSoundLibrary = initializeWeaponSoundLibrary();
	private static ClassLoader c = SoundLibrary.class.getClassLoader();
	
	
	
	// FX Sound Library
	private Map<String, String> initializeFXSoundLibrary() {
		Map<String, String> fx = new HashMap<String, String>();

		fx.put("walk", c.getResource("sounds/fx/walk.wav").toString().substring(5));
		fx.put("startUpSound", c.getResource("sounds/fx/walk.wav").toString().substring(5));
//		fx.put("zombieWalk", "http://theboxofficial.com/work/move.wav");
			
			
		return Collections.unmodifiableMap(fx);
	}

	// Weapon Sound Library
	private static Map<Class<?>, String> initializeWeaponSoundLibrary() {
		Map<Class<?>, String> weapon = new HashMap<Class<?>, String>();
		
		weapon.put(model.weapon.MachineGun.class, "http://theboxofficial.com/work/gun.wav");
		weapon.put(model.weapon.Grenade.class, "Grenade.... BOOOOM");
		weapon.put(model.weapon.Sword.class, "Swordfight!");
		weapon.put(model.weapon.PortalGun.class, "Portal fires!");
		weapon.put(model.weapon.Melee.class, "Zombie Melee");
		
		
		
		return Collections.unmodifiableMap(weapon);
	}

	// Background Music Library
	private static Map<Integer, String> initializeBackgroundMusicLibrary() {
		Map<Integer, String> bgm = new HashMap<Integer, String>();

		bgm.put(1, "http://theboxofficial.com/work/frankTheTank.wav");
		bgm.put(2, "http://theboxofficial.com/work/1bar.wav");
		bgm.put(3, "bgm.wav");
		bgm.put(4, "http://theboxofficial.com/work/half.wav");

		return Collections.unmodifiableMap(bgm);
	}

	
	
	/*
	 * Returns the file path for any given sound in the library as a String. If
	 * sound code is invalid, null is given back.
	 */

	public static String getWeaponSound(Class code) {

		return weaponSoundLibrary.get(code);

	}

	public static String getBackgroundMusic(int id) {
		return backgroundMusicLibrary.get(id);

	}
	
	public static int getBGMLibrarySize(){
		return backgroundMusicLibrary.size();
	}

	public String getFXSound(String code) {
		String m = c.getResource("sounds/fx/" + code + ".wav").toString().substring(5);
		
		return m;
	}
	
	
	
	

}
