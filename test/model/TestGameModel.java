/**
*	TestGameModel.java
*
*	@author Johan
*/

package model;

import static org.junit.Assert.*;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import constants.Direction;

public class TestGameModel implements PropertyChangeListener {

	private GameModel game;
	private boolean notified;
	private Direction oldDir;
	private Direction newDir;
	
	@Before
	public void setUp() throws Exception {
		this.notified = false;
		this.game = new GameModel();
		this.game.addObserver(this);
	}

	@Test
	public void testFirePropertyChange() {
		this.game.updateDirection(Direction.NORTH);
		assertTrue(this.notified);
	}
	
	@Test
	public void testUpdateDirection() {
		this.game.updateDirection(Direction.NORTH);
		
		assertEquals(Direction.NORTH, this.newDir);
	}
	

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		this.notified = true;
		this.oldDir = (Direction) evt.getOldValue();
		this.newDir = (Direction) evt.getNewValue();
	}
	
	@After
	public void cleanUp() {
		this.game.removeObserver(this);
	}

}
