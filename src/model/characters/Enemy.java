/**
*	Enemy.java
*
*	@author Johan
*/

package model.characters;

import java.awt.Dimension;

import model.Position;

public class Enemy extends Character {

	
	public Enemy() {
		super();
	}

	@Override
	public void attack(Character target) {
		target.takeDamage(30);
	}
	
	
	
	
}
