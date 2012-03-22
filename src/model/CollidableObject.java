package model;

import java.awt.Rectangle;

/**
 * The collidable object super class
 * 
 * @author Johan
 *
 */
public abstract class CollidableObject {
	
	private Rectangle collisionBox;
	private Position position;
	private Position offset;
	
	// Temp constructor
	public CollidableObject(){
		this(new Rectangle(), new Position(0,0));
	}
	
	public CollidableObject(Rectangle collisionBox, Position offset) {
		this(collisionBox, new Position(collisionBox.getX(), collisionBox.getY()), offset);
	}
	
	public CollidableObject(Rectangle collBox, Position position, Position offset){
		this.collisionBox = collBox;
		this.position = position;
		this.offset = offset;
	}
	
	
	/**
	 * Get the position of the object in the game world.
	 * 
	 * @return The position
	 */
	public Position getPosition(){
		return this.position;
	}
	
	/**
	 * Set the position of the object in the game world.
	 * 
	 * @param pos The position
	 */
	public void setPosition(Position pos){
		this.position = pos;
		this.collisionBox.x = (int)pos.getX();
		this.collisionBox.y = (int)pos.getY();
	}
	
	/**
	 * Set the position of the object in the game world.
	 * 
	 * @param x The X coordinate
	 * @param y The Y coordinate
	 */
	public void setPosition(double x, double y) {
		
		//@todo Need to refactor: we shouldn't keep track of both
		// the collision box's position and our own.
		this.position = new Position(x, y);
		this.collisionBox.x = (int)x;
		this.collisionBox.y = (int)y;
	}
	
	
	/**
	 * Get the collision box of the object.
	 * 
	 * @return The collision box
	 */
	public Rectangle getCollisionBox(){
		return this.collisionBox;
	}
	
	public boolean isColliding(CollidableObject obj){
		
		return this.collisionBox.intersects(obj.getCollisionBox());
	}
}
