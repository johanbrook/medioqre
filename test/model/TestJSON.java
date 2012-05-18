/**
*	TestJSON.java
*
*	@author Johan
*/

package model;

import static org.junit.Assert.*;

import model.character.Enemy;
import model.character.Player;
import model.item.AmmoCrate;
import model.item.MedPack;
import model.weapon.AbstractWeapon;
import model.weapon.MachineGun;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import tools.datamanagement.ResourceLoader;

public class TestJSON {


	@Test
	public void testCreatePlayerFromJSON() {
		JSONObject player = ResourceLoader.parseJSONFromPath("gamedata/player.json");
		assertNotNull(player);
		
		Player p = new Player(player);
		assertNotNull(p);
		
		// Some property checking
		assertEquals(player.optInt("speed"), p.getMovementSpeed());
	}
	
	
	@Test
	public void testCreateEnemyFromJSON() {
		JSONObject enemy = ResourceLoader.parseJSONFromPath("gamedata/enemy.json");
		assertNotNull(enemy);
		
		Enemy p = new Enemy(enemy);
		assertNotNull(p);
		
		// Some property checking
		assertEquals(enemy.optInt("speed"), p.getMovementSpeed());
	}
	
	@Test
	public void testCreateItemFromJSON() {
		JSONArray items = ResourceLoader.parseJSONFromPath("gamedata/items.json").optJSONArray("items");
		assertNotNull(items);
		
		JSONObject medpack = items.optJSONObject(0);
		JSONObject ammocrate = items.optJSONObject(1);
		
		assertNotNull(medpack);
		assertNotNull(ammocrate);
		
		MedPack m = new MedPack(1, 1, medpack);
		AmmoCrate a = new AmmoCrate(1, 1, ammocrate);
		
		assertEquals(medpack.optInt("amount"), m.getAmount());
		assertEquals(ammocrate.optInt("amount"), a.getAmount());
	}

}
