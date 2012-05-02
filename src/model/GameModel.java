package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import model.character.AbstractCharacter;
import model.character.Enemy;
import model.character.Player;
import model.weapon.Projectile;
import constants.Direction;
import event.Event;
import event.Event.Property;
import event.IMessageListener;

/**
 * Model for a game.
 * 
 * @author Johan
 *
 */
public class GameModel implements IGameModel, IMessageListener {

	private AbstractCharacter player;
	private Enemy[] enemies;

	private List<Entity> entities;

	public GameModel() {

		initEntities();
	}

	
	@Override
	public void onMessage(Event evt) {
		Property name = evt.getProperty();
		
		switch(name) {
		case DID_STOP:
			this.player.stop(); 
			break;
		case DID_FIRE:
			this.entities.add(this.player.attack());
			System.out.println("Did add projectile");
			break;
		case CHANGED_DIRECTION:
			this.player.start();
			this.player.setDirection((Direction) evt.getValue());
			break;
		case WAS_DESTROYED:
			this.entities.remove(evt.getValue());
			break;
		case DID_ATTACK:
			this.entities.add( ((Projectile)evt.getValue()) );
			break;
		}
		
	}
	

	public void newWave() {

	}

	private void initEntities() {
		// Use CopyOnWriteArrayList since we do concurrent reads/writes, and
		// need them to be synchronized behind the scenes. Slightly more costly,
		// but negligible since 
		//	a) Our entities list is quite small
		//	b) Writes happen very infrequently.
		this.entities = new CopyOnWriteArrayList<Entity>();
		
		this.player = new Player();
		this.player.setPosition(1000, 100);
		this.entities.add(this.player);
		
		this.enemies = new Enemy[1];
		
		for (int i = 0; i < this.enemies.length; i++) {
			this.enemies[i] = new Enemy(10, 10, 20+i*2, 20+i*2);
			this.entities.add(this.enemies[i]);
			this.enemies[i].addReceiver(this);
		}
	}
	
	
	private void moveEntities(double dt) {
		for(Entity t : this.entities) {
			
			checkCollisions(t);
			
			// The entity has to move *after* collision checks have been finished, 
			// otherwise you'll be able to bug your way through other entities.
			t.move(dt);
			
			if(t instanceof Projectile)
				doProjectileHandling((Projectile) t);
		}

	}
	
	
	private void doProjectileHandling(Projectile t) {
		if(t.getDistanceTravelled() >= t.getRange().getDistance()) {
			System.out.println("REMOVE PROJECTILE");
			this.entities.remove(t);
		}
	}
	
	
	private void checkCollisions(Entity t) {
		
		for(Entity w : this.entities) {
			
			if(t != w && t.isColliding(w)) {
				
				Direction currentDirection = t.getDirection();				
				Direction blockedDirection = t.getDirectionOfObject(w);
				
				if(t instanceof Projectile) {
					this.entities.remove(t);
					if (w instanceof AbstractCharacter){
						((AbstractCharacter) w).takeDamage(((Projectile) t).getDamage());
						System.out.println("Enemy was hit, now has " + ((AbstractCharacter) w).getHealth() + " hp" + " movespeed: " + ((AbstractCharacter)w).getMovementSpeed());
					}
				}
				
				if(directionIsBlocked(currentDirection, blockedDirection)){
					t.stop();
				}
				else {
					w.start();
				}
			}
		}
	}
	
	
	private boolean directionIsBlocked(Direction currentDirection, Direction blockedDirection) {
		boolean stop = false;
		
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
		
		return stop;
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
