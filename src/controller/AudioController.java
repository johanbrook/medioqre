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
	private static SoundLibrary lib = new SoundLibrary();

	private static float BGM_VOLUME = 1;
	private static float FX_VOLUME = 1;

	private int bgmID = 1;

	public static AudioController getInstance() {
		if (sharedInstance == null) {
			sharedInstance = new AudioController();
			EventBus.INSTANCE.register(sharedInstance);

		}
		return sharedInstance;
	}

	private AudioController() {

		// Link to system sound
		try {

			SoundSystemConfig.addLibrary(LibraryJavaSound.class);

			// Link to Wav Codec
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
	 * ____________SFX_____________
	 */

	public void playerWalk() {
		soundSys.newSource(false, "playerWalk", lib.getFXSound("walk"), "walk.wav" , false,
				1f,1f, 1.0f,
				SoundSystemConfig.ATTENUATION_NONE, 0.5f);
		
		soundSys.play("playerWalk");
	}

	public void stopPlayerWalk() {
		soundSys.stop("playerWalk");
	}

	/*
	 * Clear audio buffer and turn off audio engine MUST BE DONE BEFORE GAME
	 * SHUTDOWN!
	 */
	public void shutdown() {
		soundSys.cleanup();
	}

	public void update() {

		soundSys.setListenerPosition(game.getPlayer().getPosition().x, game
				.getPlayer().getPosition().y, AudioConstants.zROLLOFF);

		soundSys.setPosition("walk", game.getPlayer().getPosition().x, game
				.getPlayer().getPosition().y, 1);

		for (Enemy e : game.getEnemies()) {
			soundSys.setPosition(soundCode(e), e.getPosition().x,
					e.getPosition().y, 1);

		}

	}

	public void playWeaponSound(Class<?> input) {

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
						&& !soundSys.playing("playerWalk")) {
					playerWalk();

				}

				if (evt.getProperty() == Event.Property.DID_STOP) {
					stopPlayerWalk();
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
						&& !soundSys.playing(soundCode(e))) {
					playZombiewalk(e);
				}

				if (evt.getProperty() == Event.Property.WAS_DESTROYED) {
					removeZombieSounds(e);
				}

				if (evt.getProperty() == Event.Property.DID_STOP) {
					stopZombiewalk(e);
				}
			}

		}

		// Weapons
		if (evt.getValue() instanceof model.weapon.AbstractWeapon) {

		}

		// FX

	}

	private void playZombiewalk(Enemy e) {

		// soundSys.newSource(true, soundCode(e),
		// lib.getFXSound("walk"), true, 1, 1, 1,
		// SoundSystemConfig.ATTENUATION_ROLLOFF, 0.5f);

		// soundSys.newSource(true, soundCode(e), lib.getFXSound("walk"),
		// "walk.wav", true, (float)1, (float)1, (float)1,
		// SoundSystemConfig.ATTENUATION_ROLLOFF, 0.5f);

		// soundSys.play(soundCode(e));

	}

	private void stopZombiewalk(Enemy e) {
		soundSys.stop(soundCode(e));
	}

	private void removeZombieSounds(Enemy e) {
		soundSys.removeSource(soundCode(e));
	}

	public float getBGMVolume() {
		return BGM_VOLUME;
	}

	public void setVolume(float f) {
		BGM_VOLUME = f;
	}

	public float getFXVolume() {
		return FX_VOLUME;
	}

	public void setFXVolume(float f) {
		FX_VOLUME = f;
	}

	public String soundCode(Entity e) {
		return "sc_" + e.hashCode();
	}

	public void playStartUpSound() {
		soundSys.newSource(false, "startUpSound", lib.getStartUpSound(),
				"startUpSound.wav", false, 0.5f, 0.5f, 1f, 1, 1f);
		soundSys.play("startUpSound");
	}

}