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
	
	public enum Range {
		SHORT_RANGE(40), MEDIUM_RANGE(200), FAR_RANGE(1500);
		
		private final double distance;
		
		Range(double distance) {
			this.distance = distance;
		}
		
		public double getDistance() {
			return this.distance;
		}
	}
	
	
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
	
	public int getDamage() {
		return this.damage;
	}
	
	public Range getRange() {
		return this.range;
	}
	
	public double getDistanceTravelled() {
		return this.distanceTravelled;
	}
	
	public void updateDistanceTravelled() {
		this.distanceTravelled = Math.abs(this.getPosition().x - this.startPos.x) + 
				Math.abs(this.getPosition().y - this.startPos.y);
	}
	
	@Override
	public String toString() {
		return "["+this.getClass()+"] "+super.toString() + " [owner/type:"+this.owner.toString()+"]";
	}
	
	@Override
	public void move(double dt) {
		super.move(dt);
		
		this.updateDistanceTravelled();
	}
	
}


