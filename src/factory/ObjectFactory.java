package factory;

import graphics.opengl.Actor;
import gui.Screen;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static tools.Logger.*;

import model.CollidableObject;
import model.ConcreteCollidableObject;
import model.Entity;
import model.character.AbstractCharacter;
import model.character.Enemy;
import model.character.Player;
import model.weapon.AbstractWeapon;
import model.weapon.Grenade;
import model.weapon.Portal;
import model.weapon.PortalGun.Mode;
import model.weapon.Projectile;
import model.weapon.Projectile.Range;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tilemap.TileMap;

public class ObjectFactory {

	private static Level level;
	
	private static JSONObject player; 
	private static JSONObject enemy;
	private static JSONArray weapons;
	private static JSONArray items;
	
	private static JSONObject config;


	private static void initJSONObjects() {
		config = level.getConfig();
		
		try {
			
			player = config.getJSONObject("playerConfig");
			enemy = config.getJSONObject("enemyConfig");
			items = config.getJSONObject("itemConfig").getJSONArray("items");
			weapons = config.getJSONObject("weaponConfig").getJSONArray("weapons");
			
			log("Config initialized");
			
		} catch (JSONException e) {
			err("Couldn't initialize all config files: "+e.getMessage());
		}
		
	}
	
	/**
	 * Get an integer from the config file based on key.
	 * 
	 * @param key The setting
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
	 * @param key The setting
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
	 * @param key The setting
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
	 * @param level The level
	 */
	public static void setLevel(Level level)
	{
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

	// Model objects

	public static Player newPlayer()
	{
		try {

			JSONObject bounds = player.getJSONObject("bounds");
			
			Player p = new Player(
					player.getInt("speed"),
					new Rectangle(bounds.getInt("boxWidth"), bounds.getInt("boxHeight")),
					new Dimension(bounds.getInt("width"), bounds.getInt("height")),
					bounds.getInt("offsetX"), bounds.getInt("offsetY"));

			p.setPosition(	player.getJSONObject("position").getInt("x"), 
					player.getJSONObject("position").getInt("y"));

			p.setHealth(player.getInt("health"));
			p.setWeaponBelt(newWeaponBelt(p));
			
			Class startingWeapon = Class.forName("model.weapon."+player.getString("startingWeapon"));
			p.setCurrentWeapon(startingWeapon);
			
			return p;

		} catch (JSONException e) {
			err("Couldn't initialize player from JSON object");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			err("Couldn't find starting weapon class: "+e);
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Create a new enemy
	 * 
	 * @return The enemy
	 */
	public static Enemy newEnemy() {

		try {
			JSONObject bounds = enemy.getJSONObject("bounds");
			Enemy en = new Enemy(enemy.getInt("speed"), 
					new Rectangle(bounds.getInt("boxWidth"), bounds.getInt("boxHeight")), 
					new Dimension(bounds.getInt("width"), bounds.getInt("height")), 
					bounds.getInt("offsetX"), bounds.getInt("offsetY"));

			en.setHealth(enemy.getInt("health"));

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
	 * <p>Uses the Fibonacci sequence to generate the number of enemies for each <code>waveNumber</code></p>
	 * 
	 * @param waveNumber The current wave
	 * @return A list with enemies, complete with weapons and projectiles
	 */
	public static List<Enemy> newEnemiesForWave(int waveNumber) {

		List<Enemy> enemies = new ArrayList<Enemy>();
		int enemiesToAdd = math.Util.fib(waveNumber);

		try {
			
			for(int i = 0; i < enemiesToAdd; i++) {

				Enemy e = newEnemy();

				JSONObject weaponObj = enemy.getJSONObject("weapon");

				AbstractWeapon melee = createWeaponFromString(weaponObj.getString("type"), new Object[]{e, weaponObj.getInt("ammo"), weaponObj.getDouble("ammoMultiplier")});

				Projectile projectile = newProjectile(melee, weaponObj);

				melee.setProjectile(projectile);
				e.setCurrentWeapon(melee);

				enemies.add(e);
			}
		}
		catch(JSONException e) {
			err(e.getMessage());
			e.printStackTrace();
		}

		return enemies;
	}

	/**
	 * Create a new list of items for a given wave.
	 * 
	 * @param waveNumber The current wave
	 * @return A list of items with randomized positions
	 */
	public static List<CollidableObject> newItemsForWave(int waveNumber) {
		
		List<CollidableObject> itemList = new ArrayList<CollidableObject>();
		Random random = new Random();
		
		try {
			
			for(int i = 0; i < items.length(); i++) {
				
				//TODO Fix these hardcoded measures!
				int x = random.nextInt(1000);
				int y = random.nextInt(1000);
				
				JSONObject it = items.getJSONObject(i);
				JSONObject bounds = it.getJSONObject("bounds");
				CollidableObject item = createItemFromString(it.getString("type"), 
															new Object[] {	it.getInt("amount"),
																			x, y,
																			bounds.getInt("width"),
																			bounds.getInt("height")} );
				if(item == null) {
					continue;
				}
				
				itemList.add(item);
			}
		}
		catch(JSONException e) {
			err("Couldn't load items! "+e.getMessage());
		}
		
		return itemList;
	}
	
	
	/**
	 * Create a new item from a given type ("MedPack", "AmmoCrate", etc.).
	 * 
	 * <p>Must be referenced in the items config file</p>
	 * 
	 * @param type The item type
	 * @return An item
	 */
	public static CollidableObject newItem (String type){
		Random random = new Random();
	
		
		for (int i = 0; i < items.length(); i++) {
			try {
				if (items.getJSONObject(i).getString("type").equals(type)){
					
					//TODO Fix these hardcoded measures!
					int x = random.nextInt(1000);
					int y = random.nextInt(1000);
					
					JSONObject it = items.getJSONObject(i);
					JSONObject bounds = it.getJSONObject("bounds");
					
					return createItemFromString(it.getString("type"), new Object[] {	it.getInt("amount"), 
						x, y,
						bounds.getInt("width"),
						bounds.getInt("height")} );
					
				}
			} catch (JSONException e) {
				System.err.println("Couldn't load item of type "+ type + "! "+e.getMessage());
			}
		}
		return null;
	}

	public static List<ConcreteCollidableObject> newWalls() {
		return null;
	}


	/**
	 * Create a weapon list from the weapons specified in the weapons JSON file.
	 * 
	 * <p><em>Note:</em> projectiles are created and set to the weapons inside this method.</p>
	 * 
	 * @param owner The weapons' owner
	 * @return A list of weapons with corresponding projectiles
	 */
	public static List<AbstractWeapon> newWeaponBelt(AbstractCharacter owner) {

		List<AbstractWeapon> weaponsList = new ArrayList<AbstractWeapon>();

		try {

			for(int i = 0; i < weapons.length(); i++) {
				JSONObject wp = weapons.getJSONObject(i);

				AbstractWeapon weapon = createWeaponFromString(wp.getString("type"),
										new Object[] {owner, wp.getInt("ammo"), wp.getDouble("ammoMultiplier")}
										);
				
				if(weapon == null) {
					continue;
				}

				if (weapon instanceof Grenade){
					if (!wp.isNull("radius")){
						((Grenade) weapon).setRadius(wp.getInt("radius"));

					}
					if (!wp.isNull("splashDamageFactor")){
						((Grenade) weapon).setSplashDamageFactor(wp.getInt("splashDamageFactor"));
					}
				}

				Projectile projectile = newProjectile(weapon, wp);

				weapon.setProjectile(projectile);
				weaponsList.add(weapon);
			}

		}
		catch(JSONException e) {
			err("Couldn't load weapons from file: "+e.getMessage());
		}
		catch(Exception e) {
			err(e.getMessage());
		}


		return weaponsList;
	}


	/**
	 * Create a new projectile from an <code>AbstractWeapon</code> and a <code>JSONObject</code> parent.
	 * 
	 * @param pwner The owning weapon
	 * @param parent The parent JSON object (typically a weapon specified in the config file)
	 * @return A Projectile
	 */
	public static Projectile newProjectile(AbstractWeapon pwner, JSONObject parent) {

		try {
			JSONObject projTemplate = parent.getJSONObject("projectile");

			Projectile projectile = new Projectile(	pwner, 
					projTemplate.getJSONObject("bounds").getInt("width"), 
					projTemplate.getJSONObject("bounds").getInt("height"), 
					projTemplate.getInt("damage"), 
					Range.valueOf(projTemplate.getString("range")), 
					projTemplate.getInt("speed"));

			return projectile;

		} catch (JSONException e) {
			err(e.getMessage());
			e.printStackTrace();
		}

		return null;
	}


	/**
	 * Create a new portal.
	 * 
	 * @param mode The portal mode
	 * @param position The position
	 * @return A Portal
	 */
	public static Portal newPortal(Mode mode, Point position) {
		try {
			JSONObject portal = config.getJSONObject("portal");
			JSONObject bounds = portal.getJSONObject("bounds");

			Portal p = new Portal(	mode,
					new Rectangle(position.x, position.y, bounds.getInt("boxWidth"), bounds.getInt("boxHeight")),
					new Dimension(bounds.getInt("width"), bounds.getInt("height")),
					bounds.getInt("offsetX"), bounds.getInt("offsetY"));

			return p;

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return null;
	}


	// View objects

	public static Actor newActor(Entity entity)
	{
		return null;
	}

	public static TileMap newTileMap()
	{
		return null;
	}

	public static Screen newScreen()
	{
		return null;
	}

	// AI objects

	public static boolean[][] getCollidables()
	{
		return null;
	}
	


	// Helpers

	/**
	 * Create an object dynamically from a name and constructor parameters.
	 * 
	 * @param name The full name of the class (i.e.: "model.weapon.Grenade")
	 * @param constructorParams An array of parameters types for the constructor (i.e. Class[] {String.class, int.class}
	 * @param objectParams An array of constructor parameter values (i.e. Object[] {"Hello World", 3})
	 * @return The object created, or null if something went wrong (exceptions are catched inside of this method)
	 */
	@SuppressWarnings("rawtypes")
	public static Object createObjectFromString(String name, Class[] constructorParams, Object[] objectParams) {

		try {
			Class cl = Class.forName(name);
			@SuppressWarnings("unchecked")
			java.lang.reflect.Constructor co = cl.getConstructor(constructorParams);

			return (Object) co.newInstance(objectParams);
		}
		catch(ClassNotFoundException e) {
			err("Couldn't find class: "+e.getMessage());
		}
		catch(InstantiationException e) {
			err("Class must be concrete: "+e);
		}
		catch(NoSuchMethodException e) {
			err("Constructor doesn't exist or is not public: "+e.getMessage());
		}
		catch(IllegalArgumentException e) {
			err("Illegal argument passed to class: "+e);
		}
		catch(Exception e) {
			err("Couldn't create instance of class "+ name);
		}

		return null;
	}


	/**
	 * Create a weapon dynamically from a name.
	 * 
	 * @param type The type of weapon. Must match property "type" in the "weapons.json" file
	 * @param objectParams The parameters to send to the constructor
	 * @return A 'type' instance with the type AbstractWeapon
	 */
	public static AbstractWeapon createWeaponFromString(String type, Object[] objectParams) {
		Object obj = createObjectFromString("model.weapon."+type, new Class[]{AbstractCharacter.class, int.class, double.class}, objectParams);

		return (AbstractWeapon) obj;
	}
	
	/**
	 * Create an item dynamically from a name.
	 * 
	 * @param type The type of item. Must match property "type" in the "items.json" file
	 * @param objectParams The parameters to send to the constructor
	 * @return A 'type' instance with the type ICollectableItem
	 */
	public static CollidableObject createItemFromString(String type, Object[] objectParams) {
		Object obj = createObjectFromString("model.item."+type, 
				new Class[]{int.class, int.class, int.class, int.class, int.class}, objectParams);

		return (CollidableObject) obj;
	}
}