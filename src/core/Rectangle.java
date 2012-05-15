package core;

/**
 * A light weight rectangle class.
 * 
 * @author John Barbero Unenge
 * 
 */
public class Rectangle {

	private float x;
	private float y;
	private Size size;

	/**
	 * Creates a rectangle.
	 * 
	 * @param x
	 *            The x position of the rectangle.
	 * @param y
	 *            The y position of the rectangle.
	 * @param width
	 *            The width of the rectangle.
	 * @param height
	 *            The height of the rectangle.
	 */
	public Rectangle(float x, float y, float width, float height) {
		this.x = x;
		this.y = y;
		this.size = new Size(width, height);
	}

	// Collision
	/**
	 * Checks whether the rectangle r intersects this rectangle.
	 * 
	 * @param r
	 *            The rectangle to check against.
	 * @return True if the rectangle r intersects this.
	 */
	public boolean intersectsRectangle(Rectangle r) {
		if (r == null)
			return false;
		if (!(r instanceof Rectangle))
			return false;

		if (this.x >= r.x + r.size.getWidth())
			return false;
		if (this.x + this.size.getWidth() <= r.x)
			return false;
		if (this.y >= r.y + r.size.getHeight())
			return false;
		if (this.y + this.size.getHeight() <= r.y)
			return false;

		return true;
	}

	// Move
	/**
	 * Moves the rectangle to the provided location.
	 * 
	 * @param x
	 *            The x coordinate.
	 * @param y
	 *            The y coordinate.
	 */
	public void moveTo(float x, float y) {
		this.setPosition(x, y);
	}
	/**
	 * Moves the rectangle by the provided amount.
	 * 
	 * Adds the x amount to the x coordinate and the y coordinate with the y
	 * amount.
	 * 
	 * @param x
	 *            The x coordinate.
	 * @param y
	 *            The y coordinate.
	 */
	public void moveBy(float x, float y) {
		this.setPosition(this.getX() + x, this.getY() + y);
	}

	// Setters
	/**
	 * Set the x coordinate of the rectangle.
	 * 
	 * @param x
	 *            The x coordinate.
	 */
	public void setX(float x) {
		this.x = x;
	}
	/**
	 * Set the y coordinate of the rectangle.
	 * 
	 * @param y
	 *            The y coordinate.
	 */
	public void setY(float y) {
		this.y = y;
	}
	/**
	 * Set the width of the rectangle.
	 * 
	 * @param width
	 *            The width.
	 */
	public void setWidth(float width) {
		this.size.setWidth(width);
	}
	/**
	 * Set the height of the rectangle.
	 * 
	 * @param height
	 *            The height.
	 */
	public void setHeight(float height) {
		this.size.setHeight(height);
	}
	/**
	 * Set the position of the rectangle.
	 * 
	 * @param x
	 *            The x coordinate.
	 * @param y
	 *            The y coordinate.
	 */
	public void setPosition(float x, float y) {
		this.setX(x);
		this.setY(y);
	}
	/**
	 * Set the dimension of the rectangle.
	 * 
	 * @param width
	 *            The width.
	 * @param height
	 *            The height.
	 */
	public void setDimension(float width, float height) {
		this.setWidth(width);
		this.setHeight(height);
	}

	// Getters
	/**
	 * Get the x coordinate of the rectangle.
	 * 
	 * @return The x coordinate.
	 */
	public float getX() {
		return this.x;
	}
	/**
	 * Get the y coordinate of the rectangle.
	 * 
	 * @return The y coordinate.
	 */
	public float getY() {
		return this.y;
	}
	/**
	 * Get the width of the rectangle.
	 * 
	 * @return The width.
	 */
	public float getWidth() {
		return this.size.getWidth();
	}
	/**
	 * Get the height of the rectangle.
	 * 
	 * @return The height.
	 */
	public float getHeight() {
		return this.size.getHeight();
	}

	public String toString() {
		return (this.getWidth() + "x" + this.getHeight() + " at " + this.getX()
				+ "," + this.getY());
	}

}
