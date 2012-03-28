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
	
	public static final int FPS = 300;
	
	private GameModel game;
	private ViewController view;

	
	public AppController(){
		this.game = new GameModel();
		this.view = new ViewController(new NavigationController(this.game), 1280, 768);
		
		EventBus.INSTANCE.register(this.view);
		Thread t = new Thread(this);
		t.start();
	}

	// FPS-Meter
	private int currentFPS;
	
	@Override
	public void run() {
		long lastLoopTime = System.nanoTime();
		final long optimalTime = 1000000000 / FPS;
		long timeThisSecond = 0;
		int framesThisSecond = 0;
		currentFPS = 0;
		
		while(!Thread.interrupted()) {
			long now = System.nanoTime();
			long updateLength = now - lastLoopTime;
			lastLoopTime = now;
			
			double dt = (double) updateLength / 20000000.0;
			
			game.update(dt);
			view.render(dt);
			
			if (timeThisSecond > 1000000000) {
				this.currentFPS = framesThisSecond;
				timeThisSecond = 0;
				framesThisSecond = 0;
				
				System.out.println("FPS: "+this.currentFPS);
			} else {
				framesThisSecond++;
				timeThisSecond += updateLength;
			}
			
			try {
				Thread.sleep(1000/FPS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}
