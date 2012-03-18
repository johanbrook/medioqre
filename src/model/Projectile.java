/**
*	Projectile.java
*
*	@author Johan
*/

package model;

import constants.WeaponType;

public class Projectile {
	
	private WeaponType type;
	
	public Projectile(WeaponType type){
		this.type = type;
	}
	
	public WeaponType getType(){
		return this.type;
	}
	
	public int getDamage(){
		return this.type.getDamage();
	}
	
	@Override
	public boolean equals(Object o){
		if(this == o)
			return true;
		if(o == null || getClass() != o.getClass())
			return false;
		
		Projectile other = (Projectile) o;
		return this.getType() == other.getType();
	}
}


