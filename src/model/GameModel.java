package model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import model.character.*;
import model.character.Character;
import constants.Direction;

/**
 * Model for a game.
 * 
 * @author Johan
 *
 */
public class GameModel {
	
	private Character player;
	private PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	
	public GameModel() {
		this.player = new Player(10);
	}
	
	public void updateDirection(Direction dir) {
		Direction oldDir = this.player.getDirection();
		this.player.setDirection(dir);
		this.pcs.firePropertyChange("playerDidChangeDirection", oldDir, dir);
	}
	
	
	public void addObserver(PropertyChangeListener listener) {
		this.pcs.addPropertyChangeListener(listener);
	}
	
}
