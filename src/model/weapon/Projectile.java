/**
*	Projectile.java
*
*	@author Johan
*/

package model.weapon;


public class Projectile {
	
	private int damage;
	private Range range;
	
	public enum Range {
		SHORT_RANGE, MEDIUM_RANGE, FAR_RANGE;
	}
	
	public Projectile(int damage, Range range){
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


