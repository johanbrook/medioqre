package model;

import java.awt.Point;

import constants.Direction;
import event.Event;
import event.Event.Property;
import event.EventBus;
import event.IMessageListener;
import event.IMessageSender;
import event.Messager;

/**
*	Entity.
*
*	A movable, collidable object.
*
*	@author Johan
*/
public abstract class Entity extends CollidableObject implements IMessageSender {
	
	private int movementSpeed;
	private boolean isMoving;
	private Direction direction;
	
	private Messager messager = new Messager();
	
	/**
	 * Create a new entity.
	 * 
	 */
	public Entity(java.awt.Rectangle box, java.awt.Dimension size, int xoffset, int yoffset, int movementSpeed){
		super(box, size, xoffset, yoffset);
		this.movementSpeed = movementSpeed;
		this.direction = Direction.ORIGIN;
		this.isMoving = true;
	}
	

	
	
	/**
	 * Move in a the set direction.
	 * 
	 * @param dt Delta time
	 * @pre isMoving() == true
	 * @see setDirection()
	 */
	public void move(double dt) {
		// Upper left corner is origin
		
		if(isMoving()){
			Point currPos = getPosition();
			
			int x = (int) (this.direction.getXRatio() * (double) this.movementSpeed * dt);		
			int y = (int) (this.direction.getYRatio() * (double) this.movementSpeed * dt);
						
			this.setPosition(currPos.x + x, currPos.y + y);
			EventBus.INSTANCE.publish(new Event(Property.DID_MOVE, this));
		}
	}
	
	/**
	 * Let the entity stop.
	 * 
	 */
	public void stop() {
		this.isMoving = false;
		
		EventBus.INSTANCE.publish(new Event(Property.DID_STOP, this));
	}
	
	
	/**
	 * Let the entity start moving.
	 * 
	 */
	public void start() {
		this.isMoving = true;		
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
		Event evt = new Event(Property.WAS_DESTROYED, this);
		EventBus.INSTANCE.publish(evt);
		messager.sendMessage(evt);
	}
	
	@Override
	public String toString() {
		return super.toString() + " [speed:"+this.movementSpeed+"] [moving:"+this.isMoving+"] [dir:"+this.direction+"]";
	}
	
	@Override
	public void addReceiver (IMessageListener listener){
		this.messager.addListener(listener);
	}
	
	
}
