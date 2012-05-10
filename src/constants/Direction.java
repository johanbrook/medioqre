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

	Direction(final double xratio, final double yratio) {
		this.xratio = xratio;
		this.yratio = yratio;
	}

	public double getXRatio() {
		return this.xratio;
	}

	public double getYRatio() {
		return this.yratio;
	}

}
