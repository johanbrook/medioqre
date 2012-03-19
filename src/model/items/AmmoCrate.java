/**
*	AmmoCrate.java
*
*	@author Johan
*/

package model.items;

import java.awt.Dimension;

import constants.WeaponType;

import model.CollidableObject;
import model.Position;

public class AmmoCrate extends CollidableObject implements ICollectableItem {

	private WeaponType ammoType;
	private double amount;
	
	public AmmoCrate(Dimension collBox, Position pos, Position offset, WeaponType type, double amount) {
		super(collBox, pos, offset);
		
		this.ammoType = type;
		this.amount = amount;
	}

	@Override
	public void pickedUpBy(Character source) {
		// TODO Auto-generated method stub

	}

}
