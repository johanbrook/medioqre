/**
*	Grenade.java
*
*	@author Johan
*/

package model.weapon;

public class Grenade implements IWeapon {

	private int numberOfGrandes;
	private int damagePerGrenade;
	
	public Grenade() {
		this.numberOfGrandes = 4;
		this.damagePerGrenade = 90;
	}
	
	@Override
	public boolean fire() {
		this.numberOfGrandes--;
		return this.numberOfGrandes != 0;
	}

	@Override
	public int getAmmo() {
		return this.numberOfGrandes;
	}

	@Override
	public int getDamage() {
		return this.damagePerGrenade;
	}

	public boolean equals(Object o) {
		if(o == null || getClass() != o.getClass())
			return false;
		
		return true;
	}
	
}
