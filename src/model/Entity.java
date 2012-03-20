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
	public Entity(int movementSpeed) {
		this.movementSpeed = movementSpeed;
		this.direction = Direction.ORIGIN;
	}
	
	public Entity(java.awt.Dimension box, Position position, Position offset, int movementSpeed){
		super(box, position, offset);
		this.movementSpeed = movementSpeed;
		this.direction = Direction.ORIGIN;
	}
	
	
	// 0 < dt < 1
	public void move(double dt) {
		Position currPos = getPosition();
		
		double x = currPos.getX() + (dt*this.movementSpeed);
		double y = currPos.getY() + (dt*this.movementSpeed);
		
		switch(this.direction) {
		case NORTH:
			setPosition(currPos.getX(), y);
			break;
		case SOUTH:
			setPosition(currPos.getX(), -y);
			break;
		case WEST:
			setPosition(-x, currPos.getY());
			break;
		case EAST:
			setPosition(currPos.getX(), y);
		}
	}
	
	public void setDirection(Direction dir) {
		if (this.direction != dir) {
			this.direction = dir;
		}
	}
	
	
	public void destroy(){
		//@todo Should the model destroy itself?
	}
	
	public void addObserver(){
		
	}
	
}
