/**
*	TestCollidableObject.java
*
*	@author Johan
*/

package model;

import static org.junit.Assert.*;


import org.junit.Before;
import org.junit.Test;

import constants.Direction;

import model.CollidableObject;
import model.character.Character;
import model.character.Player;

public class TestCollidableObject {

	private CollidableObject obj;
	
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
		CollidableObject safeObject = new Wall(0,0);
		
	}
	
	@Test
	public void testCharacterCollision() {
		Character player = new Player();
		player.setDirection(Direction.EAST);
		player.move(1.0);
		player.setDirection(Direction.SOUTH);
		player.move(1.0);
		
		this.obj.setPosition(15, 10);
		
		assertTrue(this.obj.isColliding(player));
	}

}
