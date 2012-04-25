/**
*	Projectile.java
*
*	@author Johan
*/

package model.weapon;

import java.awt.Dimension;
import java.awt.Rectangle;

import model.Entity;


public class Projectile extends Entity {
	
	private int damage;
	private Range range;
	
	public enum Range {
		SHORT_RANGE, MEDIUM_RANGE, FAR_RANGE;
	}
	
	public Projectile(int damage, Range range){
		super(new Rectangle(10,10), new Dimension(10,10), 50, 0, 0);
		this.damage = damage;
		this.range = range;
	}
	
	public int getDamage() {
		return this.damage;
	}
	
	public Range getRange() {
		return this.range;
	}
	
}


