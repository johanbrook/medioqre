package core;

/**
 * A light weight size class.
 * 
 * @author John Barbero Unenge
 * 
 */
public class Size {

	private int width;
	private int height;

	public Size(int width, int height) {
		this.width = width;
		this.height = height;
	}

	// Setters
	public void setWidth(int width) {
		this.width = width;
	}
	public void setHeight(int height) {
		this.height = height;
	}

	// Getters
	public int getWidth() {
		return this.width;
	}
	public int getHeight() {
		return this.height;
	}
	public String toString() {
		return ("WxH: " + this.getWidth() + "x" + this.getHeight());
	}

}
