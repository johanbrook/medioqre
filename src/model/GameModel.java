package model;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.ArrayList;

import model.character.Character;
import model.character.Enemy;
import model.character.Player;
import constants.Direction;
import controller.AI.AIController;

/**
 * Model for a game.
 * 
 * @author Johan
 *
 */
public class GameModel implements IGameModel {

	private Character player;
	private ArrayList<Enemy> enemies;
	private AIController ai;

	public GameModel() {
		this.player = new Player();
		this.enemies = new ArrayList <Enemy>();
		//TODO Need to access size, both in pixels and in rows/columns, where size is the width/height of each tile.
		//this.ai = new AIController(rows, columns, width, height);
		initEnemies();
	}

	/**
	 * Creates a number of zombies and adds them to the enemy-list
	 */
	private void initEnemies() {
		for (int i = 0; i < 5; i++){
			this.enemies.add(new Enemy(5,  30, new Rectangle (5,5),  new Dimension (5,5),  5,  5));
			//this.ai.addEnemy(enemies.get(i));
		}
	}

	/**
	 * Updates the game model.
	 * @param dt The time since the last update.
	 */
	public void update(double dt) {
		//this.ai.updateAI(this.player.getPosition());
		
		this.player.move(dt);
		
		for (int i = 0; i < this.enemies.size(); i++){
			this.enemies.get(i).move(dt);
			System.out.println("Enemy " + i + " has X-value: " + this.enemies.get(i).getPosition().x
								+ " and Y-value: " + this.enemies.get(i).getPosition().y);
		}
	}

	/**
	 * Updates the player's direction.
	 * 
	 * @param dir The direction
	 * @see Direction
	 */
	public void updateDirection(Direction dir) {
		this.player.setDirection(dir);
	}

	public void stopPlayer(){
		this.player.stop();
	}


}
