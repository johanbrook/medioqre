package controller;

import java.beans.PropertyChangeListener;

/**
 * Implementations of this interface are observable, i.e. they can add observers and
 * fire when properties change in their class.
 * 
 * @author Johan
 *
 */
public interface IObservable {
	
	/**
	 * Add an observer.
	 * 
	 * @param listener An observer
	 */
	public void addObserver(PropertyChangeListener listener);
	
	/**
	 * Remove all observers.
	 */
	public void removeAllObservers();
	
	/**
	 * Remove a specifik observer.
	 * 
	 * @param listener The observer to be removed
	 */
	public void removeObserver(PropertyChangeListener listener);
}
