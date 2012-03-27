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
	private boolean isMoving;
	private Direction direction;
	
	//@todo Temp constructor
	public Entity(int movementSpeed, java.awt.Rectangle box) {
		this(box, new Position(0,0), movementSpeed);
	}
	
	public Entity(java.awt.Rectangle box, Position offset, int movementSpeed){
		super(box, offset);
		this.movementSpeed = movementSpeed;
		this.direction = Direction.ORIGIN;
		this.isMoving = false;
	}
	
	
	/**
	 * Move in a certain direction.
	 * 
	 * @param dt Delta time
	 * @see setDirection()
	 */
	public void move(double dt) {
		Position currPos = getPosition();

		// Upper left corner is origin
		
		if(isMoving()){
			double x = this.direction.getXRatio() * this.movementSpeed * dt;		
			double y = this.direction.getYRatio() * this.movementSpeed * dt;
			
			this.setPosition(currPos.getX() + x, currPos.getY() + y);
			
			System.out.println("x:\t"+getPosition().getX() + ",\ty:\t"+getPosition().getY());
		}
		
		
		//EventBus.INSTANCE.publish(new Event(Property.DID_MOVE, this));
		
	}
	
	public void stop() {
		this.isMoving = false;
		
		EventBus.INSTANCE.publish(new Event(Property.DID_STOP, this));
	}
	
	public void setDirection(Direction dir) {
		this.isMoving = true;
		
		if (this.direction != dir) {
			this.direction = dir;
			EventBus.INSTANCE.publish(new Event(Property.CHANGED_DIRECTION, this));
		}
	}
	
	public Direction getDirection() {
		return this.direction;
	}
	
	
	public boolean isMoving() {
		return this.isMoving;
	}

	
	public void destroy(){
		//@todo Should the model destroy itself?
		EventBus.INSTANCE.publish(new Event(Property.WAS_DESTROYED, this));
	}
	
	
}
