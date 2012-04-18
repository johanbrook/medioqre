package controller;

import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JOptionPane;

import audio.SoundLibrary;

import model.Entity;
import event.Event;
import event.EventBus;
import event.IEventHandler;

import paulscode.sound.SoundSystem;
import paulscode.sound.SoundSystemConfig;
import paulscode.sound.SoundSystemException;
import paulscode.sound.codecs.CodecWav;
import paulscode.sound.libraries.LibraryJavaSound;

/**
 * Audio Engine
 * 
 * @author chrisnordqvist
 * 
 */

public class AudioController implements IEventHandler {

	private static AudioController sharedInstance;
	private static SoundSystem soundSys;

	private int bgmID = 1;

	public static AudioController getInstance() {
		if (sharedInstance == null) {
			sharedInstance = new AudioController();
			EventBus.INSTANCE.register(sharedInstance);

		}
		return sharedInstance;
	}

	private AudioController() {
		// Link to JavaSound
		try {
			SoundSystemConfig.addLibrary(LibraryJavaSound.class);
		} catch (SoundSystemException e) {
		}

		// Link to Wav Codec
		try {
			SoundSystemConfig.setCodec("wav", CodecWav.class);
		} catch (SoundSystemException e) {
		}

		// Initialize Sound Engine
		soundSys = new SoundSystem();

	}

	/*
	 * ____________BGM_____________
	 */

	public void playBGM() {

		String filename = SoundLibrary.getBackgroundMusic(bgmID);
		System.out.println(filename);

		URL url;
		try {
			url = new URL("file:///" + SoundSystemConfig.getSoundFilesPackage()
					+ filename);

			soundSys.backgroundMusic("Background Music", url, filename, true);

			JOptionPane.showMessageDialog(null, url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

	}

	public void nextBGM() {

		if (bgmID == SoundLibrary.getBGMLibrarySize()) {
			bgmID = 1;
		} else {
			bgmID++;
		}

		this.playBGM();

	}

	public void decrementPitchBGM(float decrementBy) {

		float newValue = soundSys.getPitch("Background Music") - decrementBy;
		soundSys.setPitch("Background Music", newValue);

		System.out.println("Pitch decreased by: " + decrementBy);
	}

	public void incrementPitchBGM(float incrementBy) {

		float newValue = soundSys.getPitch("Background Music") + incrementBy;
		soundSys.setPitch("Background Music", newValue);

		System.out.println("Pitch increased by: " + incrementBy);

	}

	public void setPitchBGM(float newValue) {

		soundSys.setPitch("Background Music", newValue);
		System.out.println("Pitch set to: " + newValue);

	}

	public void restorePitchBGM() {
		soundSys.setPitch("Background Music", (float) 1.0);
	}

	/*
	 * ____________SFX_____________
	 */

	public void playSoundFX(String code, boolean toLoop) {

		soundSys.newSource(false, code, SoundLibrary.getFXSound(code), toLoop,
				0, 1, 1, 1, 1);

		soundSys.play(code);

		// soundSys.quickPlay(true, SoundLibrary.getWeaponSound(code), toLoop,
		// 0,
		// 1, 1, 1, 2);

	}

	public void stopFX(String code) {
		soundSys.stop(code);
	}

	/*
	 * Clear audio buffer and turn off audio engine MUST BE DONE BEFORE GAME
	 * SHUTDOWN!
	 */
	public void shutdown() {
		soundSys.cleanup();
	}

	public void reInit() {
		shutdown();
		soundSys = new SoundSystem();
	}

	public void update() {

	}

	public void playGunSound(Class input){
		soundSys.quickPlay(true, SoundLibrary.getWeaponSound(input), false, 1, 1, 1, 1, 1);
	}
	
	
	@Override
	public void onEvent(Event evt) {

		// Initialize

		// Entities
		if (evt.getValue() instanceof Entity) {
			Entity p = (Entity) evt.getValue();

			if (p instanceof model.character.Player) {

				if (evt.getProperty() == Event.Property.DID_MOVE) {
					if (!soundSys.playing("walk"))
						playSoundFX("walk", true);
				}

				if (evt.getProperty() == Event.Property.DID_STOP) {
					stopFX("walk");
				}
			}
		}

		// Weapons
		if (evt.getValue() instanceof model.weapon.AbstractWeapon){
			playGunSound(evt.getValue().getClass());
			
		}
		
		
		// FX

	}

}