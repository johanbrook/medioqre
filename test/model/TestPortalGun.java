/**
*	TestPortalGun.java
*
*	@author Johan
*/

package model;

import static org.junit.Assert.*;

import model.weapon.PortalGun;
import model.weapon.PortalGun.Mode;

import org.junit.Before;
import org.junit.Test;

public class TestPortalGun {

	private PortalGun gun;
	
	@Before
	public void setUp() throws Exception {
		this.gun = new PortalGun(null, -1, 1, 0);
	}

	@Test
	public void testMode() {
		assertEquals(Mode.BLUE, this.gun.getMode());
	}
	
	@Test
	public void testSwitchMode() {
		this.gun.switchMode();
		
		assertEquals(Mode.ORANGE, this.gun.getMode());
	}

}
