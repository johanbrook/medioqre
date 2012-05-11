package model.weapon;

import model.character.AbstractCharacter;

/**
 *	Sword.java
 *
 *	@author Johan
 *	@deprecated Using Melee instead. Same functionality (2012-05-10).
 */
public class Sword extends AbstractWeapon {

	private Projectile projectile;

	public Sword(AbstractCharacter owner, int ammo, double ammoMultiplier,
			double fireInterval) {
		super(owner, ammo, ammoMultiplier, fireInterval);
	}

	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass())
			return false;

		return true;
	}

	@Override
	public Projectile createProjectile() {
		return new Projectile(this.projectile);
	}

	@Override
	public Projectile getProjectile() {
		return this.projectile;
	}

	@Override
	public void setProjectile(Projectile projectile) {
		this.projectile = projectile;
	}
}
