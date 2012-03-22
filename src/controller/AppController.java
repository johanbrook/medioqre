/**
*	AppController.java
*
*	@author Johan
*/

package controller;

import gui.ViewController;
import model.GameModel;

public class AppController implements Runnable{
	
	public static final int FPS = 30;
	
	private GameModel game;
	private ViewController view;

	
	public AppController(){
		this.game = new GameModel();
		this.view = new ViewController(new NavigationController(this.game), 400, 300);
		
		this.game.addObserver(this.view);
		Thread t = new Thread(this);
		t.start();
	}


	@Override
	public void run() {
		while(!Thread.interrupted()) {
			try {
				Thread.sleep(1000/FPS);
				double dt = 0;
				game.update(double dt);
				view.render(dt);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}
