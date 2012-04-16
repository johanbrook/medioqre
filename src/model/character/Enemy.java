/**
*	Enemy.java
*
*	@author Johan
*/

package model.character;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import event.Event;
import event.EventBus;
import event.Event.Property;
import model.weapon.AbstractWeapon;
import model.weapon.Melee;
import model.weapon.Projectile;

public class Enemy extends Character {
	
	private AbstractWeapon melee;
	
	public Enemy(int movementSpeed, int damage) {
		this(movementSpeed, damage, 0, 0);
	}
	
	public Enemy(int movementSpeed, int damage, int x, int y) {
		super(movementSpeed, new Rectangle(x, y, 16, 16), new Dimension(20,20), 0, 16);
		this.melee = new Melee();
	}

	@Override
	public Projectile attack() {
		EventBus.INSTANCE.publish(new Event(Property.DID_ATTACK, this));
		
		return this.melee.fire();
	}
	
	
	
}
