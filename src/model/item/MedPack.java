/**
*	MedPack.java
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

public class MedPack extends CollidableObject implements ICollectableItem {

	private int amount;
	
	public MedPack(int amount) {
		super(new Rectangle(10, 10), new Dimension(10, 10), 0, 0);
		this.amount = amount;
	}

	@Override
	public void pickedUpBy(Player source) {
		source.addHealth(this.amount);
		EventBus.INSTANCE.publish(new Event(Property.PICKED_UP_ITEM, this));
	}

}
