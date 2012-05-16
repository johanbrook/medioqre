package event;

/**
 * A message sender.
 * 
 * @author Johan
 */
public interface IMessageSender {

	/**
	 * Add a receiver, "listener".
	 * 
	 * @param listener
	 *            The receiver
	 */
	public void addReceiver(IEventHandler listener);
}
