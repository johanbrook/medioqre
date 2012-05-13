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
import model.weapon.Projectile.Range;

import org.junit.Before;
import org.junit.Test;

import factory.ObjectFactory;

public class TestPlayer {

	private Player player;

	@Before
	public void setUp() throws Exception {
		this.player = new Player(30, new Rectangle(20, 20), new Dimension(20,
				48), 0, 16);

		AbstractWeapon[] weapons = new AbstractWeapon[]{
				new MachineGun(player, 300, 2, 2),
				new Grenade(player, 4, 0.1, 5), new Melee(player, -1, 0, 4)};
		
		this.player.setWeaponBelt(new WeaponBelt(Arrays.asList(weapons)));
		this.player.setCurrentWeapon(MachineGun.class);

		for (AbstractWeapon w : this.player.getWeaponBelt()) {
			Projectile p = new Projectile(w, 10, 10, 10, Range.FAR_RANGE, 30);
			w.setProjectile(p);
		}
	}

	@Test
	public void testHasWeapon() {
		assertNotNull(this.player.getCurrentWeapon());
	}

	@Test
	public void testFireWeapon() {
		assertNotNull(this.player.attack());
	}

	@Test
	public void testOutOfAmmo() {
		this.player.setCurrentWeapon(Grenade.class);
		this.player.getCurrentWeapon().setFireInterval(0);
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
		this.player.setHealth(50);
		this.player.addHealth(10);

		assertEquals(60, this.player.getHealth());
	}

	@Test
	public void testAddTooMuchHealth() {
		this.player.setHealth(this.player.getMaxHealth() - 10);
		this.player.addHealth(20);

		assertEquals(this.player.getMaxHealth(), this.player.getHealth());
	}

	@Test
	public void testAddToMaximumHealth() {
		this.player.addHealth(10);

		assertEquals(this.player.getMaxHealth(), this.player.getHealth());
	}

	@Test
	public void testSetCurrentWeapon() {
		AbstractWeapon w = new Melee(this.player, -1, 0, 4);
		this.player.setCurrentWeapon(Melee.class);

		assertEquals(w, this.player.getCurrentWeapon());
	}

}