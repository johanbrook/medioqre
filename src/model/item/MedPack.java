/**
*	MedPack.java
*
*	@author Johan
*/

package model.item;

import java.awt.Dimension;
import java.awt.Rectangle;

import model.CollidableObject;

public class MedPack extends CollidableObject implements ICollectableItem {

	public MedPack(int amount) {
		super(new Rectangle(10, 10), new Dimension(10, 10), 0, 0);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void pickedUpBy(Character source) {
		// TODO Auto-generated method stub

	}

}
