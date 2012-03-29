/**
*	IGameModel.java
*
*	@author Johan
*/

package model;

import constants.Direction;

public interface IGameModel {
	
	/**
	 * Updates the game logic.
	 * 
	 * @param delta
	 */
	public void update(double delta);
	
	/**
	 * Change the player's direction
	 * 
	 * @param dir The new direction
	 */
	public void updateDirection(Direction dir); 
	
	/**
	 * Stop the player
	 * 
	 */
	public void stopPlayer();
}
