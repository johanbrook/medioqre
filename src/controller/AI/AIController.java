package controller.AI;

import java.util.List;
import model.character.Enemy;

public class AIController {
	
	private List <Enemy> enemies;
	private PathFinder pathfinder;

	
	
	public AIController (int rows, int columns){
		this.pathfinder = new PathFinder(rows, columns);
		}
	
	public void updateAI(){
		
	}
	
	public void addEnemy(Enemy enemy){
		this.enemies.add(enemy);
	}
	
	public void removeEnemy(Enemy enemy){
		this.enemies.remove(enemy);
	}
	

}
