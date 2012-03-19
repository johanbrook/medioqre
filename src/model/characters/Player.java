/**
*	Player.java
*
*	@author Johan
*/

package model.characters;

import java.util.ArrayList;

import model.Position;
import model.Projectile;
import model.Weapon;

import constants.WeaponType;

public class Player extends Character {
	
	private Weapon currentWeapon;
	private java.util.List<Weapon> weapons;
	
	public Player(int movementSpeed){
		super(movementSpeed);
		
		this.weapons = new ArrayList<Weapon>();
		this.weapons.add(new Weapon(WeaponType.RIFLE));
		this.weapons.add(new Weapon(WeaponType.GRENADE));
		this.weapons.add(new Weapon(WeaponType.SWORD));
		this.weapons.add(new Weapon(WeaponType.PORTALGUN));
		
		setCurrentWeapon(WeaponType.RIFLE);
	}
	
	public void setCurrentWeapon(WeaponType type) {
		for(Weapon w : this.weapons){
			if(w.getType() == type){
				this.currentWeapon = w;
				break;
			}
		}
	}
	
	public Weapon getCurrentWeapon(){
		return this.currentWeapon;
	}
	
	
	public Projectile fireWeapon(){
		if(this.currentWeapon.fire()) {
			return new Projectile(this.currentWeapon.getType());
		}
		
		return null;
	}

	@Override
	public void attack(Character target) {
		this.fireWeapon();
	}
}
