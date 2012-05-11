/**
 *	AppController.java
 *
 *	@author Johan
 */

package controller;

import org.json.JSONException;
import org.json.JSONObject;

import static tools.Logger.*;

import tools.TimerTool;
import controller.ai.AIController;
import controller.navigation.NavigationController;
import datamanagement.ResourceLoader;
import event.Event;
import event.EventBus;
import event.IMessageListener;
import event.IMessageSender;
import factory.Level;
import factory.ObjectFactory;
import gui.ViewController;
import model.GameModel;
import model.IGameModel;

public class AppController implements Runnable{
	
	/**
	 * Desired refresh rate
	 */
	public static final int FPS = 60;
	
	/**
	 * Production environment
	 */
	public static final int PRODUCTION = 0;
	
	/**
	 * Debugging mode
	 */
	public static final int DEBUG = 1;
	
	/**
	 * Current mode
	 */
	public static int MODE = PRODUCTION;

	private static final double DELTA_RATIO = 10E7;

	private IGameModel game;
	private ViewController view;
	private AIController ai;
	private AudioController audio;
	private NavigationController navigation;
		
	/**
	 * New AppController. 
	 * 
	 * <p>Initializes the game model, navigation controller, view controller, audio controller and AI controller,
	 * as well as relevant listeners.</p>
	 */
	public AppController(){
		String mode = (isDebugMode()) ? "debug" : "production";
		System.out.println("Initializing main controller in " + mode
				+ " mode ...");

		this.game = new GameModel();
		this.navigation = new NavigationController();

		this.view = new ViewController(this.navigation, 20 * 32, 12 * 32);
		this.ai = new AIController(48, 48, 32, 32);

		this.navigation.addReceiver((IMessageListener) this.game);
		this.ai.addReceiver((IMessageListener) this.game);
		((IMessageSender) this.game).addReceiver((IMessageListener) this.ai);

		this.audio = AudioController.getInstance();
		audio.setGame(game);
	}
	
	
	/**
	 * Initialize a new game.
	 */
	public void init() {
		Level lev = new Level("gamedata/config.json");
		ObjectFactory.setLevel(lev);

		try {
			String loggingFormat = lev.getConfig().getString("loggingFormat");
			tools.Logger.getInstance().setTimestampFormat(loggingFormat);

		} catch (JSONException e) {
			err("Couldn't load timestamp format from file! " + e.getMessage());
		}

		this.game.newGame();
		this.game.newWave();
		new Thread(this).start();

		EventBus.INSTANCE.publish(new Event(Event.Property.INIT_MODEL,
				this.game));
	}

	
	/**
	 * If the app is in debug mode or not.
	 * 
	 * @return Returns true if debug mode is activated, otherwise false
	 */
	public static boolean isDebugMode() {
		return MODE == DEBUG;
	}
	
	/**
	 * The game loop. Called repeatedly and refreshes the game logic (model), the AI and the audio
	 * with a delta time.
	 */
	@Override
	public void run() {
		long lastLoopTime = System.nanoTime();

		while (!Thread.interrupted()) {
			long now = System.nanoTime();
			long updateLength = now - lastLoopTime;
			lastLoopTime = now;

			double dt = (double) updateLength / DELTA_RATIO;
			// TimerTool.start("Update");
			this.ai.updateAI(dt);
			this.game.update(dt);
			audio.update();
			// TimerTool.stop();

			try {
				Thread.sleep(1000 / FPS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
