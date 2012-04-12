package gui.animation;

import graphics.bitmap.Bitmap;
import java.awt.Point;
import java.util.Map;
import java.util.TreeMap;

import model.Entity;
import model.Position;

import constants.Direction;

public class Actor {

	private Point position;
	private Map<String, Animation> animations;
	private Animation currentAnimation;
	private Entity entity;
	private boolean isMoving;

	public Actor clone()
	{
		Map<String, Animation> newAnimations = new TreeMap<String, Animation>();
		
		for (String s : this.animations.keySet()) {
			newAnimations.put(s, this.animations.get(s).clone());
		}
		
		Actor newActor = new Actor(this.entity, newAnimations);
		return newActor;
	}

	public Actor(Entity e, Map<String, Animation> animations)
	{
		this.entity = e;
		this.animations = animations;
		this.currentAnimation = null;
	}
	
	public void setEntity(Entity e)
	{
		this.entity = e;
	}
	public Entity getEntity()
	{
		return this.entity;
	}
	
	public Bitmap getCurrentFrame()
	{
		if (currentAnimation == null)
			return null;

		return currentAnimation.getCurrentFrame();
	}

	public Point getPosition()
	{
		if (this.entity != null) return this.entity.getPosition();
		else return new Point(0,0);
	}

	public void startMoving()
	{
		this.isMoving = true;
	}
	public void stopMoving()
	{
		this.isMoving = false;
	}
	
	public void setDirection(Direction direction)
	{
		switch (direction) {
		case NORTH:
			currentAnimation = isMoving ? animations.get("moveNorth")
					: animations.get("standNorth");
			break;
		case NORTH_EAST:
			currentAnimation = isMoving ? animations.get("moveNorthEast")
					: animations.get("standNorthEast");
			break;
		case EAST:
			currentAnimation = isMoving ? animations.get("moveEast")
					: animations.get("standEast");
			break;
		case SOUTH_EAST:
			currentAnimation = isMoving ? animations.get("moveSouthEast")
					: animations.get("standSouthEast");
			break;
		case SOUTH:
			currentAnimation = isMoving ? animations.get("moveSouth")
					: animations.get("standSouth");
			break;
		case SOUTH_WEST:
			currentAnimation = isMoving ? animations.get("moveSouthWest")
					: animations.get("standSouthWest");
			break;
		case WEST:
			currentAnimation = isMoving ? animations.get("moveWest")
					: animations.get("standWest");
			break;
		case NORTH_WEST:
			currentAnimation = isMoving ? animations.get("moveNorthWest")
					: animations.get("standNorthWest");
			break;
		}
		System.out.println(currentAnimation.getName());
	}

	public void update(double dt)
	{
		if (this.entity != null)
			this.setDirection(this.entity.getDirection());
		if (currentAnimation != null)
			currentAnimation.updateAnimation(dt);
	}
}
