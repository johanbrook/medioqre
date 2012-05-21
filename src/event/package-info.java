/**
*	Event classes.
*
*	Includes all code related to the event handling in the system.
*
*	<p>The system uses two kinds of event handling as of now: the global event bus and
*	the tighter coupled messaging system. The event bus is meant for global communication
*	between the model and the view, while the messaging implementation is for the different
*	models talking to each other. This is used instead of the more heavy-weighted 
*	<code>PropertyChangeListener</code> in Java.</p>
*/
package event;