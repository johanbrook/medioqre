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
}
