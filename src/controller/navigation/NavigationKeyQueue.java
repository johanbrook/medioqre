package controller.navigation;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


/**
 * A unique list with a maximum size.
 * 
 * @author Johan
 */
public class NavigationKeyQueue implements Collection<NavigationKey> {
	
	private List<NavigationKey> list;
	private final int maxSize;
	
	/**
	 * Create a new navigation queue with a max size, which is the number of keys which
	 * can be pressed simultaneously.
	 * 
	 * @param maxSize The maximum number of keys
	 */
	public NavigationKeyQueue(int maxSize) {
		this.list = new LinkedList<NavigationKey>();
		this.maxSize = maxSize;
	}
	
	/**
	 * Add a navigation key.
	 * 
	 *  @param key The navigation key
	 *  @return True if the key doesn't exist, and if there's room for more keys in the list
	 *  @pre size() < maxSize
	 *  @pre contains(key) == false
	 */
	public boolean add(NavigationKey key) {
		System.out.println("List size: "+this.list.size());
		System.out.println("Max: "+maxSize);
		
		if(this.list.size() < this.maxSize && !this.list.contains(key)) {
			return this.list.add(key);
		}
		
		return false;
	}
	
	/**
	 * Get a key from the index.
	 * 
	 * @param index The index
	 * @return A navigation key
	 * @throws IndexOutOfBoundsException If the index is out of bounds
	 */
	public NavigationKey get(int index) throws IndexOutOfBoundsException {
		return this.list.get(index);
	}
	
	/**
	 * Get the first key in the list.
	 * 
	 * @return The first navigation key.
	 */
	public NavigationKey first() throws IndexOutOfBoundsException {
		return this.list.get(0);
	}
	
	/**
	 * Get the last key in the list.
	 * 
	 * @return The last navigation key
	 */
	public NavigationKey last() throws IndexOutOfBoundsException {
		return this.list.get(this.list.size()-1);
	}
	
	@Override
	public boolean addAll(Collection<? extends NavigationKey> c) {
		return this.list.addAll(c);
	}

	@Override
	public void clear() {
		this.list.clear();
	}

	@Override
	public boolean contains(Object o) {
		return this.list.contains(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return this.list.containsAll(c);
	}

	@Override
	public boolean isEmpty() {
		return this.list.isEmpty();
	}

	@Override
	public Iterator<NavigationKey> iterator() {
		return this.list.iterator();
	}

	@Override
	public boolean remove(Object o) {
		return this.list.remove(o);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return this.list.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return this.list.retainAll(c);
	}

	@Override
	public int size() {
		return this.list.size();
	}

	@Override
	public Object[] toArray() {
		return this.list.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return this.list.toArray(a);
	}
	
	@Override
	public String toString() {
		return this.list.toString();
	}

}
