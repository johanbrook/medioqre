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
	
	public enum WeaponTypes {
		MACHINEGUN(0), SWORD(1), GRENADE(2), PORTALGUN(3);
		
		private int index;
		WeaponTypes(int i) {
			this.index = i;
		}
		
		public int getIndex() {
			return this.index;
		}
	}
	
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
	
	
	public IWeapon getWeapon(WeaponTypes type) {
		return this.getWeapon(type.getIndex());
	}
	
}
