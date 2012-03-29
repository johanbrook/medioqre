package controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import model.IGameModel;

import constants.Direction;

/**
 *	The class responsible for handling navigational events.
 *
 */
public class NavigationController implements KeyListener {
	
	private Set<Integer> keys;
	private Map<Integer, Direction> keyMap;
	private IGameModel game;
	
	private boolean isQuick = false;
	private Set<Integer> cachedKeys;
	
	private long start;
	private final double EPSILON = 0.2;
	
	/**
	 * Create a new NavigationController, which controls a
	 * <code>GameModel</code>.
	 * 
	 * @param game A game model
	 */
	public NavigationController(IGameModel game) {
		this.keys = new HashSet<Integer>();
		this.keyMap = new HashMap<Integer, Direction>();
		this.cachedKeys = new HashSet<Integer>();
		
		this.game = game;
		
		this.keyMap.put(KeyEvent.VK_W, Direction.NORTH);
		this.keyMap.put(KeyEvent.VK_A, Direction.WEST);
		this.keyMap.put(KeyEvent.VK_S, Direction.SOUTH);
		this.keyMap.put(KeyEvent.VK_D, Direction.EAST);
		
	}
	
	
	private void sendKeyAction(KeyEvent evt, boolean keyIsPressed) {
		
		
		if(this.isQuick){
			
			if(this.cachedKeys.contains(KeyEvent.VK_W) && this.cachedKeys.contains(KeyEvent.VK_A)) {
				this.game.updateDirection(Direction.NORTH_WEST);
			}
			else if(this.cachedKeys.contains(KeyEvent.VK_W) && this.cachedKeys.contains(KeyEvent.VK_D)) {
				this.game.updateDirection(Direction.NORTH_EAST);
			}
			else if(this.cachedKeys.contains(KeyEvent.VK_S) && this.cachedKeys.contains(KeyEvent.VK_A)) {
				this.game.updateDirection(Direction.SOUTH_WEST);
			}
			else if(this.cachedKeys.contains(KeyEvent.VK_S) && this.cachedKeys.contains(KeyEvent.VK_D)) {
				this.game.updateDirection(Direction.SOUTH_EAST);
			}
			
			this.isQuick = false;
			
			if(!keyIsPressed) {
				this.game.stopPlayer();
			}
			
			return;
		}
		
		else if(!keyIsPressed) {
			this.game.stopPlayer();
			return;
		}
		
		
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
			Object[] arr = this.keys.toArray();
			this.game.updateDirection(this.keyMap.get(arr[0]));
		}
		
	}
	
	private boolean checkKey(int i) {

		return this.keys.contains(i);
	}
	

	@Override
	public void keyPressed(KeyEvent evt) {
		if(this.keyMap.containsKey(evt.getKeyCode())){
			this.keys.add(evt.getKeyCode());
			
			this.cachedKeys.clear();
			sendKeyAction(evt, true);
			
		}
	}

	@Override
	public void keyReleased(KeyEvent evt) {
		if(this.keyMap.containsKey(evt.getKeyCode())){
			
			if(this.keys.size() == 1 && this.cachedKeys.size() > 0) {
				double seconds = ((System.nanoTime() - start) / 10E8);
				this.isQuick = seconds < EPSILON;
			}
			else if(this.keys.size() == 2) {
				this.start = System.nanoTime();
			}
			this.cachedKeys.add(evt.getKeyCode());
			this.keys.remove(evt.getKeyCode());
			
			
			sendKeyAction(evt, !this.keys.isEmpty());
		}
		
	}

	@Override
	public void keyTyped(KeyEvent evt) {
		
	}
	
	
}
