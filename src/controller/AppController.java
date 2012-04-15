/**
*	AppController.java
*
*	@author Johan
*/

package controller;


import tools.TimerTool;
import event.Event;
import event.EventBus;
import gui.ViewController;
import model.GameModel;
import model.IGameModel;
import static tools.Logger.*;

public class AppController implements Runnable{
	
	public static final int FPS = 300;
	private static final double DELTA_RATIO = 10E7;
	
	private IGameModel game;
	private ViewController view;

	
	public AppController(){
		log("Initializing main controller ...", LOG_ALL);
		this.game = new GameModel();
		this.view = new ViewController(new NavigationController(this.game), 20*32, 12*32);
		
		EventBus.INSTANCE.publish(new Event(Event.Property.INIT_MODEL, this.game));
		
		Thread t = new Thread(this);
		t.start();
		
	}

	
	@Override
	public void run() {
		long lastLoopTime = System.nanoTime();
		
		while(!Thread.interrupted()) {
			long now = System.nanoTime();
			long updateLength = now - lastLoopTime;
			lastLoopTime = now;
			
			double dt = (double) updateLength / DELTA_RATIO;
			TimerTool.start("Update");
			game.update(dt);
			TimerTool.stop();
			TimerTool.start("Rendering");
			view.render(dt);
			TimerTool.stop();
			
			
			try {
				Thread.sleep(1000/FPS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}
