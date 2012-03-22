/**
*	Entity.java
*
*	@author Johan
*/

package model;

import constants.Direction;

public abstract class Entity extends CollidableObject {
	
	private int movementSpeed;
	private Direction direction;
	
	//@todo Temp constructor
	public Entity(int movementSpeed, java.awt.Rectangle box) {
		this(box, new Position(0,0), movementSpeed);
	}
	
	public Entity(java.awt.Rectangle box, Position offset, int movementSpeed){
		super(box, offset);
		this.movementSpeed = movementSpeed;
		this.direction = Direction.ORIGIN;
	}
	
	
	// 0 < dt < 1
	public void move(double dt) {
		Position currPos = getPosition();
		
		// Upper left corner is origin
		
		double x = currPos.getX() + (dt*this.movementSpeed);
		double y = currPos.getY() + (dt*this.movementSpeed);
		
		//@todo Move in the other four directions
		switch(this.direction) {
		case NORTH:
			setPosition(currPos.getX(), -y);
			break;
		case SOUTH:
			setPosition(currPos.getX(), y);
			break;
		case WEST:
			setPosition(-x, currPos.getY());
			break;
		case EAST:
			setPosition(x, currPos.getY());
		}
	}
	
	public void setDirection(Direction dir) {
		if (this.direction != dir) {
			this.direction = dir;
		}
	}
	
	public Direction getDirection() {
		return this.direction;
	}
	
	public void destroy(){
		//@todo Should the model destroy itself?
	}
	
	public void addObserver(){
		
	}
	
}
