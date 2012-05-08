/**
*	PortalGun.java
*
*	@author Johan
*/

package model.weapon;

import model.character.AbstractCharacter;
import model.weapon.Projectile.Range;

public class PortalGun extends AbstractWeapon {

	private Projectile projectile;
	
	public enum Mode {
		BLUE, ORANGE;
	}
	
	private Mode mode;
	
	public PortalGun(AbstractCharacter owner, int ammo, double ammoMultiplier) {
		super(owner, ammo, ammoMultiplier);
		this.mode = Mode.BLUE;
	}
	
	public void setMode(Mode mode) {
		this.mode = mode;
	}
	
	public void switchMode() {
		this.mode = (this.mode == Mode.BLUE) ? Mode.ORANGE : Mode.BLUE;
		System.out.println("Current portal gun mode: "+this.mode);
	}

	public Mode getMode() {
		return this.mode;
	}

	@Override
	public Projectile getProjectile() {
		return new Projectile(this, 10, 10, 0, Range.SHORT_RANGE, 10);
	}
	
	@Override
	public void setProjectile(Projectile projectile) {
		this.projectile = projectile;
	}
}
