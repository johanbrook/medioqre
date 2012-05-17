package model.weapon;

import model.character.AbstractCharacter;
import model.weapon.Projectile.Range;

/**
 *	A portal gun.
 *
 *	@author Johan
 */
public class PortalGun extends AbstractWeapon {

	private Projectile projectile;
	
	/**
	 * The different portal gun modes.
	 * 
	 * <ul>
	 * <li><code>BLUE</code></li>
	 * <li><code>ORANGE</code></li>
	 * </ul>
	 * 
	 * @author Johan
	 */
	public enum Mode {
		BLUE, ORANGE;
	}
	
	private Mode mode;
	
	/**
	 * Create a new PortalGun. Initializes the mode to <code>BLUE</code>.
	 * 
	 * @param owner The owner
	 * @param ammo The initial ammo
	 * @param ammoMultiplier The ammo multiplier
	 * @param fireInterval The firing interval
	 */
	public PortalGun(AbstractCharacter owner, int ammo, double ammoMultiplier, double fireInterval) {
		super(owner, ammo, ammoMultiplier, fireInterval);
		this.mode = Mode.BLUE;
	}
	
	/**
	 * Set this portal gun's mode
	 * 
	 * @param mode The mode
	 */
	public void setMode(Mode mode) {
		this.mode = mode;
	}
	
	/**
	 * Toggle this portal gun's mode.
	 */
	public void switchMode() {
		this.mode = (this.mode == Mode.BLUE) ? Mode.ORANGE : Mode.BLUE;
		tools.Logger.log("Current portal gun mode: "+this.mode);
	}

	/**
	 * Get this portal gun's mode.
	 * 
	 * @return The mode
	 */
	public Mode getMode() {
		return this.mode;
	}

	@Override
	public Projectile getProjectile() {
		return this.projectile;
	}

	@Override
	public Projectile createProjectile() {
		return new Projectile(this.projectile);
	}

	@Override
	public void setProjectile(Projectile projectile) {
		this.projectile = projectile;
	}

}
