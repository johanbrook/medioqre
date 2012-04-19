package controller.AI;

import java.awt.Point;
import java.util.List;

import model.character.Enemy;

public class AIPlayer {
	private Enemy unit;
	private List <Point> path;
	private int iq;
	
	public AIPlayer (Enemy unit){
		this.unit = unit;	
	}
	
	public Enemy getEnemy(){
		return this.unit;
	}
	
	public int getIQ(){
		return this.iq;
	}
	
	public List <Point> getPath(){
		return this.path;
	}
	
	public void setPath(List <Point> path){
		this.path = path;
	}

}
