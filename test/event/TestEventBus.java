/**
*	TestEventBus.java
*
*	@author Johan
*/

package event;

import static org.junit.Assert.*;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.Arrays;

import model.character.Player;
import model.weapon.AbstractWeapon;
import model.weapon.Grenade;
import model.weapon.MachineGun;
import model.weapon.Melee;
import model.weapon.Sword;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import event.Event.Property;

public class TestEventBus {

	private Subscriber handler;
	private Player publisher;
	
	@Before
	public void setUp() throws Exception {
		this.handler = new Subscriber();
		this.publisher = new Player(30, new Rectangle(20, 20), new Dimension(20, 48), 0, 16);
		AbstractWeapon[] weapons = new AbstractWeapon[] {new Melee(this.publisher, -1, 0)};
		this.publisher.setWeaponBelt(Arrays.asList(weapons));
		
		EventBus.INSTANCE.register(this.handler);
		
		this.publisher.setCurrentWeapon(Melee.class);
	}

	@Test
	public void testDidReceiveEvent() {
		
		assertTrue(this.handler.didReceiveEvent());
	}
	
	@Test
	public void testProperty(){
		
		assertEquals(Property.CHANGED_WEAPON, this.handler.getEvent().getProperty());
	}
	
	@Test
	public void testSource() {
		Player p = (Player) this.handler.getEvent().getValue();
		assertEquals(this.publisher, p);
	}
	
	
	@Test
	public void testRemoveHandlers() {
		EventBus.INSTANCE.remove(this.handler);
		
		assertTrue(EventBus.INSTANCE.getHandlers().isEmpty());
	}
	
	
	@Test
	public void testRemoveHandler() {
		EventBus.INSTANCE.remove(this.handler);
		
		assertFalse(EventBus.INSTANCE.getHandlers().contains(this.handler));
	}
	
	@After
	public void cleanup() {
		EventBus.INSTANCE.removeAll();
	}
	
	private class Subscriber implements IEventHandler {
		private boolean didReceiveEvent = false;
		private Event event;
		
		@Override
		public void onEvent(Event evt) {
			this.didReceiveEvent = true;
			this.event = evt;
		}
		
		public boolean didReceiveEvent() {
			return this.didReceiveEvent;
		}
		
		public Event getEvent() {
			return this.event;
		}
		
	}

}
