/**
*	TestWeaponBelt.java
*
*	@author Johan
*/

package model;

import static org.junit.Assert.*;

import java.util.Arrays;

import model.weapon.AbstractWeapon;
import model.weapon.Grenade;
import model.weapon.Projectile;
import model.weapon.Projectile.Range;
import model.weapon.MachineGun;
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
		AbstractWeapon weapon = this.belt.getWeapon(0);
		
		assertEquals(new MachineGun(), weapon);
	}
	
	@Test
	public void testGetSpecificWeapon() {
		AbstractWeapon weapon = this.belt.getWeapon(MachineGun.class);
		
		assertEquals(new MachineGun(), weapon);
	}
	
	
	@Test
	public void testGetNonexistentWeapon() {
		AbstractWeapon random = this.belt.getWeapon(NonsenseWeapon.class);
		
		assertNull(random);
	}
	
	@Test(expected=IndexOutOfBoundsException.class)
	public void testGetNonexistentIndex() {
		this.belt.getWeapon(5);
	}
	
	
	@Test
	public void testCreateNewBeltFromArray() {
		AbstractWeapon[] coll = {new Sword(), new MachineGun(), new Grenade()};
		WeaponBelt customBelt = new WeaponBelt(Arrays.asList(coll));
		
		assertEquals(new Sword(), customBelt.getWeapon(0));
		assertEquals(new MachineGun(), customBelt.getWeapon(1));
		assertEquals(new Grenade(), customBelt.getWeapon(2));
	}
	
	
	
	/**
	 * Nonsense class used for testing
	 * 
	 * @author Johan
	 *
	 */
	private class NonsenseWeapon extends AbstractWeapon {

		public NonsenseWeapon() {
			super(300, new Projectile(10, Range.FAR_RANGE));
		}
		
	}

}
