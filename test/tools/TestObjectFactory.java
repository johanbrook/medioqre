/**
*	TestObjectFactory.java
*
*	@author Johan
*/

package tools;

import static org.junit.Assert.*;

import java.util.List;

import model.CollidableObject;
import model.character.Enemy;
import model.character.Player;
import model.item.ICollectableItem;
import model.item.MedPack;
import model.weapon.AbstractWeapon;
import model.weapon.MachineGun;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import tools.datamanagement.ResourceLoader;
import tools.factory.Level;
import tools.factory.ObjectFactory;

public class TestObjectFactory {

	@Before
	public void setUp() throws Exception {
		ObjectFactory.setLevel(new Level());
	}
	
	@Test
	public void testNewPlayer() {
		JSONObject jsonPlayer = ResourceLoader.parseJSONFromPath("gamedata/player.json");
		Player p = ObjectFactory.newPlayer();
		assertNotNull(p);
		
		// Some property checking
		assertEquals(jsonPlayer.optInt("speed"), p.getMovementSpeed());
		assertNotNull(p.getCurrentWeapon());
	}
	
	@Test
	public void testNewEnemy() {
		JSONObject jsonEnemy = ResourceLoader.parseJSONFromPath("gamedata/enemy.json");
		Enemy p = ObjectFactory.newEnemy();
		assertNotNull(p);
		
		// Some property checking
		assertEquals(jsonEnemy.optInt("speed"), p.getMovementSpeed());
		assertNotNull(p.getCurrentWeapon());
	}
	
	@Test
	public void testEnemyWave() {
		// For wave:
		int wave = 3;
		int nbrOfEnemies = tools.Math.prime(wave);
		
		List<Enemy> enemies = ObjectFactory.newEnemiesForWave(wave);
		assertNotNull(enemies);
		assertEquals(nbrOfEnemies, enemies.size());
	}
	
	@Test
	public void testNewItems() {
		List<CollidableObject> items = ObjectFactory.newItems();
		JSONArray jsonItems = ResourceLoader.parseJSONFromPath("gamedata/items.json")
				.optJSONArray("items");
		
		JSONObject obj1 = jsonItems.optJSONObject(0);
		ICollectableItem obj2 = (ICollectableItem) items.get(0);
		
		assertNotNull(items);
		assertEquals(jsonItems.length(), items.size());
		assertEquals(obj1.optInt("amount"), obj2.getAmount());
	}
	
	@Test
	public void testNewItem() {
		CollidableObject medpack = ObjectFactory.newItem("MedPack");
		
		assertNotNull(medpack);
		assertTrue(medpack instanceof ICollectableItem && medpack instanceof MedPack);
	}
	
	@Test
	public void testCreateWeapon() {
		Object[] params = new Object[] {null, 300, 1.0, 1.0};
		AbstractWeapon weapon = ObjectFactory.createWeaponFromString("MachineGun", params);
		
		assertNotNull(weapon);
		assertTrue(weapon instanceof MachineGun);
		assertEquals(300, weapon.getCurrentAmmo());
	}
	
	@Test
	public void testCreateItem() {
		Object[] params = new Object[] {100, 1, 1, 10, 10};
		CollidableObject medpack = ObjectFactory.createItemFromString("MedPack", params);
		
		assertNotNull(medpack);
		assertTrue(medpack instanceof MedPack && medpack instanceof ICollectableItem);
		assertEquals(100, ((ICollectableItem) medpack).getAmount());
	}
	
}
