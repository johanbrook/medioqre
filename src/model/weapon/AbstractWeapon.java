/**
 *	Weapon.java
 *
 *	@author Johan
 */

package model.weapon;


import event.Event;
import event.Event.Property;
import event.EventBus;
import model.character.AbstractCharacter;

public abstract class AbstractWeapon {

	private int ammo;
	private AbstractCharacter owner;

	public AbstractWeapon(AbstractCharacter owner, int initialAmmo) {
		this.ammo = initialAmmo;
		this.owner = owner;
	}


	public AbstractCharacter getOwner() {
		return this.owner;
	}

	public int getCurrentAmmo() {
		return this.ammo;
	}

	public void addAmmo(int amount) {
		this.ammo += amount;
	}

	public Projectile fire() {
		Projectile p = getProjectile();

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


	public abstract Projectile getProjectile();

}
