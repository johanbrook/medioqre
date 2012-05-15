package core;

/**
 * A light weight size class.
 * 
 * @author John Barbero Unenge
 * 
 */
public class Size {

	private float width;
	private float height;

	public Size(float width, float height) {
		this.width = width;
		this.height = height;
	}

	// Setters
	public void setWidth(float width) {
		this.width = width;
	}
	public void setHeight(float height) {
		this.height = height;
	}

	// Getters
	public float getWidth() {
		return this.width;
	}
	public float getHeight() {
		return this.height;
	}
	public String toString() {
		return ("WxH: " + this.getWidth() + "x" + this.getHeight());
	}

}
