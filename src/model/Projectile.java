/**
*	Projectile.java
*
*	@author Johan
*/

package model;

import model.weapon.IWeapon;

public class Projectile {
	
	private IWeapon owner;
	
	public Projectile(IWeapon owner){
		this.owner = owner;
	}
	
	public IWeapon getOwner(){
		return this.owner;
	}
	
	public int getDamage(){
		return this.owner.getDamage();
	}
}


