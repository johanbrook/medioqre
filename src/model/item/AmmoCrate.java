/**
*	AmmoCrate.java
*
*	@author Johan
*/

package model.item;

import java.awt.Dimension;
import java.awt.Rectangle;

import event.Event;
import event.EventBus;
import event.Event.Property;

import model.CollidableObject;
import model.character.Player;

public class AmmoCrate extends CollidableObject implements ICollectableItem {

	private int amount;
	
	public AmmoCrate(Rectangle collBox, Dimension size, int xoffset, int yoffset, int amount) {
		super(collBox, size, xoffset, yoffset);
		
		this.amount = amount;
	}

	@Override
	public void pickedUpBy(Player source) {
		source.getCurrentWeapon().addAmmo(this.amount);
		EventBus.INSTANCE.publish(new Event(Property.PICKED_UP_ITEM, this));
	}

}
