/**
*	Enemy.java
*
*	@author Johan
*/

package model.character;

import java.awt.Dimension;
import java.awt.Rectangle;
import model.weapon.Melee;

public class Enemy extends AbstractCharacter {
	
	
	public Enemy(int movementSpeed, int damage) {
		this(movementSpeed, damage, 0, 0);
	}
	
	public Enemy(int movementSpeed, int damage, int x, int y) {
		super(movementSpeed, new Rectangle(x, y, 16, 16), new Dimension(20,20), 0, 16);
		
		setCurrentWeapon(new Melee(this));
	}	
	
}
