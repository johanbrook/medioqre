package controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import model.GameModel;

import constants.Direction;

/**
 *	The class responsible for handling navigational events.
 *
 */
public class NavigationController implements KeyListener {
	
	private Set<Integer> keys;
	private Map<Integer, Direction> keyMap;
	private GameModel game;
	
	public NavigationController(GameModel game) {
		this.keys = new HashSet<Integer>();
		this.keyMap = new HashMap<Integer, Direction>();
		this.game = game;
		
		this.keyMap.put(KeyEvent.VK_W, Direction.NORTH);
		this.keyMap.put(KeyEvent.VK_A, Direction.WEST);
		this.keyMap.put(KeyEvent.VK_S, Direction.SOUTH);
		this.keyMap.put(KeyEvent.VK_D, Direction.EAST);
	}
	
	
	private void sendKeyAction(KeyEvent evt, boolean pressedState) {
		
		this.game.setPlayerMoving(pressedState);
		
		if(this.keys.size() > 1) {
			// Multiple keys are pressed
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
			// One single key is pressed - map directly to the direction
			this.game.updateDirection(this.keyMap.get(evt.getKeyCode()));
		}
	}
	
	private boolean checkKey(int i) {

		return this.keys.contains(i);
	}
	

	@Override
	public void keyPressed(KeyEvent evt) {
		this.keys.add(evt.getKeyCode());
		
		sendKeyAction(evt, true);
	}

	@Override
	public void keyReleased(KeyEvent evt) {
		this.keys.remove(evt.getKeyCode());
		
		sendKeyAction(evt, !this.keys.isEmpty());
		
	}

	@Override
	public void keyTyped(KeyEvent evt) {
		
	}
	
	
}
