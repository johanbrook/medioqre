/**
*	Wall.java
*
*	@author Johan
*/

package model;

public class Wall extends CollidableObject {
	
	public Wall(java.awt.Dimension collBox, Position pos, Position offset){
		super(collBox, pos, offset);
	}
}