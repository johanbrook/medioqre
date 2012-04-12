package model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import java.util.Random;

import tools.Logger;

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
	
	private List<Entity> entities;
	private List<Enemy> enemies;
	
	public GameModel() {
		this.entities = new ArrayList<Entity>();
		this.enemies = new ArrayList<Enemy>();
		
		initEntities();
	}
	
	
	public void newWave() {
		
	}
	
	private void initEntities() {
		this.player = new Player();
		this.enemy = new Enemy(20, 10);
		this.enemy.setPosition(100, 100);
		this.enemy.setDirection(Direction.NORTH);
		
		this.entities.add(this.player);
		this.entities.add(this.enemy);
	}
	
	/**
	 * Updates the game model.
	 * @param dt The time since the last update.
	 */
	public void update(double dt) {

		
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
		
		
		for(int i = 0; i < this.entities.size(); i++) {
			Entity t = this.entities.get(i);
			
			for(int j = 0; j < this.entities.size(); j++) {
				Entity w = this.entities.get(j);
				
				if(t != w) {
					System.out.println("T: "+t.getPosition());
					System.out.println("W: "+w.getPosition());
					
					if(!t.isColliding(w)) {
						t.move(dt);
					}
					else {
						System.out.println("** COLLISION **");
					}
				}
			
			}
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
	}
	
	public void stopPlayer(){
		this.player.stop();
	}
	
	
}
