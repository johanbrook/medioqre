package controller.ai;

import java.awt.Point;
import java.util.List;

import model.character.Enemy;
import model.weapon.Projectile;
import constants.Direction;

/**
 * Class for representing a player. Each AIPlayer will control one enemy. Keeps
 * track of shortest path between the two, current cooldown and how many updates
 * have gone since the enemy was last updated.
 * 
 * @author jesperpersson
 * 
 */

public class AIPlayer {
	private Enemy unit;
	private List<Point> path;
	private int iq, updateCount, distance;
	private Point currentTile;

	public AIPlayer(Enemy unit) {
		this.unit = unit;
		this.iq = 5;
		this.updateCount = 0;
		this.distance = 100;

	}

	/**
	 * get the Enemy controlled by this AIPlayer
	 * 
	 * @return the Enemy controlled by this AIPlayer
	 */
	public Enemy getEnemy() {
		return this.unit;
	}

	/**
	 * get the IQ of this AIPlayer
	 * 
	 * @return the IQ
	 */
	public int getIQ() {
		return this.iq;
	}

	/**
	 * Increment the updatecount.
	 */
	public void updateCount() {
		this.updateCount++;
	}

	/**
	 * Reset the updateCount to 0
	 */
	public void resetCount() {
		this.updateCount = 0;

	}

	/**
	 * get the updateCount
	 * 
	 * @return the updatecount.
	 */
	public int getCount() {
		return this.updateCount;
	}

	/**
	 * Set the distance between the enemy controlled by this AIPlayer and a
	 * certain point on the field (usually, the player)
	 * 
	 * @param distance
	 */
	public void setDistance(int distance) {
		this.distance = distance;

	}

	/**
	 * get the distance
	 * 
	 * @return the distance
	 */
	public int getDistance() {
		return this.distance;
	}

	/**
	 * get the currently saved Path between enemy and a certain tile in the
	 * gameboard (usually the tile containing the player)
	 * 
	 * @return
	 */
	public List<Point> getPath() {
		return this.path;
	}

	/**
	 * Set the path of the enemy.
	 * 
	 * @param path
	 */
	public void setPath(List<Point> path) {
		this.path = path;
	}

	/**
	 * Set the direction of the enemy controlled by this AIPlayer
	 * 
	 * @param dir
	 */
	public void updateEnemy(Direction dir) {
		this.unit.setDirection(dir);
	}

	/**
	 * Tell the unit controlled by this AIPlayer to attack. Returns the
	 * projectile of the currently equipped weapon of the unit.
	 * 
	 * @return projectile of selected weapon
	 */
	public Projectile doAttack() {
		return this.unit.attack();
	}

	/**
	 * Return true if the distance between enemy is shorter than the
	 * ATTACK_RANGE
	 * 
	 * @return
	 */
	public boolean inRange() {
		double range = this.unit.getCurrentWeapon().getProjectile().getRange()
				.getDistance();
		return this.distance < range;
	}
	
	public Point getMidPos(){
		return new Point ((int) this.unit.getCollisionBox().getCenterX(),(int) this.unit.getCollisionBox().getCenterY());
	}

	public Point getCurrentTile() {
		return currentTile;
	}

	public void setCurrentTile(Point currentTile) {
		this.currentTile = currentTile;
	}
}
