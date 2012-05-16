/**
*	TestEntity.java
*
*	@author Johan
*/

package model;

import static org.junit.Assert.*;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import model.character.Player;
import model.weapon.Portal;
import model.weapon.PortalGun.Mode;

import org.junit.Before;
import org.junit.Test;

import event.Event;
import event.EventBus;
import event.IEventHandler;

public class TestEntity implements IEventHandler {

	private Entity ent;
	private Event catchedEvent;
	
	@Before
	public void setUp() throws Exception {
		this.ent = new Player(10, new Rectangle(), new Dimension(), 0, 0);
		EventBus.INSTANCE.register(this);
	}

	@Test
	public void testSetMovementSpeed() {
		this.ent.setMovementSpeed(20);
		assertEquals(20, this.ent.getMovementSpeed());
	}
	
	@Test
	public void changeDirection() {
		this.ent.setDirection(Direction.EAST);
		
		assertEquals(Direction.EAST, this.ent.getDirection());
		assertEquals(Event.Property.CHANGED_DIRECTION, this.catchedEvent.getProperty());
	}
	
	@Test
	public void testMove() {
		Point oldPos = this.ent.getPosition();
		this.ent.move(1.0);
		assertEquals(Event.Property.DID_MOVE, this.catchedEvent.getProperty());
		
		assertNotSame(oldPos, this.ent.getPosition());
	}

	@Test
	public void testStop() {
		this.ent.start();
		assertTrue(this.ent.isMoving());
		
		this.ent.stop();
		assertFalse(this.ent.isMoving());
	}
	
	@Override
	public void onEvent(Event evt) {
		this.catchedEvent = evt;
	}
}
