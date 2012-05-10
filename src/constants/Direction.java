package constants;

/**
 * An enum describing directions.
 * 
 * @author Johan
 * 
 */
public enum Direction {
	NORTH(0, -1), EAST(1, 0), SOUTH(0, 1), WEST(-1, 0),

	NORTH_EAST(0.7071, -0.7071), SOUTH_EAST(0.7071, 0.7071), NORTH_WEST(
			-0.7071, -0.7071), SOUTH_WEST(-0.7071, 0.7071),

	ORIGIN(0, 0);

	private final double xratio;
	private final double yratio;
	
	/**
	 * Define a direction based on ratios of the unit circle.
	 * 
	 * <p>For instance, "north" may be defined with <code>(0, -1)</code>. Origo (0,0) is in the
	 * upper left corder.</p>
	 * 
	 * @param xratio The x ratio
	 * @param yratio The y ratio
	 */
	Direction(final double xratio, final double yratio){
		this.xratio = xratio;
		this.yratio = yratio;
	}
	
	/**
	 * Get the X ratio
	 * 
	 * @return The x ratio as an integer
	 */
	public double getXRatio(){
		return this.xratio;
	}
	
	/**
	 * Get the Y ratio.
	 * 
	 * @return The y ratio as an integer
	 */
	public double getYRatio(){
		return this.yratio;
	}

}
