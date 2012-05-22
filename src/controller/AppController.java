/**
 *	AppController.java
 *
 *	@author Johan
 */

package controller;

import java.awt.Dimension;
import javax.swing.JFrame;
import tools.factory.Level;
import tools.factory.ObjectFactory;

import static tools.Logger.*;

import controller.ai.AIController;
import controller.navigation.NavigationController;
import event.Event;
import event.Event.Property;
import event.EventBus;
import event.IEventHandler;
import event.IMessageSender;
import model.GameModel;
import model.IGameModel;

public class AppController implements Runnable, IEventHandler {
	
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
	private AIController ai;
	private AudioController audio;
	private NavigationController navigation;
	
	private static boolean paused = false;
		
	/**
	 * New AppController. 
	 * 
	 * <p>Initializes the game model, navigation controller, view controller, audio controller and AI controller,
	 * as well as relevant listeners.</p>
	 */
	public AppController(JFrame frame){
		String mode = (isDebugMode()) ? "debug" : "production";
		System.out.println("Initializing main controller in " + mode + " mode ...");

		EventBus.INSTANCE.register(this);
		
		Level lev = new Level("gamedata/config.json");
		ObjectFactory.setLevel(lev);
		
		this.game = new GameModel();
		this.navigation = new NavigationController();

		frame.setPreferredSize(new Dimension(20 * 48, 12 * 48));
		new ViewController(this.navigation, frame);
		
		this.ai = new AIController(48, 48, 48, 48);

		this.navigation.addReceiver((IEventHandler) this.game);
		this.ai.addReceiver((IEventHandler) this.game);
		((IMessageSender) this.game).addReceiver((IEventHandler) this.ai);

		this.audio = AudioController.getInstance();
		audio.setGame(game);
		
		// Logging format
		
		String loggingFormat = ObjectFactory.getConfigString("loggingFormat");
		tools.Logger.setTimestampFormat(loggingFormat);
	}
	
	
	/**
	 * Initialize a new game.
	 */
	public void init() {
		this.initNewGame();
		Thread loop = new Thread(this);
		loop.setName("Game-loop");
		loop.start();

		EventBus.INSTANCE.publish(new Event(Event.Property.INIT_MODEL, this.game));
	}
	
	
	public void initNewGame() {
		log("Initializing new game and wave ...");
		this.game.newGame();
		this.game.newWave();
	}
	
	@Override
	public void onEvent(Event evt) {
		switch(evt.getProperty()) {
		
		case GAME_OVER: 
			
			this.initNewGame();
			
			break;
		}
	}

	/**
	 * Toggled paused mode.
	 * 
	 * <p>When paused, the main game thread is stopped.</p>
	 */
	public static void togglePaused() {
		paused = !paused;
		
		if(paused) {
			EventBus.INSTANCE.publish(new Event(Property.PAUSE_GAME));
		}
		else {
			EventBus.INSTANCE.publish(new Event(Property.UNPAUSE_GAME));
		}
		
		log("Paused: "+paused);
	}
	
	/**
	 * Get the paused status.
	 * 
	 * @return True if the game is currently paused, otherwise paused
	 */
	public static boolean isPaused() {
		return paused;
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
			try {
				Thread.sleep(1000 / FPS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			long now = System.nanoTime();
			long updateLength = now - lastLoopTime;
			lastLoopTime = now;
			double dt = (double) updateLength / DELTA_RATIO;
			
			// Pause game
			if(paused) {
				continue;
			}
			
			this.ai.updateAI(dt);
			this.game.update(dt);
			audio.update();
		}
	}

}
