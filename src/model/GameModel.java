package model;

import java.util.ArrayList;
import java.util.List;

import java.util.Random;


import model.character.*;
import model.character.Character;
import constants.Direction;
import event.Event;
import event.EventBus;

/**
 * Model for a game.
 * 
 * @author Johan
 *
 */
public class GameModel implements IGameModel {
	
	private Character player;
	private Enemy[] enemies;
	private Random rand = new Random();
	
	private List<Entity> entities;
	
	public GameModel() {
		this.entities = new ArrayList<Entity>();
		
		initEntities();
	}
	
	
	public void newWave() {
		
	}
	
	private void initEntities() {
		this.player = new Player();
		
		this.enemies = new Enemy[2];
		this.enemies[0] = new Enemy(10, 10, 100, 100);
		this.enemies[1] = new Enemy(10, 10, 200, 100);
//		this.enemies[2] = new Enemy(10, 10, 300, 100);
//		this.enemies[3] = new Enemy(10, 10, 400, 100);
//		this.enemies[4] = new Enemy(10, 10, 500, 100);
//		this.enemies[5] = new Enemy(10, 10, 600, 100);
//		this.enemies[6] = new Enemy(10, 10, 700, 100);
//		this.enemies[7] = new Enemy(10, 10, 800, 100);
//		this.enemies[8] = new Enemy(10, 10, 900, 100);
//		this.enemies[9] = new Enemy(10, 10, 1000, 100);
//		
//		this.enemies[0].setDirection(Direction.SOUTH);
//		this.enemies[1].setDirection(Direction.SOUTH);
//		this.enemies[2].setDirection(Direction.SOUTH);
//		this.enemies[3].setDirection(Direction.SOUTH);
//		this.enemies[4].setDirection(Direction.SOUTH);
		
		this.entities.add(this.player);
		for (int i = 0; i < this.enemies.length; i++) 
			this.entities.add(this.enemies[i]);
	}
	
	/**
	 * Updates the game model.
	 * @param dt The time since the last update.
	 */
	public void update(double dt) {
		
		for (int i = 0; i < this.enemies.length; i++) {
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
			this.enemies[i].setDirection(d);
			this.enemies[i].start();
		}
	}
		
		for(Entity t : this.entities) {
			for(Entity w : this.entities) {
				
				Direction blockedDirection = t.getCollisionDirection(w);
				Direction currentDirection = t.getDirection();
				
				
				
				if(t.isColliding(w) && t != w) {
					t.stop();
				}
			}
		}
		
		for (Entity t : this.entities) {
			t.move(dt);
		}
	}
	
	/**
	 * Updates the player's direction.
	 * 
	 * @param dir The direction
	 * @see Direction
	 */
	public void updateDirection(Direction dir) {
		this.player.setDirection(dir);
		this.player.start();
	}
	
	public void stopPlayer(){
		this.player.stop();
	}
	
	
	public List<Entity> getEntities() {
		return this.entities;
	}
	
}
