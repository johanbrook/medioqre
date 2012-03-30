/**
*	MachineGun.java
*
*	@author Johan
*/

package model.weapon;

import model.weapon.Projectile.Range;

public class MachineGun extends AbstractWeapon {

	private final static int INITIAL_AMMO = 300;
	private final static int DAMAGE = 10;
	
	public MachineGun(){
		super(INITIAL_AMMO, new Projectile(DAMAGE, Range.FAR_RANGE));
	}
	

	public boolean equals(Object o) {
		if(o == null || getClass() != o.getClass())
			return false;
		
		return true;
	}
}
