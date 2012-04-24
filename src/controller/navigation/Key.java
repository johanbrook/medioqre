/**
*	Key.java
*
*	@author Johan
*/

package controller.navigation;

import model.character.Player;
import constants.Direction;

interface Callable {
	public void action();
}

public class Key {
	private String key;
	protected Callable callback;
	
	public Key(String name, Callable callback) {
		this.key = name;
		this.callback = callback;		
	}
	
	public void fire() {
		if(callback != null)
			callback.action();
	}
	
	public String getKey() {
		return this.key;
	}
	
	@Override
	public String toString() {
		return this.key;
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o) {
			return true;
		}
		if(o == null || getClass() != o.getClass()){
			return false;
		}
		
		Key other = (Key) o;
		return this.key.equals(other.key);
	}
}


class NavigationKey extends Key {

	private Direction direction;
	
	public NavigationKey(String name, Callable callback, Direction dir) {
		super(name, callback);
		this.direction = dir;
	}
	
	
	public void fire(Player target) {
		super.fire();
		target.start();
		target.setDirection(this.direction);
	}
	
	public Direction getDirection() {
		return this.direction;
	}
	
	@Override
	public boolean equals(Object o) {
		return super.equals(o) && this.direction == ((NavigationKey) o).direction; 
	}
}
