package graphics;

import static org.junit.Assert.*;
import graphics.opengl.animation.Animation;

import org.junit.Test;

public class TestAnimation {

	@Test
	public void testConstructors() {
		Animation a = new Animation();
		assertNotNull(a.getSprites());
		assertEquals(0, a.getSprites().length);
	}
	@Test
	public void testSetAnimationTag() {
		
	}
	@Test
	public void testGetAnimationTag() {
		
	}
	@Test
	public void testDataSerialization() {
		
	}
	
}
