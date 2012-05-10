package controller.navigation;

import model.character.Player;
import constants.Direction;

/**
 * Callable interface.
 * 
 * @author Johan
 */
interface Callable {
	public void on();
	public void off();
}

/**
 * Key class for representing a single action key.
 * 
 * @author Johan
 */
public class Key {
	private final String key;
	protected final Callable callback;

	/**
	 * Create a new key with a name and callback, which is called on action.
	 * 
	 * @param name
	 *            The name
	 * @param callback
	 *            A callback
	 */
	public Key(String name, Callable callback) {
		this.key = name;
		this.callback = callback;
	}

	/**
	 * Trigger the callback's down event.
	 */
	public void fire() {
		if (callback != null)
			callback.on();
	}

	/**
	 * Trigger the callback's up event.
	 */
	public void fireUp() {
		if (callback != null)
			callback.off();
	}

	/**
	 * Get the key name.
	 * 
	 * @return The name
	 */
	public String getKey() {
		return this.key;
	}

	@Override
	public String toString() {
		return this.key;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		Key other = (Key) o;
		return this.key.equals(other.key);
	}
}

/**
 * A navigation key with a direction.
 * 
 * @author Johan
 * 
 */
class NavigationKey extends Key {

	private final Direction direction;

	/**
	 * Create a new navigation key.
	 * 
	 * @param name
	 *            The name
	 * @param callback
	 *            The callback
	 * @param dir
	 *            The associated direction
	 */
	public NavigationKey(String name, Callable callback, Direction dir) {
		super(name, callback);
		this.direction = dir;
	}

	/**
	 * Get the direction.
	 * 
	 * @return The key's direction
	 */
	public Direction getDirection() {
		return this.direction;
	}

	@Override
	public boolean equals(Object o) {
		return super.equals(o)
				&& this.direction == ((NavigationKey) o).direction;
	}
}
