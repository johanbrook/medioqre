package controller.ai;


import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import tools.factory.ObjectFactory;


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
	private AStarTile currentTile, stop;
	// Using a approximation of the square root of 2 for diagonalcost, in order to save time.
	private final double DIAGONALCOST = 1.41421356;

	// This cost is added to the G-value (path-length) of a tile, if the current path would change direction to reach it.
	// The cost is small enough that it won't chose a longer path over a shorter, but will ensure that out of a
	// number of paths of the same length, the one with the fewest turns will be chosen.
	private final double TURNCOST = 0.0002;

	private int rows, columns;
	private boolean initiated;

	public PathFinder(int rows, int columns) {
		this.columns = columns;
		this.rows = rows;
		this.setInitiated(false);
	}

	/**
	 * Initiate the pathfinder, constructing AStarTiles for representing the gameboard state. 
	 * Using a boolean [] [] from the object factory, the AStarTiles representing walls will 
	 * be set to solid. Also makes sure that tiles generates neighbors.
	 */
	public void init() {
		boolean [][] walls = ObjectFactory.getCollidables();
		logicList = new AStarTile[rows][columns];
		for (int i = 0; i < this.rows; i++) {
			for (int l = 0; l < this.columns; l++) {
				logicList[i][l] = new AStarTile(i, l);
				if (walls[i][l]){
					logicList[i][l].setSolid(true);
				}
			}
		}
		generateNeighbors();
		this.setInitiated(true);
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
		this.stop = logicList[endPoint.x][endPoint.y];

		openList = new ArrayList<AStarTile>();
		ArrayList<AStarTile> path = new ArrayList<AStarTile>();
		openList.add(start);
		start.setH(stop);

		while (!openList.isEmpty()) {
			// Start by finding the tile in the open list with the best
			// F-value
			currentTile = findBestTile();

			// Algorithm complete and path found.
			if (currentTile.equals(stop)) {
				path.add(currentTile);
				while (currentTile != start) {
					path.add(currentTile.getParent());
					currentTile = currentTile.getParent();
				}
				clear();
				// The path is saved from player to enemy, but we want to return the path from enemy to player, hence, we reverse the order of the tiles saved
				ArrayList <AStarTile> reversePath = new ArrayList<AStarTile>();
				for (int k = 0; k <= path.size()-1; k++){
					reversePath.add(path.get((path.size()-1)-k));
				}
				
				// Convert path from AStarTiles to points, each point representing a tile in the gameworld.
				return convertPath(reversePath);

				// The goal was not found in the openList
			} else {
				updateNeighbors(currentTile.getNeighbors());
				currentTile.setClosed(true);
				currentTile.setOpen(false);
				removeFromOpen(currentTile);
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

	/**
	 *  Returns the tile in the open list most likely to be the goal (the one with the lowest f-value) 
	*/
	private AStarTile findBestTile() {
		int pointer = 0;
		double currentF = openList.get(0).getF();
		for (int i = 0; i < openList.size(); i++) {
			if (openList.get(i).getF() < currentF) {
				pointer = i;
				currentF = openList.get(i).getF();
			}
		}
		return openList.get(pointer);
	}

	/**
	 * For each neighbor in the current, will update it's open/closed-status, as well as it's
	 * G-value, depending on the current path as well as the internal state of the tile.
	 * @param currentNeighbors
	 */
	private void updateNeighbors(List<AStarTile> currentNeighbors) {

		for (int k = 0; k < currentNeighbors.size(); k++) {


			// if a tile is open and the current paths g-value would be
			// lower than its old g-value, we update the tiles g-value and
			// adds currentTile as parent
			if (currentNeighbors.get(k).isOpen()
					&& currentPathIsShorter(currentNeighbors.get(k))) {

				currentNeighbors.get(k).setG(currentTile.isDiagonal(currentNeighbors.get(k)) ?
						currentTile.getG() + DIAGONALCOST :
							currentTile.getG() + 1);

				currentNeighbors.get(k).setParent(currentTile);

				// If the current path would have to change direction in order to reach the specific tile, add a small cost.
				// The cost is 

				if (willTurn(currentNeighbors.get(k))){
					currentNeighbors.get(k).setG(currentNeighbors.get(k).getG() + TURNCOST);
				}

				// if a tile is neither open nor closed, adds tile to the
				// openList
				// and set the G-value accordingly. Set current tile as parent.
			} else if (!currentNeighbors.get(k).isOpen()
					&& !currentNeighbors.get(k).isClosed()) {

				currentNeighbors.get(k).setOpen(true);
				currentNeighbors.get(k).setH(this.stop);
				openList.add(currentNeighbors.get(k));

				currentNeighbors.get(k).setParent(currentTile);
				currentNeighbors.get(k).setG(currentTile.isDiagonal(currentNeighbors.get(k)) ?
						currentTile.getG() + DIAGONALCOST :
							currentTile.getG() + 1);

				// If the current path would have to change direction in order to reach the specific tile, add a small cost.
				// The cost is 

				if (willTurn(currentNeighbors.get(k))){
					currentNeighbors.get(k).setG(currentNeighbors.get(k).getG() + TURNCOST);
				}
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

	/**
	 * Checks if a certain tile should be added to the current Tiles list of
	 * neighbors
	 * @param currentTile
	 * @param consideredTile
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
	 * Check if the current path would have to change direction in order to reach the considered tile.
	 * @param consideredTile
	 * @return true if the path would have to change direction in order to reach the considered tile.
	 */
	private boolean willTurn(AStarTile consideredTile){
		if (currentTile.getParent() != null){
			return (currentTile.getParent().getColumn() - currentTile.getColumn() != currentTile.getColumn() - consideredTile.getColumn() 
					|| currentTile.getParent().getRow() - currentTile.getRow() != currentTile.getRow() - consideredTile.getRow());
		}
		return false;
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
	 * Removes any closed/open-status of each tile in the logicList and
	 * clears the open list
	 */
	public void clear() {
		for (int i = 0; i < logicList.length; i++) {
			for (int l = 0; l < logicList[i].length; l++) {
				logicList[i][l].setOpen(false);
				logicList[i][l].setClosed(false);
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

	/**
	 * get the status of this PathFinder, if its initiated and ready to use.
	 * @return true if ready to use.
	 */
	public boolean isInitiated() {
		return initiated;
	}

	/**
	 * Specify whether this PathFinder is initiated and ready to use.
	 * @param initiated
	 */
	public void setInitiated(boolean initiated) {
		this.initiated = initiated;
	}

}
