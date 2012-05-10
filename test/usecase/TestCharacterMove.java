/**
 *	TestCharacterMove.java
 *
 *	@author Johan
 */

package usecase;

import static org.junit.Assert.*;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import model.character.*;

import org.junit.Before;
import org.junit.Test;

import constants.Direction;

public class TestCharacterMove {

	private AbstractCharacter player;
	private final double DELTA = 1.0;

	// How far a player moves in 1.0 dt.
	private int playerMovement;

	@Before
	public void setUp() throws Exception {
		this.player = new Player(30, new Rectangle(20, 20), new Dimension(20,
				48), 0, 16);
		int playerSpeed = this.player.getMovementSpeed();
		this.playerMovement = (int) ((int) playerSpeed * DELTA);
		this.player.setPosition(0, 0);
	}

	@Test
	public void testMoveNull() {
		assertEquals(new Point(0, 0), this.player.getPosition());
	}

	@Test
	public void testMoveNorth() {
		this.player.setDirection(constants.Direction.NORTH);
		this.player.move(DELTA);

		assertEquals(new Point(0, -this.playerMovement),
				this.player.getPosition());
	}

	@Test
	public void testMoveSouth() {
		this.player.setDirection(constants.Direction.SOUTH);
		this.player.move(DELTA);

		assertEquals(new Point(0, this.playerMovement),
				this.player.getPosition());
	}

	@Test
	public void testMoveEast() {
		this.player.setDirection(constants.Direction.EAST);
		this.player.move(DELTA);

		assertEquals(new Point(this.playerMovement, 0),
				this.player.getPosition());
	}

	@Test
	public void testMoveWest() {
		this.player.setDirection(constants.Direction.WEST);
		this.player.move(DELTA);

		assertEquals(new Point(-this.playerMovement, 0),
				this.player.getPosition());
	}

	@Test
	public void testMoveNorthWest() {
		this.player.setDirection(constants.Direction.NORTH_WEST);
		this.player.move(DELTA);
		int xmovement = (int) (Direction.NORTH_WEST.getXRatio() * this.playerMovement);
		int ymovement = (int) (Direction.NORTH_WEST.getYRatio() * this.playerMovement);

		assertEquals(new Point(xmovement, ymovement), this.player.getPosition());
	}

	@Test
	public void testMoveNorthEast() {
		this.player.setDirection(constants.Direction.NORTH_EAST);
		this.player.move(DELTA);

		int xmovement = (int) (Direction.NORTH_EAST.getXRatio() * this.playerMovement);
		int ymovement = (int) (Direction.NORTH_EAST.getYRatio() * this.playerMovement);

		assertEquals(new Point(xmovement, ymovement), this.player.getPosition());
	}

	@Test
	public void testMoveSouthWest() {
		this.player.setDirection(constants.Direction.SOUTH_WEST);
		this.player.move(DELTA);

		int xmovement = (int) (Direction.SOUTH_WEST.getXRatio() * this.playerMovement);
		int ymovement = (int) (Direction.SOUTH_WEST.getYRatio() * this.playerMovement);

		assertEquals(new Point(xmovement, ymovement), this.player.getPosition());
	}

	@Test
	public void testMoveSouthEast() {
		this.player.setDirection(constants.Direction.SOUTH_EAST);
		this.player.move(DELTA);

		int xmovement = (int) (Direction.SOUTH_EAST.getXRatio() * this.playerMovement);
		int ymovement = (int) (Direction.SOUTH_EAST.getYRatio() * this.playerMovement);

		assertEquals(new Point(xmovement, ymovement), this.player.getPosition());
	}
}
