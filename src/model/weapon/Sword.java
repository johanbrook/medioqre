/**
*	Sword.java
*
*	@author Johan
*/

package model.weapon;

import model.character.AbstractCharacter;
import model.weapon.Projectile.Range;

public class Sword extends AbstractWeapon {
	
	private Projectile projectile;
	
	public Sword(AbstractCharacter owner, int ammo, double ammoMultiplier) {
		super(owner, ammo, ammoMultiplier);
	}

	public boolean equals(Object o) {
		if(o == null || getClass() != o.getClass())
			return false;
		
		return true;
	}

	@Override
	public Projectile getProjectile() {
		// TODO Auto-generated method stub
		return new Projectile(this, 10, 10, 1000, Range.SHORT_RANGE, 30);
	}

	@Override
	public void setProjectile(Projectile projectile) {
		this.projectile = projectile;
	}
}
