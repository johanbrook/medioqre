/**
*	TestCollidableObject.java
*
*	@author Johan
*/

package model;

import static org.junit.Assert.*;

import java.awt.Point;
import java.sql.Savepoint;


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
	private Character player;
	
	@BeforeClass
	public static void before() {
		Logger.setLogginEnabled(true);
	}
	
	@Before
	public void setUp() throws Exception {
		this.obj = new Player();	// I chose Player since then the sizes would be the same
		this.player = new Player();
	}

	@Test
	public void testIsColliding() {
		
		CollidableObject collidingObject1 = new Wall(10,10);	// The obj wxh is 16x16, and the wall wxh is 10x10
		CollidableObject collidingObject2 = new Wall(15,15);
						
		assertTrue(this.obj.isColliding(collidingObject1));
		assertTrue(this.obj.isColliding(collidingObject2));
	}
	
	@Test
	public void testIsNotColliding() {
		CollidableObject safeObject = new Wall(30,30);
				
		assertFalse(this.obj.isColliding(safeObject));
	}
	
	@Test
	public void testMovementCollision() {
		int playerSpeed = this.player.getMovementSpeed();
		
		this.player.setDirection(Direction.EAST);
		this.player.move(1.0);
		this.player.setDirection(Direction.SOUTH);
		this.player.move(1.0);
		
		this.obj.setPosition(1*playerSpeed, 1*playerSpeed);
		
		
		assertTrue(this.player.isColliding(this.obj));
	}
	
	@Test
	public void testEntityCollisionDirectionFromNorth() {
			
		this.player.setPosition(0, this.player.getSize().height + 1);	// obj collides from top
		Direction dir = this.player.getCollisionDirection(this.obj);
		
		assertEquals(Direction.NORTH, dir);
	}
	
	@Test
	public void testEntityCollisionDirectionFromWest() {
		
		this.player.setPosition(1, 0);	// obj collides from left
		Direction dir = this.player.getCollisionDirection(this.obj);
		
		assertEquals(Direction.WEST, dir);
	}
	
	@Test
	public void testEntityCollisionDirectionFromEast() {
		// Remember the player width/height!
		
		int totalWidth = (int) this.player.getCollisionBox().getMaxX() + 1;
		
		this.player.setPosition(-totalWidth, 0);	// obj collides from right
		Direction dir = this.player.getCollisionDirection(this.obj);
		
		assertEquals(Direction.EAST, dir);
	}
	
	@Test
	public void testEntityCollisionDirectionFromSouth() {
		// Remember the player width/height!
		int totalHeight = this.player.getCollisionBox().height + this.player.getOffsetY() + 2;
		
		this.player.setPosition(0, -totalHeight);	// obj collides from bottom
		Direction dir = this.player.getCollisionDirection(this.obj);
		
		assertEquals(Direction.SOUTH, dir);
	}
	

}
