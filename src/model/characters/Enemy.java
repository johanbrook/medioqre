/**
*	Enemy.java
*
*	@author Johan
*/

package model.characters;

public class Enemy extends Character {

	
	public Enemy(int movementSpeed) {
		super(movementSpeed);
	}

	@Override
	public void attack(Character target) {
		target.takeDamage(30);
	}
	
	
	
	
}
