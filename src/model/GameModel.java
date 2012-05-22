package model;

import static tools.Logger.log;

import java.awt.Point;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import model.character.AbstractCharacter;
import model.character.Enemy;
import model.character.Player;
import model.weapon.Grenade;
import model.weapon.Portal;
import model.weapon.PortalGun;
import model.weapon.PortalGun.Mode;
import model.weapon.Projectile;
import tools.factory.ObjectFactory;
import controller.AppController;
import event.Event;
import event.Event.Property;
import event.EventBus;
import event.IEventHandler;
import event.IMessageSender;
import event.Messager;
import graphics.opengl.tilemap.TileMap;

/**
 * Model for a game.
 * 
 * @author Johan
 * @author jesperpersson
 * 
 */
public class GameModel implements IGameModel, IEventHandler, IMessageSender {

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
	public void onEvent(Event evt) {
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
			
		case TOGGLE_PAUSE :
			
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
		setAtLegalPosition(item);
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
		
		setAtLegalPosition(item);
		log("** MedPack created **");

	}

	/**
	 * Initializes a new game with a player and walls.
	 * 
	 * <p>Sends the event <code>NEW_GAME</code> to listeners with the value
	 * <code>this</code>.</p>
	 */
	public void newGame() {
		log("Initializing new game ...");
		this.currentWave = 0;
		initPlayer();
		initWalls();
		Event evt = new Event(Event.Property.NEW_GAME, this);
		EventBus.INSTANCE.publish(evt);
		this.messager.sendMessage(evt);
	}

	/**
	 * Game over handling.
	 */
	public void gameOver() {
		log("Noob, game over");
		this.objects.clear();
		EventBus.INSTANCE.publish(new Event(Event.Property.GAME_OVER));
	}

	/**
	 * Initializes a new wave of enemies and items. Increments the wave number,
	 * and sends an event to the bus and listeners of the type <code>NEW_WAVE</code>
	 * and with the value <code>this</code>.
	 * 
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
		List<CollidableObject> items = ObjectFactory.newItems();

		log("Items received");

		for (CollidableObject item : items) {
			item.addReceiver(this);
			//If spawn point is to close to player or inside a wall, set new randomly chosen position.
			setAtLegalPosition(item);
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
			// If spawn point is to close to player or inside a wall, set new randomly chosen position.
			setAtLegalPosition(temp);
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
	 * Check if the specified object is colliding with a wall.
	 * 
	 * @param o
	 * @return True if the specified object is not colliding with a wall, otherwise
	 * false. Also returns false if player is null
	 */
	private boolean objectCollidesWithPlayer(CollidableObject o){
		if(this.player == null) {
			return false;
		}

		// Get width of a single tile
		TileMap tileMap = ObjectFactory.getTileMap();
		int width = (int) tileMap.getTileSize().getWidth();

		// If distance between the object and the player is less than 5 tiles, return false.
		if (Math.abs((o.getPosition().x - this.player.getPosition().x)) + 
				Math.abs(o.getPosition().y - this.player.getPosition().y) < 5*width) {
			return true;
		}

		return false;
	}

	/**
	 * Check if the specified object is colliding with a wall in the game.
	 * 
	 * @param o The object to check
	 * @return True if the object collides with a wall, otherwise false
	 */
	private boolean objectCollidesWithWall(CollidableObject o) {
		// If the collidable collides with a wall, return true

		for (CollidableObject obj : this.objects){
			if (obj instanceof ConcreteCollidableObject){
				if (o.isColliding(obj)){
					return true;
				}
			}
		}

		return false;
	}
	
	/**
	 * Given a Collidable object, will set the position of the object at random, until a position is found
	 * that doesn't collide with a wall or the player.
	 * @param object
	 */
	private void setAtLegalPosition(CollidableObject object){
		while (objectCollidesWithPlayer(object) || objectCollidesWithWall(object)){
			object.setPosition(getRandomPosition());
		}
	}

	/**
	 * get a randomly chosen point on the map.
	 * @return a randomly chosen point on the map.
	 */
	private Point getRandomPosition(){
		TileMap tilemap = ObjectFactory.getTileMap();

		int rows = (int) tilemap.getTileMapSize().getWidth() - 1;
		int columns = (int) tilemap.getTileMapSize().getHeight() - 1;
		int width = (int) tilemap.getTileSize().getWidth();
		int height = (int) tilemap.getTileSize().getHeight();

		Random random = new Random();

		int x = random.nextInt(width*rows);
		int y = random.nextInt(height*columns);

		return new Point (x,y);
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

				EventBus.INSTANCE.publish(new Event(Property.PORTAL_CREATED, p));
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
				
				//If close to detonation, the grenade deals full damage
				if (distance < radius/grenade.getSplashDamageFactor()){
					e.takeDamage(p.getDamage());
				}
				//If in range of detonation, the grenade deals damage depending on its splash damage factor.
				else if (distance < radius) {
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
	private void moveEntitys(double dt){
		for (CollidableObject t : this.objects) {

			if (t instanceof Entity) {
				Entity temp = (Entity) t;

				if (t instanceof AbstractCharacter) {
					((AbstractCharacter) t).update(dt);
				}
				((Entity) t).setRememberedPosition(t.getPosition());
				((Entity) t).move(dt);
				checkIfLeftPortal(temp);
			}
			
		}
		solveCollisions();
	}

	/**
	 * For each Collidable object in the GameModel, search for any occurring collisions. For any collision between a AbstractCharactar and a wall
	 * will reset the AbstractCharacter to it's last position. All other triggered events triggered by collision will be handled by the objects themselves.
	 */
	private void solveCollisions() {
		for (CollidableObject t : this.objects){
			for (CollidableObject w : this.objects){
				
				if (t.isColliding(w)){
					//Characters colliding with walls reset their positions.
					if (t instanceof AbstractCharacter && w instanceof ConcreteCollidableObject){
						t.setPosition(((AbstractCharacter)t).getRememberedPosition());
					}
					w.didCollide(t);
				}
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
	 * Updates the game model.
	 * 
	 * @param dt
	 *            The time since the last update.
	 */
	public void update(double dt) {

		moveEntitys(dt);
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
	public void addReceiver(IEventHandler listener) {
		this.messager.addListener(listener);
	}

}
