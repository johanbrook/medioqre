/**
*	TestWeapon.java
*
*	@author Johan
*/

package model;

import static org.junit.Assert.*;

import java.awt.Dimension;
import java.awt.Rectangle;

import model.character.Player;
import model.weapon.*;
import model.weapon.Projectile.Range;

import org.junit.Before;
import org.junit.Test;



public class TestWeapon {

	private AbstractWeapon machineGun;
	private AbstractWeapon grenade;
	private AbstractWeapon sword;
	private AbstractWeapon melee;
	
	@Before
	public void setUp() throws Exception {
		Player p = new Player(30, new Rectangle(20, 20), new Dimension(20, 48), 0, 16);
		this.machineGun = new MachineGun(p, 300);
		this.machineGun.setProjectile(new Projectile(machineGun, 10, 10, 10, Range.FAR_RANGE, 30));
		
		this.grenade = new Grenade(p, 4);
		this.grenade.setProjectile(new Projectile(grenade, 10, 10, 40, Range.MEDIUM_RANGE, 30));
		
		this.sword = new Sword(p, -1);
		this.sword.setProjectile(new Projectile(sword, 10, 10, 100, Range.SHORT_RANGE, 30));
		
		this.melee = new Melee(p, -1);
		this.melee.setProjectile(new Projectile(melee, 10, 10, 100, Range.SHORT_RANGE, 30));
	}
	
	@Test
	public void addAmmo() {
		int initialAmmo = this.machineGun.getCurrentAmmo();
		this.machineGun.addAmmo(10);
		
		assertEquals(initialAmmo + 10, this.machineGun.getCurrentAmmo());
	}

	@Test
	public void testMachineGun() {
		assertNotNull(this.machineGun.fire());
	}
	
	@Test
	public void testSword() {
		assertNotNull(this.sword.fire());
	}
	
	@Test
	public void testGrenade() {
		assertNotNull(this.grenade.fire());
	}
	
	@Test
	public void testMelee() {
		assertNotNull(this.melee.fire());
	}
	
	@Test
	public void testAmmo(){
		this.grenade.fire();
		this.grenade.fire();
		this.grenade.fire();
		this.grenade.fire();
		
		assertNull(this.grenade.fire());
		assertTrue(this.grenade.getCurrentAmmo() == 0);
	}
	

}
