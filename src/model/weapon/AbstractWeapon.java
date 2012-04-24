/**
*	Weapon.java
*
*	@author Johan
*/

package model.weapon;


import event.Event;
import event.Event.Property;
import event.EventBus;

public abstract class AbstractWeapon {
	
	private int ammo;
	private Projectile projectile;
	
	
	public AbstractWeapon(int initialAmmo, Projectile projectile) {
		this.ammo = initialAmmo;
		this.projectile = projectile;
	}
	
	
	public int getCurrentAmmo() {
		return this.ammo;
	}
	
	public void addAmmo(int amount) {
		this.ammo += amount;
	}
	
	public int getDamage() {
		return this.projectile.getDamage();
	} 

	
	public Projectile fire() {
		if(this.ammo > 0 || this.ammo == -1){
			this.ammo--;
			EventBus.INSTANCE.publish(new Event(Property.FIRED_WEAPON_SUCCESS, this.projectile));
			return this.projectile;
		}
		else {
			EventBus.INSTANCE.publish(new Event(Property.FIRED_WEAPON_FAIL, this.projectile));
			return null;
		}
	}
	
	
}
