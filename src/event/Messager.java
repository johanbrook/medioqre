package event;

import java.util.LinkedList;
import java.util.List;

/**
*	Messager class. Sends messages with events to its listeners.
*
*	@author Johan
*/
public class Messager {
	
	private List<IMessageListener> listeners;
	
	/**
	 * Create a new messager.
	 */
	public Messager() {
		listeners = new LinkedList<IMessageListener>();
	}
	
	/**
	 * Add a listener.
	 * 
	 * @param listener The listener.
	 */
	public void addListener(IMessageListener listener) {
		listeners.add(listener);
	}
	
	/**
	 * Send a message.
	 * 
	 * @param evt The event
	 */
	public void sendMessage(Event evt) {
		for(IMessageListener l : listeners) {
			l.onMessage(evt);
		}
	}
	
}
