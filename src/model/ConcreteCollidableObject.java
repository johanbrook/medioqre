package model;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

/**
*	A concrete implementation of CollidableObject.
*
*	@author Johan
*/
public class ConcreteCollidableObject extends CollidableObject {
	
	/**
	 * Create a concrete implementation of <code>CollidableObject</code>.
	 * 
	 * @param collBox The collision box
	 * @param size The size
	 * @param xoffset The x offset
	 * @param yoffset The y offset
	 */
	public ConcreteCollidableObject(Rectangle collBox, Dimension size, int xoffset, int yoffset){
		super(collBox, size, xoffset, yoffset);
	}
}