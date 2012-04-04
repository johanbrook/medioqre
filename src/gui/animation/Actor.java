package gui.animation;

import graphics.bitmap.Bitmap;
import java.awt.Point;
import java.util.Map;
import java.util.TreeMap;

import constants.Direction;

public class Actor {

	private Point position;
	private Map<String, Animation> animations;
	private Animation currentAnimation;

	public Actor clone()
	{
		Map<String, Animation> newAnimations = new TreeMap<String, Animation>();
		
		for (String s : this.animations.keySet()) {
			newAnimations.put(s, this.animations.get(s).clone());
		}
		
		Actor newActor = new Actor(new Point(this.position.x, this.position.y), newAnimations);
		return newActor;
	}

	public Actor(Point startingPosition, Map<String, Animation> animations)
	{
		this.position = new Point(startingPosition);
		this.animations = animations;
		this.currentAnimation = null;
	}

	public Bitmap getCurrentFrame()
	{
		if (currentAnimation == null)
			return null;

		return currentAnimation.getCurrentFrame();
	}

	public Point getPosition()
	{
		return this.position;
	}

	public void setDirection(Direction direction, boolean isMoving)
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
		currentAnimation.updateAnimation(dt);
	}

	public void setPosition(Point pos)
	{
		this.position = pos;
	}
}
