/**
*	Weapon.java
*
*	@author Johan
*/

package model.weapon;

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
			return this.projectile;
		}
		
		return null;
	}
	
	
}
