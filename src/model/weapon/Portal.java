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
	
	/**
	 * Create a new Portal.
	 * 
	 * @param mode The current mode
	 * @param collBox The collision box
	 * @param size The size
	 * @param xoffset The x offset
	 * @param yoffset The y offset
	 */
	public Portal(Mode mode, Rectangle collBox, Dimension size, int xoffset, int yoffset) {
		super(collBox, size, xoffset, yoffset);
		this.mode = mode;
	}

	public void setPositionFromCenter(Point pos) {
		tools.Logger.log("Original position is: " + pos);
		int x = pos.x - this.getCenter().x;
		int y = pos.y - this.getCenter().y;

		tools.Logger.log("New position is: " + x + ", " + y);
		this.setPosition(x, y);
	}

	public Point getCenter() {
		Point p = new Point(this.getSize().width / 2, this.getSize().height / 2);
		tools.Logger.log("Center is: " + p);
		return p;
	}

	public void center() {
		this.setPositionFromCenter(this.getPosition());
	}
	
	/**
	 * Get the current mode.
	 * 
	 * @return The mode
	 */
	public Mode getMode() {
		return this.mode;
	}
	
	/**
	 * Connect another portal to this portal to walk through.
	 * 
	 * @param p The other portal
	 */
	public void setSisterPortal(Portal p) {
		this.otherPortal = p;
	}

	/**
	 * Get this portal's sister portal.
	 * 
	 * @return The other portal
	 */
	public Portal getSisterPortal() {
		return this.otherPortal;
	}
	
	/**
	 * Let an object walk into this portal and teleport.
	 * 
	 * @param t The object
	 */
	public void walkIntoPortal(Entity t) {
		if(this.otherPortal == null) {
			throw new NullPointerException("Sister portal is not set!");
		}
		
		t.setPosition(this.otherPortal.getPosition());
	}

	@Override
	public void didCollide(CollidableObject w) {
		tools.Logger.log("Reached Portal");
		if (w instanceof Entity && this.getSisterPortal() != null){
			Entity temp = (Entity) w;
			if (!temp.isPortalVictim()){
				this.walkIntoPortal(temp);
				temp.setPortalVictim(true);
			}
		}
		
	}
}
