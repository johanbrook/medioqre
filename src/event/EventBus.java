package event;

import java.util.ArrayList;
import java.util.List;

import event.Event.Property;

import tools.Logger;

/**
 * EventBus.java
 * 
 * @author Joachim von Hacht
 * @modified Johan Brook, 2012-03-26
 */
public enum EventBus {
	INSTANCE;

	private final List<IEventHandler> handlers = new ArrayList<IEventHandler>();

	/**
	 * Registers a event handler to the event bus.
	 * 
	 * Use this method to let handlers hook into the bus and listen for all
	 * events going through.
	 * 
	 * @param handler
	 *            The handler
	 */
	public void register(IEventHandler handler) {
		this.handlers.add(handler);
	}

	/**
	 * Remove an event handler from the bus.
	 * 
	 * @param handler
	 *            The handler
	 */
	public void remove(IEventHandler handler) {
		int index = this.handlers.indexOf(handler);
		this.handlers.remove(index);
	}

	/**
	 * Remove all event handlers from the bus
	 * 
	 */
	public void removeAll() {
		this.handlers.clear();
	}

	/**
	 * Publish an event to the bus.
	 * 
	 * Pushes an event object onto the bus to let all handlers know about it.
	 * 
	 * @param event
	 *            The event
	 */
	public void publish(Event event) {
		for (IEventHandler handler : handlers) {
			handler.onEvent(event);
		}
	}

	/**
	 * Return all registered handlers of the bus.
	 * 
	 * @return The list of handlers
	 */
	public List<IEventHandler> getHandlers() {
		return this.handlers;
	}
}
