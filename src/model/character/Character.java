package model.character;

import java.awt.Dimension;
import java.awt.Rectangle;

import constants.Direction;

import event.Event;
import event.EventBus;
import event.Event.Property;

import model.Entity;
import model.weapon.Projectile;


/**
*	Character.
*
*	A character in the game. Could be a player, enemy, etc.
*
*	@author Johan
*/
public abstract class Character extends Entity {

	private int health;
	
	/**
	 * A game character with 100 health points.
	 * 
	 * @param movementSpeed
	 * @param collBox
	 * @param size
	 * @param xoffset
	 * @param yoffset
	 */
	public Character(int movementSpeed, Rectangle collBox, Dimension size, int xoffset, int yoffset) {
		super(collBox, size, xoffset, yoffset, movementSpeed);
		this.health = 100;
		this.setDirection(Direction.SOUTH);
	}
	
	
	/**
	 * Call this method to hurt the character with a certain amount
	 * of damage.
	 * 
	 * If the character's health points goes below 0, <code>destroy()</code>
	 * will be called and the entity is killed.
	 * 
	 * @param dmg The damage
	 */
	public void takeDamage(int dmg){
		this.health -= dmg;
		EventBus.INSTANCE.publish(new Event(Property.WAS_DAMAGED, this));
		if(this.health <= 0)
			this.destroy();
		
	}
	
	/**
	 * Get the character's health
	 * 
	 * @return The health
	 */
	public int getHealth() {
		return this.health;
	}
	
	public void addHealth(int amount) {
		setHealth(getHealth() + amount);
	}
	
	public void setHealth(int amount) {
		this.health = amount;
	}
	
	/**
	 * Tell the character to attack a target (another Character)
	 * 
	 * @param target The target to attack
	 */
	public abstract Projectile attack();
	
	
	@Override
	public String toString() {
		return super.toString() + " [hp:"+this.health+"]";
	}
}