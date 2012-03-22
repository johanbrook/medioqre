/**
*	Player.java
*
*	@author Johan
*/

package model.character;


import model.Projectile;
import model.weapon.*;


public class Player extends Character {
	
	private IWeapon currentWeapon;
	private WeaponBelt belt;
	
	public Player() {
		this(10);
	}
	
	public Player(int movementSpeed){
		super(movementSpeed);
		
		this.belt = new WeaponBelt();
		
		setCurrentWeapon(MachineGun.class);
	}
	
	public void setCurrentWeapon(int slot) {
		this.currentWeapon = this.belt.getWeapon(slot);
	}
	
	
	public void setCurrentWeapon(Class<? extends IWeapon> type) {
		this.currentWeapon = this.belt.getWeapon(type);
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
	}
}
