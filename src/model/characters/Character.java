/**
*	Character.java
*
*	@author Johan
*/

package model.characters;

import model.Entity;

public abstract class Character extends Entity {

	private int health;
	
	protected int movementSpeed;
	
	public Character() {
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
	
	public abstract void attack(Character target);

}
