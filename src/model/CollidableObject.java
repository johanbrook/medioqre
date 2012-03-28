package model;

import java.awt.Dimension;
import java.awt.Rectangle;

/**
 * The collidable object super class
 * 
 * @author Johan
 *
 */
public abstract class CollidableObject {
	
	private Rectangle collisionBox;
	private Dimension size;
	private int xoffset;
	private int yoffset;

	
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
	public CollidableObject(Rectangle collBox, Dimension size, int xoffset, int yoffset){
		this.collisionBox = collBox;
		this.size = size;
		this.xoffset = xoffset;
		this.yoffset = yoffset;
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
	public Position getPosition(){
		
		int x = this.collisionBox.x - xoffset;
		int y = this.collisionBox.y - yoffset;
		
		return new Position(x, y);
	}
	
	/**
	 * Set the position of the object in the game world.
	 * 
	 * @param pos The position
	 */
	public void setPosition(Position pos){

		this.collisionBox.x = pos.getX() + this.xoffset;
		this.collisionBox.y = pos.getY() + this.yoffset;
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
