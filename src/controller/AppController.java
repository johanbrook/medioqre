/**
*	AppController.java
*
*	@author Johan
*/

package controller;

import gui.ViewController;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import constants.Direction;

import model.GameModel;

public class AppController implements KeyListener {
	
	public static final int GAME_SPEED = 10;
	
	private GameModel game;
	private ViewController view;
	
	private boolean[] keys;
	private boolean isKeyPressed = false;
	
	
	public AppController(){
		this.view = new ViewController(this, 600, 300);
		this.game = new GameModel();
		this.keys = new boolean[526];
		
		this.game.addObserver(this.view);
	}
	
	public static void main(String[] args) {
		new AppController();
	}

	private void sendKeyAction(KeyEvent evt){
		
		if(checkKey(KeyEvent.VK_W) && checkKey(KeyEvent.VK_A)) {
			this.game.updateDirection(Direction.NORTH_WEST);
		}
		
		else if(checkKey(KeyEvent.VK_W) && checkKey(KeyEvent.VK_D)) {
			this.game.updateDirection(Direction.NORTH_EAST);
		}
		
		else if(checkKey(KeyEvent.VK_A) && checkKey(KeyEvent.VK_S)) {
			this.game.updateDirection(Direction.SOUTH_WEST);
		}
		
		else if(checkKey(KeyEvent.VK_S) && checkKey(KeyEvent.VK_D)) {
			this.game.updateDirection(Direction.SOUTH_EAST);
		}
		
		
		else if(checkKey(KeyEvent.VK_A)) {
			this.game.updateDirection(Direction.WEST);
		}
		
		else if(checkKey(KeyEvent.VK_W)) {
			this.game.updateDirection(Direction.NORTH);
		}
		
		else if(checkKey(KeyEvent.VK_S)) {
			this.game.updateDirection(Direction.SOUTH);
		}
		
		else if(checkKey(KeyEvent.VK_D)) {
			this.game.updateDirection(Direction.EAST);
		}
		
	}
	
	private boolean checkKey(int i) {
		if(this.keys.length > i)
			return this.keys[i];
		
		return false;
	}
	
	
	@Override
	public void keyPressed(KeyEvent e) {
		
		this.keys[e.getKeyCode()] = true;
		this.isKeyPressed = true;
		sendKeyAction(e);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		this.keys[e.getKeyCode()] = false;
		this.isKeyPressed = false;
		sendKeyAction(e);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	
	
}
