package model.character;

import java.awt.Dimension;
import java.awt.Rectangle;

import org.json.JSONObject;

import model.CollidableObject;
import model.Entity;
import model.weapon.*;

/**
 *	A player. Has a weapon belt (inventory).
 *
 *	@author Johan
 */
public class Player extends AbstractCharacter {

	private WeaponBelt weaponbelt;

	/**
	 * Create a new player.
	 * 
	 * @param speed The movement speed
	 * @param box The collision box
	 * @param size The size
	 * @param offsetX The x offset
	 * @param offsetY The y offset
	 */
	public Player(int speed, Rectangle box, Dimension size, int offsetX,
			int offsetY) {
		super(speed, box, size, offsetX, offsetY);
	}
	
	/**
	 * Create a Player from a JSON object.
	 * 
	 * @param obj The JSON object
	 */
	public Player(JSONObject obj) {
		super(obj);
	}

	/**
	 * Set the current weapon belt of the player.
	 * 
	 * @param weapons The weapon belt list
	 */
	public void setWeaponBelt(WeaponBelt weapons) {
		this.weaponbelt = weapons;
	}

	/**
	 * Get the weapon inventory.
	 * 
	 * @return The list of weapon
	 */
	public WeaponBelt getWeaponBelt() {
		return this.weaponbelt;
	}

	/**
	 * Set the current weapon from the player's weapon belt slot.
	 * 
	 * @param slot The slot number
	 */
	public void setCurrentWeapon(int slot) {
		try {
			setCurrentWeapon(this.weaponbelt.get(slot));
		}
		catch(IndexOutOfBoundsException e) {
			tools.Logger.err("Couldn't find a weapon for that slot!");
		}
	}

	/**
	 * Set the current weapon based on the weapon type.
	 * 
	 * @param type The type
	 */
	public void setCurrentWeapon(Class<? extends AbstractWeapon> type) {
		setCurrentWeapon(getWeaponFromBelt(type));
	}

	/**
	 * Get the weapon of a certain type from the belt.
	 * 
	 * @param type A weapon type (the weapon's class)
	 * @return The first weapon in the belt with the specified type
	 */
	private AbstractWeapon getWeaponFromBelt(Class<? extends AbstractWeapon> type) {

		for (AbstractWeapon w : this.weaponbelt) {
			if (w.getClass() == type) {
				return w;
			}
		}

		return null;
	}

	@Override
	public void didCollide(CollidableObject w) {
		if (w instanceof Entity && w.getClass() != Projectile.class){
			this.setPosition(this.getRememberedPosition());
		}
	}
}
