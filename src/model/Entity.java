package model;

import java.awt.Point;

import org.json.JSONObject;

import event.Event;
import event.Event.Property;
import event.EventBus;

/**
 * Entity.
 * 
 * A movable, collidable object.
 * 
 * @author Johan
 */
public abstract class Entity extends CollidableObject {

	private int movementSpeed;
	private boolean isMoving;
	private Direction direction;
	private boolean portalVictim;
	private Point rememberedPosition;
	
	/**
	 * Create a new Entity.
	 * 
	 * <p>Initialized with the direction <code>ORIGIN</code></p>.
	 * 
	 * @param box The collision box
	 * @param size The size
	 * @param xoffset The x offset
	 * @param yoffset The y offset
	 * @param movementSpeed The movement speed
	 */
	public Entity(java.awt.Rectangle box, java.awt.Dimension size, int xoffset,
			int yoffset, int movementSpeed) {
		super(box, size, xoffset, yoffset);
		this.movementSpeed = movementSpeed;
		this.direction = Direction.ORIGIN;
		this.isMoving = true;
		this.portalVictim = false;
	}

	
	/**
	 * An Entity from a JSON object.
	 * 
	 * <p>The JSON object must contain required keys of <code>CollidableObject</code>'s
	 * constructor, and also a <code>speed</code> key for <code>Entity</code>'s constructor.</p>
	 * 
	 * @param obj The JSON object
	 */
	public Entity(JSONObject obj) {
		super(obj);
		
		this.movementSpeed = obj.optInt("speed");
		this.direction = Direction.ORIGIN;
		this.isMoving = true;
		this.portalVictim = false;
	}
	
	/**
	 * Move in a the set direction.
	 * 
	 * <p>Sends a <code>DID_MOVE</code> event with the value <code>this</code></p>.
	 * 
	 * @param dt Delta time
	 * @pre isMoving() == true
	 * @see setDirection()
	 */
	public void move(double dt) {
		// Upper left corner is origin

		if (isMoving()) {
			Point currPos = getPosition();

			int x = (int) (this.direction.getXRatio()
					* (double) this.movementSpeed * dt);
			int y = (int) (this.direction.getYRatio()
					* (double) this.movementSpeed * dt);

			this.setPosition(currPos.x + x, currPos.y + y);
			EventBus.INSTANCE.publish(new Event(Property.DID_MOVE, this));
		}
	}

	/**
	 * Let the entity stop.
	 * 
	 * <p>Sends a <code>DID_STOP</code> event with the value <code>this</code></p>.
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
	 * <p>Note: Will only set a new direction if <code>dir</code>
	 * differs from the current direction.</p>
	 * 
	 * @param dir The new direction
	 * @pre getDirection() != dir
	 */
	public void setDirection(Direction dir) {

		if (this.direction != dir) {
			this.direction = dir;
			EventBus.INSTANCE.publish(new Event(Property.CHANGED_DIRECTION,
					this));
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
	 * Set the movement speed of the entity.
	 * 
	 * @param movementSpeed The movement speed
	 */
	public void setMovementSpeed(int movementSpeed) {
		this.movementSpeed = movementSpeed;
	}

	@Override
	public String toString() {
		return super.toString() + " [speed:" + this.movementSpeed
				+ "] [moving:" + this.isMoving + "] [dir:" + this.direction
				+ "]";
	}

	
	/**
	 * Get the status of this entity in regard to portals.
	 * 
	 * <p>If this entity just teleported through a portal, 
	 * this will return true.</p>
	 * 
	 * @return True if this entity came through a portal
	 */
	public boolean isPortalVictim() {
		return this.portalVictim;
	}

	/**
	 * Set this entity's portal victim status.
	 * 
	 * @param portalVictim Set the status
	 */
	public void setPortalVictim(boolean portalVictim) {
		this.portalVictim = portalVictim;
	}

	
	@Override
	public int getTag() {
		int intIsMoving = isMoving ? 1 : 0;
		int intDirection = this.direction.ordinal();
		return super.getTag() | intIsMoving | (intDirection << 4);
	}

	/**
	 * get the position this entity is currently saving
	 * @return the remebered position
	 */
	public Point getRememberedPosition() {
		return rememberedPosition;
	}

	/**
	 * Set a position for this entity to remember. Entity can only remember one position at a time.
	 * @param rememberedPosition
	 */
	public void setRememberedPosition(Point rememberedPosition) {
		this.rememberedPosition = rememberedPosition;
	}
}
