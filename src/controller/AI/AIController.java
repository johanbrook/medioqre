package controller.AI;

import java.awt.Point;
import java.util.List;

import model.character.Enemy;

/**
 * Class for controlling a list of enemies. Using a PathFinder (implementation of the A*-algorithm), the AIController is able to calculate
 * the most accurate path between a enemy and the player. 
 * @author jesperpersson
 *
 */
public class AIController {
	
	private List <Enemy> enemies;
	private PathFinder pathfinder;

	
	
	public AIController (int rows, int columns){
		this.pathfinder = new PathFinder(rows, columns);
		}
	
	public void updateAI(Point playerPos){
		
	}
	
	public void addEnemy(Enemy enemy){
		this.enemies.add(enemy);
	}
	
	public void removeEnemy(Enemy enemy){
		this.enemies.remove(enemy);
	}
	

}
