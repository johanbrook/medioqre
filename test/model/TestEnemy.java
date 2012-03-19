/**
*	TestEnemy.java
*
*	@author Johan
*/

package model;

import static org.junit.Assert.*;
import model.character.*;

import org.junit.Before;
import org.junit.Test;

public class TestEnemy {

	private Enemy enemy;
	
	@Before
	public void setUp() throws Exception {
		this.enemy = new Enemy(10);
	}

	@Test
	public void testAttackEnemy() {
		Player player = new Player(10);
		this.enemy.attack(player);
		
		assertEquals(70, player.getHealth());
	}

}
