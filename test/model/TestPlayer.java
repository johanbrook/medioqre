/**
*	TestPlayer.java
*
*	@author Johan
*/

package model;

import static org.junit.Assert.*;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.Arrays;

import model.character.Player;
import model.weapon.*;

import org.junit.Before;
import org.junit.Test;

import factory.ObjectFactory;



public class TestPlayer {

	private Player player;
	
	@Before
	public void setUp() throws Exception {
		this.player = new Player(30, new Rectangle(20, 20), new Dimension(20, 48), 0, 16);
	
		AbstractWeapon[] weapons = new AbstractWeapon[] {new MachineGun(player, 300,2), new Grenade(player, 4,0.1), new Melee(player, -1, 0)};
		this.player.setWeaponBelt(Arrays.asList(weapons));
		this.player.setCurrentWeapon(MachineGun.class);
	}
	
	@Test
	public void testHasWeapon() {
		assertNotNull(this.player.getCurrentWeapon());
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
		AbstractWeapon w = new Melee(this.player, -1, 0);
		this.player.setCurrentWeapon(Melee.class);
		
		assertEquals(w, this.player.getCurrentWeapon());
	}
	
	

}