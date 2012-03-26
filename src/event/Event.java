package event;

/**
*	A single event on the event bus.
*
*	@author Joachim von Hacht
*	@modified Johan Brook, 2012-03-26
*/
public class Event {

	public enum Property {
		WAS_CREATED,
		CHANGED_DIRECTION,
		CHANGED_WEAPON,
		WAS_DESTROYED,
		DID_MOVE,
		DID_STOP,
		WAS_DAMAGED,
		DID_ATTACK,
		PICKED_UP_ITEM;
	}
	
	private final Property name;
	private final Object value;
	
	public Event(Property property, Object value) {
		this.name = property;
		this.value = value;
	}
	
	public Property getProperty() {
		return this.name;
	}
	
	public Object getValue() {
		return this.value;
	}
	
	@Override
	public String toString() {
		return "Event: [ "+this.name+" : "+this.value+" ]";
	}
}
