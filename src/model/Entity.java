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
	
	
	public Entity(java.awt.Rectangle box, java.awt.Dimension size, int xoffset, int yoffset, int movementSpeed){
		super(box, size, xoffset, yoffset);
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
		// Upper left corner is origin
		
		if(isMoving()){
			Position currPos = getPosition();
			EventBus.INSTANCE.publish(new Event(Property.DID_MOVE, this));
			int x = (int) (this.direction.getXRatio() * (double) this.movementSpeed * dt);		
			int y = (int) (this.direction.getYRatio() * (double) this.movementSpeed * dt);
			
			this.setPosition(currPos.getX() + x, currPos.getY() + y);
		}
		
		
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

	/**
	 * The movement speed of the entity.
	 * 
	 * @return The movement speed
	 */
	public int getMovementSpeed() {
		return this.movementSpeed;
	}
	
	public void destroy(){
		//@todo Should the model destroy itself?
		EventBus.INSTANCE.publish(new Event(Property.WAS_DESTROYED, this));
	}
	
	
}
