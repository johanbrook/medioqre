package controller.AI;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
/**
 * Class capable of finding a path between different spots on a gameboard of tiles. Keeps a logic representation of that gameboard.
 * @author jesperpersson
 *
 */
public class PathFinder {

	private List<AStarTile> openList;
	private AStarTile[][] logicList;
	private AStarTile currentTile;
	//Using a approx value of sqrt(2) for diagonalcost, in order to save time.
	private final double DIAGONALCOST= 1.41421356;
	private int rows,columns;


	public PathFinder( int rows, int columns) {
		this.columns = columns;
		this.rows = rows;
		init();
	}

	private void init() {
		logicList = new AStarTile[rows][columns];
		for (int i = 0; i < this.rows; i++) {
			for (int l = 0; l < this.columns; l++) {
				logicList[i][l] = new AStarTile(i,l);
				//TODO set solid if this place in gameworld is solid
			}
		}
		generateNeighbors();

	}

	/**
	 * Main algorithm. Given a start and a goal, will return the shortest path
	 * between the two. If no path is to be found, the return will be a
	 * null-object
	 * 
	 * @param Startpoint, Endpoint
	 * @return SmartButton [][] Shortest path between Start and Stop.
	 * @return null if no path exists between Start and Stop
	 */
	public List<Point> getPath(Point startPoint,
			Point endPoint) {
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
				//Start by finding the tile in the open list with the best F-value
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
	}

	private List<Point> convertPath(ArrayList<AStarTile> path) {
		List <Point> convertedPath = new ArrayList <Point>();
		for (int i = 0; i < path.size(); i++){
			convertedPath.add(new Point (path.get(i).getRow(),path.get(i).getColumn()));
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
	 * Loop through the given list, and updates depending on its relation to its
	 * adjacent tile, the one currently selected Should a tile be in a closed or
	 * open state, the length of the path between it and the start will be
	 * compared to what it would have been had we measured the current path to
	 * it instead. Should the tile be neither closed nor open, the path to it
	 * will be updated, and it will be added to the open list for consideration
	 */
	private void updateNeighbors(List<AStarTile> currentNeighbors) {
		
		for (int k = 0; k < currentNeighbors.size(); k++) {

			// if a tile is closed and the current paths g-value would be lower
			// than its old g-value, we update the tiles g-value and sets it
			// parent to
			// currentTile
			if (currentNeighbors.get(k).isClosed() && currentPathIsShorter(currentNeighbors.get(k))) {
				
				currentNeighbors.get(k).setG(currentTile.isDiagonal(currentNeighbors.get(k)) ? 
						currentTile.getG() + DIAGONALCOST : currentTile.getG() + 1);
				
				currentNeighbors.get(k).setParent(currentTile);

				// if a tile is open and the current paths g-value would be
				// lower than its old g-value, we update the tiles g-value and
				// sets it parent to
				// currentTile
			} else if (currentNeighbors.get(k).isOpen() && currentPathIsShorter(currentNeighbors.get(k))) {
				currentNeighbors.get(k).setG(currentTile.isDiagonal(currentNeighbors.get(k)) ?
						currentTile.getG() + DIAGONALCOST : currentTile.getG() + 1);
				currentNeighbors.get(k).setParent(currentTile);

				// if a tile is neither open nor closed, adds tile to the openList
				// and set the G-value accordingly.
			} else if (!currentNeighbors.get(k).isOpen() && !currentNeighbors.get(k).isClosed()) {
				
				if (currentNeighbors.get(k).isSolid()) {
					currentNeighbors.get(k).setClosed(true);
					
				} else {
					
					currentNeighbors.get(k).setOpen(true);
					openList.add(currentNeighbors.get(k));
					currentNeighbors.get(k).setParent(currentTile);
					currentNeighbors.get(k).setG(currentTile.isDiagonal(currentNeighbors.get(k)) ? 
							currentTile.getG() + DIAGONALCOST : currentTile.getG() + 1);
				}
			}
		}
	}

	// Given a SmartButton, will return whether or not the current path from the
	// start to the button is shorter than the currently recorded.
	private boolean currentPathIsShorter(AStarTile tile) {
		return tile.getG() > (currentTile.isDiagonal(tile) ? 
				currentTile.getG() + DIAGONALCOST : currentTile.getG() + 1);
	}

	// Loops through gameboard and make sure every button calculates its
	// neighbors
	private void generateNeighbors() {
		for (int i = 0; i < logicList.length; i++) {
			for (int l = 0; l < logicList[i].length; l++) {
				calculateNeighbors(logicList[i][l]);
			}
		}
	}

	// Tells the specified button to calculate and add its neighbors.
	private void calculateNeighbors(AStarTile tile) {
		int top = tile.getColumn() + 1;
		int buttom = tile.getColumn() - 1;
		int right = tile.getRow() + 1;
		int left = tile.getRow() - 1;

		if (top < columns) {
			tile.addNeighbor(logicList[tile.getRow()][top]);
			if (right < rows) {
				tile.addNeighbor(logicList[right][top]);
			}
			if (left >= 0) {
				tile.addNeighbor(logicList[left][top]);
			}
		}
		if (buttom >= 0) {
			tile.addNeighbor(logicList[tile.getRow()][buttom]);
			if (right < rows) {
				tile.addNeighbor(logicList[right][buttom]);
			}
			if (left >= 0) {
				tile.addNeighbor(logicList[left][buttom]);
			}
		}
		if (left >= 0) {
			tile.addNeighbor(logicList[left][tile.getColumn()]);
		}
		if (right < rows) {
			tile.addNeighbor(logicList[right][tile.getColumn()]);
		}
	}

	// Removes a SmartButton from the open list
	private void removeFromOpen(AStarTile tile) {
		for (int i = 0; i < openList.size(); i++) {
			if (openList.get(i) == tile) {
				openList.remove(i);
			}
		}
	}

	public void clear() {
		for (int i=0;i<logicList.length;i++){
			for (int l=0;l<logicList[i].length;l++){
				logicList[i][l].setOpen(false);
				logicList[i][l].setClosed(false);
				logicList[i][l].setG(0);
			}
		}
		clearOpen();
	}

	private void clearOpen() {
		for (int i = 0; i < openList.size(); i++) {
			openList.remove(i);
		}
	}




}
