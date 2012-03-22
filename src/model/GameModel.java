package model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import model.character.*;
import model.character.Character;
import constants.Direction;
import controller.IObservable;

/**
 * Model for a game.
 * 
 * @author Johan
 *
 */
public class GameModel implements IObservable {
	
	private Character player;
	private PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	
	public GameModel() {
		this.player = new Player(10);
	}
	
	
	/**
	 * Updates the player's direction.
	 * 
	 * @param dir The direction
	 * @see Direction
	 */
	public void updateDirection(Direction dir) {
		Direction oldDir = this.player.getDirection();
		this.player.setDirection(dir);
		this.pcs.firePropertyChange("playerDidChangeDirection", oldDir, dir);
	}
	
	
	@Override
	public void addObserver(PropertyChangeListener listener) {
		this.pcs.addPropertyChangeListener(listener);
	}

	@Override
	public void removeAllObservers() {
		for(PropertyChangeListener l : this.pcs.getPropertyChangeListeners()) {
			this.pcs.removePropertyChangeListener(l);
		}
	}

	@Override
	public void removeObserver(PropertyChangeListener listener) {
		this.pcs.removePropertyChangeListener(listener);
	}
	
}
