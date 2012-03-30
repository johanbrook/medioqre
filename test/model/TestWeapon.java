/**
*	TestWeapon.java
*
*	@author Johan
*/

package model;

import static org.junit.Assert.*;

import model.weapon.*;

import org.junit.Before;
import org.junit.Test;



public class TestWeapon {

	private AbstractWeapon machineGun;
	private AbstractWeapon grenade;
	private AbstractWeapon sword;
	private AbstractWeapon melee;
	private AbstractWeapon portal;
	
	@Before
	public void setUp() throws Exception {
		this.machineGun = new MachineGun();
		this.grenade = new Grenade();
		this.sword = new Sword();
		this.melee = new Melee();
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
