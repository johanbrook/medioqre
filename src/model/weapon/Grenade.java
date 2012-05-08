/**
*	Grenade.java
*
*	@author Johan
*/

package model.weapon;

import model.character.AbstractCharacter;
import model.weapon.Projectile.Range;

public class Grenade extends AbstractWeapon {

	private Projectile projectile;
	private int radius, splashDamageFactor;
	
	public Grenade(AbstractCharacter owner, int ammo, double ammoMultiplier) {
		super(owner, ammo, ammoMultiplier);
	}
	

	public boolean equals(Object o) {
		if(o == null || getClass() != o.getClass())
			return false;
		
		return true;
	}
	


	@Override
	public Projectile getProjectile() {
		return new Projectile(this.projectile);
	}
	
	@Override
	public void setProjectile(Projectile projectile) {
		this.projectile = projectile;
	}

	public void setRadius(int radius){
		this.radius = radius;
	}

	public int getRadius() {
		return this.radius;
	}
	
	public void setSplashDamageFactor(int splashDamamageFactor){
		this.splashDamageFactor = splashDamamageFactor;
	}
	
	public int getSplashDamageFactor(){
		return this.splashDamageFactor;
	}
}
