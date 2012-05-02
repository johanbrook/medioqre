package controller.AI;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import model.character.Enemy;
import model.weapon.Projectile;
import constants.Direction;
import event.Event;
import event.IMessageListener;
import event.IMessageSender;
import event.Messager;

/**
 * Class for controlling a list of enemies. Using a PathFinder (implementation of the A*-algorithm), the AIController is able to calculate
 * the most accurate path between a enemy and the player. 
 * @author jesperpersson
 *
 */
public class AIController implements IMessageSender{

	private List <AIPlayer> enemies;
	private PathFinder pathfinder;
	private int width, height;
	private Point playerPos,playerTile;
	private Messager messager = new Messager();

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
	public void updateAI(double dt, Point playerPos){
		playerTile = calculateTile(playerPos);
		this.playerPos = playerPos;

		if (playerTile.x >= 0 && playerTile.y >=0 &&
				playerTile.x < 48 && playerTile.y < 48){

			if (enemies.size() > 0){

				for (AIPlayer ai : enemies){
					updateEnemy(ai, dt);
					ai.getEnemy().start();
				}
			}
		}
	}//end updateAI


	/**
	 * Given a aiPlayer, will update the direction of the players unit. Depending on the players updatecount, the length between 
	 * the unit and the player, as well as the amount of enemies in the gameworld, the calculation take different forms.
	 * @param aiPlayer
	 */
	private void updateEnemy(AIPlayer aiPlayer, double dt ){
		Point enemyTile = calculateTile(aiPlayer.getEnemy().getPosition());

		aiPlayer.setDistance(Math.abs(aiPlayer.getEnemy().getPosition().x - playerPos.x) + Math.abs(aiPlayer.getEnemy().getPosition().y
				- playerPos.y));
		
		handleAttack(aiPlayer,dt);
		if (aiPlayer.getCount() < (aiPlayer.getDistance()/15) + enemies.size()/15){
			aiPlayer.updateCount();

		}else{
			if (enemyTile.x >= 0 && enemyTile.y >=0 &&
					enemyTile.x < 48 && enemyTile.y < 48){
				aiPlayer.resetCount();

				aiPlayer.setPath(pathfinder.getPath(enemyTile, playerTile));
				if (aiPlayer.getPath() != null){

					

					//Update direction of the enemy depending on what the current path is.

					//If path is longer than 2 tiles, just calculate the direction from the path
					if (aiPlayer.getPath().size() >= 2){
						aiPlayer.updateEnemy(calculateDirection(aiPlayer.getPath()));
					}else {

						//If path is shorter, manually inserts enemy and player positions and walk straight towards them, they should be to close for there to
						//be any kind of obsticle in the way.
						aiPlayer.getPath().clear();
						aiPlayer.getPath().add(playerPos);
						aiPlayer.getPath().add(aiPlayer.getEnemy().getPosition());
						aiPlayer.updateEnemy(calculateDirection(aiPlayer.getPath()));

					}
				}else {
					aiPlayer.getEnemy().setDirection(randomDir());
				}
			}
		}
	}//end updateEnemy


	private void handleAttack(AIPlayer ai, double dt){
		if (ai.inRange()) {
			if (!ai.inCooldown(dt)){
				Projectile proj = ai.doAttack();
				messager.sendMessage(new Event(Event.Property.DID_ATTACK, proj));
			}
		}else {
			ai.resetCooldown();
		}

	}


	/**
	 * Returns a randomly selected direction
	 * @return
	 */
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
		for (AIPlayer ai : enemies){
			if (ai.getEnemy() == enemy){
				this.enemies.remove(ai);
			}
		}
			
	}

	/**
	 * Given a list of points, will return the direction between the last two points in the list.
	 * @param path
	 * @return  
	 */
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

	/**
	 * Given a point representing a position in the gameworld, will return the tile of that position
	 * @param point 
	 * @return 
	 */
	public Point calculateTile(Point point){
		return new Point(point.x/this.width, point.y/this.height);
	}

	@Override
	public void addReceiver(IMessageListener listener) {
		this.messager.addListener(listener);

	}

}
