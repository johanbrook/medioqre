package model;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

/**
 * A concrete implementation of CollidableObject.
 * 
 * @author Johan
 */
public class ConcreteCollidableObject extends CollidableObject {

	public ConcreteCollidableObject(Rectangle collBox, Dimension size,
			int xoffset, int yoffset) {
		super(collBox, size, xoffset, yoffset);
	}
}