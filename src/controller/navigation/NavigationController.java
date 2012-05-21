package controller.navigation;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

import model.Direction;

import event.Event;
import event.EventBus;
import event.IEventHandler;
import event.IMessageListener;
import event.IMessageSender;
import event.Event.Property;
import event.Messager;

/**
 * The class responsible for handling navigational events.
 * 
 */
public class NavigationController implements KeyListener, IMessageSender {

	private NavigationKeyQueue navKeys;
	private NavigationKeyQueue cachedKeys;
	private Map<Integer, Key> keyMap;
	private Map<NavigationKey, Integer> weaponMap;

	private Messager messager = new Messager();

	private boolean isQuick = false;

	private long start;
	private final double EPSILON = 0.2;

	/** True if the navigation keys should be active, i.e. used for navigating */
	private boolean doNavigation = true;

	private NavigationKey up;
	private NavigationKey down;
	private NavigationKey left;
	private NavigationKey right;
	private Key shoot;
	private Key weaponModifier;
	private Key modeSwitcher;
	private Key pause;

	/**
	 * Create a new NavigationController.
	 */
	public NavigationController() {
		initKeys();

		// Map keys to certain slots in the weapon chooser. These
		// slots should later map to weapons in the belt.
		this.weaponMap = new HashMap<NavigationKey, Integer>();
		this.weaponMap.put(up, 0);
		this.weaponMap.put(left, 1);
		this.weaponMap.put(right, 2);
		this.weaponMap.put(down, 3);
	}

	@Override
	public void addReceiver(IEventHandler listener) {
		this.messager.addListener(listener);
	}

	/**
	 * Create the keyboard keys in the game.
	 */
	private void initKeys() {
		this.navKeys = new NavigationKeyQueue(2);

		this.cachedKeys = new NavigationKeyQueue(2);
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
			public void off() {}
		});

		this.weaponModifier = new Key("chose_weapon", new Callable() {
			@Override
			public void on() {
				doNavigation = false;
				tools.Logger.log("Navigation is inactive");
				EventBus.INSTANCE.publish(new Event(Property.WEAPON_MENU_SHOW));
			}

			@Override
			public void off() {
				doNavigation = true;
				tools.Logger.log("Navigation is active");
				EventBus.INSTANCE.publish(new Event(Property.WEAPON_MENU_HIDE));
			}
		});
		
		this.modeSwitcher = new Key("switch_mode", new Callable() {
			
			@Override
			public void on() {
				messager.sendMessage(new Event(Property.PORTALGUN_SWITCHED_MODE));
			}
			
			@Override
			public void off() {}
		});
		
		this.pause = new Key("pause_game", new Callable() {
			
			@Override
			public void on() {
				messager.sendMessage(new Event(Property.TOGGLE_PAUSE));
			}
			
			@Override
			public void off() {}
		});

		this.keyMap.put(KeyEvent.VK_W, this.up);
		this.keyMap.put(KeyEvent.VK_A, this.left);
		this.keyMap.put(KeyEvent.VK_S, this.down);
		this.keyMap.put(KeyEvent.VK_D, this.right);
		this.keyMap.put(KeyEvent.VK_SPACE, this.shoot);
		this.keyMap.put(KeyEvent.VK_SHIFT, this.weaponModifier);
		this.keyMap.put(KeyEvent.VK_Q, this.modeSwitcher);
		this.keyMap.put(KeyEvent.VK_ESCAPE, this.pause);
	}

	/**
	 * Refresh the target's direction based on the keys in the navigation key
	 * list.
	 */
	private void refreshDirection() {

		if (this.isQuick) {
			NavigationKey composite = this.cachedKeys.createCompositeKey();

			if (composite != null)
				messager.sendMessage(new Event(Property.CHANGED_DIRECTION,
						composite.getDirection()));
		}

		else if (this.navKeys.hasMultiple()) {
			Direction dir;
			NavigationKey composite = this.navKeys.createCompositeKey();

			// Diagonal direction
			if (composite != null) {
				dir = composite.getDirection();
			}

			else {
				dir = this.navKeys.last().getDirection();
			}

			messager.sendMessage(new Event(Property.CHANGED_DIRECTION, dir));
		} else if (this.navKeys.size() == 1) {
			messager.sendMessage(new Event(Property.CHANGED_DIRECTION,
					this.navKeys.first().getDirection()));
		}
	}

	/**
	 * If two nav keys are released very close to each other in time, the action
	 * is a quick one. Used for determining diagonal directions.
	 */
	private void checkIsQuick() {
		if (this.navKeys.size() == 1 && this.cachedKeys.hasMultiple()) {
			double seconds = ((System.nanoTime() - start) / 10E8);
			this.isQuick = seconds < EPSILON;
		} else if (this.navKeys.size() == 2) {
			this.start = System.nanoTime();
		}
	}

	/**
	 * Tell the model about the currently selected weapon from the HUD menu. Nav
	 * keys maps to weapon slots.
	 * 
	 * @param key
	 *            The nav key released
	 */
	private void chooseWeapon(NavigationKey key) {

		if (this.weaponMap.get(key) != null) {
			this.messager.sendMessage(new Event(Property.CHANGED_WEAPON,
					this.weaponMap.get(key)));
		}
	}
	
	/**
	 * Keypressed callback.
	 */
	@Override
	public void keyPressed(KeyEvent evt) {
		Key a = this.keyMap.get(evt.getKeyCode());

		if (a != null) {

			if (a instanceof NavigationKey) {
				this.cachedKeys.clear();
				this.isQuick = false;
				this.navKeys.add((NavigationKey) a);

				if (this.doNavigation) {
					refreshDirection();
				}

			} else {
				a.fire();
			}
		}
	}

	/**
	 * Key released callback.
	 */
	@Override
	public void keyReleased(KeyEvent evt) {

		Key a = this.keyMap.get(evt.getKeyCode());

		if (a != null) {

			if (a instanceof NavigationKey) {
				this.cachedKeys.add((NavigationKey) a);
				checkIsQuick();
				this.navKeys.remove(a);

				if (this.doNavigation) {
					refreshDirection();
				} else {
					chooseWeapon((NavigationKey) a);
				}
			} else {
				a.fireUp();
			}
		}

		if (this.navKeys.isEmpty()) {
			messager.sendMessage(new Event(Property.DID_STOP));
		}

	}

	@Override
	public void keyTyped(KeyEvent evt) {

	}

}
