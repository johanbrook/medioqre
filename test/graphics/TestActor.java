package graphics;

import static org.junit.Assert.*;
import graphics.opengl.animation.Actor;
import graphics.opengl.animation.Animation;
import graphics.opengl.animation.NoSuchAnimationException;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

public class TestActor {
	
	@Test (expected = NoSuchAnimationException.class)
	public void testConstructor() {
		Actor a = new Actor();
		assertNull(a.getTextureName());
		assertNull(a.getAnimations());
		a.getAnimation(1);
	}

	@Test
	public void testDataSerialization() {
		Actor a = new Actor();
		a.setTextureName("derp");
		Animation an1 = new Animation();
		Animation an2 = new Animation();
		Animation an3 = new Animation();
		
		an1.setAnimationTag(0x00000000);
		an2.setAnimationTag(0x000000f0);
		an3.setAnimationTag(0x00000f00);
		
		a.addAnimation(an1);
		a.addAnimation(an2);
		a.addAnimation(an3);
		
		assertEquals(3, a.getAnimations().length);
		
		for (Animation b : a.getAnimations()) {
			System.out.println(b.getAnimationTag());
		}
		
		
		try {
			new Actor(new JSONObject("Hejsan! Jag Šr sneel"));
			assertTrue(false);
		} catch (JSONException e) {
			assertTrue(true);
		}
		
		
		try {
			System.out.println(a.serialize().toString());
			
			Actor a2 = new Actor(a.serialize());
			
			assertEquals(3, a2.getAnimations().length);
			
			try {
				for (Animation b : a2.getAnimations()) {
					System.out.println(b.getAnimationTag());
				}
				
				a2.getAnimation(0x00000000);
				a2.getAnimation(0x000000f0);
				a2.getAnimation(0x00000f00);
			} catch (NoSuchAnimationException nsae) {
				fail();
			}
			
		} catch (JSONException e) {
			fail();
			e.printStackTrace();
		}
	}
	
	@Test
	public void testSetAnimation() {
		
		Actor a = new Actor();
		Animation an1 = new Animation();
		Animation an2 = new Animation();
		Animation an3 = new Animation();
		an1.setAnimationTag(12345);
		an2.setAnimationTag(32154);
		an3.setAnimationTag(54321);
		
		assertNull(a.getAnimations());
		
		a.setAnimations(new Animation[]{an1, an2, an3});
		
		assertNotNull(a.getAnimations());
	}
	
	@Test
	public void testAddAnimation() {
		Actor a = new Actor();
		Animation an1 = new Animation();
		an1.setAnimationTag(12345);
		assertNull(a.getAnimations());
		a.addAnimation(an1);
		
		assertNotNull(a.getAnimation(12345));
	}
	
	@Test	
	public void testGetAnimation() {
		Actor a = new Actor();
		Animation an1 = new Animation();
		Animation an2 = new Animation();
		Animation an3 = new Animation();
		an1.setAnimationTag(12345);
		an2.setAnimationTag(32154);
		an3.setAnimationTag(54321);
		
		a.addAnimation(an1);
		a.addAnimation(an2);
		a.addAnimation(an3);
		
		try {
			a.getAnimation(12345);
			a.getAnimation(32154);
			a.getAnimation(54321);
		} catch(NoSuchAnimationException nsae) {
			fail();
		} 
		
		try {
			a.getAnimation(1337);
			fail();
		} catch (NoSuchAnimationException nsae) {
			// Nothing to do here
			assertTrue(true);
		}
		
	}
}
