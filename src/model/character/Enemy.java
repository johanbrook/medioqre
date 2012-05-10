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
	
	/**
	 * Create a new enemy
	 * 
	 * @param movementSpeed The movement speed
	 * @param collBox The collision box
	 * @param size The size
	 * @param xoffset The x offset
	 * @param yoffset The y offset
	 */
	public Enemy(int movementSpeed, Rectangle collBox, Dimension size, int xoffset, int yoffset) {
		super(movementSpeed, collBox, size, xoffset, yoffset);		
	}

	/**
	 * Called when this enemy should be pushed by another enemy.
	 * 
	 * <p>Sends a <code>DID_MOVE</code> event with the value <code>this</code></p>.
	 * 
	 * @param e The other enemy
	 */
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
