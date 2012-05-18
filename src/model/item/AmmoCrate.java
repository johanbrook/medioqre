package model.item;

import java.awt.Dimension;
import java.awt.Rectangle;

import org.json.JSONObject;

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
	 * Create a new AmmoCrate.
	 * 
	 * @param amount The amount of ammo this crate should contain
	 * @param x The x coordinate
	 * @param y The y coordinate
	 * @param width The width
	 * @param height The height
	 */
	public AmmoCrate(int amount, int x, int y, int width, int height) {
		super(new Rectangle(x, y, width, height), new Dimension(width, height),
				0, 0);

		this.amount = amount;
	}
	
	public AmmoCrate(int x, int y, JSONObject obj) {
		this(obj.optInt("amount"), x, y, 
				obj.optJSONObject("bounds").optInt("width"), 
				obj.optJSONObject("bounds").optInt("width"));
	}

	/**
	 * A Player picked up this AmmoCrate.
	 * 
	 * <p>Sends a <code>PICKED_UP_ITEM</code> event with the value <code>this</code></p>.
	 */
	@Override
	public void pickedUpBy(Player source) {
		for (AbstractWeapon weapon : source.getWeaponBelt()) {
			weapon.addAmmo(this.amount);
		}

		EventBus.INSTANCE.publish(new Event(Property.PICKED_UP_ITEM, this));
		this.destroy();
	}

	@Override
	public void didCollide(CollidableObject w) {

		if (w instanceof Player){
			this.pickedUpBy((Player) w);
		}
		
	}

	@Override
	public int getAmount() {
		return this.amount;
	}

}
