package event;

/**
 * A message listener.
 * 
 * @author Johan
 * @deprecated Use IEventHandler instead (2012-05-16).
 */
public interface IMessageListener {

	/**
	 * Message callback. Senders call this method with an event object.
	 * 
	 * @param evt
	 *            The event object
	 */
	public void onMessage(Event evt);
}
