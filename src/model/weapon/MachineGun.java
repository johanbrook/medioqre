/**
*	MachineGun.java
*
*	@author Johan
*/

package model.weapon;

import model.character.AbstractCharacter;
import model.weapon.Projectile.Range;

public class MachineGun extends AbstractWeapon {
	
	private Projectile projectile;
	
	public MachineGun(AbstractCharacter owner, int ammo){
		super(owner, ammo);
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
	
	@Override
	public void setProjectile(Projectile projectile) {
		this.projectile = projectile;
	}
}
