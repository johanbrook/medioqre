/**
*	Projectile.java
*
*	@author Johan
*/

package model.weapon;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import tools.Logger;

import constants.Direction;

import model.Entity;
import model.character.AbstractCharacter;


public class Projectile extends Entity {
	
	private int damage;
	private Range range;
	private AbstractWeapon owner;
	
	private double distanceTravelled;
	
	/**
	 * A enum for ranges.
	 * 
	 * @author Johan
	 *
	 */
	public enum Range {
		SHORT_RANGE(40), MEDIUM_RANGE(200), FAR_RANGE(1500);
		
		private final int distance;
		
		/**
		 * Create new range.
		 * 
		 * @param distance The distance
		 */
		Range(int distance) {
			this.distance = distance;
		}
		
		/**
		 * Get the distance.
		 * @return The distance
		 */
		public int getDistance() {
			return this.distance;
		}
	}
	
	/**
	 * Create a new projectile.
	 * 
	 * <p>The direction is set on init to this projectile's weapons' owner.</p>
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
		
		switch(p.getDirection()) {
			case SOUTH:
				y += (int) p.getSize().height;
				x += (int) p.getSize().width / 2;
				break;
			case NORTH:
				x += (int) p.getSize().width / 2;
				y -= p.getCollisionBox().height;
				break;
			case EAST:
				x += (int) p.getSize().width;
				y += (int) p.getSize().height / 2;
				break;
			case WEST: 
				x -= p.getCollisionBox().width;
				y += (int) p.getSize().height / 2;
				break;
			case NORTH_EAST:
				x += (int) p.getSize().width;
				break;
			case NORTH_WEST:
				x -= p.getCollisionBox().width;
				y -= p.getCollisionBox().height;
				break;
			case SOUTH_EAST:
				x += (int) p.getSize().width;
				y += (int) p.getSize().height / 2;
				break;
			case SOUTH_WEST:
				x -= this.getCollisionBox().width;
				y += (int) p.getSize().height / 2 + this.getCollisionBox().height;
				break;
		}
		
		setPosition(x, y);
		setDirection(p.getDirection());	
		
		Logger.log("Projectile created: "+this.toString());
	}
	
	/**
	 * Create a new projectile from another Projectile
	 * 
	 * @param p A projectile
	 */
	public Projectile(Projectile p) {
		this(p.owner, p.getCollisionBox().width, p.getCollisionBox().height, p.damage, p.range, p.getMovementSpeed());
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
	public void updateDistanceTravelled(double dt) {
		int x = (int) (this.getDirection().getXRatio() * (double) this.getMovementSpeed() * dt);		
		int y = (int) (this.getDirection().getYRatio() * (double) this.getMovementSpeed() * dt);
		this.distanceTravelled += Math.abs(x) + Math.abs(y);
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
		return super.toString() + " [owner/type:"+this.owner+"] [range: "+this.range +"] [damage: "+this.damage+"]";
	}
	
	@Override
	public void move(double dt) {
		super.move(dt);

		this.updateDistanceTravelled(dt);
	}
	
}


