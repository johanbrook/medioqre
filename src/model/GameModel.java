package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import model.character.Character;
import model.character.Enemy;
import model.character.Player;
import constants.Direction;

/**
 * Model for a game.
 * 
 * @author Johan
 *
 */
public class GameModel implements IGameModel {

	private Character player;
	private Enemy[] enemies;
	//private AIController ai;

	private List<Entity> entities;

	public GameModel() {
		//this.ai = new AIController(48, 48, 32, 32);

		initEntities();
	}


	public void newWave() {

	}

	private void initEntities() {
		this.entities = new ArrayList<Entity>();
		
		this.player = new Player();
		this.player.setPosition(100, 100);
		this.entities.add(this.player);
		
		this.enemies = new Enemy[2];
		
		for (int i = 0; i < this.enemies.length; i++) {
			this.enemies[i] = new Enemy(10, 10, 20+i*2, 20+i*2);
			this.entities.add(this.enemies[i]);
		}
	}
	
	
	private void moveEntities(double dt) {
		for(Entity t : this.entities) {
			
			checkCollisions(t);
			
			// The entity has to move *after* collision checks have been finished, 
			// otherwise you'll be able to bug your way through other entities.
			t.move(dt);
		}

	}
	
	private void checkCollisions(Entity t) {
		
		for(Entity w : this.entities) {
						
			if(t != w && t instanceof Player){
//				System.out.println("-----\nPlayer direction: "+t.getDirection());
			}
			
			
			if(t != w && t.isColliding(w)) {
				
				boolean stop = false;
				Direction currentDirection = t.getDirection();				
				Direction blockedDirection = t.getDirectionOfObject(w);
				
				
				if(currentDirection == Direction.EAST &&
					(blockedDirection == Direction.NORTH_EAST || 
					blockedDirection == Direction.SOUTH_EAST) ) {
					
					stop = true;
				}
				
				if(currentDirection == Direction.SOUTH_EAST && 
					(blockedDirection == Direction.NORTH_EAST ||
					 blockedDirection == Direction.SOUTH_WEST) ) {
						
					stop = true;
				}
				
				if(currentDirection == Direction.NORTH_EAST && 
						(blockedDirection == Direction.SOUTH_EAST ||
						 blockedDirection == Direction.NORTH_WEST)) {
							
					stop = true;
				}
				
				if(currentDirection == Direction.WEST &&
						(blockedDirection == Direction.NORTH_WEST || 
						blockedDirection == Direction.SOUTH_WEST) ) {
						
					stop = true;
				}
				
				if(currentDirection == Direction.SOUTH_WEST && 
					(blockedDirection == Direction.NORTH_WEST ||
					 blockedDirection == Direction.SOUTH_EAST) ) {
						
					stop = true;
				}
				
				if(currentDirection == Direction.NORTH_WEST && 
						(blockedDirection == Direction.SOUTH_WEST ||
						 blockedDirection == Direction.NORTH_EAST) ) {
							
					stop = true;
				}
				
				if(currentDirection == Direction.NORTH &&
						(blockedDirection == Direction.NORTH_EAST || 
						blockedDirection == Direction.NORTH_WEST) ) {
						
					stop = true;
				}
				
				if(currentDirection == Direction.SOUTH && 
					(blockedDirection == Direction.SOUTH_EAST ||
					blockedDirection == Direction.SOUTH_WEST) ) {
						
					stop = true;
				}
				
				
				if(blockedDirection == currentDirection){
					stop = true;
				}
				
				if(stop)
					t.stop();
				else
					w.start();
			}
		}
	}
	

	/**
	 * Updates the game model.
	 * @param dt The time since the last update.
	 */
	public void update(double dt) {

		moveEntities(dt);
	}
	
	
	/**
	 * Get the player in the game.
	 * 
	 * @return The current player
	 */
	public Player getPlayer() {
		return (Player) this.player;
	}

	
	/**
	 * Get all the entities in the game.
	 * 
	 * @return The entities
	 */
	public List<Entity> getEntities() {
		return this.entities;
	}
	
	/**
	 * Get all the enemies in the game.
	 * 
	 * @return The enemies 
	 */
	public List<Enemy> getEnemies() {
		return (List<Enemy>) Arrays.asList(this.enemies);
	}

}
