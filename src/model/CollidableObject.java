/**
*	CollidableObject.java
*
*	@author Johan
*/

package model;

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
	
	
	public Position getPosition(){
		return this.position;
	}
	
	public void setPosition(Position pos){
		this.position = pos;
	}
	
	public java.awt.Dimension getCollisionBox(){
		return this.collisionBox;
	}
	
	public boolean isColliding(CollidableObject obj){
		//@todo Implement this
		
		return false;
	}
}
