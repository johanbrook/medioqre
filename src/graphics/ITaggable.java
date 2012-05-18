/**
*	ITaggable.java
*
*	@author Johan
*/

package graphics;

public interface ITaggable {

	/**
	 * Get this object's bit tag.
	 * 
	 * @return The tag
	 */
	public int getTag();
	
	/**
	 * Set this object's tag (a bit on a certain position).
	 * 
	 * @param bit The bit
	 * @param position The desired position
	 */
	public void setBit(int bit, int position);
}
