/**
*	AppController.java
*
*	@author Johan
*/

package controller;

import gui.ViewController;
import model.GameModel;

public class AppController {
	
	public static final int GAME_SPEED = 10;
	
	private GameModel game;
	private ViewController view;

	
	public AppController(){
		this.game = new GameModel();
		this.view = new ViewController(new NavigationController(this.game), 400, 300);
		
		this.game.addObserver(this.view);
	}
	
}
