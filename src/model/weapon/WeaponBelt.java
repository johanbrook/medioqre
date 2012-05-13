package model.weapon;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * A weapon belt with weapons.
 * 
 * @author Johan
 */
public class WeaponBelt implements Iterable<AbstractWeapon> {
	private List<AbstractWeapon> weapons;

	/**
	 * Create a new empty weapon belt.
	 */
	public WeaponBelt() {
		this.weapons = new LinkedList<AbstractWeapon>();
	}
	
	/**
	 * Create a new weapon belt from a collection of weapons.
	 * 
	 * @param coll
	 *            The collection.
	 */
	public WeaponBelt(Collection<? extends AbstractWeapon> coll) {
		this.weapons = new LinkedList<AbstractWeapon>(coll);
	}

	/**
	 * Add a weapon to the belt.
	 * 
	 * @param w The weapon
	 * @return True if the weapon was added successfully. Otherwise false.        
	 */
	public boolean add(AbstractWeapon w) {
		return this.weapons.add(w);
	}

	/**
	 * Get the weapon in the belt from a certain slot.
	 * 
	 * @param slot
	 *            The slot where the weapon is (0 < slot < size())
	 * @return The weapon on that slot
	 * @throws IndexOutOfBoundsException
	 *             If no weapon exists in this slot
	 */
	public AbstractWeapon get(int slot) throws IndexOutOfBoundsException {
		return this.weapons.get(slot);
	}
	
	/**
	 * Return the index of the given weapon type.
	 * 
	 * @param type The type
	 * @return The slot position in the belt for this type
	 */
	public int indexOf(Class<? extends AbstractWeapon> type){
		return this.weapons.indexOf(this.get(type));
	}

	/**
	 * Get the weapon of a certain type from the belt.
	 * 
	 * @param type
	 *            A weapon type (the weapon's class)
	 * @return The first weapon in the belt with the specified type
	 */
	public AbstractWeapon get(Class<? extends AbstractWeapon> type) {

		for (AbstractWeapon w : this.weapons) {
			if (w.getClass() == type) {
				return w;
			}
		}

		return null;
	}

	@Override
	public Iterator<AbstractWeapon> iterator() {
		return this.weapons.iterator();
	}

}
