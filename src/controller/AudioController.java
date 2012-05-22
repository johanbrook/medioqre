package controller;

import model.Entity;
import model.IGameModel;
import model.character.Enemy;
import model.weapon.Melee;
import model.weapon.Projectile;
import paulscode.sound.SoundSystem;
import paulscode.sound.SoundSystemConfig;
import paulscode.sound.SoundSystemException;
import paulscode.sound.codecs.CodecJOrbis;
import paulscode.sound.codecs.CodecWav;
import paulscode.sound.libraries.LibraryLWJGLOpenAL;
import tools.datamanagement.PreferenceLoader;
import audio.AudioConstants;
import audio.SoundLibrary;
import event.Event;
import event.EventBus;
import event.IEventHandler;

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
	private double playerMaxHealth;

	private int bgmID = 2;

	/**
	 * Returns the AudioController instance
	 * 
	 * @return AudioController instance.
	 */
	public synchronized static AudioController getInstance() {
		if (sharedInstance == null) {
			sharedInstance = new AudioController();
			EventBus.INSTANCE.register(sharedInstance);

		}
		return sharedInstance;
	}

	/**
	 * Creates the audiocontroller instance and links it to System audio.
	 */
	private AudioController() {

		// Link to system sound
		try {

			// Use LWJGLOpenAL if Compatible
			if (SoundSystem.libraryCompatible(LibraryLWJGLOpenAL.class)) {
				soundSys = new SoundSystem(LibraryLWJGLOpenAL.class);
			} else {
				// Do not use Sound
				soundSys = new SoundSystem();
			}

			// Link to Wav Codec
			SoundSystemConfig.setCodec("wav", CodecWav.class);

			// Link to JOgg
			SoundSystemConfig.setCodec("ogg", CodecJOrbis.class);

		} catch (SoundSystemException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Sets the game to get player and enemy data from
	 * @param game the game
	 */
	
	public void setGame(IGameModel game) {
		this.game = game;
	}

	/*
	 * ____________SFX_____________
	 */

	/**
	 * Plays sound effect for player walk. No Attenuation and always center
	 * panned
	 */
	public void playerWalk() {
		soundSys.newSource(false, "playerWalk", lib.getFXSound("walk"),
				"walk.wav", false, 1f, 1f, 1.0f,
				SoundSystemConfig.ATTENUATION_NONE, 0.5f);
		soundSys.setVolume("playerWalk", PreferenceLoader.getFloat("FX_VOLUME",
				AudioConstants.standardFXVolume));
		soundSys.play("playerWalk");
	}

	/**
	 * Stops player's walk sound.
	 */
	public void stopPlayerWalk() {
		soundSys.stop("playerWalk");
	}

	/**
	 * Clear audio buffer and turn off audio engine MUST BE DONE BEFORE GAME
	 * SHUTDOWN!
	 */
	public void shutdown() {
		soundSys.cleanup();
	}

	/**
	 * Audio update method, sets listener position and positions sound sources.
	 */
	public void update() {

		soundSys.setListenerPosition(game.getPlayer().getPosition().x, game
				.getPlayer().getPosition().y, AudioConstants.zROLLOFF);

	}

	/**
	 * Plays sound effects for weapons given their class.
	 * 
	 */
	public void playPlayerWeaponSound() {
		if (PreferenceLoader.getFloat("FX_VOLUME",
				AudioConstants.standardFXVolume) != 0.0) {

			soundSys.newSource(
					false,
					"playerWeaponSound",
					lib.getWeaponSound(game.getPlayer().getCurrentWeapon()
							.getClass()),
					lib.getWeaponId(game.getPlayer().getCurrentWeapon()
							.getClass()), false, 1f, 1f, 1.0f,
					SoundSystemConfig.ATTENUATION_NONE, 0.0f);

			soundSys.setVolume("playerWeaponSound", PreferenceLoader.getFloat(
					"FX_VOLUME", AudioConstants.standardFXVolume));

			soundSys.play("playerWeaponSound");
		}
	}

	/**
	 * Plays sound effects based on events
	 */

	@Override
	public void onEvent(Event evt) {

		// Initialize
		if (evt.getProperty() == Event.Property.INIT_MODEL) {
			playerMaxHealth = game.getPlayer().getHealth();

			if (!soundSys.playing("Background Music")) {
				playBGM();
			}
		}

		// Pause
		if (evt.getProperty() == Event.Property.PAUSE_GAME) {
			pause();
		}

		// unPause
		if (evt.getProperty() == Event.Property.UNPAUSE_GAME) {
			unPause();
		}

		//
		if (evt.getProperty() == Event.Property.NEW_GAME) {
			pitchBGM();
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

				// Was hit
				if (evt.getProperty() == Event.Property.WAS_DAMAGED) {
					pitchBGM();
				}

			}

			// Enemies
			if (p instanceof model.character.Enemy) {
				Enemy e = (Enemy) evt.getValue();

				if (evt.getProperty() == Event.Property.DID_MOVE
						&& !soundSys.playing(soundCode(e))) {

				}

				if (evt.getProperty() == Event.Property.WAS_DESTROYED) {

				}

				if (evt.getProperty() == Event.Property.DID_STOP) {

				}
			}

		}

		// Weapons
		if (evt.getProperty() == Event.Property.FIRED_WEAPON_SUCCESS) {

			if (evt.getValue() instanceof Projectile) {

				Projectile p = ((Projectile) evt.getValue());

				if (!(p.getOwner() instanceof Melee)) {
					playPlayerWeaponSound();
				}
			}
		}

		// FX

		// Pickup Items
		if (evt.getProperty() == Event.Property.PICKED_UP_ITEM) {
			pitchBGM();
		}
	}

	/**
	 * Pauses background music
	 */
	
	private void pause() {
		soundSys.pause("BGM");
		soundSys.newSource(true, "pause", lib.getFXSound("pause"),
				lib.getFXId("pause"), false, 1f, 1f, 1f,
				SoundSystemConfig.ATTENUATION_NONE, 0.5f);
		soundSys.setVolume("pause",
				PreferenceLoader.getFloat("FX_VOLUME", 0.5f));
		soundSys.play("pause");

	}

	/**
	 * Unpauses background music
	 */
	
	private void unPause() {
		soundSys.newSource(true, "unPause", lib.getFXSound("unPause"),
				lib.getFXId("unPause"), false, 1f, 1f, 1f,
				SoundSystemConfig.ATTENUATION_NONE, 0.5f);
		soundSys.setVolume("unPause",
				PreferenceLoader.getFloat("FX_VOLUME", 0.5f));
		soundSys.play("unPause");

		soundSys.play("BGM");

	}

	/**
	 * Plays background music
	 */
	private void playBGM() {
		soundSys.backgroundMusic("BGM", lib.getBGMURL(bgmID),
				lib.getBGMId(bgmID), true);
		soundSys.setVolume("BGM", PreferenceLoader.getFloat("BGM_VOLUME",
				AudioConstants.standardBGMVolume));
		soundSys.play("BGM");
	}

	/**
	 * Plays walk sound effect for Enemies
	 * 
	 * @param e
	 *            Enemy
	 */
	private void playEnemyWalk(Enemy e) {

		soundSys.newSource(true, soundCode(e), lib.getFXSound("walk"),
				"walk.wav", true, (float) 1, (float) 1, (float) 1,
				SoundSystemConfig.ATTENUATION_ROLLOFF, 0.5f);

		soundSys.play(soundCode(e));

	}

	/**
	 * Stops walk sounds for a given Enemy
	 * 
	 * @param e
	 *            Enemy
	 */

	private void stopEnemyWalk(Enemy e) {
		soundSys.stop(soundCode(e));
	}

	/**
	 * Removes the sound system source for Enemies destroyed
	 * 
	 * @param e
	 *            Enemy
	 */
	private void removeEnemySounds(Enemy e) {
		soundSys.removeSource(soundCode(e));
	}

	/**
	 * Generates a code for sound sources based on hashCode
	 * 
	 * @param e
	 *            Entity to create code for
	 * @return Soundcode for e
	 */
	public String soundCode(Entity e) {
		return "sc_" + e.hashCode();
	}

	/**
	 * Pitches background music based on player health
	 */
	private void pitchBGM() {

		if (PreferenceLoader.getBoolean("PITCH_D_BGM_WHEN_HURT", false)) {
			float f = (float) (game.getPlayer().getHealth() / playerMaxHealth);
			f = (float) (f * 0.5 + 0.5);

			if (f > 1)
				f = 1f;

			soundSys.setPitch("BGM", f);
		}
	}

	/**
	 * Plays start up sound. Used only by launcher.
	 */
	public void playStartUpSound() {
		soundSys.backgroundMusic("BGM", lib.getBGMURL(1), lib.getBGMId(1), true);
		soundSys.setVolume("BGM", 0.3f);
		soundSys.play("BGM");
	}

}