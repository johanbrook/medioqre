/**
*	TestWeaponBelt.java
*
*	@author Johan
*/

package model;

import static org.junit.Assert.*;

import java.util.Arrays;

import model.weapon.Grenade;
import model.weapon.IWeapon;
import model.weapon.MachineGun;
import model.weapon.PortalGun;
import model.weapon.Sword;
import model.weapon.WeaponBelt;

import org.junit.Before;
import org.junit.Test;

public class TestWeaponBelt {

	private WeaponBelt belt;
	
	@Before
	public void setUp() throws Exception {
		this.belt = new WeaponBelt();
	}
	

	@Test
	public void testGetWeapon() {
		IWeapon weapon = this.belt.getWeapon(0);
		
		assertEquals(new MachineGun(), weapon);
	}
	
	@Test
	public void testGetSpecificWeapon() {
		IWeapon weapon = this.belt.getWeapon(MachineGun.class);
		
		assertEquals(new MachineGun(), weapon);
	}
	
	
	@Test
	public void testGetNonexistentWeapon() {
		IWeapon random = this.belt.getWeapon(NonsenseWeapon.class);
		
		assertNull(random);
	}
	
	@Test(expected=IndexOutOfBoundsException.class)
	public void testGetNonexistentIndex() {
		this.belt.getWeapon(5);
	}
	
	
	@Test
	public void testCreateNewBeltFromArray() {
		IWeapon[] coll = {new Sword(), new MachineGun(), new PortalGun(), new Grenade()};
		WeaponBelt customBelt = new WeaponBelt(Arrays.asList(coll));
		
		assertEquals(new Sword(), customBelt.getWeapon(0));
		assertEquals(new MachineGun(), customBelt.getWeapon(1));
		assertEquals(new PortalGun(), customBelt.getWeapon(2));
		assertEquals(new Grenade(), customBelt.getWeapon(3));
	}
	
	
	
	/**
	 * Nonsense class used for testing
	 * 
	 * @author Johan
	 *
	 */
	private class NonsenseWeapon implements IWeapon {

		@Override
		public boolean fire() {
			return false;
		}

		@Override
		public int getAmmo() {
			return 0;
		}

		@Override
		public int getDamage() {
			return 0;
		}
		
	}

}
