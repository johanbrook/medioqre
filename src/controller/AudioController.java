package controller;

import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JOptionPane;

import audio.AudioConstants;
import audio.SoundLibrary;

import model.Entity;
import model.IGameModel;
import model.character.Enemy;
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
	private IGameModel game;

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
		// soundSys.setListenerOrientation(1, 1, 0, 1, 1, 1);

	}

	public void setGame(IGameModel game) {
		this.game = game;
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

		soundSys.setListenerPosition(game.getPlayer().getPosition().x, game
				.getPlayer().getPosition().y, AudioConstants.zROLLOFF);

		soundSys.setPosition("walk", game.getPlayer().getPosition().x, game
				.getPlayer().getPosition().y, 1);

		for (Enemy e : game.getEnemies()) {
			soundSys.setPosition(e.hashCode() + "", e.getPosition().x,
					e.getPosition().y, 1);

		}

	}

	public void playWeaponSound(Class<?> input) {
		soundSys.quickPlay(true, SoundLibrary.getWeaponSound(input), false, 1,
				1, 1, 1, 0);
	}

	@Override
	public void onEvent(Event evt) {

		// Initialize
		if (evt.getProperty() == Event.Property.INIT_MODEL
				&& !soundSys.playing("Background Music")) {

		}

		// Entities
		if (evt.getValue() instanceof Entity) {
			Entity p = (Entity) evt.getValue();

			// Player
			if (p instanceof model.character.Player) {

				// Player Walking
				if (evt.getProperty() == Event.Property.DID_MOVE
						&& !soundSys.playing("walk")) {
					playSoundFX("walk", true);

				}

				if (evt.getProperty() == Event.Property.DID_STOP) {
					stopFX("walk");
				}

				// Pickup Items
				if (evt.getProperty() == Event.Property.PICKED_UP_ITEM) {
					// TODO Pickupljud!
				}

			}

			// Enemies
			if (p instanceof model.character.Enemy) {
				Enemy e = (Enemy) evt.getValue();

				if (evt.getProperty() == Event.Property.DID_MOVE
						&& !soundSys.playing(e.hashCode() + "")) {
					playZombiewalk(e);
				}

				if (evt.getProperty() == Event.Property.WAS_DESTROYED) {
					removeZombieSounds(e);
				}

			}

		}

		// Weapons
		if (evt.getValue() instanceof model.weapon.AbstractWeapon) {
			playWeaponSound(evt.getValue().getClass());

		}

		// FX

	}

	private void playZombiewalk(Enemy e) {

		soundSys.newSource(true, e.hashCode() + "",
				SoundLibrary.getFXSound("walk"), true, 1, 1, 1,
				SoundSystemConfig.ATTENUATION_ROLLOFF, 0.5f);

		soundSys.play(e.hashCode() + "");

	}

	private void removeZombieSounds(Enemy e) {
		soundSys.removeSource(e.hashCode() + "");
	}

}