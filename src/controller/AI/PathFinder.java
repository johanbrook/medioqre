package controller.ai;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * Class capable of finding a path between different spots on a matrice of
 * tiles. Keeps a logic representation of that matrice using AStarTiles.
 * 
 * @author jesperpersson
 * 
 */
public class PathFinder {

	private List<AStarTile> openList;
	private AStarTile[][] logicList;
	private AStarTile currentTile;
	// Using a approx value of sqrt(2) for diagonalcost, in order to save time.
	private final double DIAGONALCOST = 1.41421356;
	private int rows, columns;

	public PathFinder(int rows, int columns) {
		this.columns = columns;
		this.rows = rows;
		initTiles();
	}

	private void initTiles() {
		logicList = new AStarTile[rows][columns];
		for (int i = 0; i < this.rows; i++) {
			for (int l = 0; l < this.columns; l++) {
				logicList[i][l] = new AStarTile(i, l);
			}
		}
		generateNeighbors();
	}

	/**
	 * Given a boolean-representation of the gameboard, where every wall is set
	 * as True, will update the logicList accordingly. Will make sure no path
	 * can go through wall-tiles.
	 * 
	 * @param gameBoard
	 */
	public void initWalls(boolean[][] gameBoard) {
		for (int i = 0; i < this.rows; i++) {
			for (int l = 0; l < this.columns; l++) {
				if (gameBoard[i][l]) {
					this.logicList[i][l].setSolid(true);
				}
			}
		}
	}

	/**
	 * Main algorithm. Given a start and a goal, will return the shortest path
	 * between the two. If no path is to be found, the return will be a
	 * null-object
	 * 
	 * @param Startpoint
	 *            , Endpoint
	 * @return SmartButton [][] Shortest path between Start and Stop.
	 * @return null if no path exists between Start and Stop
	 */
	public List<Point> getPath(Point startPoint, Point endPoint) {
		AStarTile start = logicList[startPoint.x][startPoint.y];
		AStarTile stop = logicList[endPoint.x][endPoint.y];
		for (int i = 0; i < rows; i++) {
			for (int l = 0; l < columns; l++) {
				logicList[i][l].setH(stop);
			}
			openList = new ArrayList<AStarTile>();
			ArrayList<AStarTile> path = new ArrayList<AStarTile>();
			openList.add(start);

			while (!openList.isEmpty()) {
				// Start by finding the tile in the open list with the best
				// F-value
				currentTile = openList.get(findBestTile());

				// Algorithm complete and path found.
				if (currentTile.equals(stop)) {
					path.add(currentTile);
					while (currentTile != start) {
						path.add(currentTile.getParent());
						currentTile = currentTile.getParent();
					}
					clear();
					return convertPath(path);

					// The goal was not found in the openList
				} else {
					updateNeighbors(currentTile.getNeighbors());
					currentTile.setClosed(true);
					currentTile.setOpen(false);
					removeFromOpen(currentTile);
				}
			}
		}
		clear();
		return null;
	}// end getPath()

	/**
	 * Converts a list of AStarTiles into a list of Points. Each point will
	 * represent the position of a tile in the matrice.
	 * 
	 * @param path
	 * @return convertedPath
	 */
	private List<Point> convertPath(ArrayList<AStarTile> path) {
		List<Point> convertedPath = new ArrayList<Point>();
		for (int i = 0; i < path.size(); i++) {
			convertedPath.add(new Point(path.get(i).getRow(), path.get(i)
					.getColumn()));
		}
		return convertedPath;
	}

	/* Returns the index of the tile in the open list with the lowest f-value */
	private int findBestTile() {
		int pointer = 0;
		double currentF = openList.get(0).getF();
		for (int i = 0; i < openList.size(); i++) {
			if (openList.get(i).getF() < currentF) {
				pointer = i;
				currentF = openList.get(i).getF();
			}
		}
		return pointer;
	}

	/*
	 Loop through all neighbors of the currently considered tile
	 updating their G-values, and whether they belong to the open list och closed list. 
	The G-value is only updated if the path to the currently considered tile is shorter than any other 
	considered path to the neighbor in question (that is, if the current G-value is higher than the one we
	are calculating through the current tile)
	 */
	private void updateNeighbors(List<AStarTile> currentNeighbors) {

		for (int k = 0; k < currentNeighbors.size(); k++) {

			// if a tile is closed and the current paths g-value would be lower
			// than the tiles old g-value, we update the tiles g-value and sets it to the newly calculated value
			// Should this be the case, the neighbor will set the current tile as its parrent
			if (currentNeighbors.get(k).isClosed()
					&& currentPathIsShorter(currentNeighbors.get(k))) {

				currentNeighbors.get(k).setG(
						currentTile.isDiagonal(currentNeighbors.get(k))
						? currentTile.getG() + DIAGONALCOST
								: currentTile.getG() + 1);

				currentNeighbors.get(k).setParent(currentTile);

				// if a tile is open and the current paths g-value would be
				// lower than its old g-value, we update the tiles g-value and
				// adds currentTile as parent
			} else if (currentNeighbors.get(k).isOpen()
					&& currentPathIsShorter(currentNeighbors.get(k))) {
				currentNeighbors.get(k).setG(
						currentTile.isDiagonal(currentNeighbors.get(k))
						? currentTile.getG() + DIAGONALCOST
								: currentTile.getG() + 1);
				currentNeighbors.get(k).setParent(currentTile);

				// if a tile is neither open nor closed, adds tile to the
				// openList
				// and set the G-value accordingly. Set current tile as parent.
			} else if (!currentNeighbors.get(k).isOpen()
					&& !currentNeighbors.get(k).isClosed()) {

				currentNeighbors.get(k).setOpen(true);
				openList.add(currentNeighbors.get(k));
				currentNeighbors.get(k).setParent(currentTile);
				currentNeighbors.get(k).setG(
						currentTile.isDiagonal(currentNeighbors.get(k))
						? currentTile.getG() + DIAGONALCOST
								: currentTile.getG() + 1);
			}
		}
	}// end updateNeighbors


