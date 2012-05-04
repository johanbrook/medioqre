package model.item;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import event.Event;
import event.EventBus;
import event.Event.Property;

import model.CollidableObject;
import model.character.Player;

/**
*	An ammo crate.
*
*	@author Johan
*/
public class AmmoCrate extends CollidableObject implements ICollectableItem {

	private int amount;
	
	/**
	 * Create a new ammo crate.
	 * 
	 * @param amount The ammo amount
	 * @param x The x coordinate
	 * @param y The y coordinate
	 */
	public AmmoCrate(int amount, int x, int y) {
		super(new Rectangle(x, y, 10, 10), new Dimension(10, 10), 0, 0);
		
		this.amount = amount;
	}

	@Override
	public void pickedUpBy(Player source) {
		source.getCurrentWeapon().addAmmo(this.amount);
		EventBus.INSTANCE.publish(new Event(Property.PICKED_UP_ITEM, this));
		this.destroy();
	}

}
