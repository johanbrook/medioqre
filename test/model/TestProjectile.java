/**
*	TestProjectile.java
*
*	@author Johan
*/

package model;

import static org.junit.Assert.*;

import java.awt.Point;

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
		this.owner = new Player();
		this.owner.setPosition(10, 10);
		this.owner.setDirection(Direction.EAST);
		
		this.projectile = new Projectile(new MachineGun(this.owner), 10, 10, 30, Range.FAR_RANGE, 30); 
	}

	@Test
	public void testProjectilePosition() {
		// A projectile should be at its parent weapon's owner's position when created:
		
		assertEquals(new Point(10, 10), this.projectile.getPosition());
	}

}
