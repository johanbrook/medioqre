package controller.AI;

import java.util.ArrayList;
import java.util.List;

public class AStarTile {

	private double g,f,h;

	private boolean open, closed;

	private boolean solid;
	private int row, column;

	//The parent of this AStarTile is the tile before this one in the currently considered path.
	private AStarTile parent;
	// The neighbors are simply the AStarTile adjacent to this one. Won't keep walls or tiles that cannot be reached with one "move" as neighbors.
	private List<AStarTile> neighbors;

	public AStarTile(int row, int column) {
		this.row = row;
		this.column = column;
		open = false;
		closed = false;
		this.neighbors = new ArrayList<AStarTile>();
	}


	/**
	 * get the row of this AStarTile
	 * @return the row of this AStarTile
	 */
	public int getRow() {
		return this.row;
	}

	/**
	 * get the column of this AStarTile
	 * @return the column of this AStarTile
	 */
	public int getColumn() {
		return this.column;
	}

	/**
	 * get the H-value
	 * @return return the H-value of this AStarTile
	 */
	public double getH() {
		return this.h;
	}

	/**
	 * get the F-value of this AStarTile
	 * @return the F-value of this AStarTile
	 */
	public double getF() {
		return this.h;
	}

	/**
	 * get the G-value of this AStarTile
	 * @return the G-value of this AStarTile
	 */
	public double getG() {
		return this.g;
	}

	/**
	 * get the Parent AStarTile of this AStarTile
	 * @return the Parent AStarTile of this AStarTile
	 */
	public AStarTile getParent() {
		return this.parent;
	}

	/**
	 * 
	 * @return true if this AStarTile is set as a open candidate for consideration
	 */
	public boolean isOpen() {
		return this.open;
	}

	/**
	 * 
	 * @return true if this tile has been checked/don't need to be checked
	 */
	public boolean isClosed() {
		return this.closed;
	}

	/**
	 * 
	 * @return true if this AStarTile represents a solid
	 */
	public boolean isSolid() {
		return this.solid;
	}

	/**
	 * Specify whether this AStarTile is to be considered as a candidate for the goal.
	 * @param value
	 */
	public void setOpen(boolean value) {
		this.open = value;
	}

	
	public void setClosed(boolean value) {
		this.closed = value;
	}

	/**
	 * Set a AStarTile as a parent to this AStarTile
	 * @param parent
	 */
	public void setParent(AStarTile parent) {
		this.parent = parent;
	}


	/**
	 * Set the value of G. Will update the value of F, since it's dependant on both G and H, and H is constant throughout entire pathfinding-process. The G-value represents the length between
	 * the starting point of pathfinding and this tile. 
	 * @param g
	 */
	public void setG(double g) {
		this.g = g;
		if (this.h > 0) {
			//Updates the value of F, since F is dependent on the constant value of h and G.
			updateF();
		}
	}

	/**
	 * Set the H-value of this tile. The value simply represents a "guess" of the distance between this tile and the specified tile (usually the player). That is, how long the path would be from
	 * this tile, if there were no walls in the gameboard. Not needed for pathfinding, but will increase the speed.
	 * @param stop
	 */
	public void setH(AStarTile stop) {
		this.h = (Math.abs(this.row - stop.getRow()) + Math.abs(this.column- stop.getColumn()));
	}

	/**
	 * Update this tiles F-value. The F-value is simply G+H, so no parameter is needed.
	 */
	public void updateF() {
		this.f = this.h + this.g;
	}

	/**
	 * Specify whether this tile is to be counted as a solid or not.
	 * @param value
	 */
	public void setSolid(boolean value) {
		this.solid = value;
	}

	/**
	 * get the list of neighbors of this AStarTile
	 * @return
	 */
	public List<AStarTile> getNeighbors() {
		return neighbors;
	}

	/**
	 * Adds a specified tile to this tiles list of neighbors.
	 * @param tile to be added
	 */
	public void addNeighbor(AStarTile tile) {
		neighbors.add(tile);
	}

	/**
	 *  Checks whether this tile is diagonal to the specified tile.
	 * @param tile
	 * @return 
	 */
	public boolean isDiagonal(AStarTile tile) {
		return (this.getRow() != tile.getRow() && this.getColumn() != tile
				.getColumn());
	}

}
