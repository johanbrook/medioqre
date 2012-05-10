package event;

/**
 * A single event on the event bus.
 * 
 * @author Joachim von Hacht
 * @modified Johan Brook, 2012-03-26
 */
public class Event {

	/**
	 * Enum describing different event property names, i.e. the different types
	 * of events.
	 * 
	 * @author Johan
	 */
	public enum Property {
		WAS_CREATED, CHANGED_DIRECTION, CHANGED_WEAPON, WAS_DESTROYED, DID_MOVE, DID_STOP, WAS_DAMAGED, DID_ATTACK, DID_FIRE, PICKED_UP_ITEM, INIT_MODEL, WEAPON_MENU_SHOW, WEAPON_MENU_HIDE, FIRED_WEAPON_SUCCESS, FIRED_WEAPON_FAIL, NEW_WAVE, GAME_OVER, NEW_GAME, PORTAL_CREATED
	};

	private final Property name;
	private final Object value;

	/**
	 * Create a new event, with a name and value.
	 * 
	 * @param property
	 *            The name, or "identifier"
	 * @param value
	 *            The value, or object associated with the event
	 */
	public Event(Property property, Object value) {
		this.name = property;
		this.value = value;
	}

	/**
	 * Create an event without an associated value.
	 * 
	 * @param property
	 *            The property name
	 */
	public Event(Property property) {
		this.name = property;
		this.value = null;
	}

	/**
	 * Get the event's property name.
	 * 
	 * @return The property name
	 */
	public Property getProperty() {
		return this.name;
	}

	/**
	 * Get the event's value. Is an <code>Object</code>, so be sure to typecast
	 * correctly.
	 * 
	 * @return The event's value
	 */
	public Object getValue() {
		return this.value;
	}

	@Override
	public String toString() {
		return "Event: [ " + this.name + " : " + this.value + " ]";
	}
}
