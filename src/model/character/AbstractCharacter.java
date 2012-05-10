package model.character;

import java.awt.Dimension;
import java.awt.Rectangle;
import static tools.Logger.log;

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
	private int maxHealth;
	private AbstractWeapon currentWeapon;
	
	/**
	 * A game character with 100 health points.
	 * 
	 * @param movementSpeed The movement speed
	 * @param collBox The collision box
	 * @param size The size
	 * @param xoffset The x offset
	 * @param yoffset The y offset
	 */
	public AbstractCharacter(int movementSpeed, Rectangle collBox, Dimension size, int xoffset, int yoffset) {
		super(collBox, size, xoffset, yoffset, movementSpeed);
		this.health = this.maxHealth = 100;
		this.setDirection(Direction.SOUTH);
	}
	
	/**
	 * Set the current weapon.
	 * 
	 * <p>Sends a <code>CHANGED_WEAPON</code> event with the value <code>this</code></p>.
	 * 
	 * @param w The weapon
	 */
	public void setCurrentWeapon(AbstractWeapon w) {
		this.currentWeapon = w;
		EventBus.INSTANCE.publish(new Event(Property.CHANGED_WEAPON, this));
		log("Current weapon is " + getCurrentWeapon());
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
	 * <p>If the character's health points goes below 0, <code>destroy()</code>
	 * will be called and the entity is killed.</p>
	 * 
	 * <p>Sends a <code>WAS_DAMAGED</code> event with the value <code>this</code></p>.
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
	 * Get the character's current health
	 * 
	 * @return The health
	 */
	public int getHealth() {
		return this.health;
	}
	
	/**
	 * Add health.
	 * 
	 * <p>Will never make the health higher than the original max health. If the health and the amount
	 * specified are together higher than the max health, the new health will be the max health.</p>
	 * 
	 * @param amount The amount
	 */
	public void addHealth(int amount) {
		if(this.health < this.maxHealth){
			if(this.health + amount > this.maxHealth) {
				this.health = this.maxHealth;
			}
			else {
				this.health += amount;
			}
		}
	}
	
	//TODO Why update? Not move?
	public void update (double dt){
		super.move(dt);
		this.currentWeapon.updateCooldown(dt);
	}
	
	/**
	 * Set the health. Bypasses the maximum health, i.e. you're able to set
	 * the health higher than the maximum health.
	 * 
	 * @param amount The new health
	 */
	public void setHealth(int amount) {
		this.health = amount;
	}
	
	/**
	 * Get the maximum, or starting, health for this character as it was set on init.
	 * 
	 * @return The starting health
	 */
	public int getMaxHealth() {
		return this.maxHealth;
	}
	
	/**
	 * Tell the character to attack a target (another Character)
	 * 
	 * <p>Sends a <code>DID_ATTACK</code> event with the value <code>this</code></p>.
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