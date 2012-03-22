/**
*	MedPack.java
*
*	@author Johan
*/

package model.item;

import java.awt.Dimension;

import model.CollidableObject;
import model.Position;

public class MedPack extends CollidableObject implements ICollectableItem {

	public MedPack(Dimension collBox, Position pos, Position offset) {
		super(collBox, pos, offset);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void pickedUpBy(Character source) {
		// TODO Auto-generated method stub

	}

}
