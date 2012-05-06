/**
 *	Enemy.java
 *
 *	@author Johan
 */

package model.character;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import model.weapon.Melee;
import event.Event;
import event.Event.Property;
import event.EventBus;

public class Enemy extends AbstractCharacter {


	public Enemy(int movementSpeed, int damage) {
		this(movementSpeed, damage, 0, 0);
	}

	public Enemy(int movementSpeed, int damage, int x, int y) {
		super(movementSpeed, new Rectangle(x, y, 16, 16), new Dimension(20,20), 0, 16);

		setCurrentWeapon(new Melee(this));
	}	

	public void getPushed(Enemy e){
		if (!e.isMoving()){
			Point currPos = this.getPosition();

			int x = (int) Math.signum((e.getDirection().getXRatio()));		
			int y = (int) Math.signum((e.getDirection().getYRatio()));

			this.setPosition(currPos.x + x, currPos.y + y);
			EventBus.INSTANCE.publish(new Event(Property.DID_MOVE, this));
		}
	}

}
