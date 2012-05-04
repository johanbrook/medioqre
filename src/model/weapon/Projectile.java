/**
*	Projectile.java
*
*	@author Johan
*/

package model.weapon;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import constants.Direction;

import model.Entity;
import model.character.AbstractCharacter;


public class Projectile extends Entity {
	
	private int damage;
	private Range range;
	private AbstractWeapon owner;
	
	private double distanceTravelled;
	private Point startPos;
	
	/**
	 * A enum for ranges.
	 * 
	 * @author Johan
	 *
	 */
	public enum Range {
		SHORT_RANGE(40), MEDIUM_RANGE(200), FAR_RANGE(1500);
		
		private final double distance;
		
		/**
		 * Create new range.
		 * 
		 * @param distance The distance
		 */
		Range(double distance) {
			this.distance = distance;
		}
		
		/**
		 * Get the distance.
		 * @return The distance
		 */
		public double getDistance() {
			return this.distance;
		}
	}
	
	/**
	 * Create a new projectile.
	 * 
	 * @param owner The projectile's owner (a weapon)
	 * @param width The width
	 * @param height The height
	 * @param damage The damage this should do
	 * @param range The range
	 * @param movementSpeed The speed
	 */
	public Projectile(AbstractWeapon owner, int width, int height, int damage, Range range, int movementSpeed) {
		super(new Rectangle(width, height), new Dimension(width, height), 0, 0, movementSpeed);
		
		this.owner = owner;
		this.damage = damage;
		this.range = range;
		this.distanceTravelled = 0;
		
		AbstractCharacter p = this.owner.getOwner();
		
		int x = p.getPosition().x;
		int y = p.getPosition().y;
		
		if(p.getDirection() == Direction.SOUTH) {
			y = (int) p.getCollisionBox().getMaxY();
		}
		
		setPosition(x, y);
		setDirection(this.owner.getOwner().getDirection());	
		
		this.startPos = this.getPosition();
	}
	
	/**
	 * Get the damage
	 * 
	 * @return The damage
	 */
	public int getDamage() {
		return this.damage;
	}
	
	/**
	 * Get the range
	 * 
	 * @return The range
	 */
	public Range getRange() {
		return this.range;
	}
	
	/**
	 * Get the distance travelled while moving from the starting position.
	 * 
	 * @return The distance travelled
	 */
	public double getDistanceTravelled() {
		return this.distanceTravelled;
	}
	
	/**
	 * Refresh the distance travelled.
	 */
	public void updateDistanceTravelled() {
		this.distanceTravelled = Math.abs(this.getPosition().x - this.startPos.x) + 
				Math.abs(this.getPosition().y - this.startPos.y);
	}
	
	/**
	 * Get this projectile's owner.
	 * 
	 * @return The owner
	 */
	public AbstractWeapon getOwner() {
		return this.owner;
	}
	
	@Override
	public String toString() {
		return super.toString() + " [owner/type:"+this.owner+"]";
	}
	
	@Override
	public void move(double dt) {
		super.move(dt);
		
		this.updateDistanceTravelled();
	}
	
}


