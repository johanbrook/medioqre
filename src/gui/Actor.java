package gui;

import model.Position;

public class Actor {

	private Position position;

	public Actor(Position startingPosition) {
		this.position = new Position(startingPosition);
	}

	public Position getPosition() {
		return this.position;
	}
}
