package model.weapon;


import event.Event;
import event.Event.Property;
import event.EventBus;
import model.character.AbstractCharacter;

/**
 *	Weapon.java
 *
 *	@author Johan
 */
public abstract class AbstractWeapon {

	private int ammo;
	private AbstractCharacter owner;
	private double ammoMultiplier;

	/**
	 * Create a new weapon with an owner and initial ammo.
	 * 
	 * @param owner The owner
	 * @param initialAmmo The ammo
	 */
	public AbstractWeapon(AbstractCharacter owner, int initialAmmo) {
		this.ammo = initialAmmo;
		this.owner = owner;
	}

	/**
	 * Return this weapon's owner (an AbstractCharacter).
	 * 
	 * @return Its owner
	 */
	public AbstractCharacter getOwner() {
		return this.owner;
	}

	/**
	 * Get the current ammo amount.
	 * 
	 * @return The amount
	 */
	public int getCurrentAmmo() {
		return this.ammo;
	}

	/**
	 * Add ammo
	 * 
	 * @param amount The amount
	 */
	public void addAmmo(int amount) {
		this.ammo += amount;
	}

	/**
	 * Fire this weapon and return the resulting projectile.
	 * 
	 * @return The projectile from this weapon.
	 */
	public Projectile fire() {
		Projectile p = this.getProjectile();

		if (this.ammo != 0){
			if(this.ammo != -1)
				this.ammo--;
			
			EventBus.INSTANCE.publish(new Event(Property.FIRED_WEAPON_SUCCESS, p));
			return p;
		}
		else {
			EventBus.INSTANCE.publish(new Event(Property.FIRED_WEAPON_FAIL, p));
			return null;
		}
	}
	
	
	@Override
	public String toString() {
		return "["+this.getClass().getSimpleName() + " [ammo:"+this.ammo+"]]";
	}

	/**
	 * Get the associated projectile from this weapon.
	 * 
	 * @return The projectile.
	 */
	public abstract Projectile getProjectile();
	
	
	/**
	 * Set the associated projectile for this weapon.
	 * 
	 * @param projectile The projectile
	 */
	public abstract void setProjectile(Projectile projectile);

	/**
	 * get the ammo multiplier
	 * @return the ammoFactor
	 */
	public double getAmmoMultiplier() {
		return ammoMultiplier;
	}

	/**
	 * Set the ammo multiplier
	 * @param ammoMultiplier
	 */
	public void setammoMultiplier(double ammoMultiplier) {
		this.ammoMultiplier = ammoMultiplier;
	}

}
