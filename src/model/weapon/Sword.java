/**
*	Sword.java
*
*	@author Johan
*/

package model.weapon;

public class Sword implements IWeapon {

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
		return 0;
	}
	
	
	public boolean equals(Object o) {
		if(o == null || getClass() != o.getClass())
			return false;
		
		return true;
	}
}
