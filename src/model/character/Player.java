/**
*	Player.java
*
*	@author Johan
*/

package model.character;


import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.List;

import event.Event;
import event.EventBus;
import event.Event.Property;
import factory.ObjectFactory;
import model.weapon.*;


public class Player extends AbstractCharacter {
	
	private List<AbstractWeapon> weaponbelt;
	
	/**
	 * Create a new player.
	 * 
	 * @param speed The movement speed
	 * @param box The collision box
	 * @param size The size
	 * @param offsetX The x offset
	 * @param offsetY The y offset
	 */
	public Player(int speed, Rectangle box, Dimension size, int offsetX, int offsetY){
		super(speed, box, size, offsetX, offsetY);
	}
	
	/**
	 * Set the current weapon belt of the player.
	 * 
	 * @param weapons The weapon belt list
	 */
	public void setWeaponBelt(List<AbstractWeapon> weapons) {
		this.weaponbelt = weapons;
	}
	
	
	/**
	 * Get the weapon inventory.
	 * 
	 * @return The list of weapon
	 */
	public List<AbstractWeapon> getWeaponBelt() {
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
		
		for(AbstractWeapon w : this.weaponbelt) {
			if(w.getClass() == type) {
				return w;
			}
		}
		
		return null;
	}
}
