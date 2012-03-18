/**
*	Entity.java
*
*	@author Johan
*/

package model;

public abstract class Entity extends CollidableObject {
	
	public Entity() {
		super();
	}
	
	public Entity(java.awt.Dimension box, Position position, Position offset){
		super(box, position, offset);
	}
	
	
	public void move(Position pos){
		
	}
	
	public void move(int dx, int dy){
		
	}
	
	public void destroy(){
		
	}
}
