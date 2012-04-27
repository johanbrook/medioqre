package controller.navigation;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import constants.Direction;
import event.Event;
import event.EventBus;
import event.IMessageListener;
import event.IMessageSender;
import event.Event.Property;
import event.Messager;

/**
 *	The class responsible for handling navigational events.
 *
 */
public class NavigationController implements KeyListener, IMessageSender {
	
	private NavigationKeyQueue navKeys;
	private Map<Integer, Key> keyMap;
	
	private Messager messager = new Messager();
	
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
	 * Create a new NavigationController.
	 */
	public NavigationController() {

		initKeys();
	}
	
	@Override
	public void addReceiver(IMessageListener listener) {
		this.messager.addListener(listener);
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
				messager.sendMessage(new Event(Property.DID_FIRE));
			}

			@Override
			public void off() {
				
			}
		});
		
		this.weaponModifier = new Key("chose_weapon", new Callable() {
			@Override
			public void on() {
				System.out.println("Show weapon menu");
				EventBus.INSTANCE.publish(new Event(Property.WEAPON_MENU_SHOW));
			}

			@Override
			public void off() {
				System.out.println("Hide weapon menu");
				EventBus.INSTANCE.publish(new Event(Property.WEAPON_MENU_HIDE));
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
		System.out.println(this.navKeys);
		
		if(this.navKeys.size() > 1){
			NavigationKey composite = createCompositeKey();
			
			if(composite != null){
				messager.sendMessage(new Event(Property.CHANGED_DIRECTION, composite.getDirection()));
			}
			else {
				messager.sendMessage(new Event(Property.CHANGED_DIRECTION, this.navKeys.last().getDirection()));
			}
				
		}
		else if(this.navKeys.size() == 1) {
			messager.sendMessage(new Event(Property.CHANGED_DIRECTION, this.navKeys.first().getDirection()));
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

		
		if(this.navKeys.contains(up) && this.navKeys.contains(right)){
			return new NavigationKey("top_right", null, Direction.NORTH_EAST);
		}
		
		if(this.navKeys.contains(up) && this.navKeys.contains(left)){
			return new NavigationKey("top_left", null, Direction.NORTH_WEST);
		}
		
		if(this.navKeys.contains(down) && this.navKeys.contains(right)){
			return new NavigationKey("bottom_right", null, Direction.SOUTH_EAST);
		}
		
		if(this.navKeys.contains(down) && this.navKeys.contains(left)){
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
		
		if(a != null) {
			if(a instanceof NavigationKey){
				this.navKeys.remove(a);
				refreshDirection();
			}
			else {
				a.fireUp();
			}
		}
		
		
		
		if(this.navKeys.isEmpty()){
			messager.sendMessage(new Event(Property.DID_STOP));
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
