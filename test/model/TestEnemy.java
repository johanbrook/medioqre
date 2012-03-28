/**
*	TestEnemy.java
*
*	@author Johan
*/

package model;

import static org.junit.Assert.*;

import java.awt.Dimension;
import java.awt.Rectangle;

import model.character.*;

import org.junit.Before;
import org.junit.Test;

public class TestEnemy {

	private Enemy enemy;
	
	@Before
	public void setUp() throws Exception {
		this.enemy = new Enemy(10, 30, new Rectangle(), new Dimension(), 0, 0);
	}

	@Test
	public void testAttackEnemy() {
		Player player = new Player();
		this.enemy.attack(player);
		
		assertEquals(70, player.getHealth());
	}

}
