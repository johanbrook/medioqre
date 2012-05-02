package controller.AI;

import java.awt.Point;
import java.util.List;

import model.character.Enemy;
import constants.Direction;

public class AIPlayer {
	private Enemy unit;
	private List <Point> path;
	private int iq,updateCount,distance;
	
	public AIPlayer (Enemy unit){
		this.unit = unit;	
		this.iq = 5;
		this.updateCount = 0;
		this.distance = 100;
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
	
	public void doAttack(){
		
	}
}
