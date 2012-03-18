/**
*	TestWeapon.java
*
*	@author Johan
*/

package model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import constants.WeaponType;

public class TestWeapon {

	private Weapon rifle;
	private Weapon grenade;
	private Weapon sword;
	private Weapon portal;
	
	@Before
	public void setUp() throws Exception {
		this.rifle = new Weapon(WeaponType.RIFLE);
		this.grenade = new Weapon(WeaponType.GRENADE);
		this.sword = new Weapon(WeaponType.SWORD);
		this.portal = new Weapon(WeaponType.SWORD);
	}

	@Test
	public void testFire() {
		assertTrue(this.rifle.fire());
		assertTrue(this.grenade.fire());
		assertTrue(this.sword.fire());
		assertTrue(this.portal.fire());
	}
	
	@Test
	public void testGrenade(){
		this.grenade.fire();
		this.grenade.fire();
		this.grenade.fire();
		
		assertFalse(this.grenade.fire());
		assertTrue(this.grenade.getAmmo() == 0);
	}
	

}
