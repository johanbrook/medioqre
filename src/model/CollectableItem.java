/**
*	CollectableItem.java
*
*	@author Johan
*/

package model;

import constants.ItemType;

public abstract class CollectableItem extends CollidableObject {
	
	private ItemType type;
	private double value;
	
	public CollectableItem(java.awt.Dimension collBox, Position pos, Position offset) {
		super(collBox, pos, offset);
	}
	
	public CollectableItem(ItemType type, java.awt.Dimension collBox, Position pos, Position offset){
		this(collBox, pos, offset);
		
		this.type = type;
		this.value = type.getValue();
	}
	
	public double getValue() {
		return this.value;
	}
	
	public ItemType getType() {
		return this.type;
	}
}
