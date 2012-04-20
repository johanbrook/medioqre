package controller.navigation;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import model.character.Player;

import constants.Direction;

/**
 *	The class responsible for handling navigational events.
 *
 */
public class NavigationController implements KeyListener {
	
	private Set<Key> keys;
	private Map<Integer, Direction> keyMap;
	
	private Player player;
	
	private boolean isQuick = false;
	private Set<Integer> cachedKeys;
	
	private long start;
	private final double EPSILON = 0.2;
	
	
	private Key up;
	private Key down;
	private Key left;
	private Key right;
	private Key shoot;
	
	private Map<Integer, Key> keySet;
	
	/**
	 * Create a new NavigationController, which controls a
	 * <code>GameModel</code>.
	 * 
	 * @param game A game model
	 */
	public NavigationController(Player target) {
		this.keys = new HashSet<Key>();
		this.keyMap = new HashMap<Integer, Direction>();
		this.cachedKeys = new HashSet<Integer>();
		
		this.player = target;
		
		this.keyMap.put(KeyEvent.VK_W, Direction.NORTH);
		this.keyMap.put(KeyEvent.VK_A, Direction.WEST);
		this.keyMap.put(KeyEvent.VK_S, Direction.SOUTH);
		this.keyMap.put(KeyEvent.VK_D, Direction.EAST);
		
		initKeys();
	}
	
	
	private void initKeys() {
		this.keySet = new HashMap<Integer, Key>();
		
		this.up = new Key("up", new Callable() {
			@Override
			public void action() {
				updatePlayerDirection(Direction.NORTH);
			}
			
		});
		
		this.down = new Key("down", new Callable() {
			@Override
			public void action() {
				updatePlayerDirection(Direction.SOUTH);
			}
			
		});
		
		this.left = new Key("left", new Callable() {
			@Override
			public void action() {
				updatePlayerDirection(Direction.WEST);
			}
			
		});
		
		this.right = new Key("right", new Callable() {
			@Override
			public void action() {
				updatePlayerDirection(Direction.EAST);
			}
			
		});
		
		this.shoot = new Key("shoot", new Callable() {
			@Override
			public void action() {
				System.out.println("DID ATTACK");
				player.attack();
			}
		});
		
		this.keySet.put(KeyEvent.VK_W, this.up);
		this.keySet.put(KeyEvent.VK_A, this.left);
		this.keySet.put(KeyEvent.VK_S, this.down);
		this.keySet.put(KeyEvent.VK_D, this.right);
		this.keySet.put(KeyEvent.VK_SPACE, this.shoot);
	}
	
	
	private void updatePlayerDirection(Direction dir) {
		this.player.setDirection(dir);
		this.player.start();
	}
	
	private void stopPlayer() {
		this.player.stop();
	}
	
	
	private void sendKeyAction(Key key, boolean keyIsPressed) {
		String is = (keyIsPressed) ? "down" : "up";
		System.out.println(key +" is "+is);
		System.out.println("Keys are: "+this.keys);
		
//		if(this.isQuick){
//			
//			if(this.cachedKeys.contains(KeyEvent.VK_W) && this.cachedKeys.contains(KeyEvent.VK_A)) {
//				updatePlayerDirection(Direction.NORTH_WEST);
//			}
//			else if(this.cachedKeys.contains(KeyEvent.VK_W) && this.cachedKeys.contains(KeyEvent.VK_D)) {
//				updatePlayerDirection(Direction.NORTH_EAST);
//			}
//			else if(this.cachedKeys.contains(KeyEvent.VK_S) && this.cachedKeys.contains(KeyEvent.VK_A)) {
//				updatePlayerDirection(Direction.SOUTH_WEST);
//			}
//			else if(this.cachedKeys.contains(KeyEvent.VK_S) && this.cachedKeys.contains(KeyEvent.VK_D)) {
//				updatePlayerDirection(Direction.SOUTH_EAST);
//			}
//			
//			this.isQuick = false;
//			
//			if(!keyIsPressed) {
//				stopPlayer();
//			}
//			
//			return;
//		}
//		
		if(!keyIsPressed) {
			stopPlayer();
			return;
		}
		
		
//		if(this.keys.size() > 1){
			if(this.keys.contains(down) && this.keys.contains(left)) {
				updatePlayerDirection(Direction.SOUTH_WEST);
			}
			if(this.keys.contains(down) && this.keys.contains(right)) {
				updatePlayerDirection(Direction.SOUTH_EAST);
			}
			if(this.keys.contains(up) && this.keys.contains(left)) {
				updatePlayerDirection(Direction.NORTH_WEST);
			}
			if(this.keys.contains(up) && this.keys.contains(right)) {
				updatePlayerDirection(Direction.NORTH_EAST);
			}
//		}
//		
		if(this.keys.size() == 1){
			System.out.println("One key");
			key.fire();
		}
		
	}
	

	@Override
	public void keyPressed(KeyEvent evt) {
		
		if(this.keySet.containsKey(evt.getKeyCode())) {
			Key a = this.keySet.get(evt.getKeyCode());
			this.keys.add(a);
			
			sendKeyAction(a, true);
		}
		
		if(this.keyMap.containsKey(evt.getKeyCode())){
			
			this.cachedKeys.clear();
//			sendKeyAction(evt, true);
		}
	}

	@Override
	public void keyReleased(KeyEvent evt) {
		
		if(this.keySet.containsKey(evt.getKeyCode())) {
			Key a = this.keySet.get(evt.getKeyCode());
			this.keys.remove(a);
			sendKeyAction(a, !this.keys.isEmpty());
		}
		
		if(this.keyMap.containsKey(evt.getKeyCode())){
			
			if(this.keys.size() == 1 && this.cachedKeys.size() > 0) {
				double seconds = ((System.nanoTime() - start) / 10E8);
				this.isQuick = seconds < EPSILON;
			}
			else if(this.keys.size() == 2) {
				this.start = System.nanoTime();
			}
			
			
			this.cachedKeys.add(evt.getKeyCode());
			
//			sendKeyAction(evt, !this.keys.isEmpty());
		}
		
	}

	@Override
	public void keyTyped(KeyEvent evt) {
		
	}
	
	
}
