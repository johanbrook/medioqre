/**
*	Enemy.java
*
*	@author Johan
*/

package model.character;

import model.weapon.IWeapon;
import model.weapon.Melee;

public class Enemy extends Character {
	
	private IWeapon melee;
	
	public Enemy(int movementSpeed) {
		super(movementSpeed);
		
		this.melee = new Melee(30);
	}

	@Override
	public void attack(Character target) {
		target.takeDamage(this.melee.getDamage());
	}
	
	@Override
	public int getCurrentWeaponDamage() {
		return this.melee.getDamage();
	}
	
	
}