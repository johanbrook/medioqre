package controller.navigation;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import model.character.Player;

import constants.Direction;

/**
 *	The class responsible for handling navigational events.
 *
 */
public class NavigationController implements KeyListener {
	
	private Set<NavigationKey> navKeys;
	private Map<Integer, Key> keyMap;
	
	private Player player;
	
	private boolean isQuick = false;
	private Set<Integer> cachedKeys;
	
	private long start;
	private final double EPSILON = 0.2;
	
	
	private NavigationKey up;
	private NavigationKey down;
	private NavigationKey left;
	private NavigationKey right;
	private Key shoot;
	
	
	/**
	 * Create a new NavigationController, which controls a
	 * <code>GameModel</code>.
	 * 
	 * @param game A game model
	 */
	public NavigationController(Player target) {
		this.player = target;
		
		initKeys();
	}
	
	
	private void initKeys() {
		this.navKeys = new HashSet<NavigationKey>();
		this.cachedKeys = new HashSet<Integer>();
		this.keyMap = new HashMap<Integer, Key>();
		
		this.up = new NavigationKey("up", null, Direction.NORTH);
		this.down = new NavigationKey("down", null, Direction.SOUTH);
		this.left = new NavigationKey("left", null, Direction.WEST);
		this.right = new NavigationKey("right", null, Direction.EAST);
		
		this.shoot = new Key("shoot", new Callable() {
			@Override
			public void action() {
				System.out.println("** Attacked once **");
				player.attack();
			}
		});
		
		this.keyMap.put(KeyEvent.VK_W, this.up);
		this.keyMap.put(KeyEvent.VK_A, this.left);
		this.keyMap.put(KeyEvent.VK_S, this.down);
		this.keyMap.put(KeyEvent.VK_D, this.right);
		this.keyMap.put(KeyEvent.VK_SPACE, this.shoot);
	}

	
	
	private void refreshDirection() {
		if(this.navKeys.size() > 1){
			createCompositeKey().fire(this.player);
		}
		else if(this.navKeys.size() == 1){
			((NavigationKey)this.navKeys.toArray()[0]).fire(this.player);
		}
	}
	
	
	private NavigationKey createCompositeKey() {

		System.out.println("Creating composite: " + this.navKeys);
		
		if(this.navKeys.contains(up) && this.navKeys.contains(right)){
			System.out.println("TOP RIGHT");
			return new NavigationKey("top_right", null, Direction.NORTH_EAST);
		}
		
		if(this.navKeys.contains(up) && this.navKeys.contains(left)){
			System.out.println("TOP LEFT");
			return new NavigationKey("top_left", null, Direction.NORTH_WEST);
		}
		
		if(this.navKeys.contains(down) && this.navKeys.contains(right)){
			System.out.println("BOTTOM RIGHT");
			return new NavigationKey("bottom_right", null, Direction.SOUTH_EAST);
		}
		
		if(this.navKeys.contains(down) && this.navKeys.contains(left)){
			System.out.println("BOTTOM LEFT");
			return new NavigationKey("bottom_left", null, Direction.SOUTH_WEST);
		}
		
		
		return null;
		
	}
	
	@Override
	public void keyPressed(KeyEvent evt) {
		Key a = this.keyMap.get(evt.getKeyCode());
		
		if(a != null) {
			
			if(a instanceof NavigationKey) {
				this.navKeys.add((NavigationKey) a);
				refreshDirection();
			}
			else{
				a.fire();
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent evt) {
		
		Key a = this.keyMap.get(evt.getKeyCode());
		
		if(a instanceof NavigationKey){
			this.navKeys.remove(a);
			refreshDirection();
		}
		
		
		
		if(this.navKeys.isEmpty()){
			this.player.stop();
		}
		
//		if(this.keyMap.containsKey(evt.getKeyCode())){
//			
//			if(this.keys.size() == 1 && this.cachedKeys.size() > 0) {
//				double seconds = ((System.nanoTime() - start) / 10E8);
//				this.isQuick = seconds < EPSILON;
//			}
//			else if(this.keys.size() == 2) {
//				this.start = System.nanoTime();
//			}
//			
//			
//			this.cachedKeys.add(evt.getKeyCode());
//			
//			sendKeyAction(evt, !this.keys.isEmpty());
//		}
//		
	}

	@Override
	public void keyTyped(KeyEvent evt) {
		
	}
	
	
}
