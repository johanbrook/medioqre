/**
*	Portal.java
*
*	@author Johan
*/

package model.weapon;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import model.CollidableObject;
import model.Entity;
import model.weapon.PortalGun.Mode;

public class Portal extends CollidableObject {

	private Portal otherPortal;
	private Mode mode;
	
	public Portal(Mode mode, Rectangle collBox, Dimension size, int xoffset, int yoffset) {
		super(collBox, size, xoffset, yoffset);
		this.mode = mode;
	}
	
	public void setPositionFromCenter(Point pos) {
		tools.Logger.log("Original position is: "+ pos);
		int x = pos.x - this.getCenter().x;
		int y = pos.y - this.getCenter().y;
		
		tools.Logger.log("New position is: "+x + ", "+y);
		this.setPosition(x, y);
	}
	
	public Point getCenter() {
		Point p = new Point(this.getSize().width / 2, this.getSize().height / 2);
		tools.Logger.log("Center is: "+p);
		return p;
	}
	
	public void center() {
		this.setPositionFromCenter(this.getPosition());
	}
	
	public Mode getMode() {
		return this.mode;
	}
	
	public void setSisterPortal(Portal p) {
		this.otherPortal = p;
	}

	
	public Portal getSisterPortal() {
		return this.otherPortal;
	}

}
