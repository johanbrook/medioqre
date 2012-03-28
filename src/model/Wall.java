/**
*	Wall.java
*
*	@author Johan
*/

package model;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

public class Wall extends CollidableObject {
	
	public Wall(int x, int y){
		super(new Rectangle(new Point(x, y), new Dimension(10, 10)), new Dimension(10, 10), 0, 0);
		//TODO Dummy-värden .. Ladda från fil
	}
}