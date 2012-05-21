package graphics.opengl.core;

/**
 * A light weight size class.
 * 
 * @author John Barbero Unenge
 * 
 */
public class Size {

	private float width;
	private float height;

	/**
	 * Creates a size with the given width and height.
	 * 
	 * The values have to be floating point values.
	 * 
	 * @param width The width
	 * @param height The height
	 */
	public Size(float width, float height) {
		this.width = width;
		this.height = height;
	}

	// Setters
	/**
	 * Set the width of the Size.
	 * 
	 * @param width The width
	 */
	public void setWidth(float width) {
		this.width = width;
	}
	
	/**
	 * Set the height of the Size.
	 * 
	 * @param height The height
	 */
	public void setHeight(float height) {
		this.height = height;
	}

	// Getters
	/**
	 * Get the width of the Size.
	 * 
	 * @return The width
	 */
	public float getWidth() {
		return this.width;
	}
	
	/**
	 * Get the height of the Size.
	 * 
	 * @return The height
	 */
	public float getHeight() {
		return this.height;
	}

	public String toString() {
		return ("WxH: " + this.getWidth() + "x" + this.getHeight());
	}

}
