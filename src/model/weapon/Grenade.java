package model.weapon;

import model.character.AbstractCharacter;

/**
 *	A grenade with a radius and splash damage properties.
 *
 *	@author Johan
 */

public class Grenade extends AbstractWeapon {

	private Projectile projectile;
	private int radius, splashDamageFactor;
	
	/**
	 * Create a new Grenade.
	 * 
	 * @param owner The owner
	 * @param ammo Initial ammo
	 * @param ammoMultiplier The ammo multiplier
	 * @param fireInterval The firing interval
	 */
	public Grenade(AbstractCharacter owner, int ammo, double ammoMultiplier, double fireInterval) {
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

	/**
	 * Set radius for splash damage.
	 * @param radius 
	 */
	public void setRadius(int radius){
		this.radius = radius;
	}

	/**
	 * get the splash range
	 * @return the splash range
	 */
	public int getRadius() {
		return this.radius;
	}
	
	/**
	 * set the factor splash damage. Splash damage 2 means half the weapon damage is done in radius around collision
	 * @param splashDamamageFactor
	 */
	public void setSplashDamageFactor(int splashDamamageFactor){
		this.splashDamageFactor = splashDamamageFactor;
	}
	
	/**
	 * get the splash damage factor
	 * @return the splash damage factor
	 */
	public int getSplashDamageFactor(){
		return this.splashDamageFactor;
	}
	
}
