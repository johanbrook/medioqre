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
	 * Get all the entities in the game
	 * 
	 * @return The entities
	 */
	public List<Entity> getEntities();
}
