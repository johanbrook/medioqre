package tools.factory;

import graphics.opengl.GLScreen;
import graphics.opengl.animation.Actor;
import graphics.opengl.bitmapfont.GLBitmapFont;
import graphics.opengl.core.Size;
import graphics.opengl.tilemap.TileMap;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static tools.Logger.*;

import model.CollidableObject;
import model.ConcreteCollidableObject;
import model.character.AbstractCharacter;
import model.character.Enemy;
import model.character.Player;
import model.item.ICollectableItem;
import model.weapon.AbstractWeapon;
import model.weapon.Grenade;
import model.weapon.MachineGun;
import model.weapon.Portal;
import model.weapon.WeaponBelt;
import model.weapon.PortalGun.Mode;
import model.weapon.Projectile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tools.datamanagement.ResourceLoader;

import controller.AppController;



/**
 * A class used to create all files in FrankTheTank.
 * 
 * It should load all its files from JSON.
 * 
 * @author Johan Brook and John Barbero Unenge.
 * 
 */
public class ObjectFactory {

	private static Level level;

	// Model

	private static JSONObject player;
	private static JSONObject enemy;
	private static JSONArray weapons;
	private static JSONArray items;

	private static JSONObject config;
	private static JSONObject objectTypes;

	private static final int SPAWN_MARGIN = 100;

	// View
	private static JSONArray actors;
	private static JSONObject tileMap;
	private static TileMap tileMapInstance;
	
	private static JSONArray bitmapFonts;

	private ObjectFactory() {
	}

	private static void initJSONObjects() {
		try {
			config = level.getConfig();

			// Model

			player = config.getJSONObject("playerConfig");
			enemy = config.getJSONObject("enemyConfig");
			items = config.getJSONObject("itemConfig").getJSONArray("items");
			weapons = config.getJSONObject("weaponConfig").getJSONArray(
					"weapons");
			objectTypes = config.getJSONObject("objectTypes");

			// View

			JSONArray actorsData = config.getJSONObject("actorsConfig")
					.getJSONArray("actors");
			actors = new JSONArray();

			for (int i = 0; i < actorsData.length(); i++) {
				String json = ResourceLoader
						.loadJSONStringFromResources(actorsData.getString(i));
				actors.put(new JSONObject(json));
			}

			log("Initializing tile map ...");
			tileMap = config.getJSONObject("tileMapConfig");
			tileMapInstance = ResourceLoader.loadTileMapFromResources(tileMap
					.getString("levelbitmap"));

			JSONArray bitmapFontData = config.getJSONObject("bitmapsConfig").getJSONArray("bitmaps");
			bitmapFonts = new JSONArray();
			for (int i = 0; i < bitmapFontData.length(); i++) {
				bitmapFonts.put(new JSONObject(ResourceLoader.loadJSONStringFromResources(bitmapFontData.getString(i))));
			}
			
			log("Config initialized");

		} catch (JSONException e) {
			err("Couldn't initialize all config files: " + e.getMessage());
		}

	}

