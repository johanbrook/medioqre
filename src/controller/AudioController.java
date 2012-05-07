package controller;

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
	private double playerMaxHealth;

	private static float BGM_VOLUME = 1;
	private static float FX_VOLUME = 1;

	private int bgmID = 1;

	/**
	 * Returns the AudioController instance
	 * 
	 * @return AudioController instance.
	 */
	public static AudioController getInstance() {
		if (sharedInstance == null) {
			sharedInstance = new AudioController();
			EventBus.INSTANCE.register(sharedInstance);

		}
		return sharedInstance;
	}

	/**
	 * Creates the audiocontroller and links it to System audio.
	 */
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

	/**
	 * Plays sound effect for player walk. No Attenuation and always center
	 * panned
	 */
	public void playerWalk() {
		soundSys.newSource(false, "playerWalk", lib.getFXSound("walk"),
				"walk.wav", false, 1f, 1f, 1.0f,
				SoundSystemConfig.ATTENUATION_NONE, 0.5f);

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

		for (Enemy e : game.getEnemies()) {
			soundSys.setPosition(soundCode(e), e.getPosition().x,
					e.getPosition().y, 1);

		}

	}

	/**
	 * Plays sound effects for weapons given their class.
	 * 
	 * @param wType
	 *            weapon type (Class)
	 */
	public void playWeaponSound(Class<?> wType) {
		soundSys.newSource(false, "playerWeaponSound",
				lib.getWeaponSound(wType), lib.getWeaponId(wType), false, 1f,
				1f, 1.0f, SoundSystemConfig.ATTENUATION_NONE, 0.5f);
		soundSys.setVolume("playerWeaponSound", AudioConstants.WEAPON_VOLUME);

		soundSys.play("playerWeaponSound");
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

				// Was hit
				if (evt.getProperty() == Event.Property.WAS_DAMAGED) {
					float f = (float) (game.getPlayer().getHealth() / playerMaxHealth);
					f = (float) (f * 0.5 + 0.5);

					soundSys.setPitch("BGM", f);
				}

			}

			// Enemies
			if (p instanceof model.character.Enemy) {
				Enemy e = (Enemy) evt.getValue();

				if (evt.getProperty() == Event.Property.DID_MOVE
						&& !soundSys.playing(soundCode(e))) {
					playEnemyWalk(e);
				}

				if (evt.getProperty() == Event.Property.WAS_DESTROYED) {
					removeEnemySounds(e);
				}

				if (evt.getProperty() == Event.Property.DID_STOP) {
					stopEnemyWalk(e);
				}
			}

		}

		// Weapons
		if (evt.getProperty() == Event.Property.FIRED_WEAPON_SUCCESS) {
			playWeaponSound(game.getPlayer().getCurrentWeapon().getClass());
		}

		// FX

	}

	/**
	 * Plays background music
	 */
	private void playBGM() {
		soundSys.backgroundMusic("BGM", lib.getBGMURL(bgmID),
				lib.getBGMId(bgmID), true);
		soundSys.play("BGM");
	}

	/**
	 * Plays walk sound effect for Enemies
	 * 
	 * @param e Enemy
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
	 * @param e Enemy 
	 */
	
	private void stopEnemyWalk(Enemy e) {
		soundSys.stop(soundCode(e));
	}

	/**
	 * Removes the sound system source for Enemies destroyed
	 * 
	 * @param e Enemy
	 */
	private void removeEnemySounds(Enemy e) {
		soundSys.removeSource(soundCode(e));
	}

	/**
	 * Generates a code for sound sources based on hashCode
	 * 
	 * @param e Entity to create code for
	 * @return Soundcode for e
	 */
	public String soundCode(Entity e) {
		return "sc_" + e.hashCode();
	}

	/**
	 * Plays start up sound. Used only by launcher.
	 */
	public void playStartUpSound() {
		soundSys.newSource(false, "startUpSound", lib.getStartUpSound(),
				"startUpSound.wav", false, 0.5f, 0.5f, 1f, 1, 1f);
		soundSys.play("startUpSound");
	}

}