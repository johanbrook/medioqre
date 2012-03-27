/**
*	TestCollidableObject.java
*
*	@author Johan
*/

package model;

import static org.junit.Assert.*;

import java.awt.Rectangle;

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
		Rectangle collisionBox = new Rectangle(10, 10, 10, 10);
		this.obj = new Wall(collisionBox, new Position(0, 0));
	}

	@Test
	public void testIsColliding() {
		Rectangle collidingRect1 = new Rectangle(15, 5, 10, 10);
		Rectangle collidingRect2 = new Rectangle(0, 0, 100, 100);
		
		CollidableObject collidingObject1 = new Wall(collidingRect1, new Position(0,0));
		CollidableObject collidingObject2 = new Wall(collidingRect2, new Position(0,0));
		
		assertTrue(this.obj.isColliding(collidingObject1));
		assertTrue(this.obj.isColliding(collidingObject2));
	}
	
	@Test
	public void testCharacterCollision() {
		Character player = new Player();
		player.setDirection(Direction.EAST);
		player.move(1.0);
		player.setDirection(Direction.SOUTH);
		player.move(1.0);
		
		this.obj.setPosition(15, -10);
		
		System.out.println(this.obj.getPosition());
		System.out.println(player.getPosition());
		
		assertTrue(this.obj.isColliding(player));
	}

}
