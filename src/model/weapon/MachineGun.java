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
	
	/**
	 * Create a new MachineGun.
	 * 
	 * @param owner The owner
	 * @param ammo Initial ammo
	 * @param ammoMultiplier The ammo multiplier
	 * @param fireInterval The firing interval
	 */
	public MachineGun(AbstractCharacter owner, int ammo, double ammoMultiplier, double fireInterval){
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
