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
import model.Projectile;
import model.weapon.*;


public class Player extends Character {
	
	private IWeapon currentWeapon;
	private WeaponBelt belt;
	
	public Player(){
		super(10, new Rectangle(10, 10), new Dimension(20,20), 0, 0);
		
		this.belt = new WeaponBelt();
		
		setCurrentWeapon(MachineGun.class);
	}
	
	public void setCurrentWeapon(int slot) {
		this.currentWeapon = this.belt.getWeapon(slot);
		EventBus.INSTANCE.publish(new Event(Property.CHANGED_WEAPON, this));
	}
	
	
	public void setCurrentWeapon(Class<? extends IWeapon> type) {
		this.currentWeapon = this.belt.getWeapon(type);
		EventBus.INSTANCE.publish(new Event(Property.CHANGED_WEAPON, this));
	}
	
	public IWeapon getCurrentWeapon(){
		return this.currentWeapon;
	}
	
	public Projectile fireWeapon(){
		
		return (this.currentWeapon.fire()) ? new Projectile(this.currentWeapon) : null;
	}
	
	@Override
	public int getCurrentWeaponDamage() {
		return this.currentWeapon.getDamage();
	}

	@Override
	public void attack(Character target) {
		//@todo Still not sure of the flow here
		this.fireWeapon();
		EventBus.INSTANCE.publish(new Event(Property.DID_ATTACK, this));
	}
}
