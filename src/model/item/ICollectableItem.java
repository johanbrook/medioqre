package model.item;

import model.character.Player;

/**
*	Interface for collectable items, such as medpacks, ammo crates.
*
*	@author Johan
*/
public interface ICollectableItem {
		
	/**
	 * Callback when this item is picked up by a player
	 * 
	 * @param source The player
	 */
	public void pickedUpBy(Player source);
}
