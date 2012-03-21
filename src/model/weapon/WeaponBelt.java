/**
*	WeaponBelt.java
*
*	@author Johan
*/

package model.weapon;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class WeaponBelt {
	private List<IWeapon> weapons;
	
	public WeaponBelt() {
		IWeapon[] temp = {new MachineGun(), new Sword(), new Grenade(), new PortalGun()};
		
		this.weapons = new LinkedList<IWeapon>(Arrays.asList(temp));
	}
	
	public WeaponBelt(Collection<? extends IWeapon> coll){
		this.weapons = new LinkedList<IWeapon>(coll);
	}
	
	public void addWeapon(IWeapon w) {
		this.weapons.add(w);
	}
	
	public IWeapon getWeapon(int slot) {
		return this.weapons.get(slot);
	}
	
	public IWeapon getWeapon(Class<? extends IWeapon> type) {
		
		for(IWeapon w : this.weapons) {
			if(w.getClass() == type) {
				return w;
			}
		}
		
		return null;
	}
	
	
}
