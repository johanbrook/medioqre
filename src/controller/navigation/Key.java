/**
*	Key.java
*
*	@author Johan
*/

package controller.navigation;

interface Callable {
	public void action();
}

public class Key {
	private String key;
	private Callable callback;
	
	public Key(String name, Callable callback) {
		this.key = name;
		this.callback = callback;
		
		System.out.println("*** NEW KEY ***");
	}
	
	public void fire() {
		System.out.println("***** FIRED ******");
		callback.action();
	}
	
	public String getKey() {
		return this.key;
	}
	
	@Override
	public String toString() {
		return this.key;
	}
}
