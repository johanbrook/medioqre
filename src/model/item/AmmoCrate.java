/**
*	AmmoCrate.java
*
*	@author Johan
*/

package model.item;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import event.Event;
import event.EventBus;
import event.Event.Property;

import model.CollidableObject;
import model.character.Player;

public class AmmoCrate extends CollidableObject implements ICollectableItem {

	private int amount;
	
	public AmmoCrate(int amount, int x, int y) {
		super(new Rectangle(x, y, 10, 10), new Dimension(10, 10), 0, 0);
		
		this.amount = amount;
	}

	@Override
	public void pickedUpBy(Player source) {
		source.getCurrentWeapon().addAmmo(this.amount);
		EventBus.INSTANCE.publish(new Event(Property.PICKED_UP_ITEM, this));
		System.out.println("Picked up AmmoCrate");
		this.destroy();
	}

}
