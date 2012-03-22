/**
*	Character.java
*
*	@author Johan
*/


package model.character;

import model.Entity;

public abstract class Character extends Entity {

	private int health;
	
	public Character(int movementSpeed) {
		super(movementSpeed);
		this.health = 100;
	}
	
	public void takeDamage(int dmg){
		this.health -= dmg;
		if(this.health <= 0)
			this.destroy();
	}
	
	
	public int getHealth() {
		return this.health;
	}
	
	public abstract int getCurrentWeaponDamage();
	
	public abstract void attack(Character target);

}