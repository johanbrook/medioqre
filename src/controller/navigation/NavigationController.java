package controller.navigation;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import model.character.Player;

import constants.Direction;
import event.Event;
import event.EventBus;
import event.Event.Property;

/**
 *	The class responsible for handling navigational events.
 *
 */
public class NavigationController implements KeyListener {
	
	private NavigationKeyQueue navKeys;
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
	private Key weaponModifier;
	
	
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
	
	/**
	 * Create the keyboard keys in the game.
	 */
	private void initKeys() {
		this.navKeys = new NavigationKeyQueue(2);
		
		this.cachedKeys = new HashSet<Integer>();
		this.keyMap = new HashMap<Integer, Key>();
		
		this.up = new NavigationKey("up", null, Direction.NORTH);
		this.down = new NavigationKey("down", null, Direction.SOUTH);
		this.left = new NavigationKey("left", null, Direction.WEST);
		this.right = new NavigationKey("right", null, Direction.EAST);
		
		this.shoot = new Key("shoot", new Callable() {
			@Override
			public void on() {
				player.attack();
			}

			@Override
			public void off() {
				
			}
		});
		
		this.weaponModifier = new Key("chose_weapon", new Callable() {
			@Override
			public void on() {
				System.out.println("Show weapon menu");
				EventBus.INSTANCE.publish(new Event(Property.WEAPON_MENU_SHOW, player));
			}

			@Override
			public void off() {
				System.out.println("Hide weapon menu");
				EventBus.INSTANCE.publish(new Event(Property.WEAPON_MENU_HIDE, player));
			}
		});
		
		this.keyMap.put(KeyEvent.VK_W, this.up);
		this.keyMap.put(KeyEvent.VK_A, this.left);
		this.keyMap.put(KeyEvent.VK_S, this.down);
		this.keyMap.put(KeyEvent.VK_D, this.right);
		this.keyMap.put(KeyEvent.VK_SPACE, this.shoot);
		this.keyMap.put(KeyEvent.VK_SHIFT, this.weaponModifier);
	}

	
	/**
	 * Refresh (if necessary) the target's direction based on the keys in
	 * the navigation key list.
	 */
	private void refreshDirection() {
		if(this.navKeys.size() > 1){
			NavigationKey composite = createCompositeKey();
			
			if(composite != null)
				composite.fire(this.player);
		}
		else if(this.navKeys.size() == 1){
			this.navKeys.first().fire(this.player);
		}
	}
	
	
	/**
	 * Creates a composite navigation key with a direction from the 
	 * existing keys in the navigation list.
	 * 
	 * @return A new navigation key with a diagonal direction. If the list doesn't
	 * contain matching diagonal key, null is returned.
	 * @see Direction
	 */
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
				System.out.println("Keys: "+this.navKeys);
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
		else {
			a.fireUp();
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
