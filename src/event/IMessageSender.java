/**
*	IMessageSender.java
*
*	@author Johan
*/

package event;

public interface IMessageSender {
	public void addReceiver(IMessageListener listener);
}
