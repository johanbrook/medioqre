/**
*	AppController.java
*
*	@author Johan
*/

package controller;

import event.EventBus;
import gui.ViewController;
import model.GameModel;

public class AppController implements Runnable{
	
	public static final int FPS = 30;
	
	private GameModel game;
	private ViewController view;

	
	public AppController(){
		this.game = new GameModel();
		this.view = new ViewController(new NavigationController(this.game), 400, 300);
		
		EventBus.INSTANCE.register(this.view);
		Thread t = new Thread(this);
		t.start();
	}


	@Override
	public void run() {
		long lastLoopTime = System.nanoTime();
		final long optimalTime = 1000000000 / FPS;
		
		while(!Thread.interrupted()) {
			long now = System.nanoTime();
			long updateLength = now - lastLoopTime;
			lastLoopTime = now;
			
			double dt = updateLength / ((double) optimalTime);
			
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
