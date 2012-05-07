package factory;

import graphics.opengl.Actor;
import gui.Screen;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import model.CollidableObject;
import model.ConcreteCollidableObject;
import model.Entity;
import model.character.AbstractCharacter;
import model.character.Enemy;
import model.character.Player;
import model.item.ICollectableItem;
import model.item.MedPack;
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
import datamanagement.ResourceLoader;

public class ObjectFactory {

	private static Level level;
	private static JSONObject player; 
	private static JSONObject enemy;
	private static JSONArray weapons;
	private static JSONArray items;
	private static JSONObject world;

	private static JSONObject levelData;


	private static void initJSONObjects() {
		try {
			player = new JSONObject(ResourceLoader.loadJSONStringFromResources(level.getPlayerData()));
			enemy = new JSONObject(ResourceLoader.loadJSONStringFromResources(level.getEnemyData()));
			weapons = new JSONArray(ResourceLoader.loadJSONStringFromResources(level.getWeaponData()));
			items = new JSONArray(ResourceLoader.loadJSONStringFromResources(level.getItemData()));
			world = new JSONObject(ResourceLoader.loadJSONStringFromResources(level.getWorldData()));

			levelData = new JSONObject(ResourceLoader.loadJSONStringFromResources(level.getLevelData()));

		} catch (JSONException e) {
			System.err.println("Couldn't initialize all JSON objects: "+e.getMessage());
		}
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

			return p;

		} catch (JSONException e) {
			System.err.println("Couldn't initialize player from JSON object");
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
			System.err.println(e.getMessage());
			e.printStackTrace();
		}

		return null;
	}


	public static List<Enemy> newEnemiesForWave(int waveNumber) {

		System.out.println("Current wave: "+waveNumber);
		List<Enemy> enemies = new ArrayList<Enemy>();

		int enemiesToAdd = math.Util.fib(waveNumber);
		System.out.println("Fib for wave nr "+waveNumber+": "+enemiesToAdd);

		try {

			for(int i = 0; i < enemiesToAdd; i++) {

				Enemy e = newEnemy();

				JSONObject weaponObj = enemy.getJSONObject("weapon");

				AbstractWeapon melee = createWeaponFromString(weaponObj.getString("type"), new Object[]{e, weaponObj.getInt("ammo")});

				Projectile projectile = newProjectile(melee, weaponObj);

				melee.setProjectile(projectile);
				e.setCurrentWeapon(melee);

				enemies.add(e);
			}
		}
		catch(JSONException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}

		return enemies;
	}

	public static List<CollidableObject> newItemsForWave(int waveNumber) {
		
		List<CollidableObject> itemList = new ArrayList<CollidableObject>();
		
		try {
			for(int i = 0; i < items.length(); i++) {
				JSONObject it = items.getJSONObject(i);
				JSONObject bounds = it.getJSONObject("bounds");
				CollidableObject item = createItemFromString(it.getString("type"), 
															new Object[] {	it.getInt("amount"),
																			10, 10,
																			bounds.getInt("width"),
																			bounds.getInt("height")} );
				if(item == null) {
					continue;
				}
				
				itemList.add(item);
			}
		}
		catch(JSONException e) {
			System.err.println("Couldn't load items! "+e.getMessage());
		}
		
		return itemList;
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
										new Object[] {owner, wp.getInt("ammo")}
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
			System.err.println("Couldn't load weapons from file: "+e.getMessage());
		}
		catch(Exception e) {
			System.err.println(e.getMessage());
		}


		return weaponsList;
	}


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
			System.err.println(e.getMessage());
			e.printStackTrace();
		}

		return null;
	}


	public static Portal newPortal(Mode mode, Point position) {
		try {
			JSONObject portal = world.getJSONObject("portal");
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
			System.err.println("Couldn't find class: "+e.getMessage());
		}
		catch(InstantiationException e) {
			System.err.println("Class must be concrete: "+e);
		}
		catch(NoSuchMethodException e) {
			System.err.println("Constructor doesn't exist or is not public: "+e.getMessage());
		}
		catch(IllegalArgumentException e) {
			System.err.println("Illegal argument passed to class: "+e);
		}
		catch(Exception e) {
			System.err.println("Couldn't create instance of class "+ name);
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
		Object obj = createObjectFromString("model.weapon."+type, new Class[]{AbstractCharacter.class, int.class}, objectParams);

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