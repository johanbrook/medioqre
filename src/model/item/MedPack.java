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
 * A medical kit.
 * 
 * @author Johan
 */
public class MedPack extends CollidableObject implements ICollectableItem {

	private int amount;

	/**
	 * Create a new med pack.
	 * 
	 * @param amount
	 *            The HP amount
	 * @param x
	 *            The x coordinate
	 * @param y
	 *            The y coordinate
	 */
	public MedPack(int amount, int x, int y, int width, int height) {
		super(new Rectangle(x, y, width, height), new Dimension(width, height),
				0, 0);
		this.amount = amount;
	}

	@Override
	public void pickedUpBy(Player source) {
		source.addHealth(this.amount);
		EventBus.INSTANCE.publish(new Event(Property.PICKED_UP_ITEM, this));
		System.out.println("Picked up MedPack");

		this.destroy();
	}

}
