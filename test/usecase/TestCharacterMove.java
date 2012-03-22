/**
*	TestCharacterMove.java
*
*	@author Johan
*/

package usecase;

import static org.junit.Assert.*;

import model.character.*;
import model.character.Character;
import model.Position;

import org.junit.Before;
import org.junit.Test;


public class TestCharacterMove {

	private Character player;
	private Character enemy;
	private final double DELTA = 0.1;
	
	@Before
	public void setUp() throws Exception {
		this.player = new Player(10);
		this.enemy = new Enemy(7, 30);
	}

	
	@Test
	public void testMoveNull(){
		assertEquals(new Position(0,0), this.player.getPosition());
	}
	
	@Test
	public void testMoveNorth() {
		this.player.setDirection(constants.Direction.NORTH);
		this.enemy.setDirection(constants.Direction.NORTH);
		this.player.move(DELTA);
		this.enemy.move(DELTA);
		
		assertEquals(new Position(0, DELTA*10), this.player.getPosition());
		assertEquals(new Position(0, DELTA*7), this.enemy.getPosition());
	}
	
	@Test
	public void testMoveSouth() {
		this.player.setDirection(constants.Direction.SOUTH);
		this.player.move(DELTA);
		
		assertEquals(new Position(0,-1.0), this.player.getPosition());
	}

}
