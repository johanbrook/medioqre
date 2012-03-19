/**
*	MachineGun.java
*
*	@author Johan
*/

package model.weapon;

public class MachineGun implements IWeapon {

	private int ammo;
	private int damagePerAttack;
	
	public MachineGun(){
		this.ammo = 300;
		this.damagePerAttack = 10;
	}
	
	@Override
	public boolean fire() {
		ammo--;
		return ammo != 0;
	}

	@Override
	public int getAmmo() {
		return this.ammo;
	}

	@Override
	public int getDamage() {
		return this.damagePerAttack;
	}

	public boolean equals(Object o) {
		if(o == null || getClass() != o.getClass())
			return false;
		
		return true;
	}
}
