/**
*	Entity.java
*
*	@author Johan
*/

package model;

import constants.Direction;
import event.Event;
import event.EventBus;
import event.Event.Property;

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
		
		EventBus.INSTANCE.publish(new Event(Property.DID_MOVE, this));
		
	}
	
	public void stop(Direction lastDir) {
		this.movementSpeed = 0;
		
		if(this.direction != lastDir)	
			EventBus.INSTANCE.publish(new Event(Property.DID_STOP, this));
	}
	
	public void setDirection(Direction dir) {
		if (this.direction != dir) {
			this.direction = dir;
			EventBus.INSTANCE.publish(new Event(Property.CHANGED_DIRECTION, this));
		}
	}
	
	public Direction getDirection() {
		return this.direction;
	}
	
	
	public boolean isMoving() {
		return this.movementSpeed > 0;
	}

	
	public void destroy(){
		//@todo Should the model destroy itself?
		EventBus.INSTANCE.publish(new Event(Property.WAS_DESTROYED, this));
	}
	
	
}
