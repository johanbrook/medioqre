/**
*	TestCharacterAttacks.java
*
*	@author Johan
*/

package usecase;

import static org.junit.Assert.*;

import model.character.*;
import model.character.Character;
import model.weapon.IWeapon;
import model.weapon.Sword;

import org.junit.Before;
import org.junit.Test;

public class TestCharacterAttacks {

	private Player player;
	private Character enemy;
	
	@Before
	public void setUp() throws Exception {
		this.player =  new Player(10);
		this.enemy = new Enemy(10);
	}

	@Test
	public void testPlayerAttacksWithSword() {
		IWeapon sword = new Sword();
		this.player.setCurrentWeapon(Sword.class);
		this.player.attack(enemy);
		
		assertEquals(100 - sword.getDamage(), this.enemy.getHealth());
	}
	
	@Test
	public void testEnemyAttack() {
		this.enemy.attack(player);
		
		assertEquals(100 - this.enemy.getCurrentWeaponDamage(), this.player.getHealth());
	}

}
