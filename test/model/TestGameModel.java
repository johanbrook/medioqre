/**
*	TestGameModel.java
*
*	@author Johan
*/

package model;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import constants.Direction;
import event.Event;
import event.Event.Property;
import event.EventBus;
import event.IEventHandler;
import model.Entity;

public class TestGameModel implements IEventHandler {

	private GameModel game;
	private Direction newDir;
	
	@Before
	public void setUp() throws Exception {
		this.game = new GameModel();
		EventBus.INSTANCE.register(this);
	}

	
	@Test
	public void testUpdateDirection() {
		this.game.getPlayer().setDirection(Direction.NORTH);
		
		assertEquals(Direction.NORTH, this.newDir);
	}

	@Override
	public void onEvent(Event evt) {
		
		if(evt.getProperty() == Property.CHANGED_DIRECTION){
			this.newDir = ((Entity) evt.getValue()).getDirection();
		}
			
	}
	
	@After
	public void cleanUp() {
		EventBus.INSTANCE.remove(this);
	}

}
