/**
 *	TestCollidableObject.java
 *
 *	@author Johan
 */

package model;

import static org.junit.Assert.*;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.sql.Savepoint;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import event.Event;
import event.EventBus;
import event.IEventHandler;

import tools.Logger;


import model.CollidableObject;
import model.character.AbstractCharacter;
import model.character.Player;

public class TestCollidableObject implements IEventHandler {

	private CollidableObject obj;
	private AbstractCharacter player;
	private Event catchedEvent;

	@Before
	public void setUp() throws Exception {
		this.obj = new ConcreteCollidableObject(new Rectangle(20, 20), new Dimension(20, 48), 0, 16);
		
		// I chose Player since then the sizes would be the same
		this.player = new Player(30, new Rectangle(20, 20), new Dimension(20, 48), 0, 16);
		EventBus.INSTANCE.register(this);
	}

	@Test
	public void testIsColliding() {

		CollidableObject collidingObject1 = new ConcreteCollidableObject(
				new Rectangle(10, 10, 10, 10), new Dimension(10, 10), 0, 0); 
		// The obj wxh is 16x16, and the wall wxh is 10x10

		CollidableObject collidingObject2 = new ConcreteCollidableObject(
				new Rectangle(15, 15, 10, 10), new Dimension(10, 10), 0, 0);

		assertTrue(this.obj.isColliding(collidingObject1));
		assertTrue(this.obj.isColliding(collidingObject2));
	}

	@Test
	public void testIsNotColliding() {
		CollidableObject safeObject = new ConcreteCollidableObject(
				new Rectangle(30, 30, 10, 10), new Dimension(10, 10), 0, 0);

		assertFalse(this.obj.isColliding(safeObject));
	}

	@Test
	public void testMovementCollision() {
		int playerSpeed = this.player.getMovementSpeed();

		this.player.setDirection(Direction.EAST);
		this.player.move(1.0);
		this.player.setDirection(Direction.SOUTH);
		this.player.move(1.0);

		this.obj.setPosition(1 * playerSpeed, 1 * playerSpeed);

		assertTrue(this.player.isColliding(this.obj));
	}

	@Test
	public void testEntityCollisionDirectionFromNorth() {

		this.player.setPosition(0, this.player.getSize().height + 1); // obj
																		// collides
																		// from
																		// top
		Direction dir = this.player.getDirectionOfObject(this.obj);

		assertEquals(Direction.NORTH, dir);
	}

	@Test
	public void testEntityCollisionDirectionFromWest() {

		this.player.setPosition(1, 0); // obj collides from left
		Direction dir = this.player.getDirectionOfObject(this.obj);

		assertEquals(Direction.WEST, dir);
	}

	@Test
	public void testEntityCollisionDirectionFromEast() {
		// Remember the player width/height!

		int totalWidth = (int) this.player.getCollisionBox().getMaxX() + 1;

		this.player.setPosition(-totalWidth, 0); // obj collides from right
		Direction dir = this.player.getDirectionOfObject(this.obj);

		assertEquals(Direction.EAST, dir);
	}

	@Test
	public void testEntityCollisionDirectionFromSouth() {
		// Remember the player width/height!
		int totalHeight = this.player.getCollisionBox().height
				+ this.player.getOffsetY() + 2;

		this.player.setPosition(0, -totalHeight); // obj collides from bottom
		Direction dir = this.player.getDirectionOfObject(this.obj);

		assertEquals(Direction.SOUTH, dir);
	}
	
	@Test
	public void testDestroy() {
		this.obj.destroy();
		assertEquals(Event.Property.WAS_DESTROYED, this.catchedEvent.getProperty());
	}
	
	@Override
	public void onEvent(Event evt) {
		this.catchedEvent = evt;
	}
}
