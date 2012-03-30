package gui.animation;

import graphics.bitmap.Bitmap;
import graphics.bitmap.BitmapTool;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import json.test.ResourceManager;

import constants.Direction;

public class Actor {

	private Point position;
	private Animation[] animations;
	private Animation currentAnimation;
	
	public Actor(Point startingPosition, Animation[] animations) {
		this.position = new Point(startingPosition);
		this.animations = animations;
		this.currentAnimation = animations[0];
	}
	
	public Bitmap getCurrentFrame() {
		return currentAnimation.getCurrentFrame();
	}
	
	public Point getPosition() {
		return this.position;
	}
	public void setDirection(Direction direction)
	{
		switch(direction) {
		case NORTH :
			currentAnimation = animations[4];
			break;
		case NORTH_EAST :
			currentAnimation = animations[5];
			break;
		case EAST :
			currentAnimation = animations[6];
			break;
		case SOUTH_EAST :
			currentAnimation = animations[7];
			break;
		case SOUTH :
			currentAnimation = animations[0];
			break;
		case SOUTH_WEST :
			currentAnimation = animations[1];
			break;
		case WEST :
			currentAnimation = animations[2];
			break;
		case NORTH_WEST :
			currentAnimation = animations[3];
			break;
		}
		System.out.println(currentAnimation.getName());
	}
	
	public void update(double dt)
	{
		currentAnimation.updateAnimation(dt);
	}
	
	public void setPosition(Point pos) {
		this.position = pos;
	}
}
