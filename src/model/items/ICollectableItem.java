/**
*	CollectableItem.java
*
*	@author Johan
*/

package model.items;

import model.CollidableObject;
import model.Position;

public interface ICollectableItem {
		
	public void pickedUpBy(Character source);
}
