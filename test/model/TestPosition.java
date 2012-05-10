package model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * TestPosition.java
 * 
 * @author Johan
 * @deprecated 2012-04-15
 */
public class TestPosition {

	private Position position;

	@Before
	public void setUp() throws Exception {
		this.position = new Position(1, 3);
	}

	@Test
	public void testPosition() {
		assertEquals(this.position.getX(), 1, 0.1);
		assertEquals(this.position.getY(), 3, 0.1);
	}

	@Test
	public void testEquals() {
		Position other = new Position(1, 3);
		Position other2 = position;
		Position other3 = new Position(other);

		assertTrue(this.position.equals(other));
		assertTrue(this.position.equals(other3));
		assertTrue(!this.position.equals(null));
		assertTrue(this.position == other2);
	}

}
