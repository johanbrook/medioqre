package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import model.character.AbstractCharacter;
import model.character.Enemy;
import model.character.Player;
import model.item.AmmoCrate;
import model.item.ICollectableItem;
import model.item.MedPack;
import model.weapon.Projectile;
import constants.Direction;
import event.Event;
import event.EventBus;
import event.IMessageSender;
import event.Event.Property;
import event.IMessageListener;
import event.Messager;

/**
 * Model for a game.
 * 
 * @author Johan
 *
 */
public class GameModel implements IGameModel, IMessageListener, IMessageSender {

	private Messager messager = new Messager();
	
	private Player player;
	private List<Enemy> enemies;

	private List<CollidableObject> objects;

	private int currentLevel;
	private final double LEVEL_MULTIPLIER = 1.5;

	/**
	 * Create a new model for a game. 
	 * 
	 */
	public GameModel() {
		this.currentLevel = 0;

		// Use CopyOnWriteArrayList since we do concurrent reads/writes, and
		// need them to be synchronized behind the scenes. Slightly more costly,
		// but negligible since 
		//	a) Our entities list is quite small
		//	b) Writes happen very infrequently.
		this.objects = new CopyOnWriteArrayList<CollidableObject>();
		this.enemies = new ArrayList<Enemy>();
		
		
	}


	@Override
	public void onMessage(Event evt) {
		Property name = evt.getProperty();

		switch(name) {
		case DID_STOP:
			this.player.stop(); 
			break;
			
		case DID_FIRE:
			Projectile projectile = this.player.attack();
			if (projectile != null){
				projectile.addReceiver(this);
				this.objects.add(projectile);
				System.out.println("Did add projectile of type: "+projectile.getOwner().getClass().getSimpleName());
			}else {
				System.out.println("Out of ammo");
			}
			break;
			
		case CHANGED_DIRECTION:
			
			this.player.start();
			this.player.setDirection((Direction) evt.getValue());
			break;
			
		case WAS_DESTROYED:
			this.objects.remove(evt.getValue());
			
			if(evt.getValue() instanceof Enemy) {
				System.out.println("Was ENEMY, removing from ENEMIES list");
				this.enemies.remove(evt.getValue());
				this.messager.sendMessage(evt);
				checkEnemiesLeft();
				
			} else if (evt.getValue() instanceof Player){
				gameOver();
			}
			
			System.out.println(evt.getValue().getClass().getSimpleName() + " was destroyed");
			break;
			
		case DID_ATTACK:
			Projectile enemyProjectile = (Projectile) evt.getValue();
			enemyProjectile.addReceiver(this);
			this.objects.add(enemyProjectile);
			break;
		
		case CHANGED_WEAPON:
			int slot = (Integer) evt.getValue();
			this.player.setCurrentWeapon(slot);
			
			break;
		}
	}
	
	public void newGame(){
		initPlayer();
		this.messager.sendMessage(new Event(Event.Property.NEW_GAME,this));
	}

	
	private void gameOver(){
		EventBus.INSTANCE.publish(new Event(Event.Property.GAME_OVER, this));
		this.objects.clear();
		newGame();
	}

	public void newWave() {
		this.currentLevel++;

		initEnemies(1);
		addItems(5);
		
		Event evt = new Event(Property.NEW_WAVE, this.enemies);
		this.messager.sendMessage(evt);
		EventBus.INSTANCE.publish(evt);
	}

	private void addItems(int amount) {
		
		for(int i = 0; i < amount; i++) {
			AmmoCrate tempAmmo = new AmmoCrate(30, 30+i*2, 30+i*2);
			tempAmmo.addReceiver(this);

			MedPack tempMed = new MedPack(25, 50+i*5, 50+i*5);
			tempMed.addReceiver(this);

			this.objects.add(tempAmmo);
			this.objects.add(tempMed);

		}
	}

	private void initEnemies(int amount) {
		this.enemies.clear();
		
		for (int i = 0; i < amount; i++) {
			Enemy temp = new Enemy(10, 10, 20+i*2, 20+i*2);
			temp.addReceiver(this);
			this.enemies.add(temp);
			this.objects.add(temp);
		}
		
		System.out.println("** Enemies added");
	}

	private void initPlayer() {
		this.player = new Player();
		this.player.setPosition(1000, 100);
		this.objects.add(this.player);
		this.player.addReceiver(this);
	}


	private void moveEntities(double dt) {
		for(CollidableObject t : this.objects) {

			// The entity has to move *after* collision checks have been finished, 
			// otherwise you'll be able to bug your way through other entities.
			if(t instanceof Entity) {
				Entity temp = (Entity) t;
				checkCollisions(temp);
				temp.move(dt);
			}

			if(t instanceof Projectile){
				doProjectileHandling((Projectile) t);
			}
		}

	}


	private void doProjectileHandling(Projectile t) {
		if(t.getDistanceTravelled() >= t.getRange().getDistance()) {
			t.destroy();
		}
	}


	private void checkCollisions(Entity t) {

		for(CollidableObject w : this.objects) {

			if(t != w && t.isColliding(w)) {

				Direction currentDirection = t.getDirection();				
				Direction blockedDirection = t.getDirectionOfObject(w);

				if(t instanceof Projectile && !(w instanceof ICollectableItem)) {
					t.destroy();
					
					if (w instanceof AbstractCharacter){
						((AbstractCharacter) w).takeDamage(((Projectile) t).getDamage());
						
						System.out.println(w.getClass().getSimpleName()+" was hit, now has " + ((AbstractCharacter) w).getHealth() + " hp");
					}


				} else if (w instanceof ICollectableItem && t instanceof Player){
					((ICollectableItem) w).pickedUpBy( ((Player) t));
					
					if (w instanceof AmmoCrate)
						System.out.println("Picked up AmmoCrate, current ammo: " + this.player.getCurrentWeapon().getCurrentAmmo());
					if (w instanceof MedPack)
						System.out.println("Picked up MedPack, current HP: " + this.player.getHealth());

				}
				
				
				
				if(!(w instanceof ICollectableItem)) {
					if(directionIsBlocked(currentDirection, blockedDirection)){
						
						t.stop();
					}
					else {
						//w.start();
					}
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

	
	private void checkEnemiesLeft() {
		if(this.enemies.isEmpty()) {
			this.newWave();
		}
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
	public List<CollidableObject> getObjects() {
		return this.objects;
	}

	/**
	 * Get all the enemies in the game.
	 * 
	 * @return The enemies 
	 */
	public List<Enemy> getEnemies() {
		return this.enemies;
	}


	@Override
	public void addReceiver(IMessageListener listener) {
		this.messager.addListener(listener);
	}


}
