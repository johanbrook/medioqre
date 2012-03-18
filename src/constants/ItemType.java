/**
*	ItemTypes.java
*
*	@author Johan
*/

package constants;

public enum ItemType {
	MEDPACK(0.3), AMMOCRATE(0.3);
	
	private final double value;
	
	ItemType(final double value) {
		this.value = value;
	}
	
	public double getValue(){
		return this.value;
	}
}
