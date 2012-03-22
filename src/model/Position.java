/**
*	Position.java
*
*	@author Johan
*/

package model;

public class Position {
	
	private final double x;
	private final double y;
	
	public Position(final double x, final double y) {
		this.x = x;
		this.y = y;
	}
	
	public Position(final Position p) {
		this.x = p.x;
		this.y = p.y;
	}
	
	public double getX(){
		return this.x;
	}
	
	public double getY(){
		return this.y;
	}
	
	@Override
	public String toString() {
		return "["+this.x + ", "+this.y+"]";
	}
	
	@Override
	public boolean equals(Object o){
		if(this == o){
			return true;
		}
		
		if(o == null || getClass() != o.getClass()){
			return false;
		}
		
		Position other = (Position) o;
		
		return this.x == other.x && this.y == other.y;
	}
	
}
