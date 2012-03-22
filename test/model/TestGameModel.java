/**
*	TestGameModel.java
*
*	@author Johan
*/

package model;

import static org.junit.Assert.*;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import org.junit.Before;
import org.junit.Test;

public class TestGameModel implements PropertyChangeListener {

	private PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	private GameModel game;
	
	@Before
	public void setUp() throws Exception {
		this.game = new GameModel();
		this.game.addObserver(this);
	}

	@Test
	public void test() {
		fail("Not yet implemented");
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// TODO Auto-generated method stub
		
	}

}
