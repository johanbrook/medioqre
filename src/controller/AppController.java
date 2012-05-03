/**
*	AppController.java
*
*	@author Johan
*/

package controller;

import tools.TimerTool;
import controller.AI.AIController;
import controller.navigation.NavigationController;
import event.Event;
import event.EventBus;
import event.IMessageListener;
import event.IMessageSender;
import gui.ViewController;
import model.GameModel;
import model.IGameModel;

public class AppController implements Runnable{
	
	public static final int FPS = 300;
	private static final double DELTA_RATIO = 10E7;
	
	private IGameModel game;
	private ViewController view;
	private AIController ai;
//	private AudioController audio;
	private NavigationController navigation;
		
	public AppController(){
		System.out.println("Initializing main controller ...");
		
		this.game = new GameModel();
		this.navigation = new NavigationController();
		
		this.view = new ViewController(this.navigation, 20*32, 12*32);
		this.ai = new AIController(48, 48, 32, 32);
		
		this.navigation.addReceiver((IMessageListener) this.game);
		this.ai.addReceiver((IMessageListener) this.game);
		((IMessageSender) this.game).addReceiver((IMessageListener) this.ai);
		
//		this.audio = AudioController.getInstance();
	}
	
	
	public void init() {
		new Thread(this).start();
		this.game.newWave();
		this.ai.setEnemies(this.game.getEnemies());
		
		EventBus.INSTANCE.publish(new Event(Event.Property.INIT_MODEL, this.game));
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
			this.ai.updateAI(dt, this.game.getPlayer().getPosition());
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
