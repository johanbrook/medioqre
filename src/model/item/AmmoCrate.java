/**
*	AmmoCrate.java
*
*	@author Johan
*/

package model.item;

import java.awt.Dimension;
import model.CollidableObject;
import model.Position;
import model.weapon.IWeapon;

public class AmmoCrate extends CollidableObject implements ICollectableItem {

	private IWeapon type;
	private double amount;
	
	public AmmoCrate(Dimension collBox, Position pos, Position offset, IWeapon type, double amount) {
		super(collBox, pos, offset);
		
		this.type = type;
		this.amount = amount;
	}

	@Override
	public void pickedUpBy(Character source) {
		// TODO Auto-generated method stub
	}

}
