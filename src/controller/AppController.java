/**
*	AppController.java
*
*	@author Johan
*/

package controller;


import event.EventBus;
import gui.ViewController;
import model.GameModel;
import model.IGameModel;

public class AppController implements Runnable{
	
	public static final int FPS = 300;
	private static final double DELTA_RATIO = 10E7;
	
	private IGameModel game;
	private ViewController view;

	
	public AppController(){
		this.game = new GameModel();
		this.view = new ViewController(new NavigationController(this.game), 1280, 768);
		
		EventBus.INSTANCE.register(this.view);
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
			
			game.update(dt);
			view.render(dt);
			
			
			try {
				Thread.sleep(1000/FPS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}
