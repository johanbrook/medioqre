/**
*	Melee.java
*
*	@author Johan
*/

package model.weapon;

import model.character.AbstractCharacter;
import model.weapon.Projectile.Range;

public class Melee extends AbstractWeapon {
	
	private Projectile projectile;
	
	public Melee(AbstractCharacter owner, int ammo) {
		super(owner, ammo);
	}

	public boolean equals(Object o) {
		if(o == null || getClass() != o.getClass())
			return false;
		
		return true;
	}

	@Override
	public Projectile getProjectile() {
		return new Projectile(this.projectile);
	}
	
	@Override
	public void setProjectile(Projectile projectile) {
		this.projectile = projectile;
	}

}
