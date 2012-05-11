/**
*	TestWeaponbelt.java
*
*	@author Johan
*/

package model;

import static org.junit.Assert.*;

import java.util.Arrays;

import model.weapon.AbstractWeapon;
import model.weapon.Grenade;
import model.weapon.MachineGun;
import model.weapon.Melee;
import model.weapon.PortalGun;
import model.weapon.Sword;
import model.weapon.WeaponBelt;

import org.junit.Before;
import org.junit.Test;

public class TestWeaponbelt {

	private WeaponBelt belt;
	
	@Before
	public void setUp() throws Exception {
		AbstractWeapon[] weapons = new AbstractWeapon[] {
				new MachineGun(null, 300, 1, 0),
				new Melee(null, -1, 1, 0),
				new PortalGun(null, -1, 1, 0),
				new Grenade(null, 4, 1, 0)
		};
		
		this.belt = new WeaponBelt(Arrays.asList(weapons));
	}

	@Test
	public void testAddWeapon() {
		assertTrue(this.belt.addWeapon(new MachineGun(null, 300, 1, 0)));
	}
	
	
	@Test
	public void testGetWeaponFromSlot() {
		assertNotNull(this.belt.getWeapon(0));
	}
	
	@Test
	public void testGetSpecificWeapon() {
		AbstractWeapon machinegun = new MachineGun(null, 300, 1, 0);
		
		assertEquals(machinegun, this.belt.getWeapon(0));
		assertEquals(machinegun, this.belt.getWeapon(MachineGun.class));
	}
	
	@Test
	public void testGetNonExistingWeapon() {
		assertNull(this.belt.getWeapon(Sword.class));
	}
	
	@Test(expected=IndexOutOfBoundsException.class)
	public void testGetNonExistingIndex() {
		this.belt.getWeapon(10);
	}

}
