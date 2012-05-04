package model.character;

import java.awt.Dimension;
import java.awt.Rectangle;

import constants.Direction;

import event.Event;
import event.EventBus;
import event.Event.Property;

import model.Entity;
import model.weapon.AbstractWeapon;
import model.weapon.Projectile;


/**
*	Character.
*
*	A character in the game. Could be a player, enemy, etc.
*
*	@author Johan
*/
public abstract class AbstractCharacter extends Entity {

	private int health;
	private AbstractWeapon currentWeapon;
	
	/**
	 * A game character with 100 health points.
	 * 
	 * @param movementSpeed
	 * @param collBox
	 * @param size
	 * @param xoffset
	 * @param yoffset
	 */
	public AbstractCharacter(int movementSpeed, Rectangle collBox, Dimension size, int xoffset, int yoffset) {
		super(collBox, size, xoffset, yoffset, movementSpeed);
		this.health = 100;
		this.setDirection(Direction.SOUTH);
	}
	
	/**
	 * Set the current weapon.
	 * 
	 * @param w The weapon
	 */
	public void setCurrentWeapon(AbstractWeapon w) {
		this.currentWeapon = w;
		EventBus.INSTANCE.publish(new Event(Property.CHANGED_WEAPON, this));
	}
	
	/**
	 * Get the current weapon.
	 * 
	 * @return The weapon
	 */
	public AbstractWeapon getCurrentWeapon(){
		return this.currentWeapon;
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
	
	/**
	 * Add health.
	 * 
	 * @param amount The amount
	 */
	public void addHealth(int amount) {
		setHealth(getHealth() + amount);
	}
	
	/**
	 * Set the health.
	 * 
	 * @param amount The new health
	 */
	public void setHealth(int amount) {
		this.health = amount;
	}
	
	/**
	 * Tell the character to attack a target (another Character)
	 * 
	 * @param target The target to attack
	 */
	public Projectile attack() {
		EventBus.INSTANCE.publish(new Event(Property.DID_ATTACK, this));
		return this.currentWeapon.fire();
	}
	
	
	@Override
	public String toString() {
		return super.toString() + " [hp:"+this.health+"]";
	}
}