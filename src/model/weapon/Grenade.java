/**
*	Grenade.java
*
*	@author Johan
*/

package model.weapon;

import model.character.AbstractCharacter;
import model.weapon.Projectile.Range;

public class Grenade extends AbstractWeapon {

	private Projectile projectile;
	
	public Grenade(AbstractCharacter owner, int ammo) {
		super(owner, ammo);
	}
	

	public boolean equals(Object o) {
		if(o == null || getClass() != o.getClass())
			return false;
		
		return true;
	}


	@Override
	public Projectile getProjectile() {
		// TODO Implement correctly
		return new Projectile(this, 10, 10, 10, Range.MEDIUM_RANGE, 30);
	}
	
	@Override
	public void setProjectile(Projectile projectile) {
		this.projectile = projectile;
	}
}