	/**
	 * 	Given a tile, will return whether or not the current path from the
	start to the tile is shorter than the currently recorded.
	 * @param tile
	 * @return
	 */
	private boolean currentPathIsShorter(AStarTile tile) {
		return tile.getG() > (currentTile.isDiagonal(tile) ? currentTile.getG()
				+ DIAGONALCOST : currentTile.getG() + 1);
	}

	/**
	 * Loops through gameboard and make sure every button calculates its neighbors, and 
	 * adds them to their list of neighbors.
	 */
	private void generateNeighbors() {
		for (int i = 0; i < logicList.length; i++) {
			for (int l = 0; l < logicList[i].length; l++) {
				if (logicList[i][l].isSolid() == false) {
					calculateNeighbors(logicList[i][l]);
				}
			}
		}
	}


	/**
	 *Tells the specified button to calculate and add its neighbors. Only adds non-solids.
	 * Calls the method isRelevant (AStarTile tile, AStarTile considered) to find out whether
	 * or not the considered neighbor is a tile that is adjecent to this one, with a wall
	 * in between them. If that would be the case, no path between the two would ever be possible,
	 * and the tile is not added.
	 * @param tile
	 */
	private void calculateNeighbors(AStarTile tile) {
		int top = tile.getColumn() + 1;
		int buttom = tile.getColumn() - 1;
		int right = tile.getRow() + 1;
		int left = tile.getRow() - 1;

		if (top < columns) {
			if (isRelevant(tile, logicList[tile.getRow()][top])) {
				tile.addNeighbor(logicList[tile.getRow()][top]);
			}

			if (right < rows) {
				if (isRelevant(tile, logicList[right][top])) {
					tile.addNeighbor(logicList[right][top]);
				}
			}
			if (left >= 0) {
				if (isRelevant(tile, logicList[left][top])) {
					tile.addNeighbor(logicList[left][top]);
				}
			}
		}
		if (buttom >= 0) {
			if (isRelevant(tile, logicList[tile.getRow()][buttom])) {
				tile.addNeighbor(logicList[tile.getRow()][buttom]);
			}

			if (right < rows) {
				if (isRelevant(tile, logicList[right][buttom])) {
					tile.addNeighbor(logicList[right][buttom]);
				}
			}

			if (left >= 0) {
				if (isRelevant(tile, logicList[left][buttom])) {
					tile.addNeighbor(logicList[left][buttom]);
				}
			}
		}
		if (left >= 0) {
			if (isRelevant(tile, logicList[left][tile.getColumn()])) {
				tile.addNeighbor(logicList[left][tile.getColumn()]);
			}
		}
		if (right < rows) {
			if (isRelevant(tile, logicList[right][tile.getColumn()])) {
				tile.addNeighbor(logicList[right][tile.getColumn()]);
			}
		}
	}

	/*
	 * Checks if a certain tile should be added to the current Tiles list of
	 * neighbors
	 * 
	 * @return True if the considered tile is not a solid, and the path between
	 * the two tiles will not go through a wall.
	 */
	private boolean isRelevant(AStarTile currentTile, AStarTile consideredTile) {
		if (consideredTile.isSolid()) {
			return false;
		} else {
			int dx = consideredTile.getRow() - currentTile.getRow();
			int dy = consideredTile.getColumn() - currentTile.getColumn();
			return (!logicList[currentTile.getRow() + dx][currentTile
			                                              .getColumn()].isSolid() && !logicList[currentTile.getRow()][currentTile
			                                                                                                          .getColumn() + dy].isSolid());
		}
	}

	/**
	 * Removes a SmartButton from the open list
	 * @param tile to be removed
	 */
	private void removeFromOpen(AStarTile tile) {
		for (int i = 0; i < openList.size(); i++) {
			if (openList.get(i) == tile) {
				openList.remove(i);
			}
		}
	}

	/**
	 * Removes any closed/open-status of each tile in the logicList, 
	 * sets the G-value to 0 in every tile. Clears the open list
	 */
	public void clear() {
		for (int i = 0; i < logicList.length; i++) {
			for (int l = 0; l < logicList[i].length; l++) {
				logicList[i][l].setOpen(false);
				logicList[i][l].setClosed(false);
				logicList[i][l].setG(0);
			}
		}
		clearOpen();
	}


	/**
	 * Clears the open list.
	 */
	private void clearOpen() {
		openList.clear();
	}

}
