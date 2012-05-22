package model.weapon;

import model.character.AbstractCharacter;

/**
 *	A melee weapon.
 *
 *	@author Johan
 */
public class Melee extends AbstractWeapon {

	private Projectile projectile;
	
	/**
	 * Create a new Melee.
	 * 
	 * @param owner The owner
	 * @param ammo Initial ammo
	 * @param ammoMultiplier The ammo multiplier
	 * @param fireInterval The firing interval
	 */
	public Melee(AbstractCharacter owner, int ammo, double ammoMultiplier, double fireInterval) {
		super(owner, ammo, ammoMultiplier, fireInterval);
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass())
			return false;

		return true;
	}
	
	@Override
	public int hashCode() {
		return 42;
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
