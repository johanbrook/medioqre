package model;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
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
import constants.Direction;
import event.Event;
import event.Event.Property;
import event.EventBus;
import event.IMessageListener;
import event.IMessageSender;
import event.Messager;
import factory.ObjectFactory;

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
	private List<CollidableObject> portalVictims;

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
		//	a) Our entities list is quite small
		//	b) Writes happen very infrequently.
		this.objects = new CopyOnWriteArrayList<CollidableObject>();
		this.enemies = new CopyOnWriteArrayList<Enemy>();
		this.portals = new Portal[2];
		this.portalVictims = new CopyOnWriteArrayList<CollidableObject>();
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

			}else {
				log("Out of ammo");
			}
			break;

		case CHANGED_DIRECTION:

			this.player.start();
			this.player.setDirection((Direction) evt.getValue());
			break;

		case WAS_DESTROYED:


			if(evt.getValue() instanceof Projectile) {
				Projectile p = (Projectile) evt.getValue();
				if(p.getOwner() instanceof PortalGun) {
					PortalGun g = (PortalGun) p.getOwner();

					// Always position new portals in the middle of where the portal
					// projectile landed:
					int x = (int) p.getPosition().x + p.getCollisionBox().width / 2;
					int y = (int) p.getPosition().y + p.getCollisionBox().height / 2;
					
					deployPortal(g.getMode(), new Point(x, y));

				}else if (p.getOwner() instanceof Grenade){
					doSplashDamage(p);
				}
			}

			this.objects.remove(evt.getValue());
			if(evt.getValue() instanceof Enemy) {
				this.enemies.remove(evt.getValue());
				this.messager.sendMessage(evt);
				checkEnemiesLeft();
				randomizeItem();

			} else if (evt.getValue() instanceof Player){
				gameOver();
			}

			log(evt.getValue().getClass().getSimpleName() + " was destroyed");
			break;

		case DID_ATTACK:
			Projectile enemyProjectile = (Projectile) evt.getValue();
			enemyProjectile.addReceiver(this);
			this.objects.add(enemyProjectile);
			break;

		case CHANGED_WEAPON:
			int slot = (Integer) evt.getValue();

			if(this.player.getCurrentWeapon() instanceof PortalGun && slot == 3) {
				PortalGun g = (PortalGun) this.player.getCurrentWeapon();
				g.switchMode();
			}
			else{
				this.player.setCurrentWeapon(slot);
			}

			break;
		}
	}

	private void randomizeItem() {
		Random random = new Random();
		int rand = random.nextInt(100);
		if (rand < ObjectFactory.getItemSpawnChance()) {
			newAmmoCrate();
			newMedPack();
		}
		
	
	}
	
	private void newAmmoCrate(){
		CollidableObject item = ObjectFactory.newItem("AmmoCrate");
		item.addReceiver(this);
		this.objects.add(item);
		log("** Ammo Crate created **");
	}
	
	private void newMedPack(){
		CollidableObject item = ObjectFactory.newItem("MedPack");
		item.addReceiver(this);
		this.objects.add(item);
		log("** MedPack created **");
		
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
		this.currentWave++;

		initEnemies();
		addItems();

		Event evt = new Event(Property.NEW_WAVE, this);
		this.messager.sendMessage(evt);
		EventBus.INSTANCE.publish(evt);
		
		log("New wave: "+this.currentWave);
	}

	private void addItems() {
		List<CollidableObject> items = ObjectFactory.newItemsForWave(this.currentWave);
		
		log("Items received");
		log(items);
		
		for(CollidableObject item : items) {
			item.addReceiver(this);
		}

		this.objects.addAll(items);
		
		log("** "+items.size() + " items added");

	}

	private void initEnemies() {
		this.objects.removeAll(this.enemies);
		this.enemies.clear();

		List<Enemy> tempEnemies = ObjectFactory.newEnemiesForWave(this.currentWave);

		log("Enemies received");
		log(tempEnemies);

		for(Enemy temp : tempEnemies) {
			temp.addReceiver(this);
		}

		this.enemies.addAll(tempEnemies);
		this.objects.addAll(tempEnemies);

		log("** "+ tempEnemies.size() +" enemies added");
	}

	private void initPlayer() {
		this.player = ObjectFactory.newPlayer();
		this.player.setCurrentWeapon(0);
		this.objects.add(this.player);
		this.player.addReceiver(this);
	}


	private void deployPortal(Mode mode, Point position) {

		for(int i = 0; i < this.portals.length; i++) {

			if(this.portals[i] == null) {
				Portal p = ObjectFactory.newPortal(mode, position);
				p.center();
				EventBus.INSTANCE.publish(new Event(Property.PORTAL_CREATED, p));
				this.objects.add(p);
				this.portals[i] = p;

				if(i == 1) {
					p.setSisterPortal(this.portals[0]);
					this.portals[0].setSisterPortal(p);
				}

				return;
			}
			else if(this.portals[i].getMode() == mode) {
				this.portals[i].setPositionFromCenter(position);
				return;
			}
		}
	}

	private void doSplashDamage(Projectile p) {
		if (p.getOwner() instanceof Grenade){
			
			Grenade grenade = ((Grenade) p.getOwner());
			int radius = grenade.getRadius();
			Rectangle splash = new Rectangle(p.getPosition().x - radius/2, p.getPosition().y - radius/2, radius,radius);
			for (Enemy e : this.enemies){
				if (splash.intersects(e.getCollisionBox())){
					
					e.takeDamage(p.getDamage()/grenade.getSplashDamageFactor());
					log("Enemy was hit by Grenade splash! Took: " + p.getDamage()/grenade.getSplashDamageFactor() + " damage! Now has " + e.getHealth() + " hp left");
				}
			}
		}

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

				if(t instanceof Projectile && !(w instanceof ICollectableItem || w instanceof Portal)) {
					
					// If an enemy's projectile hit another enemy, don't withdraw damage
					// I.e. if the projectile's weapon's owner is an enemy, and the target is
					// an enemy, don't do anything.
					if(! ( ((Projectile) t).getOwner().getOwner() instanceof Enemy &&
						w instanceof Enemy )) {
						
						// If the projectile hit the player
						if (w instanceof AbstractCharacter){
							((AbstractCharacter) w).takeDamage(((Projectile) t).getDamage());
	
							log(w.getClass().getSimpleName()+" was hit, now has " + ((AbstractCharacter) w).getHealth() + " hp");
						}
					}
					
					t.destroy();
				} 
				else if (w instanceof ICollectableItem && t instanceof Player){
					((ICollectableItem) w).pickedUpBy( ((Player) t));
				}

				if(w instanceof Portal) {
					Portal p = (Portal) w;

					if(p.getSisterPortal() != null) {

						if(!this.portalVictims.contains(t)) {
							// Teleport
							t.setPosition(p.getSisterPortal().getPosition());
							this.portalVictims.add(t);
						}
					}

				}



				if(!(w instanceof ICollectableItem) && !(w instanceof Portal)) {
					if(directionIsBlocked(currentDirection, blockedDirection)){

						t.stop();

						if (t instanceof Enemy && w instanceof Enemy){
							((Enemy)w).getPushed((Enemy)t);
						}
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
		checkIfLeftPortal();
	}


	private void checkIfLeftPortal() {
		for(CollidableObject obj : this.portalVictims) {
			boolean isColliding = false;
			for(CollidableObject portal : this.portals) {
				if(obj.isColliding(portal)) {
					isColliding = true;
				}
			}
			if (!isColliding)
				this.portalVictims.remove(obj);
		}
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
