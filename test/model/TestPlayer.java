/**
*	TestPlayer.java
*
*	@author Johan
*/

package model;

import static org.junit.Assert.*;

import model.characters.Player;

import org.junit.Before;
import org.junit.Test;

import constants.WeaponType;

public class TestPlayer {

	private Player player;
	
	@Before
	public void setUp() throws Exception {
		this.player = new Player();
	}
	
	@Test
	public void testFireWeapon(){
		assertNotNull(this.player.fireWeapon());
	}
	
	@Test
	public void testCreateProjectile(){
		Projectile p = new Projectile(WeaponType.RIFLE);
		
		assertEquals(p, this.player.fireWeapon());
	}
	
	@Test
	public void testOutOfAmmo(){
		this.player.setCurrentWeapon(WeaponType.GRENADE);
		this.player.fireWeapon();
		this.player.fireWeapon();
		this.player.fireWeapon();
		
		assertEquals(null, this.player.fireWeapon());
		assertEquals(0, this.player.getCurrentWeapon().getAmmo());
	}
	
	@Test
	public void testSetCurrentWeapon(){
		Weapon w = new Weapon(WeaponType.RIFLE);
		this.player.setCurrentWeapon(WeaponType.RIFLE);
		
		assertEquals(w, this.player.getCurrentWeapon());
	}
	
	

}