package model.weapon;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import model.CollidableObject;
import model.Entity;
import model.weapon.PortalGun.Mode;

import org.json.JSONObject;

/**
 *	A portal. Have a reference to another portal and a given mode.
 *
 *	@author Johan
 */
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
	
	/**
	 * Create a new portal from a JSON object at a position.
	 * 
	 * @param mode The current mode
	 * @param position The position
	 * @param obj The JSON object
	 */
	public Portal(Mode mode, Point position, JSONObject obj) {
		super(obj);
		this.mode = mode;
		this.setPosition(position);
	}

	/**
	 * Create a new portal from a JSON object.
	 * 
	 * <p>Note that the current position will be what's set as bounding
	 * rectangle in the JSON data.</p>
	 * 
	 * @param mode The current mode
	 * @param obj The JSON object
	 */
	public Portal(Mode mode, JSONObject obj) {
		super(obj);
		this.mode = mode;
	}
	
	/**
	 * Center this portal around a given position.
	 * 
	 * @param pos The position
	 */
	public void setPositionFromCenter(Point pos) {
		int x = pos.x - this.getCollisionBox().width / 2;
		int y = pos.y - this.getCollisionBox().height / 2;

		this.setPosition(x, y);
	}

	/**
	 * Get the center of this portal.
	 * 
	 * @return The center as a position
	 */
	public Point getCenter() {
		int x = this.getPosition().x + this.getCollisionBox().width / 2;
		int y = this.getPosition().y + this.getCollisionBox().height / 2;
		
		return new Point(x, y);
	}

	/**
	 * Center this portal around it's current position.
	 */
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
		if (w instanceof Entity && this.getSisterPortal() != null){
			Entity temp = (Entity) w;
			if (!temp.isPortalVictim()){
				this.walkIntoPortal(temp);
				temp.setPortalVictim(true);
			}
		}
		
	}
	
	
	@Override
	public int getTag() {
		int mode = ((this.mode == Mode.BLUE) ? 1 : 0);
		return super.getTag() | (mode << 4*5);
	}
}
