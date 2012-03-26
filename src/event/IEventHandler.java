package event;

/**
*	Classes implementing this interface are event handlers,
*	i.e. they are registered to listen for events.
*
*	@author Joachim von Hacht
*	@modified Johan Brook, 2012-03-26
*/
public interface IEventHandler {
	
	/**
	 * Event callback.
	 * 
	 * @param evt The event object containing info
	 */
	public void onEvent(Event evt);
}
