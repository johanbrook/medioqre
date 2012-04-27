/**
*	Sword.java
*
*	@author Johan
*/

package model.weapon;

import model.character.AbstractCharacter;
import model.weapon.Projectile.Range;

public class Sword extends AbstractWeapon {

	private final static int INITIAL_AMMO = -1;
	private final static int DAMAGE = 10;
	
	public Sword(AbstractCharacter owner) {
		super(owner, INITIAL_AMMO);
	}

	public boolean equals(Object o) {
		if(o == null || getClass() != o.getClass())
			return false;
		
		return true;
	}

	@Override
	public Projectile getProjectile() {
		// TODO Auto-generated method stub
		return null;
	}

}
