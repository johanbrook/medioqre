/**
*	Projectile.java
*
*	@author Johan
*/

package model.weapon;

import java.awt.Dimension;
import java.awt.Rectangle;

import constants.Direction;

import model.Entity;
import model.character.AbstractCharacter;
import model.character.Player;


public class Projectile extends Entity {
	
	private int damage;
	private Range range;
	private AbstractWeapon owner;
	
	public enum Range {
		SHORT_RANGE, MEDIUM_RANGE, FAR_RANGE;
	}
	
	
	public Projectile(AbstractWeapon owner, int width, int height, int damage, Range range, int movementSpeed) {
		super(new Rectangle(width, height), new Dimension(width, height), 0, 0, movementSpeed);
		
		this.owner = owner;
		this.damage = damage;
		this.range = range;
		
		AbstractCharacter p = this.owner.getOwner();
		Direction dir = p.getDirection();
		
		int x = p.getPosition().x;
		int y = p.getPosition().y;
		
		if(dir == Direction.SOUTH) {
			y = p.getPosition().y + p.getSize().height;
		}
		
		setPosition(x, y);
		setDirection(this.owner.getOwner().getDirection());
	}
	
	public int getDamage() {
		return this.damage;
	}
	
	public Range getRange() {
		return this.range;
	}
	
}


