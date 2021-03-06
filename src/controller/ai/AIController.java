package controller.ai;

import java.awt.Point;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import model.Direction;
import model.GameModel;
import model.character.Enemy;
import model.character.Player;
import model.weapon.Projectile;
import event.Event;
import event.IEventHandler;
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
public class AIController implements IMessageSender, IEventHandler {

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

	@Override
	public void onEvent(Event evt) {

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
		this.playerPos = getMidOfPlayerPos();
		playerTile = calculateTile(playerPos);
		for (AIPlayer ai : enemies) {
			updateEnemy(ai, dt);
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
			
			Direction orgdir = aiPlayer.getEnemy().getDirection();

			aiPlayer.setDistance(Math.abs(aiPlayer.getMidPos().x - getMidOfPlayerPos().x) +
					Math.abs(aiPlayer.getMidPos().y - getMidOfPlayerPos().y));

			handleAttack(aiPlayer, dt);

			//If no path is set, the player is no longer at the end of the path, the enemy was teleported or pushed, calculate new path.
			if (aiPlayer.getPath() == null || 
					!aiPlayer.getPath().get(aiPlayer.getPath().size()-1).equals(playerTile) ||
					aiPlayer.getEnemy().isPortalVictim() ||
					aiPlayer.getEnemy().wasPushed()){
				aiPlayer.setCurrentTile(calculateTile(aiPlayer.getMidPos()));

				//Update more often if closer to player. No need for enemies far away to update each frame.
				if (aiPlayer.getCount() > aiPlayer.getDistance()/this.width){
					getNewPath(aiPlayer);
					aiPlayer.resetCount();
					aiPlayer.getEnemy().setPushed(false);
				}else {
					aiPlayer.updateCount();
				}

			}
			if (aiPlayer.getPath() != null) {

				followPath(aiPlayer);

			} else {
				aiPlayer.updateEnemy(findLineToPlayer(aiPlayer));
				getNewPath(aiPlayer);
			}
			
			if (aiPlayer.getEnemy().getDirection() != orgdir){
				tools.Logger.log("CHANGED DIRECTION OF ENEMY! NEW DIRECTION: " + aiPlayer.getEnemy().getDirection());
			}
		}
	}





	/**
	 * Given a AIPlayer, will update the unit controlled by the AIPlayer according to its internally stored path.
	 * @param aiPlayer
	 */
	private void followPath(AIPlayer aiPlayer) {


		// If path is longer than two tiles, just follow the path.
		if (aiPlayer.getPath().size() >= 2){
			aiPlayer.updateEnemy(calculateDirection(aiPlayer.getMidPos(), getMidOfTile(aiPlayer.getPath().get(1))));

			// If the unit is close to the center of the next tile in the path, remove the tile in
			// index 0, which represents the last tile. Calculate new current tile.
			if (positionsAreClose(aiPlayer.getMidPos(), getMidOfTile(aiPlayer.getPath().get(1)))){
				aiPlayer.setCurrentTile(calculateTile(aiPlayer.getMidPos()));
				aiPlayer.getPath().remove(0);
			}

			// If path is shorter than two tiles, manually insert enemy and player
			// positions and follow the line between them, they should
			// be to close for there to
			// be any kind of obstacle in the way.
		}else{	aiPlayer.getEnemy().setDirection(
				findLineToPlayer(aiPlayer));
		}

	}

	/**
	 * Checks whether two positions are close to each other. 
	 * @param myPos
	 * @param targetPos
	 * @return True if the specified positions are close.
	 */
	private boolean positionsAreClose (Point myPos, Point targetPos){
		return (Math.abs(myPos.x - targetPos.x) < 8 && Math.abs(myPos.y - targetPos.y) < 8);
	}

	/**
	 * Will calculate a new path between the specified aiPlayer's controlled unit and the player. Sets it if algorithm works and a path is found.
	 * If no path is found, the AIPlayers path won't be changed.
	 * @param aiPlayer
	 */
	private void getNewPath(AIPlayer aiPlayer) {
		List <Point> pathInTiles = this.pathfinder.getPath(aiPlayer.getCurrentTile(), playerTile);
		if (pathInTiles != null){
			aiPlayer.setPath(pathInTiles);
		}
	}


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
		ai.getEnemy().getCurrentWeapon().updateCooldown(dt);

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
		return calculateDirection(aiPlayer.getMidPos(), getMidOfPlayerPos());
	}

	/**
	 * get the middle point of the players collision box.
	 * @return the mid point of the players collision box.
	 */
	private Point getMidOfPlayerPos() {
		return new Point ((int)this.player.getCollisionBox().getCenterX(),(int) this.player.getCollisionBox().getCenterY());
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
		int dx = (int) Math.signum(stop.x - start.x);
		int dy = (int) Math.signum(stop.y - start.y);

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
	}//End calculateDirection

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
	public void addReceiver(IEventHandler listener) {
		this.messager.addListener(listener);

	}

}
