package model;

/**
 * The collidable object super class
 * 
 * @author Johan
 *
 */
public abstract class CollidableObject {
	
	private java.awt.Dimension collisionBox;
	private Position position;
	private Position offset;
	
	// Temp constructor
	public CollidableObject(){
		this(new java.awt.Dimension(1,1), new Position(0,0), new Position(0,0));
	}
	
	public CollidableObject(java.awt.Dimension collBox, Position position, Position offset){
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
	}
	
	/**
	 * Set the position of the object in the game world.
	 * 
	 * @param x The X coordinate
	 * @param y The Y coordinate
	 */
	public void setPosition(double x, double y) {
		this.position = new Position(x, y);
	}
	
	
	/**
	 * Get the collision box of the object.
	 * 
	 * @return The collision box
	 */
	public java.awt.Dimension getCollisionBox(){
		return this.collisionBox;
	}
	
	public boolean isColliding(CollidableObject obj){
		//@todo Implement this
		
		return false;
	}
}