	/**
	 * Get an integer from the config file based on key.
	 * 
	 * @param key
	 *            The setting
	 * @return The value
	 */
	public static int getConfigInt(String key) {
		try {
			return config.getInt(key);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return -1;
	}

	/**
	 * Get a string from the config file based on key.
	 * 
	 * @param key
	 *            The setting
	 * @return The value
	 */
	public static String getConfigString(String key) {
		try {
			return config.getString(key);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Get a JSONObject from the config file based on key.
	 * 
	 * @param key
	 *            The setting
	 * @return The value
	 */
	public static JSONObject getConfigObject(String key) {
		try {
			return config.getJSONObject(key);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Set the level
	 * 
	 * @param level
	 *            The level
	 */
	public static void setLevel(Level level) {
		ObjectFactory.level = level;
		initJSONObjects();
	}

	/**
	 * Get the current loaded level
	 * 
	 * @return The level
	 */
	public static Level getLevel() {
		return level;
	}
	
	
	/**
	 * Get the current tilemap
	 * @return The tilemap
	 */
	public static TileMap getTileMap(){
		return tileMapInstance;
	}

	// Model objects

	/**
	 * Creates a new Player.
	 * 
	 * <p>Sets the object type to Player for the returned object.</p>
	 * 
	 * @return A new Player
	 */
	public static Player newPlayer() {
		try {

			Player p = new Player(player);

			p.setPosition(player.getJSONObject("position").getInt("x"), player
					.getJSONObject("position").getInt("y"));

			p.setHealth(player.getInt("health"));
			p.setWeaponBelt(newWeaponBelt(p));

			Class startingWeapon = Class.forName("model.weapon."
					+ player.getString("startingWeapon"));
			p.setCurrentWeapon(startingWeapon);
			p.setBit(objectTypes.getInt("player"), 4);
			
			p.stop();
			
			return p;

		} catch (JSONException e) {
			err("Couldn't initialize player from JSON object");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			err("Couldn't find starting weapon class: " + e);
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Create a new enemy.
	 * 
	 * <p>Sets the object type to Enemy for the returned object.</p>
	 * 
	 * @return The enemy
	 */
	public static Enemy newEnemy() {

		try {
			Enemy en = new Enemy(enemy);

			en.setHealth(enemy.getInt("health"));
			
			JSONObject weaponObj = enemy.getJSONObject("weapon");
			AbstractWeapon melee = createWeaponFromJSON(en, weaponObj);
			Projectile projectile = newProjectile(melee, 
							weaponObj.getJSONObject("projectile"));
			

			melee.setProjectile(projectile);
			en.setCurrentWeapon(melee);
			en.setBit(objectTypes.getInt("enemy"), 4);
			
			return en;
			
		} catch (JSONException e) {
			err(e.getMessage());
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Create a new wave of enemies from a given wave number.
	 * 
	 * <p>
	 * Uses the Fibonacci sequence to generate the number of enemies for each
	 * <code>waveNumber</code>
	 * </p>
	 * 
	 * @param waveNumber
	 *            The current wave
	 * @return A list with enemies, complete with weapons and projectiles
	 */
	public static List<Enemy> newEnemiesForWave(int waveNumber) {

		List<Enemy> enemies = new ArrayList<Enemy>();
		int enemiesToAdd = tools.Math.prime(waveNumber);


		for (int i = 0; i < enemiesToAdd; i++) {

			Enemy e = newEnemy();
			Random random = new Random();

			int x = (int) (SPAWN_MARGIN + random.nextFloat()
					* (tileMapInstance.getTileMapSize().getWidth()
							* tileMapInstance.getTileSize().getWidth() - (2 * SPAWN_MARGIN)));
			int y = (int) (SPAWN_MARGIN + random.nextFloat()
					* (tileMapInstance.getTileMapSize().getHeight()
							* tileMapInstance.getTileSize().getHeight() - (2 * SPAWN_MARGIN)));

			e.setPosition(x, y);
			enemies.add(e);
		}

		return enemies;
	}

	/**
	 * Create a new list of items for a given wave.
	 * 
	 * @param waveNumber
	 *            The current wave
	 * @return A list of items with randomized positions
	 */
	public static List<CollidableObject> newItems() {

		List<CollidableObject> itemList = new ArrayList<CollidableObject>();
		Random random = new Random();

		try {

			for (int i = 0; i < items.length(); i++) {

				int x = (int) (SPAWN_MARGIN + random.nextFloat()
						* (tileMapInstance.getTileMapSize().getWidth()
								* tileMapInstance.getTileSize().getWidth() - (2 * SPAWN_MARGIN)));
				int y = (int) (SPAWN_MARGIN + random.nextFloat()
						* (tileMapInstance.getTileMapSize().getHeight()
								* tileMapInstance.getTileSize().getHeight() - (2 * SPAWN_MARGIN)));

				JSONObject it = items.getJSONObject(i);
				CollidableObject item = createItemFromJSON(it, x, y);
						
				if (item == null) {
					continue;
				}

				itemList.add(item);
			}
		} catch (JSONException e) {
			err("Couldn't load items! " + e.getMessage());
		}

		return itemList;
	}

	/**
	 * Create a new item from a given type ("MedPack", "AmmoCrate", etc.).
	 * 
	 * <p>
	 * Must be referenced in the items config file
	 * </p>
	 * 
	 * @param type
	 *            The item type
	 * @return An item
	 */
	public static CollidableObject newItem(String type) {
		Random random = new Random();

		for (int i = 0; i < items.length(); i++) {
			try {
				if (items.getJSONObject(i).getString("type").equals(type)) {

					int x = (int) (random.nextFloat()
							* tileMapInstance.getTileMapSize().getWidth() * tileMapInstance
							.getTileSize().getWidth());
					int y = (int) (random.nextFloat() * (tileMapInstance
							.getTileMapSize().getHeight() * tileMapInstance
							.getTileSize().getHeight()));

					JSONObject it = items.getJSONObject(i);
					return createItemFromJSON(it, x, y);

				}
			} catch (JSONException e) {
				err("Couldn't load item of type " + type + "! "
						+ e.getMessage());
			}
		}
		return null;
	}

	public static List<ConcreteCollidableObject> newWalls() {
		List<ConcreteCollidableObject> walls = new LinkedList<ConcreteCollidableObject>();

		boolean[][] coll = getCollidables();

		int tileWidth = (int) tileMapInstance.getTileSize().getWidth();
		int tileHeight = (int) tileMapInstance.getTileSize().getHeight();

		for (int x = 0; x < coll.length; x++) {
			for (int y = 0; y < coll[x].length; y++) {
				if (coll[x][y]) {
					walls.add(new ConcreteCollidableObject(
							new Rectangle(x * tileWidth, y * tileHeight,
									tileWidth, tileHeight), new Dimension(
									tileWidth, tileHeight), 0, 0));
				}
			}
		}

		return walls;
	}

	/**
	 * Create a weapon list from the weapons specified in the weapons JSON file.
	 * 
	 * <p>
	 * <em>Note:</em> projectiles are created and set to the weapons inside this
	 * method.
	 * </p>
	 * 
	 * @param owner
	 *            The weapons' owner
	 * @return A list of weapons with corresponding projectiles
	 */
	public static WeaponBelt newWeaponBelt(AbstractCharacter owner) {

		WeaponBelt weaponsList = new WeaponBelt();

		try {

			for (int i = 0; i < weapons.length(); i++) {
				JSONObject wp = weapons.getJSONObject(i);

				AbstractWeapon weapon = createWeaponFromJSON(owner, wp);

				if (weapon == null) {
					continue;
				}

				if (weapon instanceof Grenade) {
					if (!wp.isNull("radius")) {
						((Grenade) weapon).setRadius(wp.getInt("radius"));

					}
					if (!wp.isNull("splashDamageFactor")) {
						((Grenade) weapon).setSplashDamageFactor(wp
								.getInt("splashDamageFactor"));
					}
				}

				weapon.setBit(objectTypes.getInt("weapon"), 4);
				weapon.setBit(wp.getInt("id"), 3);
				Projectile projectile = newProjectile(weapon, wp.getJSONObject("projectile"));
				
				weapon.setProjectile(projectile);
				weaponsList.add(weapon);
			}

		} catch (JSONException e) {
			err("Couldn't load weapons from file: " + e.getMessage());
		} catch (Exception e) {
			err(e.getMessage());
		}

		return weaponsList;
	}

	
	/**
	 * Create a new projectile from a JSON object.
	 * 
	 * <p>Sets the object type to Projectile for the returned object.</p>
	 * 
	 * @param pwner The weapon owner
	 * @param obj The JSON object
	 * @return The Projectile
	 */
	public static Projectile newProjectile(AbstractWeapon pwner, JSONObject obj) {
		
		try{
			Projectile proj = new Projectile(pwner, obj);
			proj.setBit(objectTypes.getInt("projectile"), 4);
			
			return proj;
		}
		catch(JSONException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	/**
	 * Create a new portal.
	 * 
	 * @param mode
	 *            The portal mode
	 * @param position
	 *            The position
	 * @return A Portal
	 */
	public static Portal newPortal(Mode mode, Point position) {
		try {
			JSONObject portal = config.getJSONObject("portal");
			Portal p = new Portal(mode, position, portal);
			
			p.setBit(objectTypes.getInt("portal"), 4);
			
			return p;

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return null;
	}

	// View objects

	/**
	 * Creates a new Actor.
	 * 
	 * It checks what kind of CollidableObject is sent and chooses its actor
	 * depending on that.
	 * 
	 * @param collidableObject
	 *            A CollidableObject to follow
	 * @return A new Actor
	 */
	public static Actor newActor(Object collidableObject) {
		Actor newA = null;

		try {

			if (collidableObject instanceof Player)
				newA = new Actor(actors.getJSONObject(0));
			else if (collidableObject instanceof Enemy)
				newA = new Actor(actors.getJSONObject(1));
			else if (collidableObject instanceof ICollectableItem)
				newA = new Actor(actors.getJSONObject(2));
			else if (collidableObject instanceof Projectile) {
				Projectile p = (Projectile) collidableObject;
				 if (p.getOwner() instanceof MachineGun)
					 newA = new Actor(actors.getJSONObject(3));
				 else if (p.getOwner() instanceof Grenade)
					 newA = new Actor(actors.getJSONObject(4));
			}
			else if (collidableObject instanceof Portal)
				newA = new Actor(actors.getJSONObject(5));
			else if (collidableObject instanceof Integer) {
				Integer i = (Integer) collidableObject;
				
				if (i == 0) {
					newA = new Actor(actors.getJSONObject(6));
				} else if (i == 1) {
					newA = new Actor(actors.getJSONObject(7));
				}
			}
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		if (newA != null && collidableObject instanceof CollidableObject) {
			newA.setObject((CollidableObject)collidableObject);

			if (AppController.isDebugMode()) {
				newA.setShowCollisionBox(true);
			}
		}

		return newA;
	}

	/**
	 * Returns a cached tilemap and sets it tilesheet, viewport and tilesize to
	 * the appropriate ones.
	 * 
	 * @return A new tilemap
	 */
	public static TileMap newTileMap() {
		try {
			tileMapInstance.setTileSheet(ResourceLoader
					.loadTileSheetFromResource(tileMap.getString("tilesheet")));
			tileMapInstance.setViewPortSize(new Size(48 * 12, 48 * 20));
			tileMapInstance.setTileSize(new Size(48, 48));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return tileMapInstance;
	}

	/**
	 * Not yet implemented.
	 * 
	 * @return null
	 */
	public static GLScreen newScreen() {
		return null;
	}

	public static GLBitmapFont newBitmapFont() {
		try {
			return new GLBitmapFont(bitmapFonts.getJSONObject(0));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	// AI objects

	/**
	 * Get what tiles are collidable in the tilemap.
	 * 
	 * @return A two dimensional array of booleans.
	 */
	public static boolean[][] getCollidables() {
		return ObjectFactory.newTileMap().getCollidables();
	}

	// Helpers

	/**
	 * Create an object dynamically from a name and constructor parameters.
	 * 
	 * @param name
	 *            The full name of the class (i.e.: "model.weapon.Grenade")
	 * @param constructorParams
	 *            An array of parameters types for the constructor (i.e. Class[]
	 *            {String.class, int.class}
	 * @param objectParams
	 *            An array of constructor parameter values (i.e. Object[]
	 *            {"Hello World", 3})
	 * @return The object created, or null if something went wrong (exceptions
	 *         are catched inside of this method)
	 */
	@SuppressWarnings("rawtypes")
	public static Object createObjectFromString(String name,
			Class[] constructorParams, Object[] objectParams) {

		try {
			Class cl = Class.forName(name);
			@SuppressWarnings("unchecked")
			java.lang.reflect.Constructor co = cl
					.getConstructor(constructorParams);

			return (Object) co.newInstance(objectParams);
		} catch (ClassNotFoundException e) {
			err("Couldn't find class: " + e.getMessage());
		} catch (InstantiationException e) {
			err("Class must be concrete: " + e);
		} catch (NoSuchMethodException e) {
			err("Constructor doesn't exist or is not public: " + e.getMessage());
		} catch (IllegalArgumentException e) {
			err("Illegal argument passed to class: " + e);
		} catch (Exception e) {
			err("Couldn't create instance of class " + name);
		}

		return null;
	}

	/**
	 * Create a weapon dynamically from a name.
	 * 
	 * @param type
	 *            The type of weapon. Must match property "type" in the
	 *            "weapons.json" file
	 * @param objectParams
	 *            The parameters to send to the constructor
	 * @return A 'type' instance with the type AbstractWeapon
	 */
	public static AbstractWeapon createWeaponFromString(String type,
			Object[] objectParams) {
		Object obj = createObjectFromString("model.weapon." + type,
				new Class[]{AbstractCharacter.class, int.class, double.class,
						double.class}, objectParams);

		AbstractWeapon weapon = (AbstractWeapon) obj;
		
		try {
			weapon.setBit(objectTypes.getInt("weapon"), 4);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return weapon;
	}
	
	/**
	 * Create a weapon from a JSON object.
	 * 
	 * <p>The kind of weapon (Melee, MachineGun ..) must be specified in the 'type'
	 * parameter in the JSON object. </p>
	 *
	 * @param owner The weapon's owner
	 * @param obj The JSON object
	 * @return An AbstractWeapon from the JSON parameters specified
	 */
	public static AbstractWeapon createWeaponFromJSON(AbstractCharacter owner, JSONObject obj) {
		
		try {
			Object[] params = new Object[]{owner, obj.getInt("ammo"),
					obj.getDouble("ammoMultiplier"),
					obj.getDouble("fireInterval")};
			
			AbstractWeapon w = createWeaponFromString(obj.getString("type"), params);
			w.setBit(obj.getInt("id"), 3);
			
			return w;
		}
		catch(JSONException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	/**
	 * Create an item dynamically from a name.
	 * 
	 * @param type
	 *            The type of item. Must match property "type" in the
	 *            "items.json" file
	 * @param objectParams
	 *            The parameters to send to the constructor
	 * @return A 'type' instance with the type ICollectableItem
	 */
	public static CollidableObject createItemFromString(String type,
			Object[] objectParams) {
		Object obj = createObjectFromString("model.item." + type, new Class[]{
				int.class, int.class, int.class, int.class, int.class},
				objectParams);
		try {
			((CollidableObject) obj).setBit(objectTypes.getInt(type), 4);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return (CollidableObject) obj;
	}
	
	/**
	 * Create an item from a JSON object.
	 * 
	 * <p>The kind of item (MedPack, AmmoCrate ..) should be specified in the
	 * 'type' key in the JSON object.</p>
	 * 
	 * @param obj The JSON object
	 * @param x The x position
	 * @param y The y position
	 * @return An item with the type CollidableObject
	 */
	public static CollidableObject createItemFromJSON(JSONObject obj, int x, int y) {
		try {
			JSONObject bounds = obj.getJSONObject("bounds");
			Object[] params = new Object[]{obj.getInt("amount"), x, y,
					bounds.getInt("width"), bounds.getInt("height")};
			
			CollidableObject item = createItemFromString(obj.getString("type"), params);
			return item;
		}
		catch(JSONException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}