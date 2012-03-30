/**
*	TestPlayer.java
*
*	@author Johan
*/

package model;

import static org.junit.Assert.*;

import model.character.Player;
import model.weapon.*;

import org.junit.Before;
import org.junit.Test;



public class TestPlayer {

	private Player player;
	
	@Before
	public void setUp() throws Exception {
		this.player = new Player();
	}
	
	@Test
	public void testFireWeapon(){
		assertNotNull(this.player.attack());
	}
	
	@Test
	public void testOutOfAmmo(){
		this.player.setCurrentWeapon(Grenade.class);
		this.player.attack();
		this.player.attack();
		this.player.attack();
		this.player.attack();
		
		assertEquals(null, this.player.attack());
		assertEquals(0, this.player.getCurrentWeapon().getCurrentAmmo());
	}
	
	@Test
	public void testSetHealth() {
		int initialHealth = this.player.getHealth();
		this.player.setHealth(initialHealth + 10);
		
		assertEquals(initialHealth + 10, this.player.getHealth());
	}
	
	@Test
	public void testAddHealth() {
		int initialHealth = this.player.getHealth();
		this.player.addHealth(10);
		
		assertEquals(initialHealth + 10, this.player.getHealth());
	}
	
	@Test
	public void testSetCurrentWeapon(){
		AbstractWeapon w = new Sword();
		this.player.setCurrentWeapon(Sword.class);
		
		assertEquals(w, this.player.getCurrentWeapon());
	}
	
	

}