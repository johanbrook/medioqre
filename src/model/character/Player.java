/**
*	Player.java
*
*	@author Johan
*/

package model.character;


import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import model.weapon.*;


public class Player extends AbstractCharacter {
	
	private List<AbstractWeapon> weaponbelt;
	
	public Player(){
		super(30, new Rectangle(16, 16), new Dimension(20,20), 0, 16);

		AbstractWeapon[] temp = {new MachineGun(this), new Sword(this), new Grenade(this)};
		this.weaponbelt = new ArrayList<AbstractWeapon>(Arrays.asList(temp));

		setCurrentWeapon(MachineGun.class);
		this.getCurrentWeapon().addAmmo(-this.getCurrentWeapon().getCurrentAmmo()+1);
		System.out.println(this.getCurrentWeapon().getCurrentAmmo());
	}
	
	public void setCurrentWeapon(int slot) {
		setCurrentWeapon(this.weaponbelt.get(slot));
	}
	
	public void setCurrentWeapon(Class<? extends AbstractWeapon> type) {
		setCurrentWeapon(getWeaponFromBelt(type));
	}
	
	/**
	 * Get the weapon of a certain type from the belt.
	 * 
	 * @param type A weapon type (the weapon's class)
	 * @return The first weapon in the belt with the specified type
	 */
	private AbstractWeapon getWeaponFromBelt(Class<? extends AbstractWeapon> type) {
		
		for(AbstractWeapon w : this.weaponbelt) {
			if(w.getClass() == type) {
				return w;
			}
		}
		
		return null;
	}
}
