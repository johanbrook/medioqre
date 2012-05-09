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

	private double fireInterval, cooldown;

	/**
	 * Create a new weapon with an owner and initial ammo.
	 * 
	 * @param owner The owner
	 * @param initialAmmo The ammo
	 */
	public AbstractWeapon(AbstractCharacter owner, int initialAmmo, double ammoMultiplier, double fireInterval) {
		this.ammo = initialAmmo;
		this.owner = owner;
		this.ammoMultiplier = ammoMultiplier;
		this.fireInterval = fireInterval;
		this.cooldown = 0;
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
		this.ammo += amount * ammoMultiplier;
	}

	/**
	 * Fire this weapon and return the resulting projectile.
	 * 
	 * @return The projectile from this weapon.
	 */
	public Projectile fire() {
		Projectile p = this.createProjectile();

		if (!inCooldown()){
			
			if (this.ammo != 0){
				if(this.ammo != -1)
					this.ammo--;

				EventBus.INSTANCE.publish(new Event(Property.FIRED_WEAPON_SUCCESS, p));
				resetCooldown();
				return p;
			}

			else {
				EventBus.INSTANCE.publish(new Event(Property.FIRED_WEAPON_FAIL, p));
				return null;
			}
		}
		else {
			EventBus.INSTANCE.publish(new Event(Property.FIRED_WEAPON_FAIL, p));
			return null;
		}

	}

	/**
	 * subtracts dt from the current cooldown, if cooldown is zero or below, resets the cooldown and returns false, else returns true.
	 * @param dt
	 * @return
	 */
	public boolean inCooldown(){
		return this.cooldown > 0;
	}

	public void resetCooldown(){
		this.cooldown = this.fireInterval;
	}

	public void updateCooldown(double dt){
		if (this.cooldown > 0)
			this.cooldown -=dt;
	}


	@Override
	public String toString() {
		return "["+this.getClass().getSimpleName() + "[owner:"+this.owner.getClass().getSimpleName()+"] [ammo:"+this.ammo+"]]";
	}

	/**
	 * Create a new projectile from this weapon. Note that the returned projectile
	 * is a new instance.
	 * 
	 * @return The projectile.
	 */
	public abstract Projectile createProjectile();


	/**
	 * Get the associated projectile from this weapon.
	 * 
	 * @return The projectile
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
	
	/**
	 * Set the fireInterval of this weapon. Lestt fireIntervall means faster firerate.
	 * @param fireInterval
	 */
	public void setFireInterval(double fireInterval){
		this.fireInterval = fireInterval;
	}
	
	/**
	 * get the fireInterval of this weapon
	 * @return the fireInterval of this weapon
	 */
	public double getFireInterval (){
		return this.fireInterval;
	}

}
