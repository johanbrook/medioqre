/**
*	CollectableItem.java
*
*	@author Johan
*/

package model.item;

import model.character.Player;


public interface ICollectableItem {
		
	public void pickedUpBy(Player source);
}
