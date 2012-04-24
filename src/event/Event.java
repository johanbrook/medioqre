package event;

/**
*	A single event on the event bus.
*
*	@author Joachim von Hacht
*	@modified Johan Brook, 2012-03-26
*/
public class Event {

	/**
	 * Enum describing different event property names, i.e. the 
	 * different types of events.
	 * 
	 * <h3>Types</h3>
	 * 
	 * <ul>
	 * <li>WAS_CREATED</li>
	 * <li>CHANGED_DIRECTION</li>
	 * <li>CHANGED_WEAPON</li>
	 * <li>WAS_DESTROYED</li>
	 * <li>DID_MOVE</li>
	 * <li>DID_STOP</li>
	 * <li>WAS_DAMAGED</li>
	 * <li>DID_ATTACK</li>
	 * <li>PICKED_UP_ITEM</li>
	 * </ul>
	 * 
	 * @author Johan
	 */
	public enum Property {
		WAS_CREATED,
		CHANGED_DIRECTION,
		CHANGED_WEAPON,
		WAS_DESTROYED,
		DID_MOVE,
		DID_STOP,
		WAS_DAMAGED,
		DID_ATTACK,
		PICKED_UP_ITEM,
		INIT_MODEL,
		SHOW_WEAPON_MENU,
		HIDE_WEAPON_MENU;
	}
	
	private final Property name;
	private final Object value;
	
	/**
	 * Create a new event, with a name and value.
	 * 
	 * @param property The name, or "identifier"
	 * @param value The value, or object associated with the event
	 */
	public Event(Property property, Object value) {
		this.name = property;
		this.value = value;
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
	 * Get the event's value. Is an <code>Object</code>, so be sure
	 * to typecast correctly.
	 * 
	 * @return The event's value
	 */
	public Object getValue() {
		return this.value;
	}
	
	@Override
	public String toString() {
		return "Event: [ "+this.name+" : "+this.value+" ]";
	}
}
