package event;

/**
*	A message listener.
*
*	@author Johan
*/
public interface IMessageListener {
	
	/**
	 * Message callback. Senders call this method with an 
	 * event object.
	 * 
	 * @param evt The event object
	 */
	public void onMessage(Event evt);
}
