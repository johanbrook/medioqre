/**
 *	TestGameModel.java
 *
 *	@author Johan
 */

package model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import tools.factory.Level;
import tools.factory.ObjectFactory;

import event.Event;
import event.EventBus;
import event.IEventHandler;

public class TestGameModel implements IEventHandler {

	private GameModel game;
	private Event catchedEvent;

	@Before
	public void setUp() throws Exception {
		ObjectFactory.setLevel(new Level());
		EventBus.INSTANCE.register(this);
		
		this.game = new GameModel();
	}

	@Test
	public void testNewGame() {
		this.game.newGame();
		
		assertEquals(Event.Property.NEW_GAME, this.catchedEvent.getProperty());
		assertNotNull(this.game.getObjects());
		assertNotNull(this.game.getPlayer());
		assertEquals(0, this.game.getCurrentWaveCount());
	}
	
	@Test
	public void testNewWave() {
		this.game.newWave();
		
		assertEquals(Event.Property.NEW_WAVE, this.catchedEvent.getProperty());
		assertEquals(1, this.game.getCurrentWaveCount());
		assertNotNull(this.game.getEnemies());
	}
	
	@Test
	public void testMoreWaves() {
		this.game.newWave();
		int waveCount = this.game.getCurrentWaveCount();
		assertEquals(1, waveCount);
		
		this.game.newWave();
		assertEquals(waveCount+1, this.game.getCurrentWaveCount());
	}
	
	@Test
	public void testWaveEnemyMultiplier() {
		this.game.newWave();
		assertNotNull(this.game.getEnemies());
		
		assertEquals(tools.Math.fib(1), this.game.getEnemies().size());
		
		this.game.newGame();
		assertEquals(tools.Math.fib(2), this.game.getEnemies().size());
	}
	
	@Test
	public void testGameOver() {
		this.game.newGame();
		this.game.gameOver();
		
		assertEquals(Event.Property.GAME_OVER, this.catchedEvent.getProperty());
		assertEquals(0, this.game.getObjects().size());
	}
	
	@Override
	public void onEvent(Event evt) {
		this.catchedEvent = evt;
	}

}
