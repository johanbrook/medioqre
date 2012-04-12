/**
*	TestCollidableObject.java
*
*	@author Johan
*/

package model;

import static org.junit.Assert.*;

import java.awt.Point;


import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import tools.Logger;

import constants.Direction;

import model.CollidableObject;
import model.character.Character;
import model.character.Player;

public class TestCollidableObject {

	private CollidableObject obj;
	
	@BeforeClass
	public static void before() {
		Logger.setLogginEnabled(true);
	}
	
	@Before
	public void setUp() throws Exception {
		this.obj = new Wall(0, 0);
	}

	@Test
	public void testIsColliding() {
		
		CollidableObject collidingObject1 = new Wall(0,0);
		CollidableObject collidingObject2 = new Wall(9,9);
		
		assertTrue(this.obj.isColliding(collidingObject1));
		assertTrue(this.obj.isColliding(collidingObject2));
	}
	
	@Test
	public void testIsNotColliding() {
		CollidableObject safeObject = new Wall(20,20);
		
		assertFalse(this.obj.isColliding(safeObject));
	}
	
	@Test
	public void testCharacterCollision() {
		Character player = new Player();
		int playerSpeed = player.getMovementSpeed();
		
		player.setDirection(Direction.EAST);
		player.move(1.0);
		player.setDirection(Direction.SOUTH);
		player.move(1.0);
		
		this.obj.setPosition(1*playerSpeed, 1*playerSpeed);
		
		assertTrue(this.obj.isColliding(player));
	}
	
	@Test
	public void testEntityCollisionDirection() {
		
		Entity e = new Player();
		assertEquals(new Point(0,0), e.getPosition());
		
		e.setPosition(0, 1);	// South
		e.getCollisionDirection(this.obj);
		e.setPosition(1, 0);	// East
		e.getCollisionDirection(this.obj);
		this.obj.setPosition(1, 3);
		e.setPosition(0, 1);	// North
		e.getCollisionDirection(this.obj);
		e.setPosition(-1, 0);	// West
		e.getCollisionDirection(this.obj);
		
	}

}
