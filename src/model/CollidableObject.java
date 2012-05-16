package model;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import model.character.Player;

import tools.Logger;

import event.Event;
import event.EventBus;
import event.IEventHandler;
import event.IMessageListener;
import event.Event.Property;
import event.IMessageSender;
import event.Messager;

/**
 * The collidable object super class.
 * 
 * <p>Consists of a collision box, a size, and offsets. The size is the actual size of the object - picture it like
 * the area rendered on screen, including the sprite. The collision box is the rectangular area that actually determines
 * if the object hits another CollidableObject's collision box. You may not want the collision box to be positioned in
 * the upper left corner of the object, so there are two properties - the vertical and horizontal offset - to offset the
 * collision box from the corner. Thus, the object may have a collision box <u>smaller</u> than the object's size, and
 * may not cover the whole object's area.
 * 
 * @author Johan
 * 
 */
public abstract class CollidableObject implements IMessageSender {

	private Rectangle collisionBox;
	private Dimension size;
	private int xoffset;
	private int yoffset;

	private Messager messager = new Messager();

	/**
	 * Point is located on top.
	 */
	public final static int TOP = 1;
	/**
	 * Point is located on the bottom.
	 */
	public final static int BOTTOM = 2;
	/**
	 * Point is located to the right.
	 */
	public final static int RIGHT = 4;
	/**
	 * Point is located to the left.
	 */
	public final static int LEFT = 8;

	/**
	 * A collidable object with a collision box, size, and offsets for the 
	 * collision box.
	 * 
	 * The object will get its position from <code>collBox's</code> position.
	 * 
	 * @param collBox The collision box
	 * @param size The size of the model
	 * @param xoffset The X offset of the collision box
	 * @param yoffset The Y offset of the collision box
	 */
	public CollidableObject(Rectangle collBox, Dimension size, int xoffset,
			int yoffset) {
		this.collisionBox = collBox;
		this.size = size;
		this.xoffset = xoffset;
		this.yoffset = yoffset;

		this.collisionBox.x += this.xoffset;
		this.collisionBox.y += this.yoffset;
	}

	@Override
	public void addReceiver(IEventHandler listener) {
		this.messager.addListener(listener);
	}

	/**
	 * Destroy the entity.
	 * 
	 * <p>Publishes an <code>Event</code> and sends a message to the listeners.</p>
	 * 
	 * <p>The event includes the property name <code>WAS_DESTROYED</code> and the property
	 * value is <code>this</code> (the object).</p> 
	 */
	public void destroy() {
		Event evt = new Event(Property.WAS_DESTROYED, this);
		EventBus.INSTANCE.publish(evt);
		messager.sendMessage(evt);
	}

	/**
	 * Get the size of this object.
	 * 
	 * @return The size
	 */
	public Dimension getSize() {
		return this.size;
	}

	/**
	 * Get the position of the object in the game world.
	 * 
	 * @return The position
	 */
	public Point getPosition() {

		int x = this.collisionBox.x - this.xoffset;
		int y = this.collisionBox.y - this.yoffset;

		return new Point(x, y);
	}

	/**
	 * Set the position of the object in the game world.
	 * 
	 * @param pos
	 *            The position
	 */
	public void setPosition(Point pos) {
		setPosition(pos.x, pos.y);
	}

	/**
	 * Set the position of the object in the game world.
	 * 
	 * @param x The X coordinate
	 * @param y The Y coordinate
	 */
	public void setPosition(int x, int y) {

		this.collisionBox.x = x + this.xoffset;
		this.collisionBox.y = y + this.yoffset;
	}

	/**
	 * Get the horizontal offset.
	 * 
	 * The offset is how the collision box is positioned relative to the upper
	 * left corner.
	 * 
	 * @return The X offset
	 */
	public int getOffsetX() {
		return this.xoffset;
	}

	/**
	 * Get the vertical offset.
	 * 
	 * The offset is how the collision box is positioned relative to the upper
	 * left corner.
	 * 
	 * @return The Y offset
	 */
	public int getOffsetY() {
		return this.yoffset;
	}

	/**
	 * Get the collision box of the object.
	 * 
	 * @return The collision box
	 */
	public Rectangle getCollisionBox() {
		return this.collisionBox;
	}

	/**
	 * Check whether this object is colliding with another CollidableObject.
	 * 
	 * The objects are colliding if their collision boxes intersect.
	 * 
	 * @param obj
	 *            The specified CollidableObject
	 * @return True if the objects are colliding, false otherwise
	 */
	public boolean isColliding(CollidableObject obj) {

		return this.collisionBox.intersects(obj.getCollisionBox());
	}

	/**
	 * Get the direction of where another object is located relative to this object.
	 * 
	 * @param obj Another object
	 * @return a <code>Direction</code>
	 */
	public Direction getDirectionOfObject(CollidableObject obj) {
		int code = getLocationOfPoint(obj.getCollisionBox().getLocation());

		Direction d = Direction.ORIGIN;

		if (code == TOP)
			d = Direction.NORTH;
		if (code == RIGHT)
			d = Direction.EAST;
		if (code == LEFT)
			d = Direction.WEST;
		if (code == BOTTOM)
			d = Direction.SOUTH;

		if (code == BOTTOM + LEFT)
			d = Direction.SOUTH_WEST;
		if (code == BOTTOM + RIGHT)
			d = Direction.SOUTH_EAST;
		if (code == TOP + LEFT)
			d = Direction.NORTH_WEST;
		if (code == TOP + RIGHT)
			d = Direction.NORTH_EAST;

		return d;
	}

	/**
	 * Get the location of a given point relative to this object.
	 * 
	 * @param p The point
	 * @return An integer constant, TOP, RIGHT, BOTTOM, LEFT
	 * @see TOP
	 * @see BOTTOM
	 * @see LEFT
	 * @see RIGHT
	 */
	public int getLocationOfPoint(Point p) {
		return getLocationOfPoint(p.x, p.y);
	}
	
	/**
	 * Get the location of given coordinates relative to this object.
	 * 
	 * @param x The x coordinate
	 * @param y The y coordinate
	 * @return An integer constant, TOP, RIGHT, BOTTOM, LEFT
	 * @see TOP
	 * @see BOTTOM
	 * @see LEFT
	 * @see RIGHT
	 */
	public int getLocationOfPoint(int x, int y) {
		int out = 0;
		if (this.collisionBox.width <= 0) {
			out = LEFT | RIGHT;
		} else if (x < this.collisionBox.x) {
			out |= LEFT;
		} else if (x > this.collisionBox.x) {
			out |= RIGHT;
		}
		if (this.collisionBox.height <= 0) {
			out |= TOP | BOTTOM;
		} else if (y < this.collisionBox.y) {
			out |= TOP;
		} else if (y > this.collisionBox.y) {
			out |= BOTTOM;
		}
		return out;
	}

	// Overrides

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + " [x:" + this.getPosition().x
				+ ":y:" + this.getPosition().y + "] [w:" + this.getSize().width
				+ ":h:" + this.getSize().height + "]";
	}
	
	/**
	 * Callback for when this object collided with another.
	 * 
	 * <p>Any typecasts have to be done in each inherited object's
	 * implementation of this method.</p>
	 * 
	 * @param w The other object
	 */
	public abstract void didCollide(CollidableObject w);
}
