package model.item;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import event.Event;
import event.EventBus;
import event.Event.Property;

import model.CollidableObject;
import model.character.Player;
import model.weapon.AbstractWeapon;

/**
 * An ammo crate.
 * 
 * @author Johan
 */
public class AmmoCrate extends CollidableObject implements ICollectableItem {

	private int amount;

	/**
	 * Create a new ammo crate.
	 * 
	 * @param amount
	 *            The ammo amount
	 * @param x
	 *            The x coordinate
	 * @param y
	 *            The y coordinate
	 */
	public AmmoCrate(int amount, int x, int y, int width, int height) {
		super(new Rectangle(x, y, width, height), new Dimension(width, height),
				0, 0);

		this.amount = amount;
	}

	@Override
	public void pickedUpBy(Player source) {
		for (AbstractWeapon weapon : source.getWeaponBelt()) {
			weapon.addAmmo(this.amount);
		}

		EventBus.INSTANCE.publish(new Event(Property.PICKED_UP_ITEM, this));
		this.destroy();
	}

}
