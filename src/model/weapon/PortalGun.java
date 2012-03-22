/**
*	PortalGun.java
*
*	@author Johan
*/

package model.weapon;

public class PortalGun implements IWeapon {

	@Override
	public boolean fire() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public int getAmmo() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getDamage() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public boolean equals(Object o) {
		if(o == null || getClass() != o.getClass())
			return false;
		
		return true;
	}
}
