/**
*	AmmoCrate.java
*
*	@author Johan
*/

package model.item;

import java.awt.Dimension;
import java.awt.Rectangle;

import model.CollidableObject;
import model.weapon.IWeapon;

public class AmmoCrate extends CollidableObject implements ICollectableItem {

	private IWeapon type;
	private int amount;
	
	public AmmoCrate(Rectangle collBox, Dimension size, int xoffset, int yoffset, IWeapon type, int amount) {
		super(collBox, size, xoffset, yoffset);
		
		this.type = type;
		this.amount = amount;
	}

	@Override
	public void pickedUpBy(Character source) {
		// TODO Auto-generated method stub. Do stuff with amount/type
	}

}
