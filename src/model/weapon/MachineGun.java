/**
 *	MachineGun.java
 *
 *	@author Johan
 */

package model.weapon;

import model.character.AbstractCharacter;
import model.weapon.Projectile.Range;

public class MachineGun extends AbstractWeapon {

	private Projectile projectile;

	public MachineGun(AbstractCharacter owner, int ammo, double ammoMultiplier,
			double fireInterval) {
		super(owner, ammo, ammoMultiplier, fireInterval);
	}

	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass())
			return false;

		return true;
	}

	@Override
	public Projectile getProjectile() {
		return this.projectile;
	}

	@Override
	public Projectile createProjectile() {
		return new Projectile(this.projectile);
	}

	@Override
	public void setProjectile(Projectile projectile) {
		this.projectile = projectile;
	}
}
