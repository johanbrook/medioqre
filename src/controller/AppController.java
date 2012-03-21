/**
*	AppController.java
*
*	@author Johan
*/

package controller;

import gui.ViewController;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import constants.Direction;

import model.GameModel;

public class AppController implements KeyListener {
	
	public static final int GAME_SPEED = 10;
	
	private GameModel game;
	private ViewController view;
	
	private boolean[] keys;
	private Set<Integer> lkeys;
	
	private Map<Integer, Direction> keyMap;
	
	
	public AppController(){
		this.view = new ViewController(this, 400, 300);
		this.game = new GameModel();
		this.keys = new boolean[526];
		
		this.lkeys = new HashSet<Integer>();
		
		this.keyMap = new HashMap<Integer, Direction>();
		this.keyMap.put(KeyEvent.VK_W, Direction.NORTH);
		this.keyMap.put(KeyEvent.VK_A, Direction.WEST);
		this.keyMap.put(KeyEvent.VK_S, Direction.SOUTH);
		this.keyMap.put(KeyEvent.VK_D, Direction.EAST);
		
		this.game.addObserver(this.view);
	}
	
	public static void main(String[] args) {
		new AppController();
	}

	private void sendKeyAction(KeyEvent evt, boolean isKeyPressed){
		
		this.game.pressedState(isKeyPressed);
		
		if(this.lkeys.size() > 1) {
			
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
			
		}
		else {
			// One single key is pressed
			this.game.updateDirection(this.keyMap.get(evt.getKeyCode()));
		}
		
	}
	
	private boolean checkKey(int i) {

		return this.lkeys.contains(i);
	}
	
	
	@Override
	public void keyPressed(KeyEvent e) {
		
		this.lkeys.add(e.getKeyCode());
		System.out.println(this.lkeys);
		
		sendKeyAction(e, true);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
		this.lkeys.remove(e.getKeyCode());
		sendKeyAction(e, !this.lkeys.isEmpty());
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	
	
}
