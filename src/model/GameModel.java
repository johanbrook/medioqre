package model;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import model.character.Character;
import model.character.Enemy;
import model.character.Player;
import constants.Direction;
import controller.AI.AIController;

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
		this.entities.add(this.player);
		
		this.enemies = new Enemy[10];
		
		for (int i = 0; i < this.enemies.length; i++) {
			this.enemies[i] = new Enemy(10, 10, i, i);
			this.entities.add(this.enemies[i]);
		}
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
				
				//@todo Collisions are ignored if the player moves in diagonal direction
				
				// This baby is in need of a grave refactor :)
				
				boolean stop = false;
				
				if(t != w && t.isColliding(w)) {
					
					Direction currentDirection = t.getDirection();				
					Direction blockedDirection = t.getCollisionDirection(w);

					if(t != w && t instanceof Player){
						System.out.println("-----\nPlayer direction: "+currentDirection);
						System.out.println("Collision direction: "+blockedDirection);
						
						int c = t.getCode(w);
						
						System.out.println("CODE: "+c);
						System.out.println("Top: "+ ((c & Rectangle.OUT_TOP) == Rectangle.OUT_TOP));
						System.out.println("Bottom: "+ ((c & Rectangle.OUT_BOTTOM) == Rectangle.OUT_BOTTOM));
						System.out.println("Left: "+ ((c & Rectangle.OUT_LEFT) == Rectangle.OUT_LEFT));
						System.out.println("Right: "+ ((c & Rectangle.OUT_RIGHT) == Rectangle.OUT_RIGHT));
					}
					
					System.out.println("----------\n"+t);
					System.out.println(w+"\n------------");
										
					if( (blockedDirection == Direction.NORTH_WEST ||
						 blockedDirection == Direction.WEST)
							&& (currentDirection == Direction.WEST || 
								currentDirection == Direction.NORTH_WEST ||
								currentDirection == Direction.NORTH)) {
						
						stop = true;
					}
					
					if( (blockedDirection == Direction.NORTH_EAST ||
						 blockedDirection == Direction.EAST) 
							&& (currentDirection == Direction.EAST || 
								currentDirection == Direction.NORTH_EAST ||
								currentDirection == Direction.NORTH) ){
								
						stop = true;
					}
					
					if( (blockedDirection == Direction.SOUTH_WEST ||
							 blockedDirection == Direction.WEST) 
								&& (currentDirection == Direction.WEST || 
									currentDirection == Direction.SOUTH_WEST ||
									currentDirection == Direction.SOUTH) ){
						
						
						stop = true;
					}
					
					if( (blockedDirection == Direction.SOUTH_EAST ||
							 blockedDirection == Direction.EAST) 
								&& (currentDirection == Direction.EAST || 
									currentDirection == Direction.SOUTH_EAST ||
									currentDirection == Direction.SOUTH)){
									
						stop = true;
					}
					
					if(blockedDirection == currentDirection){
						stop = true;
					}
						
					
					if(stop) t.stop();
				}
			}
			
			// The entity has to move *after* collision checks have been finished, 
			// otherwise you'll be able to bug your way through other entities.
			t.move(dt);
		}

	}

	
	public Player getPlayer() {
		return (Player) this.player;
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
	
	public List<Enemy> getEnemies() {
		return (List<Enemy>) Arrays.asList(this.enemies);
	}

}
