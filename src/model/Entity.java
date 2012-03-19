/**
*	Entity.java
*
*	@author Johan
*/

package model;

import constants.Direction;

public abstract class Entity extends CollidableObject {
	
	private int movementSpeed;
	private Direction direction;
	
	//@todo Temp constructor
	public Entity(int movementSpeed) {
		this.movementSpeed = movementSpeed;
	}
	
	public Entity(java.awt.Dimension box, Position position, Position offset, int movementSpeed){
		super(box, position, offset);
		this.movementSpeed = movementSpeed;
		this.direction = Direction.ORIGIN;
	}
	
	
	public void moveTo(Position pos){
		moveTo(pos.getX(), pos.getY());
	}
	
	public void moveTo(double x, double y){
		
	}
	
	public void setDirection(Direction dir) {
		if (this.direction != dir) {
			this.direction = dir;
		}
	}
	
	
	public void destroy(){
		//@todo Should the model destroy itself?
	}
	
	public void addObserver(){
		
	}
	
}
