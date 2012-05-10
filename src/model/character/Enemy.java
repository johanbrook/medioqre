/**
 *	Enemy.java
 *
 *	@author Johan
 */

package model.character;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import event.Event;
import event.Event.Property;
import event.EventBus;

public class Enemy extends AbstractCharacter {

	public Enemy(int movementSpeed, Rectangle collBox, Dimension size,
			int xoffset, int yoffset) {
		super(movementSpeed, collBox, size, xoffset, yoffset);
	}

	public void getPushed(Enemy e) {
		if (!e.isMoving()) {
			Point currPos = this.getPosition();

			int x = (int) Math.signum((e.getDirection().getXRatio()));
			int y = (int) Math.signum((e.getDirection().getYRatio()));

			this.setPosition(currPos.x + x, currPos.y + y);
			EventBus.INSTANCE.publish(new Event(Property.DID_MOVE, this));
		}
	}

}
