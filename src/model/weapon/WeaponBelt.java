package model.weapon;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * The weapon belt class
 * 
 * @author Johan
 *
 */
public class WeaponBelt {
	private List<IWeapon> weapons;
	
	/**
	 * Create a new weapon belt with the four standard weapons (machine gun, grenade,
	 * sword and portal gun).
	 */
	public WeaponBelt() {
		IWeapon[] temp = {new MachineGun(), new Sword(), new Grenade(), new PortalGun()};
		
		this.weapons = new LinkedList<IWeapon>(Arrays.asList(temp));
	}
	
	/**
	 * Create a new weapon belt from a collection of weapons.
	 * 
	 * @param coll The collection.
	 */
	public WeaponBelt(Collection<? extends IWeapon> coll){
		this.weapons = new LinkedList<IWeapon>(coll);
	}
	
	/**
	 * Add a weapon to the belt.
	 * 
	 * @param w The weapon
	 */
	public void addWeapon(IWeapon w) {
		this.weapons.add(w);
	}
	
	/**
	 * Get the weapon in the belt from a certain slot.
	 * 
	 * @param slot The slot
	 * @return The weapon on that slot
	 */
	public IWeapon getWeapon(int slot) {
		return this.weapons.get(slot);
	}
	
	/**
	 * Get the weapon of a certain type from the belt.
	 * 
	 * @param type A weapon type (the weapon's class)
	 * @return The first weapon in the belt with the specified type
	 */
	public IWeapon getWeapon(Class<? extends IWeapon> type) {
		
		for(IWeapon w : this.weapons) {
			if(w.getClass() == type) {
				return w;
			}
		}
		
		return null;
	}
	
	
}