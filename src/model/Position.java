package model;

/**
 * Position
 * 
 * @author Johan
 * @deprecated Using java.awt.Point instead (2012-03-29)
 */
public class Position {

	private final int x;
	private final int y;

	public Position(final int x, final int y) {
		this.x = x;
		this.y = y;
	}

	public Position(final Position p) {
		this.x = p.x;
		this.y = p.y;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	@Override
	public String toString() {
		return "[" + this.x + ", " + this.y + "]";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}

		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		Position other = (Position) o;

		return this.x == other.x && this.y == other.y;
	}
	
	@Override
	public int hashCode() {
		int hash = 1;
		hash = hash * 13 + this.x;
		hash = hash * 17 + this.y;
		
		return hash;
	}

}
