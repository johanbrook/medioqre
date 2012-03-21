package model.weapon;

/**
 * The weapon interface.
 * 
 * @author Johan
 *
 */
public interface IWeapon {
	
	/**
	 * Fire the weapon.
	 * 
	 * @return True if the weapon was fired successfully, otherwise false
	 */
	public boolean fire();
	
	/**
	 * Show the amount of ammo left.
	 * 
	 * @return The amount of ammo
	 */
	public int getAmmo();
	
	/**
	 * Show how much damage the weapon makes.
	 * 
	 * @return The amount of damage
	 */
	public int getDamage();
}
