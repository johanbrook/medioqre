/**
*	PortalGun.java
*
*	@author Johan
*/

package model.weapon;

import model.character.AbstractCharacter;
import model.weapon.Projectile.Range;

public class PortalGun extends AbstractWeapon {

	public enum Mode {
		BLUE, ORANGE;
	}
	
	private Mode mode;
	
	public PortalGun(AbstractCharacter owner) {
		super(owner, -1);
		this.mode = Mode.BLUE;
	}
	
	public void setMode(Mode mode) {
		this.mode = mode;
	}
	
	public void switchMode() {
		this.mode = (this.mode == Mode.BLUE) ? Mode.ORANGE : Mode.BLUE;
	}

	public Mode getMode() {
		return this.mode;
	}

	@Override
	public Projectile getProjectile() {
		return new Projectile(this, 10, 10, 0, Range.SHORT_RANGE, 10);
	}
}
