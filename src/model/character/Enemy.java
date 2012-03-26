/**
*	Enemy.java
*
*	@author Johan
*/

package model.character;

import event.Event;
import event.EventBus;
import event.Event.Property;
import model.weapon.IWeapon;
import model.weapon.Melee;

public class Enemy extends Character {
	
	private IWeapon melee;
	
	public Enemy(int movementSpeed, int damage) {
		super(movementSpeed);
		
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
