package model;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import tools.factory.ObjectFactory;
import static tools.Logger.log;

import model.character.AbstractCharacter;
import model.character.Enemy;
import model.character.Player;
import model.item.AmmoCrate;
import model.item.ICollectableItem;
import model.item.MedPack;
import model.weapon.Grenade;
import model.weapon.Portal;
import model.weapon.PortalGun;
import model.weapon.PortalGun.Mode;
import model.weapon.Projectile;
import controller.AppController;
import event.Event;
import event.Event.Property;
import event.EventBus;
import event.IMessageListener;
import event.IMessageSender;
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

	// Portals
	private Portal[] portals;

	private int currentWave;

	/**
	 * Create a new model for a game.
	 * 
	 */
	public GameModel() {
		this.currentWave = 0;

		// Use CopyOnWriteArrayList since we do concurrent reads/writes, and
		// need them to be synchronized behind the scenes. Slightly more costly,
		// but negligible since
		// a) Our entities list is quite small
		// b) Writes happen very infrequently.
		this.objects = new CopyOnWriteArrayList<CollidableObject>();
		this.enemies = new CopyOnWriteArrayList<Enemy>();
		this.portals = new Portal[2];
	}

	@Override
	public void onMessage(Event evt) {
		Property name = evt.getProperty();

		switch (name) {
		case DID_STOP :
			this.player.stop();
			break;

		case DID_FIRE :
			Projectile projectile = this.player.attack();
			if (projectile != null) {
				projectile.addReceiver(this);
				this.objects.add(projectile);

			} else {
				log("Out of ammo");
			}
			break;

		case CHANGED_DIRECTION :

			this.player.start();
			this.player.setDirection((Direction) evt.getValue());
			break;

		case WAS_DESTROYED :

			if (evt.getValue() instanceof Projectile) {
				Projectile p = (Projectile) evt.getValue();
				if (p.getOwner() instanceof PortalGun) {
					PortalGun g = (PortalGun) p.getOwner();

					// Always position new portals in the middle of where
					// the portal
					// projectile landed:
					int x = (int) p.getPosition().x
							+ p.getCollisionBox().width / 2;
					int y = (int) p.getPosition().y
							+ p.getCollisionBox().height / 2;

					deployPortal(g.getMode(), new Point(x, y));

				} else if (p.getOwner() instanceof Grenade) {
					doSplashDamage(p);
				}
			}

			this.objects.remove(evt.getValue());
			if (evt.getValue() instanceof Enemy) {
				this.enemies.remove(evt.getValue());
				this.messager.sendMessage(evt);
				checkEnemiesLeft();
				randomizeItem();

			} else if (evt.getValue() instanceof Player) {
				gameOver();
			}

			log(evt.getValue().getClass().getSimpleName()
					+ " was destroyed");
			break;

		case DID_ATTACK :
			Projectile enemyProjectile = (Projectile) evt.getValue();
			enemyProjectile.addReceiver(this);
			this.objects.add(enemyProjectile);
			break;

		case CHANGED_WEAPON :
			int slot = (Integer) evt.getValue();
			int portalGunPosition = this.player.getWeaponBelt().indexOf(PortalGun.class);
			
			if (this.player.getCurrentWeapon() instanceof PortalGun && 
					slot == portalGunPosition) {
				
				switchPortalGunMode();
			} else {
				this.player.setCurrentWeapon(slot);
			}

			break;
			
		case PORTALGUN_SWITCHED_MODE :
			switchPortalGunMode();
			break;
			
		case PAUSE_GAME :
			
			AppController.togglePaused();
			
			break;
		}
	}
	
	
	private void switchPortalGunMode() {
		((PortalGun) this.player.getWeaponBelt().get(PortalGun.class)).switchMode();
	}

	/**
	 * Create new medpacks and ammocrates based on a probability value.
	 * 
	 * <p>I.e., new items will only spawn if a random integer is lower than the
	 * config entry 'itemSpawnChange'.</p>
	 */
	private void randomizeItem() {
		Random random = new Random();
		int rand = random.nextInt(100);

		if (rand < ObjectFactory.getConfigInt("itemSpawnChance")) {
			newAmmoCrate();
			newMedPack();
		}

	}

	/**
	 * Create a new ammocrate from the factory, setup listener and add
	 * to the game world.
	 */
	private void newAmmoCrate() {
		CollidableObject item = ObjectFactory.newItem("AmmoCrate");
		item.addReceiver(this);
		this.objects.add(item);
		log("** Ammo Crate created **");
	}

	/**
	 * Create a new medpack from the factory, setup listener and add
	 * to the game world.
	 */
	private void newMedPack() {
		CollidableObject item = ObjectFactory.newItem("MedPack");
		item.addReceiver(this);
		this.objects.add(item);
		log("** MedPack created **");

	}

	/**
	 * Initializes a new game with a player and walls.
	 * 
	 * <p>Sends the event <code>NEW_GAME</code> to listeners with the value
	 * <code>this</code>.</p>
	 */
	public void newGame() {
		initPlayer();
		initWalls();
		this.messager.sendMessage(new Event(Event.Property.NEW_GAME, this));
	}

	/**
	 * Game over handling.
	 */
	private void gameOver() {
		log("Noob, game over");
		EventBus.INSTANCE.publish(new Event(Event.Property.GAME_OVER, this));
		this.objects.clear();
		newGame();
	}

	/**
	 * Initializes a new wave of enemies and items. Increments the wave number,
	 * and sends an event to the bus and listeners of the type <code>NEW_WAVE</code>
	 * and with the value <code>this</code>.
	 */
	public void newWave() {
		this.currentWave++;

		initEnemies();
		addItems();

		Event evt = new Event(Property.NEW_WAVE, this);
		this.messager.sendMessage(evt);
		EventBus.INSTANCE.publish(evt);

		log("New wave: " + this.currentWave);
	}

	/**
	 * Add new walls to the game world.
	 * 
	 */
	private void initWalls() {
		List<ConcreteCollidableObject> walls = ObjectFactory.newWalls();
		for (CollidableObject co : walls) {
			this.objects.add(co);
		}
	}
	
	/**
	 * Add relevant items to the game world from the factory. Sets up
	 * relevant listeners.
	 */
	private void addItems() {
		List<CollidableObject> items = ObjectFactory
				.newItemsForWave(this.currentWave);

		log("Items received");

		for (CollidableObject item : items) {
			item.addReceiver(this);
		}

		this.objects.addAll(items);

		log("** " + items.size() + " items added");

	}

	/**
	 * Initialize enemies.
	 * 
	 * <p>Get new enemies for the current wave from the factory,
	 * set up listeners and add them to the enemies list and game world.</p>
	 * 
	 * <p>Clears the previous list of enemies.</p>
	 */
	private void initEnemies() {
		this.objects.removeAll(this.enemies);
		this.enemies.clear();

		List<Enemy> tempEnemies = ObjectFactory
				.newEnemiesForWave(this.currentWave);

		log("Enemies received");

		for (Enemy temp : tempEnemies) {
			temp.addReceiver(this);
		}

		this.enemies.addAll(tempEnemies);
		this.objects.addAll(tempEnemies);

		log("** " + tempEnemies.size() + " enemies added");
	}

	/**
	 * Initialize a new player from the factory and adds a listener.
	 */
	private void initPlayer() {
		this.player = ObjectFactory.newPlayer();

		this.objects.add(this.player);
		this.player.addReceiver(this);
	}

	/**
	 * Create a new portal with a mode on a position in the game world.
	 * 
	 * <p>Handles the logic associated with the two portals, i.e. connect
	 * them and position them correctly.</p>
	 * 
	 * @param mode The mode the new portal should be initialized with
	 * @param position The position the new portal should be initialized on
	 * @see PortalGun.Mode
	 */
	private void deployPortal(Mode mode, Point position) {

		for (int i = 0; i < this.portals.length; i++) {

			if (this.portals[i] == null) {
				Portal p = ObjectFactory.newPortal(mode, position);
				p.center();
				EventBus.INSTANCE
				.publish(new Event(Property.PORTAL_CREATED, p));
				this.objects.add(p);
				this.portals[i] = p;

				if (i == 1) {
					p.setSisterPortal(this.portals[0]);
					this.portals[0].setSisterPortal(p);
				}

				return;
			} else if (this.portals[i].getMode() == mode) {
				this.portals[i].setPositionFromCenter(position);
				return;
			}
		}
	}

	/**
	 * Do splash damage from a projectile.
	 * 
	 * @param p The projectile
	 */
	private void doSplashDamage(Projectile p) {
		if (p.getOwner() instanceof Grenade) {

			Grenade grenade = ((Grenade) p.getOwner());
			int radius = grenade.getRadius();
			int distance = Math.abs(this.player.getPosition().x
					- p.getPosition().x)
					+ Math.abs(this.player.getPosition().y - p.getPosition().y);
			if (distance < radius) {
				this.player.takeDamage(p.getDamage()
						/ grenade.getSplashDamageFactor());
				log("Player was hit by Grenade splash! Took: " + p.getDamage()
						/ grenade.getSplashDamageFactor() + " damage! Now has "
						+ player.getHealth() + " hp left!");
			}

			for (Enemy e : this.enemies) {
				distance = Math.abs(e.getPosition().x - p.getPosition().x)
						+ Math.abs(e.getPosition().y - p.getPosition().y);
				if (distance < radius) {
					e.takeDamage(p.getDamage()
							/ grenade.getSplashDamageFactor());
					log("Enemy was hit by Grenade splash! Took "
							+ p.getDamage() / grenade.getSplashDamageFactor()
							+ " damage! Now has " + e.getHealth() + " hp left!");

				}
			}
		}

	}

	/**
	 * Move all relevant entities based on a delta time, and take 
	 * relevant actions.
	 * 
	 * @param dt The delta time
	 */
	private void moveEntities(double dt) {
		for (CollidableObject t : this.objects) {
			
			if (t instanceof Entity) {
				Entity temp = (Entity) t;

				if (t instanceof AbstractCharacter) {
					((AbstractCharacter) t).update(dt);
				}

				// Save the old position of the entity .. 
				Point oldPos = temp.getPosition();
				
				// .. move and check eventual collisions
			
				
				checkCollisions(temp);
				temp.move(dt);
				
				// .. and nudge the entity back to the old position
				// if it collides with a wall
				boolean canMove = true;
				for (CollidableObject o : this.objects) {
					if (o instanceof ConcreteCollidableObject)
						if (o.isColliding(temp)){
							canMove = false;
							
							//Manually tell projectiles they collided with a wall, they are the only 
							//kind of entity that have an actual behavior related to wall-collisions.
							temp.didCollide(o);
						}
				}
				if (!canMove){
					temp.setPosition(oldPos);
				}

				checkIfLeftPortal(temp);
			}


		}

	}

	/**
	 * Checks if an entity moved out of a portal and if it did,
	 * change its state.
	 * 
	 * @param temp The entity
	 */
	private void checkIfLeftPortal(Entity temp){
		boolean isVictim = false;
		for (Portal p : this.portals){ 
			if (p != null && p.isColliding(temp)){
				isVictim = true;
			}	
		}

		if (!isVictim)
			temp.setPortalVictim(isVictim);
	}

	
	/**
	 * Check collisions for an entity.
	 * 
	 * @param t The entity
	 */
	private void checkCollisions(Entity t) {
		
		for (CollidableObject w : this.objects) {
			
			if (t != w && t.isColliding(w)) {
				
				// If the given entity (t) is colliding with another
				// object, call both object's collision callbacks and 
				// stop t if it's direction is blocked by w.
				w.didCollide(t);
				t.didCollide(w);
				stopIfBlocked(t, w);
			}
		}
	}

	/**
	 * Stop an entity if its path is blocked by another collidable.
	 * 
	 * <p>Doesn't stop the entity if the collidable is an instance of
	 * <code>ICollectableItem</code> or <code>Portal</code> (which entities
	 * should be able to move through.</p>
	 * 
	 * @param t The entity to manipulate
	 * @param w The collidable object the entity may collide with
	 */
	private void stopIfBlocked (Entity t, CollidableObject w){
		Direction currentDirection = t.getDirection();
		Direction blockedDirection = t.getDirectionOfObject(w);

		if (!(w instanceof ICollectableItem) && !(w instanceof Portal)) {
			if (directionIsBlocked(currentDirection, blockedDirection)) {

				t.stop();

			}
		}
	}

	/**
	 * Check if an entity with a given direction will be blocked by another entity.
	 * 
	 * @param currentDirection The given, current, direction
	 * @param blockedDirection The other (potentially) blocking object's direction
	 * @return
	 */
	private boolean directionIsBlocked(Direction currentDirection, Direction blockedDirection) {
		
		boolean stop = false;

		if (currentDirection == Direction.EAST
				&& (blockedDirection == Direction.NORTH_EAST || blockedDirection == Direction.SOUTH_EAST)) {

			stop = true;
		}

		if (currentDirection == Direction.SOUTH_EAST
				&& (blockedDirection == Direction.NORTH_EAST || blockedDirection == Direction.SOUTH_WEST)) {

			stop = true;
		}

		if (currentDirection == Direction.NORTH_EAST
				&& (blockedDirection == Direction.SOUTH_EAST || blockedDirection == Direction.NORTH_WEST)) {

			stop = true;
		}

		if (currentDirection == Direction.WEST
				&& (blockedDirection == Direction.NORTH_WEST || blockedDirection == Direction.SOUTH_WEST)) {

			stop = true;
		}

		if (currentDirection == Direction.SOUTH_WEST
				&& (blockedDirection == Direction.NORTH_WEST || blockedDirection == Direction.SOUTH_EAST)) {

			stop = true;
		}

		if (currentDirection == Direction.NORTH_WEST
				&& (blockedDirection == Direction.SOUTH_WEST || blockedDirection == Direction.NORTH_EAST)) {

			stop = true;
		}

		if (currentDirection == Direction.NORTH
				&& (blockedDirection == Direction.NORTH_EAST || blockedDirection == Direction.NORTH_WEST)) {

			stop = true;
		}

		if (currentDirection == Direction.SOUTH
				&& (blockedDirection == Direction.SOUTH_EAST || blockedDirection == Direction.SOUTH_WEST)) {

			stop = true;
		}

		if (blockedDirection == currentDirection) {
			stop = true;
		}

		return stop;
	}

	/**
	 * Updates the game model.
	 * 
	 * @param dt
	 *            The time since the last update.
	 */
	public void update(double dt) {

		moveEntities(dt);
	}


	/**
	 * Callback for checking if a new wave should be created based
	 * on if the number of enemies is zero.
	 * 
	 */
	private void checkEnemiesLeft() {
		if (this.enemies.isEmpty()) {
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

	/**
	 * Get the current wave
	 * 
	 * @return The number of the current wave
	 */
	public int getCurrentWaveCount() {
		return this.currentWave;
	}

	@Override
	public void addReceiver(IMessageListener listener) {
		this.messager.addListener(listener);
	}

}
