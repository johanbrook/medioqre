/**
*	TestResourceLoader.java
*
*	@author Johan
*/

package tools;

import static org.junit.Assert.*;

import org.json.JSONObject;
import org.junit.Test;

import tools.datamanagement.ResourceLoader;

public class TestResourceLoader {

	@Test(expected=IllegalArgumentException.class)
	public void testParseNonExistingFile() {
		JSONObject enemy = ResourceLoader.parseJSONFromPath("gamedata/falsy.json");
		assertNull(enemy);
	}
	
	
	@Test
	public void testGetJSONString() {
		String json = ResourceLoader.loadJSONStringFromResources("gamedata/enemy.json");
		
		assertNotNull(json);
	}
	
	@Test
	public void testParseNestedObjects() {
		JSONObject json = ResourceLoader.parseJSONFromPath("gamedata/config.json");
		JSONObject obj = ResourceLoader.parseNestedConfigFiles(json);
		
		assertNotNull(obj);
		
		// Traverse down the file ..
		assertNotNull(obj.optJSONObject("playerConfig"));
		assertNotNull(obj.optJSONObject("playerConfig").optJSONObject("bounds"));
		
		// and check some values
		JSONObject player = ResourceLoader.parseJSONFromPath("gamedata/player.json");
		assertEquals(player.optInt("speed"), obj.optJSONObject("playerConfig").optInt("speed"));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testParseNullObject() {
		ResourceLoader.parseNestedConfigFiles(null);
	}
}
