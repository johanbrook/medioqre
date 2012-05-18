/**
*	TestBitTags.java
*
*	@author Johan
*/

package model;

import static org.junit.Assert.*;

import graphics.ITaggable;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import model.character.AbstractCharacter;
import model.character.Enemy;
import model.character.Player;
import model.item.AmmoCrate;
import model.weapon.AbstractWeapon;
import model.weapon.MachineGun;
import model.weapon.Melee;
import model.weapon.Portal;
import model.weapon.PortalGun.Mode;
import model.weapon.Projectile;
import model.weapon.Projectile.Range;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import tools.datamanagement.ResourceLoader;
import tools.factory.Level;
import tools.factory.ObjectFactory;

public class TestBitTags {
	
	private CollidableObject obj;
	private AbstractCharacter entity;

	@Before
	public void setUp() throws Exception {
		ObjectFactory.setLevel(new Level());
		
		this.obj = new ConcreteCollidableObject(new Rectangle(), new Dimension(), 0, 0);
		this.entity = new Player(10, new Rectangle(), new Dimension(), 0, 0);
		this.entity.setCurrentWeapon(new Melee(entity, 100, 1.0, 1.0));
	}

	
	// Bit tag tests
	
	@Test
	public void testVanillaTag() {
		assertEquals(0x00000000, this.obj.getTag());
	}
	
	@Test
	public void testSetBitPosition() {
		this.obj.setBit(1, 2);
		assertEquals(0x0000100, this.obj.getTag());
		
		this.obj.setBit(1, 3);
		assertEquals(0x0001100, this.obj.getTag());
	}
	
	@Test
	public void testIsMoving() {
		this.entity.start();
		
		// Entity should move
		assertEquals(1, this.entity.getTag() & 0x00000001);
		// but not the regular collidable
		assertEquals(0, this.obj.getTag() & 0x00000001);
	}
	
	@Test
	public void testDirection() {
		this.entity.stop();
		this.entity.setDirection(Direction.SOUTH);
		
		int ordinal = 0x00000000 | this.entity.getDirection().ordinal() << 4;		
		assertEquals(ordinal, this.entity.getTag());
		assertEquals(0, this.obj.getTag() & 0x00000010);
	}
	
	@Test
	public void testWeapon() {
		// Make sure no weapon is set on init:
		assertEquals(0, this.entity.getTag() & 0x00000100);
		
		// Assign '1' to a weapon
		ITaggable w = new MachineGun(entity, 100, 1.0, 1.0);
		w.setBit(1, 4);
		assertEquals(0x00010000, w.getTag());
		
		// .. and assign the weapon to the character
		this.entity.setCurrentWeapon((AbstractWeapon)w);
		
		// And ensure that the 3rd position of the character tag
		// (weapon type) is '1'.
		
		// Desired bit pattern: 0000 0100
		assertEquals(0x00000100, this.entity.getTag() & 0x00000100);
		assertEquals(0, this.obj.getTag() & 0x00000100);
	}
	
	@Test
	public void testProjectile() {
		
		// Desired bit pattern: 0001 0001
		// where '1' (5th pos) is the weapon's object type
		// and '1' (1st pos) signals moving
		
		// The object type on 5th position:
		ITaggable w = new MachineGun(entity, 100, 1.0, 1.0);
		w.setBit(7, 4); 	// Type for weapons
		w.setBit(1, 3);		// ID for machine gun
		
		Projectile p = new Projectile((AbstractWeapon)w, 10, 10, 10, Range.FAR_RANGE, 10);
		p.setBit(1, 4); 	// Set type for projectile
		
		// The projectile should now include its owner's id on 
		// the 4th position:
		int weaponTag = w.getTag() & 0x00001000;
		int projTag = p.getTag() & 0x00001000;
		
		assertEquals(weaponTag, projTag);
		
		// Desired bit pattern: 0001 1000
		// where the last '0' signals not moving
		// where '1' is the weapon ID
		// where '1' is the projectile object type
		
		p.stop();
		assertEquals(0x0011000, p.getTag() & 0x00011001);
	}
	
	@Test
	public void testProjectileFromFactory() {

		// Desired bit pattern: 0001 3021
		// where '3' is the id of the weapon (Melee from JSON file)
		// and '1' is the object type of projectile
		
		ITaggable w = ObjectFactory.newEnemy().getCurrentWeapon();
		JSONArray weapons = ResourceLoader.parseJSONFromPath("gamedata/weapons.json")
				.optJSONArray("weapons");
		
		JSONObject mgun = weapons.optJSONObject(0);
		
		Projectile p = ObjectFactory.newProjectile((AbstractWeapon) w, mgun.optJSONObject("projectile"));
		
		assertEquals(0x00013021, p.getTag());
	}
	
	@Test
	public void testPortal() {
		
		// Desired bit pattern: 0014 0000
		// where '1' is for BLUE portal mode
		// where '' is for Portal's object type
		
		ITaggable p = new Portal(Mode.BLUE, new Rectangle(), new Dimension(), 0, 0);
		p.setBit(4, 4);
		
		assertEquals(0x00140000, p.getTag());
		
	}
	
	@Test
	public void testPortalFromFactory() {
		// Now for some JSON reading and factory initializations!
		
		// Desired bit pattern: 0014 0000
		// where '4' is the portal object type
		// where '1' is a BLUE portal
		ITaggable p = ObjectFactory.newPortal(Mode.BLUE, new Point(0,0));
		
		assertEquals(0x00140000, p.getTag());
	}
	
	@Test
	public void testItemFromFactory() {
		
		// Desired bit pattern: 0006 0000 for Ammocrate
		// Desired bit pattern: 0005 0000 for MedPack
		// where '6' and '5' are the object types
		
		ITaggable c = ObjectFactory.newItem("AmmoCrate");
		ITaggable m = ObjectFactory.newItem("MedPack");
		
		assertEquals(0x00060000, c.getTag());
		assertEquals(0x00050000, m.getTag());
	}

	
	@Test
	public void testEnemyFromFactory() {

		// Desired bit pattern: 0003 0000
		// where '3' is the enemy object type
		
		ITaggable e = ObjectFactory.newEnemy();
		
		assertEquals(0x00030000, e.getTag() & 0x00030000);
	}
	
	@Test
	public void testPlayerFromFactory() {

		// Desired bit pattern: 0002 0000
		// where '2' is the player object type
		
		ITaggable p = ObjectFactory.newPlayer();
		
		assertEquals(0x00020000, p.getTag() & 0x00020000);
	}
}
