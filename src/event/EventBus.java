package event;

import java.util.ArrayList;
import java.util.List;

/**
*	EventBus.java
*
*	@author Joachim von Hacht
*	@modified Johan Brook, 2012-03-26
*/
public enum EventBus {
	INSTANCE;
	
	private final List<IEventHandler> handlers = new ArrayList<IEventHandler>();
	
	
	public void register(IEventHandler handler) {
		this.handlers.add(handler);
	}
	
	public void remove(IEventHandler handler) {
		int index = this.handlers.indexOf(handler);
		this.handlers.remove(index);
	}
	
	public void removeAll() {
		this.handlers.clear();
	}
	
	public void publish(Event event) {
		for(IEventHandler handler : handlers) {
			handler.onEvent(event);
		}
	}
}
