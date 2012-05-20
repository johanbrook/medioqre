package model.character;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import org.json.JSONObject;

import model.CollidableObject;

import event.Event;
import event.Event.Property;
import event.EventBus;

/**
 *	An enemy.
 *
 *	@author Johan
 */
public class Enemy extends AbstractCharacter {
	private boolean wasPushed;
	
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
		this.wasPushed = false;
	}
	
	public Enemy(JSONObject obj) {
		super(obj);
	}

	/**
	 * Called when this enemy should push another enemy.
	 * 
	 * <p>Sends a <code>DID_MOVE</code> event with the value <code>this</code></p>.
	 * 
	 * @param e The other enemy
	 */
	public void push(Enemy e){
		if (!this.isMoving()){
			Point currPos = e.getPosition();

			int x = (int) Math.signum((this.getDirection().getXRatio()));
			int y = (int) Math.signum((this.getDirection().getYRatio()));

			e.setPosition(currPos.x + x, currPos.y + y);
			EventBus.INSTANCE.publish(new Event(Property.DID_MOVE, e));
			e.setPushed(true);
		}
	}
	
	/**
	 * Specify whether or not this unit has recently been pushed away.
	 * @param value
	 */
	public void setPushed(boolean value){
		this.wasPushed = value;
	}
	
	/**
	 * get whether or not this unit was pushed away recently. 
	 * @return True if this unit was recently pushed.
	 */
	public boolean wasPushed(){
		return this.wasPushed;
	}

	@Override
	public void didCollide(CollidableObject w) {

		// Make enemies push each other if they collide
		if (w instanceof Enemy) {
			((Enemy) w).push(this);
		}
	}

}
