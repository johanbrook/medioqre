/**
 *	IGameModel.java
 *
 *	@author Johan
 */

package model;

import java.util.List;

import model.character.Enemy;
import model.character.Player;

public interface IGameModel {

	/**
	 * Updates the game logic.
	 * 
	 * @param delta
	 */
	public void update(double delta);

	/**
	 * Initiates a new game
	 */
	public void newGame();

	/**
	 * Get the player
	 * 
	 * @return The player
	 */
	public Player getPlayer();

	/**
	 * Get all the enemies.
	 * 
	 * @return The enemies
	 */
	public List<Enemy> getEnemies();

	/**
	 * Get all the objects in the game
	 * 
	 * @return The objects
	 */
	public List<CollidableObject> getObjects();

	/**
	 * Initialize a new wave/round.
	 * 
	 */
	public void newWave();
}
