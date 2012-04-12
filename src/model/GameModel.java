package model;

import java.util.Random;

import model.character.*;
import model.character.Character;
import constants.Direction;

/**
 * Model for a game.
 * 
 * @author Johan
 *
 */
public class GameModel implements IGameModel {
	
	private Character player;
	private Character enemy;
	private Random rand = new Random();
	
	public GameModel() {
		this.player = new Player();
		this.enemy = new Enemy(20, 0);
		this.enemy.setPosition(100, 100);
		this.enemy.setDirection(Direction.NORTH);
	}
	
	/**
	 * Updates the game model.
	 * @param dt The time since the last update.
	 */
	public void update(double dt) {

		this.player.move(dt);
		if (rand.nextInt((int)(100)) == 0) {
			int r = rand.nextInt(8);
			Direction d = Direction.ORIGIN;
			switch (r) {
			case 0:
				d = Direction.EAST;
				break;
			case 1:
				d = Direction.NORTH;
				break;
			case 2:
				d = Direction.NORTH_EAST;
				break;
			case 3:
				d = Direction.NORTH_WEST;
				break;
			case 4:
				d = Direction.SOUTH;
				break;
			case 5:
				d = Direction.SOUTH_EAST;
				break;
			case 6:
				d = Direction.SOUTH_WEST;
				break;
			case 7:
				d = Direction.WEST;
				break;
			}
			this.enemy.setDirection(d);
		}
		this.enemy.move(dt);
	}
	
	/**
	 * Updates the player's direction.
	 * 
	 * @param dir The direction
	 * @see Direction
	 */
	public void updateDirection(Direction dir) {
		this.player.setDirection(dir);
	}
	
	public void stopPlayer(){
		this.player.stop();
	}
	
	
}
