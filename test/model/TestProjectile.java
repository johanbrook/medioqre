/**
*	TestProjectile.java
*
*	@author Johan
*/

package model;

import static org.junit.Assert.*;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import model.character.AbstractCharacter;
import model.character.Player;
import model.weapon.MachineGun;
import model.weapon.Projectile;
import model.weapon.Projectile.Range;

import org.junit.Before;
import org.junit.Test;

import constants.Direction;

public class TestProjectile {

	private Projectile projectile;
	private AbstractCharacter owner;
	
	@Before
	public void setUp() throws Exception {
		this.owner = new Player(30, new Rectangle(20, 20), new Dimension(20, 48), 0, 16);
		this.owner.setPosition(10, 10);
		this.owner.setDirection(Direction.EAST);
		
		this.projectile = new Projectile(new MachineGun(this.owner, 300,2, 2), 10, 10, 30, Range.FAR_RANGE, 30); 
	}

//	@Test
//	public void testProjectilePosition() {
//		// A projectile should be at its parent weapon's owner's position when created:
//		
//		assertEquals(new Point(10, 10), this.projectile.getPosition());
//	}
	
	
	@Test
	public void testDistanceTravelledHorizontal() {
		int speed = this.projectile.getMovementSpeed();
		
		this.projectile.move(1.0);
		
		// Should've moved speed*1.0
		assertEquals(speed*1.0, this.projectile.getDistanceTravelled(), 0.5);
	}
	
	@Test
	public void testDistanceTravelledVertical() {
		int speed = this.projectile.getMovementSpeed();
		
		this.projectile.setDirection(Direction.SOUTH);
		this.projectile.move(1.0);
		
		// Should've moved speed*1.0
		assertEquals(speed*1.0, this.projectile.getDistanceTravelled(), 0.5);
	}

}
