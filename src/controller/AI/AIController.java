package controller.AI;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import model.character.Enemy;
import constants.Direction;

/**
 * Class for controlling a list of enemies. Using a PathFinder (implementation of the A*-algorithm), the AIController is able to calculate
 * the most accurate path between a enemy and the player. 
 * @author jesperpersson
 *
 */
public class AIController {

	private List <AIPlayer> enemies;
	private PathFinder pathfinder;
	private int width, height;
	private Point playerPos,playerTile;

	public AIController (int rows, int columns, int width, int height) {
		this(new ArrayList <Enemy>(), rows, columns, width, height);
	}

	public AIController (List<Enemy> enemies, int rows, int columns, int width, int height){
		this.pathfinder = new PathFinder(rows, columns);
		this.width = width;
		this.height = height;
		this.enemies = new ArrayList <AIPlayer>();
		for (int i = 0; i < enemies.size(); i++) {
			this.enemies.add(new AIPlayer(enemies.get(i)));
		}
	}

	/**
	 * For each existing enemy, will update that enemies direction in order to reach the player in shortest amount of time possible
	 * @param playerPos
	 */
	public void updateAI(Point playerPos){
		playerTile = calculateTile(playerPos);
		this.playerPos = playerPos;

		if (playerTile.x >= 0 && playerTile.y >=0 &&
				playerTile.x < 48 && playerTile.y < 48){

			if (enemies.size() > 0){
				for (int i = 0; i < enemies.size(); i++){
					updateEnemy(enemies.get(i));
				}
			}
		}
	}



	private void updateEnemy(AIPlayer currentEnemy ){
		currentEnemy.getEnemy().start();
		Point enemyTile = calculateTile(currentEnemy.getEnemy().getPosition());
		int length = (Math.abs(enemyTile.x - playerTile.x) + Math.abs(enemyTile.y
				- playerTile.y));
		//Calculates the path between enemy and player
		if(length > 15 ){
			currentEnemy.updateEnemy(randomDir());
		}else {

			currentEnemy.setPath(pathfinder.getPath(enemyTile, playerTile));
			if (currentEnemy.getPath() != null){

				//Update direction of the enemy depending on what the current path is.

				//If path is longer than 2 tiles, just calculate the direction from the path
				if (currentEnemy.getPath().size() >= 2){
					currentEnemy.updateEnemy(calculateDirection(currentEnemy.getPath()));
				}else {

					//If path is shorter, manually inserts enemy and player positions and walk straight towards them, they should be to close for there to
					//be any kind of obsticle in the way.
					currentEnemy.getPath().clear();
					currentEnemy.getPath().add(playerPos);
					currentEnemy.getPath().add(currentEnemy.getEnemy().getPosition());
					currentEnemy.updateEnemy(calculateDirection(currentEnemy.getPath()));
				}
			}
		}
	}



	private Direction randomDir() {
		Random rand = new Random();
		int r = rand.nextInt(8);
		Direction d = Direction.ORIGIN;
		switch (r) {
		case 0:
			d = Direction.EAST;
			break;
		case 1:
			d = Direction.NORTH;
			break;
		case 2:
			d = Direction.NORTH_EAST;
			break;
		case 3:
			d = Direction.NORTH_WEST;
			break;
		case 4:
			d = Direction.SOUTH;
			break;
		case 5:
			d = Direction.SOUTH_EAST;
			break;
		case 6:
			d = Direction.SOUTH_WEST;
			break;
		case 7:
			d = Direction.WEST;
			break;
		}
		return d;
	}

	/**
	 * Replace the current list of enemies with a new one.
	 * 
	 * @param enemies A list of enemies to track
	 */
	public void setEnemies(List<Enemy> enemies) {
		this.enemies.clear();
		for (int i = 0; i< enemies.size();i++){
			this.enemies.add(new AIPlayer (enemies.get(i)));
		}
	}

	/**
	 * Adds a enemy to the list of enemies the AIController keeps track of
	 * @param enemy
	 */
	public void addEnemy(Enemy enemy){
		this.enemies.add(new AIPlayer(enemy));
	}

	/**
	 * Given a enemy, that enemy will no longer be controlled by this AIController
	 * @param enemy
	 */
	public void removeEnemy(Enemy enemy){
		this.enemies.remove(enemy);
	}

	public Direction calculateDirection(List <Point> path){
		// Compare enemy position with next calculated position in path.
		int dx = (int) Math.signum(path.get(path.size()-2).getX()-path.get(path.size()-1).getX());
		int dy = (int) Math.signum(path.get(path.size()-2).getY()-path.get(path.size()-1).getY());

		//Return direction depending on the values of dx and dy.
		switch (dx){
		case 1:
			if(dy == -1)
				return Direction.NORTH_EAST;


			else if(dy == 0)
				return Direction.EAST;

			else{
				return Direction.SOUTH_EAST;
			}

		case 0:
			if (dy == -1)
				return Direction.NORTH;


			else {
				return Direction.SOUTH;
			}

		case -1:
			if (dy == -1)
				return Direction.NORTH_WEST;

			else if (dy == 0)
				return Direction.WEST;


			else {
				return Direction.SOUTH_WEST;
			}
		}//Should never reach this point since dx will always be 1,0 or -1
		return null;

	}

	public Point calculateTile(Point point){
		return new Point(point.x/this.width, point.y/this.height);
	}


}
