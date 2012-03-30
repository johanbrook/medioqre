/**
*	Grenade.java
*
*	@author Johan
*/

package model.weapon;

import model.weapon.Projectile.Range;

public class Grenade extends AbstractWeapon {

	private final static int INITIAL_AMMO = 4;
	private final static int DAMAGE = 10;
	
	public Grenade() {
		super(INITIAL_AMMO, new Projectile(DAMAGE, Range.SHORT_RANGE));
	}

	public boolean equals(Object o) {
		if(o == null || getClass() != o.getClass())
			return false;
		
		return true;
	}
	
}
