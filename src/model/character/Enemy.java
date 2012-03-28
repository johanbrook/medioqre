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
import model.weapon.IWeapon;
import model.weapon.Melee;

public class Enemy extends Character {
	
	private IWeapon melee;
	
	public Enemy(int movementSpeed, int damage, Rectangle collBox, Dimension size, int xoffset, int yoffset) {
		super(movementSpeed, collBox, size, xoffset, yoffset);
		this.melee = new Melee(damage);
	}

	@Override
	public void attack(Character target) {
		target.takeDamage(this.melee.getDamage());
		EventBus.INSTANCE.publish(new Event(Property.DID_ATTACK, this));
	}
	
	@Override
	public int getCurrentWeaponDamage() {
		return this.melee.getDamage();
	}
	
	
}
