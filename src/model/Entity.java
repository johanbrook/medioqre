package model;

import java.awt.Point;

import constants.Direction;
import event.Event;
import event.EventBus;
import event.Event.Property;

/**
*	Entity.
*
*	A movable, collidable object.
*
*	@author Johan
*/
public abstract class Entity extends CollidableObject {
	
	private int movementSpeed;
	private boolean isMoving;
	private Direction direction;
	
	/**
	 * Create a new entity.
	 * 
	 * @param box The collision box
	 * @param size The model size
	 * @param xoffset The X offset
	 * @param yoffset The Y offset
	 * @param movementSpeed The movement speed
	 */
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
	 * @pre isMoving() == true
	 * @see setDirection()
	 */
	public void move(double dt) {
		// Upper left corner is origin
		
		if(isMoving()){
			Point currPos = getPosition();
			EventBus.INSTANCE.publish(new Event(Property.DID_MOVE, this));
			int x = (int) (this.direction.getXRatio() * (double) this.movementSpeed * dt);		
			int y = (int) (this.direction.getYRatio() * (double) this.movementSpeed * dt);
						
			this.setPosition(currPos.x + x, currPos.y + y);
		}
		
	}
	
	/**
	 * Stop the entity.
	 * 
	 */
	public void stop() {
		this.isMoving = false;
		
		EventBus.INSTANCE.publish(new Event(Property.DID_STOP, this));
	}
	
	/**
	 * Set a direction of the entity.
	 * 
	 * Note: Will only set a new direction if <code>dir</code>
	 * differs from the current direction.
	 * 
	 * @param dir The new direction
	 * @pre getDirection() != dir
	 */
	public void setDirection(Direction dir) {
		this.isMoving = true;
		
		if (this.direction != dir) {
			this.direction = dir;
			EventBus.INSTANCE.publish(new Event(Property.CHANGED_DIRECTION, this));
		}
	}
	
	/**
	 * Get the current direction
	 * 
	 * @return The direction
	 */
	public Direction getDirection() {
		return this.direction;
	}
	
	/**
	 * If the entity is moving or not
	 * 
	 * @return If it's moving
	 */
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
	
	/**
	 * Destroy the entity
	 * 
	 */
	public void destroy(){
		//@todo Should the model destroy itself?
		EventBus.INSTANCE.publish(new Event(Property.WAS_DESTROYED, this));
	}
	
	
}
