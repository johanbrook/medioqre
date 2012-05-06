package controller.AI;

import java.awt.Point;
import java.util.List;

import model.character.Enemy;
import model.weapon.Projectile;
import constants.Direction;

/**
 * Class for representing a player. Each AIPlayer will control one enemy. Keeps track of shortest path between the two, current cooldown and how many updates
 * have gone since the enemy was last updated.
 * @author jesperpersson
 *
 */

public class AIPlayer {
	private Enemy unit;
	private List <Point> path;
	private int iq,updateCount,distance;
	private final int ATTACK_RANGE = 35;
	private double cooldown, attackIntervall;
	
	public AIPlayer (Enemy unit){
		this.unit = unit;	
		this.iq = 5;
		this.updateCount = 0;
		this.distance = 100;
		this.cooldown = 0;
		this.attackIntervall = 5;
		
	}
	
	public Enemy getEnemy(){
		return this.unit;
	}
	
	public int getIQ(){
		return this.iq;
	}
	
	public void updateCount(){
		this.updateCount ++;
	}
	
	public void resetCount(){
		this.updateCount = 0;
		
	}
	
	public int getCount(){
		return this.updateCount;
	}
	
	public void setDistance(int distance){
		this.distance = distance;
	
	}
	
	public int getDistance(){
		return this.distance;
	}
	
	
	public List <Point> getPath(){
		return this.path;
	}
	
	public void setPath(List <Point> path){
		this.path = path;
	}
	
	public void updateEnemy(Direction dir){
		this.unit.setDirection(dir);
	}
	
	
	
	public Projectile doAttack(){
		return this.unit.attack();
	}
	
	public boolean inRange(){
		return this.distance < ATTACK_RANGE;
	}
	
	/**
	 * Adds the dt to the current cooldown, if cooldown is zero or below, resets the cooldown and returns false, else returns true.
	 * @param dt
	 * @return
	 */
	 public boolean inCooldown(double dt){
		 this.cooldown -= dt;
		 if (this.cooldown <= 0){
			 this.cooldown = attackIntervall;
			 return false;
		 }else {
			 return true;
		 }
	 }
	 
	 public double getAttackIntervall(){
		 return this.attackIntervall;
	 }
	 
	 public void setAttackIntervall(double a){
		this.attackIntervall = a; 
	 }

	public void resetCooldown() {
		this.cooldown = 0;
	}
}
