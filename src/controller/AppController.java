/**
*	AppController.java
*
*	@author Johan
*/

package controller;


import controller.AI.AIController;
import tools.TimerTool;
import event.Event;
import event.EventBus;
import gui.ViewController;
import model.GameModel;
import model.IGameModel;

public class AppController implements Runnable{
	
	public static final int FPS = 300;
	private static final double DELTA_RATIO = 10E7;
	
	private IGameModel game;
	private ViewController view;
	private AIController ai;
	private AudioController audio;
	
	public AppController(){
		System.out.println("Initializing main controller ...");
		this.game = new GameModel();
		
		this.view = new ViewController(new NavigationController(this.game.getPlayer()), 20*32, 12*32);
		this.ai = new AIController(this.game.getEnemies(), 48, 48, 32, 32);
		
		this.audio = AudioController.getInstance();
		
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
//			TimerTool.start("Update");
			this.ai.updateAI(this.game.getPlayer().getPosition());
			this.game.update(dt);
//			TimerTool.stop();
			
//			TimerTool.start("Rendering");
			this.view.render(dt);
//			TimerTool.stop();
			
			
			try {
				Thread.sleep(1000/FPS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}
