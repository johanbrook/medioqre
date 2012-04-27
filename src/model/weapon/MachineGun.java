/**
*	MachineGun.java
*
*	@author Johan
*/

package model.weapon;

import model.character.AbstractCharacter;
import model.weapon.Projectile.Range;

public class MachineGun extends AbstractWeapon {

	private final static int INITIAL_AMMO = 300;
	private final static int DAMAGE = 10;
	
	public MachineGun(AbstractCharacter owner){
		super(owner, INITIAL_AMMO);
	}
	

	public boolean equals(Object o) {
		if(o == null || getClass() != o.getClass())
			return false;
		
		return true;
	}


	@Override
	public Projectile getProjectile() {
		return new Projectile(this, 10, 10, 10, Range.FAR_RANGE, 40);
	}
}
