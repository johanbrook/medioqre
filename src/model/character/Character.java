/**
*	Character.java
*
*	@author Johan
*/


package model.character;

import java.awt.Dimension;
import java.awt.Rectangle;

import event.Event;
import event.EventBus;
import event.Event.Property;

import model.Entity;

public abstract class Character extends Entity {

	private int health;
	
	
	public Character(int movementSpeed, Rectangle collBox, Dimension size, int xoffset, int yoffset) {
		super(collBox, size, xoffset, yoffset, movementSpeed);
		this.health = 100;
	}
	
	public void takeDamage(int dmg){
		this.health -= dmg;
		EventBus.INSTANCE.publish(new Event(Property.WAS_DAMAGED, this));
		if(this.health <= 0)
			this.destroy();
		
	}
	
	
	public int getHealth() {
		return this.health;
	}
	
	public abstract int getCurrentWeaponDamage();
	
	public abstract void attack(Character target);

}