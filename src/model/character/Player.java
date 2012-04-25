/**
*	Player.java
*
*	@author Johan
*/

package model.character;


import java.awt.Dimension;
import java.awt.Rectangle;

import event.Event;
import event.Event.Property;
import event.EventBus;
import model.weapon.*;


public class Player extends Character {
	
	private AbstractWeapon currentWeapon;
	private WeaponBelt belt;
	
	public Player(){
		super(30, new Rectangle(16, 16), new Dimension(20,20), 0, 16);
		
		this.belt = new WeaponBelt();
		setCurrentWeapon(MachineGun.class);
	}
	
	public void setCurrentWeapon(int slot) {
		this.currentWeapon = this.belt.getWeapon(slot);
		EventBus.INSTANCE.publish(new Event(Property.CHANGED_WEAPON, this));
	}
	
	 
	public void setCurrentWeapon(Class<? extends AbstractWeapon> type) {
		this.currentWeapon = this.belt.getWeapon(type);
		EventBus.INSTANCE.publish(new Event(Property.CHANGED_WEAPON, this));
	}
	
	public AbstractWeapon getCurrentWeapon(){
		return this.currentWeapon;
	}


	@Override
	public Projectile attack() {
		EventBus.INSTANCE.publish(new Event(Property.DID_ATTACK, this));
		return this.currentWeapon.fire();
	}

	
}
