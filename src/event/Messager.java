/**
*	Messager.java
*
*	@author Johan
*/

package event;

import java.util.LinkedList;
import java.util.List;


public class Messager {
	
	private List<IMessageListener> listeners;
	
	public Messager() {
		listeners = new LinkedList<IMessageListener>();
	}
	
	public void addListener(IMessageListener listener) {
		listeners.add(listener);
	}
	
	public void sendMessage(Event evt) {
		for(IMessageListener l : listeners) {
			l.onMessage(evt);
		}
	}
	
}
