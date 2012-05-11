package controller.ai;

import java.awt.Point;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import model.GameModel;
import model.character.Enemy;
import model.character.Player;
import model.weapon.Projectile;
import constants.Direction;
import event.Event;
import event.IMessageListener;
import event.IMessageSender;
import event.Messager;

/**
 * Class for controlling a list of enemies. Using a PathFinder (implementation
 * of the A*-algorithm), the AIController is able to calculate the most accurate
 * path between an enemy and the player.
 * 
 * @author jesperpersson
 * 
 */
public class AIController implements IMessageSender, IMessageListener {

	private List<AIPlayer> enemies;
	private PathFinder pathfinder;
	private int width, height;
	private Point playerPos, playerTile;
	private Player player;
	private Messager messager = new Messager();

	public AIController(int rows, int columns, int width, int height) {
		this.pathfinder = new PathFinder(rows, columns);
		this.width = width;
		this.height = height;
		this.enemies = new CopyOnWriteArrayList<AIPlayer>();
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onMessage(Event evt) {

		switch (evt.getProperty()) {
		case NEW_GAME :
			player = ((GameModel) evt.getValue()).getPlayer();
			this.pathfinder.init();
			break;
		case NEW_WAVE :
			this.setEnemies(((GameModel) evt.getValue()).getEnemies());
			break;

		case WAS_DESTROYED :
			this.removeEnemy((Enemy) evt.getValue());
			break;
		}
	}

	/**
	 * Updates each enemy the AIController keeps track of.
	 * 
	 * @param dt
	 *            Time since last update
	 */
	public void updateAI(double dt) {
		this.playerPos = player.getPosition();
		playerTile = calculateTile(playerPos);

		if (playerTile.x >= 0 && playerTile.y >= 0 && playerTile.x < 48
				&& playerTile.y < 48) {

			if (enemies.size() > 0) {

				for (AIPlayer ai : enemies) {
					updateEnemy(ai, dt);
					ai.getEnemy().start();
				}
			}
		}
	}// end updateAI

	/**
	 * Given a aiPlayer, will update the direction of the players unit.
	 * Depending on the players updatecount, the length between the unit and the
	 * player, as well as the amount of enemies in the gameworld, the
	 * calculation take different forms.
	 * 
	 * @param aiPlayer
	 */
	private void updateEnemy(AIPlayer aiPlayer, double dt) {
		if (this.pathfinder.isInitiated()){
			Point enemyTile = calculateTile(aiPlayer.getMidPos());

			aiPlayer.setDistance(Math.abs(aiPlayer.getMidPos().x
					- playerPos.x)
					+ Math.abs(aiPlayer.getMidPos().y - playerPos.y));

			aiPlayer.getEnemy().getCurrentWeapon().updateCooldown(dt);
			handleAttack(aiPlayer, dt);
			if (aiPlayer.getCount() < (aiPlayer.getDistance() / 15)
					+ enemies.size() / 15) {
				aiPlayer.updateCount();

			} else {
				if (enemyTile.x >= 0 && enemyTile.y >= 0 && enemyTile.x < 48
						&& enemyTile.y < 48) {
					aiPlayer.resetCount();

					aiPlayer.setPath(pathfinder.getPath(enemyTile, playerTile));
					if (aiPlayer.getPath() != null) {

						// Update direction of the enemy depending on what the
						// current path is.

						// If path is longer than 2 tiles, just calculate the
						// direction from the path
						if (aiPlayer.getPath().size() >= 2) {
							
							//Path is from player to enemy - enemy will always be in the last slot of the list
							//This also means that the next position we want the enemy to walk to is the next to last slot in the list.
							Point nextPos = getMidOfTile(aiPlayer.getPath().get(aiPlayer.getPath().size()-2).getLocation());
							Point enemyPos =getMidOfTile(aiPlayer.getPath().get(aiPlayer.getPath().size()-1).getLocation());
							
							aiPlayer.updateEnemy(calculateDirection(enemyPos, nextPos));
						} else {

							// If path is shorter, manually inserts enemy and player
							// positions and walk straight towards them, they should
							// be to close for there to
							// be any kind of obstacle in the way.
							aiPlayer.getEnemy().setDirection(
									findLineToPlayer(aiPlayer));
						}
					} else {
						aiPlayer.getEnemy().setDirection(randomDir());
					}
				}
			}
		}
	}// end updateEnemy

	/**
	 * If enemy is in range of player, will try to send projectiles into the
	 * game. The AIPlayer keeps track of cooldown.
	 * 
	 * @param ai
	 *            AIPlayer in control of this specific enemy
	 * @param dt
	 *            time since last update
	 */
	private void handleAttack(AIPlayer ai, double dt) {
		if (ai.inRange()) {
			if (!ai.getEnemy().getCurrentWeapon().inCooldown()) {
				Projectile proj = ai.doAttack();
				messager.sendMessage(new Event(Event.Property.DID_ATTACK, proj));
			}
		} else {
			ai.getEnemy().getCurrentWeapon().resetCooldown();
		}
	}

	/**
	 * Given a AIPlayer, will return the direction towards the player, not
	 * taking any walls or other collidables into consideration
	 * 
	 * @param aiPlayer
	 * @return
	 */
	private Direction findLineToPlayer(AIPlayer aiPlayer) {
		
		return calculateDirection(aiPlayer.getMidPos(), this.playerPos);

	}

	/**
	 * Returns a randomly selected direction
	 * 
	 * @return
	 */
	private Direction randomDir() {
		Random rand = new Random();
		int r = rand.nextInt(8);
		Direction d = Direction.ORIGIN;
		switch (r) {
		case 0 :
			d = Direction.EAST;
			break;
		case 1 :
			d = Direction.NORTH;
			break;
		case 2 :
			d = Direction.NORTH_EAST;
			break;
		case 3 :
			d = Direction.NORTH_WEST;
			break;
		case 4 :
			d = Direction.SOUTH;
			break;
		case 5 :
			d = Direction.SOUTH_EAST;
			break;
		case 6 :
			d = Direction.SOUTH_WEST;
			break;
		case 7 :
			d = Direction.WEST;
			break;
		}
		return d;
	}

	/**
	 * Replace the current list of enemies with a new one.
	 * 
	 * @param enemies
	 *            A list of enemies to track
	 */
	public void setEnemies(List<Enemy> enemies) {
		this.enemies.clear();
		for (Enemy e : enemies) {
			this.enemies.add(new AIPlayer(e));
		}
	}

	/**
	 * Adds a enemy to the list of enemies the AIController keeps track of
	 * 
	 * @param enemy
	 */
	public void addEnemy(Enemy enemy) {
		this.enemies.add(new AIPlayer(enemy));
	}

	/**
	 * Given a enemy, that enemy will no longer be controlled by this
	 * AIController
	 * 
	 * @param enemy
	 */
	public void removeEnemy(Enemy enemy) {
		for (AIPlayer ai : this.enemies) {
			if (ai.getEnemy() == enemy) {
				this.enemies.remove(ai);
			}
		}

	}

	/**
	 * Given a list of points, will return the direction between the last two
	 * points in the list.
	 * 
	 * @param path
	 * @return
	 */
	public Direction calculateDirection(Point start, Point stop) {
		// Compare enemy position with next calculated position in path.
		
		
		
		int dx = (int) Math.signum(stop.x - start.x);
		int dy = (int) Math.signum(stop.y - start.y);
		

		// Return direction depending on the values of dx and dy.
		switch (dx) {
		case 1 :
			if (dy == -1)
				return Direction.NORTH_EAST;

			else if (dy == 0)
				return Direction.EAST;

			else {
				return Direction.SOUTH_EAST;
			}

		case 0 :
			if (dy == -1)
				return Direction.NORTH;

			else {
				return Direction.SOUTH;
			}

		case -1 :
			if (dy == -1)
				return Direction.NORTH_WEST;

			else if (dy == 0)
				return Direction.WEST;

			else {
				return Direction.SOUTH_WEST;
			}
		}// Should never reach this point since dx will always be 1,0 or -1
		return null;

	}

	/**
	 * Given a point representing a position in the gameworld, will return the
	 * tile in which that position exists
	 * 
	 * @param point
	 * @return
	 */
	private Point calculateTile(Point point) {
		return new Point(point.x / this.width, point.y / this.height);
	}
	
	/**
	 * Given a tile, will return the point that represent the center of that tile.
	 * @param tile
	 * @return Point representing the center of the specified tile.
	 */
	private Point getMidOfTile(Point tile){
		int x = (tile.x * this.width) + this.width / 2;
		int y = (tile.y * this.height) + this.height / 2;
		return new Point (x,y);
		
	}

	@Override
	public void addReceiver(IMessageListener listener) {
		this.messager.addListener(listener);

	}

}
