package model;

import static org.junit.Assert.*;

import java.util.Arrays;

import model.character.Player;
import model.weapon.AbstractWeapon;
import model.weapon.Grenade;
import model.weapon.Projectile;
import model.weapon.Projectile.Range;
import model.weapon.MachineGun;
import model.weapon.Sword;
import model.weapon.WeaponBelt;

import org.junit.Before;
import org.junit.Test;

/**
*	TestWeaponBelt.java
*
*	@author Johan
*	@deprecated 2012-04-26
*/
public class TestWeaponBelt {

	private WeaponBelt belt;
	private Player owner;
	
	@Before
	public void setUp() throws Exception {
		this.belt = new WeaponBelt();
		this.owner = new Player();
	}
	

	@Test
	public void testGetWeapon() {
		AbstractWeapon weapon = this.belt.getWeapon(0);
		
		assertEquals(new MachineGun(this.owner), weapon);
	}
	
	@Test
	public void testGetSpecificWeapon() {
		AbstractWeapon weapon = this.belt.getWeapon(MachineGun.class);
		
		assertEquals(new MachineGun(this.owner), weapon);
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
		AbstractWeapon[] coll = {new Sword(this.owner), new MachineGun(this.owner), new Grenade(this.owner)};
		WeaponBelt customBelt = new WeaponBelt(Arrays.asList(coll));
		
		assertEquals(new Sword(this.owner), customBelt.getWeapon(0));
		assertEquals(new MachineGun(this.owner), customBelt.getWeapon(1));
		assertEquals(new Grenade(this.owner), customBelt.getWeapon(2));
	}
	
	
	
	/**
	 * Nonsense class used for testing
	 * 
	 * @author Johan
	 *
	 */
	private class NonsenseWeapon extends AbstractWeapon {

		public NonsenseWeapon() {
			super(owner, 300);
		}

		@Override
		public Projectile getProjectile() {
			return new Projectile(belt.getWeapon(NonsenseWeapon.class), 10, 10, 30, Range.FAR_RANGE, 30);
		}
		
	}

}
