package model;

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

	private List<CollidableObject> objects;

	private int currentLevel;
	private final double LEVEL_MULTIPLIER = 1.5;

	public GameModel() {
		this.currentLevel = 0;

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
			Projectile projectile = this.player.attack();
			if (projectile != null){
				this.objects.add(projectile);
				System.out.println("Did add projectile");
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
			break;
		case DID_ATTACK:
			this.objects.add( ((Projectile)evt.getValue()) );
			break;
		}

	}


	public void newWave() {
		this.currentLevel++;

		addEnemies(1);
		addItems();
	}

	private void addItems() {
		for(int i = 0; i < 5; i++) {
			AmmoCrate tempAmmo = new AmmoCrate(30, 30+i*2, 30+i*2);
			tempAmmo.addReceiver(this);

			MedPack tempMed = new MedPack(25, 50+i*5, 50+i*5);
			tempMed.addReceiver(this);

			this.objects.add(tempAmmo);
			this.objects.add(tempMed);

		}
	}

	private void addEnemies(int amount) {
		this.enemies = new Enemy[amount];

		for (int i = 0; i < this.enemies.length; i++) {
			this.enemies[i] = new Enemy(10, 10, 20+i*2, 20+i*2);
			this.objects.add(this.enemies[i]);
			this.enemies[i].addReceiver(this);
		}
	}

	private void initEntities() {
		// Use CopyOnWriteArrayList since we do concurrent reads/writes, and
		// need them to be synchronized behind the scenes. Slightly more costly,
		// but negligible since 
		//	a) Our entities list is quite small
		//	b) Writes happen very infrequently.
		this.objects = new CopyOnWriteArrayList<CollidableObject>();

		this.player = new Player();
		this.player.setPosition(1000, 100);
		this.objects.add(this.player);

		addEnemies(0);
		addItems();
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
			System.out.println("REMOVE PROJECTILE");
			this.objects.remove(t);
		}
	}


	private void checkCollisions(Entity t) {

		for(CollidableObject w : this.objects) {

			if(t != w && t.isColliding(w)) {

				Direction currentDirection = t.getDirection();				
				Direction blockedDirection = t.getDirectionOfObject(w);

				if(t instanceof Projectile) {
					this.objects.remove(t);
					if (w instanceof AbstractCharacter){
						((AbstractCharacter) w).takeDamage(((Projectile) t).getDamage());
						System.out.println( w.getClass().getName() + " was hit, now has " + ((AbstractCharacter) w).getHealth() + " hp" + " movespeed: " + ((AbstractCharacter)w).getMovementSpeed());
					}


				} else if (w instanceof ICollectableItem && t instanceof Player){
					((ICollectableItem) w).pickedUpBy( ((Player) t));
					if (w instanceof AmmoCrate)
						System.out.println("Picked up AmmoCrate, current ammo: " + this.player.getCurrentWeapon().getCurrentAmmo());
					if (w instanceof MedPack)
						System.out.println("Picked up MedPack, current HP: " + this.player.getHealth());

				}
				if(directionIsBlocked(currentDirection, blockedDirection)){
					t.stop();
				}
				else {
					//					w.start();
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
	public List<CollidableObject> getObjects() {
		return this.objects;
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
