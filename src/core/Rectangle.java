package core;

public class Rectangle {

	private int x;
	private int y;
	private int width;
	private int height;
	
	/**
	 * Creates a rectangle.
	 * 
	 * @param x The x position of the rectangle.
	 * @param y The y position of the rectangle.
	 * @param width The width of the rectangle.
	 * @param height The height of the rectangle.
	 */
	public Rectangle(int x, int y, int width, int height)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	// Collision
	/**
	 * Checks whether the rectangle r intersects this rectangle.
	 * 
	 * @param r The rectangle to check against.
	 * @return True if the rectangle r intersects this.
	 */
	public boolean intersectsRectangle(Rectangle r)
	{
		if (r == null) return false;
		if (!(r instanceof Rectangle)) return false;
		
		if (this.x >= r.x + r.width) return false;
		if (this.x + this.width <= r.x) return false;
		if (this.y >= r.y + r.height) return false;
		if (this.y + this.height <= r.y) return false;
		
		return true;
	}
	
	
	// Move
	/**
	 * Moves the rectangle to the provided location.
	 *  
	 * @param x The x coordinate.
	 * @param y The y coordinate.
	 */
	public void moveTo(int x, int y)
	{
		this.setPosition(x, y);
	}
	/**
	 * Moves the rectangle by the provided amount.
	 * 
	 * Adds the x amount to the x coordinate and the y coordinate 
	 * with the y amount.
	 * @param x The x coordinate.
	 * @param y The y coordinate.
	 */
	public void moveBy(int x, int y)
	{
		this.setPosition(this.getX() + x, this.getY() + y);
	}
	
	// Setters
	/**
	 * Set the x coordinate of the rectangle.
	 * 
	 * @param x The x coordinate.
	 */
	public void setX(int x)
	{
		this.x = x;
	}
	/**
	 * Set the y coordinate of the rectangle.
	 * 
	 * @param y The y coordinate.
	 */
	public void setY(int y)
	{
		this.y = y;
	}
	/**
	 * Set the width of the rectangle.
	 * 
	 * @param width The width.
	 */
	public void setWidth(int width)
	{
		this.width = width;
	}
	/**
	 * Set the height of the rectangle.
	 * 
	 * @param height The height.
	 */
	public void setHeight(int height)
	{
		this.height = height;
	}
	/**
	 * Set the position of the rectangle.
	 * 
	 * @param x The x coordinate.
	 * @param y The y coordinate.
	 */
	public void setPosition(int x, int y)
	{
		this.setX(x);
		this.setY(y);
	}
	/**
	 * Set the dimension of the rectangle.
	 * 
	 * @param width The width.
	 * @param height The height.
	 */
	public void setDimension(int width, int height)
	{
		this.setWidth(width);
		this.setHeight(height);
	}

	// Getters
	/**
	 * Get the x coordinate of the rectangle.
	 * @return The x coordinate.
	 */
	public int getX()
	{
		return this.x;
	}
	/**
	 * Get the y coordinate of the rectangle.
	 * 
	 * @return The y coordinate.
	 */
	public int getY()
	{
		return this.y;
	}
	/**
	 * Get the width of the rectangle.
	 * 
	 * @return The width.
	 */
	public int getWidth()
	{
		return this.width;
	}
	/**
	 * Get the height of the rectangle.
	 * 
	 * @return The height.
	 */
	public int getHeight()
	{
		return this.height;
	}
	
}
