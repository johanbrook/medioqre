/**
*	TestPlayer.java
*
*	@author Johan
*/

package model;

import static org.junit.Assert.*;

import model.character.Player;
import model.weapon.*;
import model.weapon.WeaponBelt.WeaponTypes;

import org.junit.Before;
import org.junit.Test;



public class TestPlayer {

	private Player player;
	
	@Before
	public void setUp() throws Exception {
		this.player = new Player(10);
	}
	
	@Test
	public void testFireWeapon(){
		assertNotNull(this.player.fireWeapon());
	}
	
	@Test
	public void testOutOfAmmo(){
		this.player.setCurrentWeapon(WeaponTypes.GRENADE);
		this.player.fireWeapon();
		this.player.fireWeapon();
		this.player.fireWeapon();
		
		assertEquals(null, this.player.fireWeapon());
		assertEquals(0, this.player.getCurrentWeapon().getAmmo());
	}
	
	@Test
	public void testSetCurrentWeapon(){
		IWeapon w = new Sword();
		this.player.setCurrentWeapon(WeaponTypes.SWORD);
		
		assertEquals(w, this.player.getCurrentWeapon());
	}
	
	

}