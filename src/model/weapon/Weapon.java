/**
*	Weapon.java
*
*	@author Johan
*/

package model.weapon;


public class Weapon {
	
	private WeaponType type;
	private int ammo;
	
	public Weapon(WeaponType type) {
		this.type = type;
		this.ammo = type.getStartingAmmo();
	}
	
	public WeaponType getType(){
		return this.type;
	}
	
	public boolean fire(){
		ammo--;
		if(ammo == 0)
			return false;
		
		return true;
	}
	
	public int getAmmo(){
		return this.ammo;
	}
	
	public int getDamage(){
		return this.type.getDamage();
	}
	
	
	@Override
	public boolean equals(Object o) {
		if(this == o)
			return true;
		if(o == null || getClass() != o.getClass())
			return false;
		
		Weapon other = (Weapon) o;
		return this.getType() == other.getType();
	}
}
