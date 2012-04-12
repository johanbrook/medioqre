/**
*	Enemy.java
*
*	@author Johan
*/

package model.character;

import java.awt.Dimension;
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
		super(movementSpeed, new Rectangle(10, 10), new Dimension(20,20), 0, 0);
		this.melee = new Melee();
	}

	@Override
	public Projectile attack() {
		EventBus.INSTANCE.publish(new Event(Property.DID_ATTACK, this));
		
		return this.melee.fire();
	}
	
	
	
}
