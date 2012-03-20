/**
*	Melee.java
*
*	@author Johan
*/

package model.weapon;

public class Melee implements IWeapon {

	private int damagePerAttack;
	
	public Melee(int dmg) {
		this.damagePerAttack = dmg;
	}
	
	@Override
	public boolean fire() {
		return true;
	}

	@Override
	public int getAmmo() {
		return -1;
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
