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

	private IWeapon rifle;
	private IWeapon grenade;
	private IWeapon sword;
	private IWeapon portal;
	
	@Before
	public void setUp() throws Exception {
		this.rifle = new MachineGun();
		this.grenade = new Grenade();
		this.sword = new Sword();
		this.portal = new PortalGun();
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
