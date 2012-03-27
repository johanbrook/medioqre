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
		
		if(isMoving()){
			double x = this.direction.getXRatio() * this.movementSpeed * dt;		
			double y = this.direction.getYRatio() * this.movementSpeed * dt;
			
			this.setPosition(currPos.getX() + x, currPos.getY() + y);
			
//			System.out.println("xratio:\t"+this.direction.getXRatio() +",\tyratio:\t"+this.direction.getYRatio());
//			System.out.println("x:\t"+x +",\ty:\t"+y + ",\tdt:\t"+dt);
			System.out.println("x:\t"+(int)getPosition().getX() + ",\ty:\t"+(int)getPosition().getY());
		}
		
		
		//EventBus.INSTANCE.publish(new Event(Property.DID_MOVE, this));
		
	}
	
	public void stop() {
		this.movementSpeed = 0;
		
		EventBus.INSTANCE.publish(new Event(Property.DID_STOP, this));
	}
	
	public void setDirection(Direction dir) {
		this.movementSpeed = 5;
		
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
