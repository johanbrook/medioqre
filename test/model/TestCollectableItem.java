/**
*	TestCollectableItem.java
*
*	@author Johan
*/

package model;

import static org.junit.Assert.*;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.Arrays;


import model.character.Player;
import model.item.AmmoCrate;
import model.item.ICollectableItem;
import model.item.MedPack;
import model.weapon.AbstractWeapon;
import model.weapon.Grenade;
import model.weapon.MachineGun;
import model.weapon.Melee;

import org.junit.Before;
import org.junit.Test;

public class TestCollectableItem {

	private ICollectableItem medpack;
	private ICollectableItem ammocrate;
	private Player player;
	
	@Before
	public void setUp() throws Exception {
		this.medpack = new MedPack(30, 1, 1);
		this.ammocrate = new AmmoCrate(30, 1, 1);
		this.player = new Player(30, new Rectangle(20, 20), new Dimension(20, 48), 0, 16);
		AbstractWeapon[] weapons = new AbstractWeapon[] {new MachineGun(player, 300), new Grenade(player, 4), new Melee(player, -1)};
		this.player.setWeaponBelt(Arrays.asList(weapons));
		this.player.setCurrentWeapon(MachineGun.class);
	}

	@Test
	public void testPlayerPicksUpMedpack() {
		int initialHealth = this.player.getHealth();
		this.medpack.pickedUpBy(player);
		
		assertEquals(initialHealth + 30, this.player.getHealth());
	}
	
	@Test
	public void testPlayerPicksUpAmmo() {
		int ammo = this.player.getCurrentWeapon().getCurrentAmmo();
		this.ammocrate.pickedUpBy(player);
		
		assertEquals(ammo + 30, this.player.getCurrentWeapon().getCurrentAmmo());
	}

}
