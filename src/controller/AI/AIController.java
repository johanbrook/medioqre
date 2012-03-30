package controller.AI;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import model.character.Enemy;
import constants.Direction;

/**
 * Class for controlling a list of enemies. Using a PathFinder (implementation of the A*-algorithm), the AIController is able to calculate
 * the most accurate path between a enemy and the player. 
 * @author jesperpersson
 *
 */
public class AIController {
	
	private List <Enemy> enemies;
	private PathFinder pathfinder;
	private int rows,columns,width, height;

	
	
	public AIController (int rows, int columns, int width, int height){
		this.pathfinder = new PathFinder(rows, columns);
		this.enemies = new ArrayList <Enemy>();
		this.rows = rows;
		this.columns = columns;
		this.width = width;
		this.height = height;
		}
	
	/**
	 * For each existing enemy, will update that enemies direction in order to reach the player in shortest amount of time possible
	 * @param playerPos
	 */
	public void updateAI(Point playerPos){
		if (enemies.size() > 0){
			for (int i = 0; i < enemies.size(); i++){
				List <Point> path = pathfinder.getPath(calculateTile(enemies.get(i).getPosition()), calculateTile(playerPos));
				if (path != null ){
					//Update direction of the zombie
					enemies.get(i).setDirection(calculateDirection(path));
				}
			}
		}
	}
	
	
	/**
	 * Adds a enemy to the list of enemies the AIController keeps track of
	 * @param enemy
	 */
	public void addEnemy(Enemy enemy){
		this.enemies.add(enemy);
	}
	
	/**
	 * Given a enemy, that enemy will no longer be controlled by this AIController
	 * @param enemy
	 */
	public void removeEnemy(Enemy enemy){
		this.enemies.remove(enemy);
	}
	
	public Direction calculateDirection(List <Point> path){
		//If path is long enough to calculate.
		if (path.size() >= 2){
			int dx = (int) Math.signum(path.get(path.size()-2).getX()-path.get(path.size()-1).getX());
			int dy = (int) Math.signum(path.get(path.size()-2).getY()-path.get(path.size()-1).getY());
			
			switch (dx){
			case -1:
				if(dy == -1){
					return Direction.NORTH_EAST;
				}
				else if(dy == 0){
					return Direction.EAST;
				}
				else{
					return Direction.SOUTH_EAST;
				}
			case 0:
				if (dy == -1){
					return Direction.NORTH;
				}else {
					return Direction.SOUTH;
				}
			case 1:
				if (dy == -1){
					return Direction.NORTH_WEST;
				}
				else if (dy == 0){
					return Direction.WEST;
				}else {
					return Direction.SOUTH_WEST;
				}
			}

		}
		return null;
	}
	
	public Point calculateTile(Point point){
		return new Point(point.x/this.width, point.y/this.height);
	}
	

}
